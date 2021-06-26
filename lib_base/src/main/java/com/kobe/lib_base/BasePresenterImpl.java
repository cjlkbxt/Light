package com.kobe.lib_base;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.Fragment;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BasePresenterImpl<V extends IBaseView> implements IBasePresenter {

    protected WeakReference<V> mViewRef;//View 接口类型的弱引用

    public BasePresenterImpl(V view) {
        mViewRef = new WeakReference<V>(view);
        start();
    }

    protected V getView() {
        return isAttach() ? mViewRef.get() : null;
    }

    protected Context getContext() {
        if (isAttach()) {
            if (mViewRef.get() instanceof Fragment) {
                return ((Fragment) mViewRef.get()).getContext();
            } else {
                return (Activity) mViewRef.get();
            }
        } else {
            return null;
        }

    }

    protected boolean isAttach() {
        return mViewRef != null &&
                mViewRef.get() != null;
    }

    @Override
    public void detach() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
        unDisposable();
    }

    @Override
    public void start() {

    }

    //将所有正在处理的Subscription都添加到CompositeSubscription中。统一退出的时候注销观察
    private CompositeDisposable mCompositeDisposable;

    /**
     * 将Disposable添加
     *
     * @param subscription
     */
    @Override
    public void addDisposable(Disposable subscription) {
        //csb 如果解绑了的话添加 sb 需要新的实例否则绑定时无效的
        if (mCompositeDisposable == null || mCompositeDisposable.isDisposed()) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(subscription);
    }

    /**
     * 在界面退出等需要解绑观察者的情况下调用此方法统一解绑，防止Rx造成的内存泄漏
     */
    @Override
    public void unDisposable() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
    }


}
