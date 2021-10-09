package com.kobe.light.ui.work_info;

import com.kobe.lib_base.BasePresenterImpl;
import com.kobe.light.api.LightApi;
import com.kobe.light.api.RetrofitServiceManager;
import com.kobe.light.request.SubmitRequest2;
import com.kobe.light.response.BaseResponse;
import com.kobe.light.response.ListResponse;

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
        DisposableObserver<ListResponse> observer = new DisposableObserver<ListResponse>() {
            @Override
            public void onNext(ListResponse listResponse) {
                if (listResponse.code == 0) {
                    getView().showListResponse(listResponse);
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {

            }
        };

        mMedbitApi.list()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        addDisposable(observer);
    }

}
