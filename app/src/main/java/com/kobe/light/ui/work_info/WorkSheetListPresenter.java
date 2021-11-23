package com.kobe.light.ui.work_info;

import com.kobe.lib_base.BasePresenterImpl;
import com.kobe.light.api.LightApi;
import com.kobe.light.api.RetrofitServiceManager;
import com.kobe.light.response.WorkSheetListResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class WorkSheetListPresenter extends BasePresenterImpl<WorkSheetListContract.view> implements WorkSheetListContract.presenter {
    private LightApi mMedbitApi = RetrofitServiceManager.getInstance().create(LightApi.class);

    public WorkSheetListPresenter(WorkSheetListContract.view view) {
        super(view);
    }


    @Override
    public void getWorkSheetList() {
        DisposableObserver<WorkSheetListResponse> observer = new DisposableObserver<WorkSheetListResponse>() {
            @Override
            public void onNext(WorkSheetListResponse listResponse) {
                if (listResponse.code == 0) {
                    getView().showWorkSheetListResponse(listResponse);
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {

            }
        };

        mMedbitApi.getWorkSheetList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        addDisposable(observer);
    }

}
