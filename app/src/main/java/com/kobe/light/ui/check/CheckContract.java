package com.kobe.light.ui.check;

import com.kobe.lib_base.IBasePresenter;
import com.kobe.lib_base.IBaseView;
import com.kobe.light.request.SubmitRequest;
import com.kobe.light.response.DeviceResponse;
import com.kobe.light.response.DictResponse;
import com.kobe.light.response.SubmitResponse;
import com.kobe.light.response.UploadResponse;

import java.util.List;

import okhttp3.MultipartBody;


public interface CheckContract {

    interface view extends IBaseView {

        void showPoleInfo(DeviceResponse deviceResponse);

        void showRoadDirectInfo(DictResponse dictResponse);

        void showShineCarRoadInfo(DictResponse dictResponse);

        void showShineDirectInfo(DictResponse dictResponse);

        void showShineRoadInfo(DictResponse dictResponse);

        void showPolePositionInfo(DictResponse dictResponse);

        void submitSuccess(SubmitResponse submitResponse);

        void submitError(SubmitResponse submitResponse);

        void uploadSuccess(UploadResponse uploadResponse);

    }

    interface presenter extends IBasePresenter {

        void getDict(String dictType);

        void getPoleInfo(String dictType);

        void submit(SubmitRequest submitRequest);

        void upLoad(List<MultipartBody.Part> partList);


    }
}
