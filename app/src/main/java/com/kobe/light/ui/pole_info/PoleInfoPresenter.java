package com.kobe.light.ui.pole_info;

import com.kobe.lib_base.BasePresenterImpl;
import com.kobe.light.api.LightApi;
import com.kobe.light.api.RetrofitServiceManager;
import com.kobe.light.request.BrightRequest;
import com.kobe.light.request.SwitchRequest;
import com.kobe.light.response.BaseResponse;
import com.kobe.light.response.DeviceResponse;
import com.kobe.light.response.LampInfoResponse;
import com.kobe.light.response.PoleInfoResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class PoleInfoPresenter extends BasePresenterImpl<PoleInfoContract.view> implements PoleInfoContract.presenter {
    private LightApi mMedbitApi = RetrofitServiceManager.getInstance().create(LightApi.class);

    public PoleInfoPresenter(PoleInfoContract.view view) {
        super(view);
    }


    @Override
    public void getPoleInfo2(String dictType) {
        DisposableObserver<PoleInfoResponse> observer = new DisposableObserver<PoleInfoResponse>() {
            @Override
            public void onNext(PoleInfoResponse poleInfoResponse) {
                if (poleInfoResponse.code == 0) {
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

        mMedbitApi.getPoleInfo2(dictType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        addDisposable(observer);

    }

    @Override
    public void getLampInfo2(String dictType) {
        DisposableObserver<LampInfoResponse> observer = new DisposableObserver<LampInfoResponse>() {
            @Override
            public void onNext(LampInfoResponse lampInfoResponse) {
                if (lampInfoResponse.code == 0) {
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

        mMedbitApi.getLampInfo2(dictType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        addDisposable(observer);
    }

    @Override
    public void switchOnOff(SwitchRequest switchRequest) {
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

        mMedbitApi.switchOnOff(switchRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        addDisposable(observer);
    }

    @Override
    public void dimming(BrightRequest brightRequest) {
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

        mMedbitApi.dimming(brightRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        addDisposable(observer);
    }
}
