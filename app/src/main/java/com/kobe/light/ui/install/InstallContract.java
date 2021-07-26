package com.kobe.light.ui.install;

import com.kobe.lib_base.IBasePresenter;
import com.kobe.lib_base.IBaseView;
import com.kobe.light.request.BindRequest;
import com.kobe.light.request.SubmitRequest;
import com.kobe.light.response.DeviceResponse;
import com.kobe.light.response.DictResponse;
import com.kobe.light.response.SubmitResponse;
import com.kobe.light.response.UploadResponse;

import java.util.List;

import okhttp3.MultipartBody;


public interface InstallContract {

    interface view extends IBaseView {
        void showPoleInfo(DeviceResponse deviceResponse);

    }

    interface presenter extends IBasePresenter {

        void getPoleInfo(String dictType);

        void bind(BindRequest bindRequest);
    }
}
