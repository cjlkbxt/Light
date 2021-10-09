package com.kobe.light.ui.work_info;

import com.kobe.lib_base.IBasePresenter;
import com.kobe.lib_base.IBaseView;
import com.kobe.light.request.BrightRequest;
import com.kobe.light.request.SubmitRequest2;
import com.kobe.light.request.SwitchRequest;
import com.kobe.light.response.BaseResponse;
import com.kobe.light.response.DeviceResponse;
import com.kobe.light.response.DictResponse;
import com.kobe.light.response.ListResponse;


public interface WorkInfoContract {

    interface view extends IBaseView {

        void showListResponse(ListResponse listResponse);

    }

    interface presenter extends IBasePresenter {

        void list();

    }
}
