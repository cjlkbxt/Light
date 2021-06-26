package com.kobe.light.ui.login;

import com.kobe.lib_base.BasePresenterImpl;
import com.kobe.light.api.LightApi;
import com.kobe.light.api.RetrofitServiceManager;
import com.kobe.light.request.LoginRequest;
import com.kobe.light.response.LoginResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenter extends BasePresenterImpl<LoginContract.view> implements LoginContract.presenter {
    private LightApi mMedbitApi = RetrofitServiceManager.getInstance().create(LightApi.class);

    public LoginPresenter(LoginContract.view view) {
        super(view);
    }

    @Override
    public void login(LoginRequest loginRequest) {
        DisposableObserver<LoginResponse> observer = new DisposableObserver<LoginResponse>() {
            @Override
            public void onNext(LoginResponse loginResponse) {
                if (loginResponse.code == 0) {
                    getView().loginSuccess(loginResponse);
                } else {
                    getView().loginFailed(loginResponse);
                }
            }

            @Override
            public void onError(Throwable e) {
                getView().showError();
            }

            @Override
            public void onComplete() {

            }
        };

        mMedbitApi.login(loginRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        addDisposable(observer);
    }
}
