package com.kobe.light.ui.work_info;

import com.kobe.lib_base.IBasePresenter;
import com.kobe.lib_base.IBaseView;
import com.kobe.light.request.SubmitRequest2;
import com.kobe.light.response.ListResponse;


public interface WorkSheetDetailContract {

    interface view extends IBaseView {

        void submitSuccess();

    }

    interface presenter extends IBasePresenter {

        void submit(SubmitRequest2 submitRequest2);

    }
}
