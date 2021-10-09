package com.kobe.lib_base;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


/**
 * <p>Class: com.medbit.android.product.dsp.app.youkybaselibrary.mvpbase.baseImpl.BaseLayLoadFragment</p>
 * <p>Description: </p>
 * <pre>
 *
 *  </pre>
 *
 * @author lujunjie
 * @date 2018/8/20/12:04.
 */
public abstract class BaseLayLoadFragment<P extends IBasePresenter> extends Fragment implements IBaseView {

    protected P mPresenter;

    public Context context;

    protected View convertView;//显示的convertView

    private Bundle savedInstanceState;

    protected static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";

    public abstract int getLayoutId(); //获取布局资源文件

    public abstract P initPresenter();

    public abstract void initViews();//初始化控件

    public abstract void registerListener();//注册监听事件

    public abstract void initData();//初始化数据，如：网络请求获取数据


    private boolean isFirstLoad = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        mPresenter = initPresenter();
        if (savedInstanceState != null) {
            boolean isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);

            FragmentTransaction ft = getFragmentManager().beginTransaction();
//            FragmentTransaction cft = getChildFragmentManager().beginTransaction();
            if (isSupportHidden) {
                ft.hide(this);
//                cft.hide(this);
            } else {
                ft.show(this);
//                cft.show(this);
            }
            ft.commit();
//            cft.commit();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        convertView = inflater.inflate(getLayoutId(), container, false);//用布局填充器填充布局
        isFirstLoad = true;//视图创建完成，将变量置为true
        if (getUserVisibleHint()) {//如果Fragment可见进行数据加载
            onLazyLoad();
            isFirstLoad = false;
        }
        return convertView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    /**
     * 懒加载
     * 如果不是第一次加载、没有初始化控件、不对用户可见就不加载
     */
    protected void onLazyLoad() {
        initViews();//初始化控件
        initData();//初始化数据
        registerListener();//注册监听事件
    }

    /**
     * 是否对用户可见
     *
     * @param isVisibleToUser true:表示对用户可见，反之则不可见
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isFirstLoad && isVisibleToUser) {//视图变为可见并且是第一次加载
            onLazyLoad();
            isFirstLoad = false;
        }
    }

    @Override
    public void onDestroyView() {
        if (mPresenter != null) {
            mPresenter.detach();
            mPresenter.unDisposable();
            mPresenter = null;
        }
        isFirstLoad = false;//视图销毁将变量置为false
        super.onDestroyView();
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
    }

    public Bundle getSavedInstanceState() {
        return savedInstanceState;
    }

    /**
     * 判断是否有某项权限
     *
     * @param string_permission 权限
     * @param request_code      请求码
     * @return
     */
    public boolean checkReadPermission(String string_permission, int request_code) {
        boolean flag = false;
        if (ContextCompat.checkSelfPermission(getActivity(), string_permission) == PackageManager.PERMISSION_GRANTED) {//已有权限
            flag = true;
        } else {//申请权限
            ActivityCompat.requestPermissions(getActivity(), new String[]{string_permission}, request_code);
        }
        return flag;
    }


    protected void setFitSystem(boolean fitSystemWindows) {
        ViewGroup mContentView = (ViewGroup) getActivity().getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 预留出系统 View 的空间.
            mChildView.setFitsSystemWindows(fitSystemWindows);
        }
    }

    protected void setStatusBar(int color) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

            ViewGroup decorViewGroup = (ViewGroup) getActivity().getWindow().getDecorView();
            View statusBarView = new View(getActivity().getWindow().getContext());
            int statusBarHeight = getStatusBarHeight(getActivity().getWindow().getContext());
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, statusBarHeight);
            params.gravity = Gravity.TOP;
            statusBarView.setLayoutParams(params);
            statusBarView.setBackgroundColor(color);
            decorViewGroup.addView(statusBarView);
        } else {
            Window window = getActivity().getWindow();
            //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            window.setStatusBarColor(color);
        }
    }

    protected int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }
}
