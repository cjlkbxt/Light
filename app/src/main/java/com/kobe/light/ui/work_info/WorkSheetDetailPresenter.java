package com.kobe.light.ui.work_info;

import com.kobe.lib_base.BasePresenterImpl;
import com.kobe.light.api.LightApi;
import com.kobe.light.api.RetrofitServiceManager;
import com.kobe.light.request.SubmitRequest2;
import com.kobe.light.response.BaseResponse;
import com.kobe.light.response.UploadResponse;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;

public class WorkSheetDetailPresenter extends BasePresenterImpl<WorkSheetDetailContract.view> implements WorkSheetDetailContract.presenter {
    private LightApi mMedbitApi = RetrofitServiceManager.getInstance().create(LightApi.class);

    public WorkSheetDetailPresenter(WorkSheetDetailContract.view view) {
        super(view);
    }


    @Override
    public void submitWorkSheet(SubmitRequest2 submitRequest2) {
        DisposableObserver<BaseResponse> observer = new DisposableObserver<BaseResponse>() {
            @Override
            public void onNext(BaseResponse baseResponse) {
                if (baseResponse.code == 0) {
                    getView().submitSuccess();
                }else{
                    getView().showToast(baseResponse.msg);
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {

            }
        };

        mMedbitApi.submitWorkSheet(submitRequest2)
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
