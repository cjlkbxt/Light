package com.kobe.light.ui.work_info;

import com.kobe.lib_base.BasePresenterImpl;
import com.kobe.light.api.LightApi;
import com.kobe.light.api.RetrofitServiceManager;
import com.kobe.light.response.BaseResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class WorkInfoPresenter extends BasePresenterImpl<WorkInfoContract.view> implements WorkInfoContract.presenter {
    private LightApi mMedbitApi = RetrofitServiceManager.getInstance().create(LightApi.class);

    public WorkInfoPresenter(WorkInfoContract.view view) {
        super(view);
    }


    @Override
    public void list() {
        DisposableObserver<BaseResponse> observer = new DisposableObserver<BaseResponse>() {
            @Override
            public void onNext(BaseResponse baseResponse) {
                if (baseResponse.code == 0) {
//                    getView().showPoleInfo(baseResponse);
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {

            }
        };

        mMedbitApi.list(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        addDisposable(observer);
    }

    @Override
    public void submit() {
        DisposableObserver<BaseResponse> observer = new DisposableObserver<BaseResponse>() {
            @Override
            public void onNext(BaseResponse baseResponse) {
                if (baseResponse.code == 0) {
//                    getView().showPoleInfo(baseResponse);
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {

            }
        };

        mMedbitApi.handle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        addDisposable(observer);
    }
}
