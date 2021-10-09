package com.kobe.light.ui.pole_info;

import com.kobe.lib_base.IBasePresenter;
import com.kobe.lib_base.IBaseView;
import com.kobe.light.request.BrightRequest;
import com.kobe.light.request.SubmitRequest;
import com.kobe.light.request.SwitchRequest;
import com.kobe.light.response.BaseResponse;
import com.kobe.light.response.DeviceResponse;
import com.kobe.light.response.DictResponse;
import com.kobe.light.response.SubmitResponse;
import com.kobe.light.response.UploadResponse;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface PoleInfoContract {

    interface view extends IBaseView {

        void showPoleInfo(DeviceResponse deviceResponse);

        void showLampInfo(DictResponse dictResponse);

        void switchOnOff(BaseResponse baseResponse);

        void dimming(BaseResponse baseResponse);

    }

    interface presenter extends IBasePresenter {

        void getPoleInfo2(String dictType);

        void getLampInfo2(String dictType);

        void switchOnOff(SwitchRequest switchRequest);

        void dimming(BrightRequest brightRequest);

    }
}
