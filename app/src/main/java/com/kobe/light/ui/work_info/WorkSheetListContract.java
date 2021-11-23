package com.kobe.light.ui.work_info;

import com.kobe.lib_base.IBasePresenter;
import com.kobe.lib_base.IBaseView;
import com.kobe.light.response.WorkSheetListResponse;

public interface WorkSheetListContract {

    interface view extends IBaseView {
        void showWorkSheetListResponse(WorkSheetListResponse listResponse);
    }

    interface presenter extends IBasePresenter {
        void getWorkSheetList();
    }
}
