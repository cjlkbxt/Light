package com.kobe.light.ui.scan;

import android.text.TextUtils;

import com.kobe.lib_base.BasePresenterImpl;
import com.kobe.light.api.LightApi;
import com.kobe.light.api.RetrofitServiceManager;
import com.kobe.light.constants.DictType;
import com.kobe.light.request.SubmitRequest;
import com.kobe.light.response.DeviceResponse;
import com.kobe.light.response.DictResponse;
import com.kobe.light.response.SubmitResponse;
import com.kobe.light.response.UploadResponse;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ScanPresenter extends BasePresenterImpl<ScanContract.view> implements ScanContract.presenter {
    private LightApi mMedbitApi = RetrofitServiceManager.getInstance().create(LightApi.class);

    public ScanPresenter(ScanContract.view view) {
        super(view);
    }

    @Override
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
    }

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
    public void submit(SubmitRequest submitRequest) {
        DisposableObserver<SubmitResponse> observer = new DisposableObserver<SubmitResponse>() {
            @Override
            public void onNext(SubmitResponse submitResponse) {
                if (submitResponse.code == 0) {
                    getView().submitSuccess(submitResponse);
                } else {
                    getView().submitError(submitResponse);
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {

            }
        };

        mMedbitApi.submit(submitRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        addDisposable(observer);
    }

    @Override
    public void upLoad(List<MultipartBody.Part> partList) {
        DisposableObserver<UploadResponse> observer = new DisposableObserver<UploadResponse>() {
            @Override
            public void onNext(@NotNull UploadResponse uploadResponse) {
                if (uploadResponse.code == 0) {
                    getView().uploadSuccess(uploadResponse);
                }

            }

            @Override
            public void onError(@NotNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

//        List<MultipartBody.Part> partList = new ArrayList<>();
//        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        MultipartBody.Part filePart = MultipartBody.Part.createFormData("files", file.getName(), requestBody);
//        partList.add(filePart);

        mMedbitApi.upload(partList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        addDisposable(observer);
    }


}
