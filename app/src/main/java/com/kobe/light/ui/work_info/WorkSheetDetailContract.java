package com.kobe.light.ui.work_info;

import com.kobe.lib_base.IBasePresenter;
import com.kobe.lib_base.IBaseView;
import com.kobe.light.request.SubmitRequest2;
import com.kobe.light.response.UploadResponse;

import java.util.List;

import okhttp3.MultipartBody;


public interface WorkSheetDetailContract {

    interface view extends IBaseView {

        void submitSuccess();

        void showToast(String msg);

        void uploadSuccess(UploadResponse uploadResponse);

    }

    interface presenter extends IBasePresenter {

        void submitWorkSheet(SubmitRequest2 submitRequest2);

        void upLoad(List<MultipartBody.Part> partList);

    }
}
