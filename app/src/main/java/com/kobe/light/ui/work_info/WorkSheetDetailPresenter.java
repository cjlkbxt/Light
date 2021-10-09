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

public class WorkSheetDetailPresenter extends BasePresenterImpl<WorkSheetDetailContract.view> implements WorkSheetDetailContract.presenter {
    private LightApi mMedbitApi = RetrofitServiceManager.getInstance().create(LightApi.class);

    public WorkSheetDetailPresenter(WorkSheetDetailContract.view view) {
        super(view);
    }


    @Override
    public void submit(SubmitRequest2 submitRequest2) {
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

        mMedbitApi.handle(submitRequest2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        addDisposable(observer);
    }
}
