package com.kobe.light.ui.install;

import com.kobe.lib_base.BasePresenterImpl;
import com.kobe.light.api.LightApi;
import com.kobe.light.api.RetrofitServiceManager;
import com.kobe.light.request.BindRequest;
import com.kobe.light.response.DeviceResponse;
import com.kobe.light.response.SubmitResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class InstallPresenter extends BasePresenterImpl<InstallContract.view> implements InstallContract.presenter {
    private LightApi mMedbitApi = RetrofitServiceManager.getInstance().create(LightApi.class);

    public InstallPresenter(InstallContract.view view) {
        super(view);
    }

 /*   @Override
    public void getDict(String dictType) {
        DisposableObserver<DictResponse> observer = new DisposableObserver<DictResponse>() {
            @Override
            public void onNext(DictResponse dictResponse) {
                if (dictResponse.code == 0) {
                    if (TextUtils.equals(dictType, DictType.ROAD_DIRECTION_TYPE)) {
                        getView().showRoadDirectInfo(dictResponse);
                    } else if (TextUtils.equals(dictType, DictType.IRRADIATE_ROAD)) {
                        getView().showShineCarRoadInfo(dictResponse);
                    } else if (TextUtils.equals(dictType, DictType.SHINE_DIRECTION)) {
                        getView().showShineDirectInfo(dictResponse);
                    } else if (TextUtils.equals(dictType, DictType.IRRADIATE_ROAD_TYPE)) {
                        getView().showShineRoadInfo(dictResponse);
                    } else if (TextUtils.equals(dictType, DictType.LAMP_POLE_POSITION)) {
                        getView().showPolePositionInfo(dictResponse);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {

            }
        };

        mMedbitApi.getDict(dictType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        addDisposable(observer);
    }*/

    @Override
    public void getPoleInfo(String key) {
        DisposableObserver<DeviceResponse> observer = new DisposableObserver<DeviceResponse>() {
            @Override
            public void onNext(DeviceResponse deviceResponse) {
                if (deviceResponse.code == 0) {
                    getView().showPoleInfo(deviceResponse);
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {

            }
        };

        mMedbitApi.getPoleInfo(key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        addDisposable(observer);
    }

    @Override
    public void bind(BindRequest bindRequest) {
        DisposableObserver<SubmitResponse> observer = new DisposableObserver<SubmitResponse>() {
            @Override
            public void onNext(SubmitResponse submitResponse) {
//                if (submitResponse.code == 0) {
//                    getView().submitSuccess(submitResponse);
//                } else {
//                    getView().submitError(submitResponse);
//                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {

            }
        };

        mMedbitApi.bind(bindRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        addDisposable(observer);
    }



}
