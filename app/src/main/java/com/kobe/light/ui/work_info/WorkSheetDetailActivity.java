package com.kobe.light.ui.work_info;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kobe.lib_base.BaseActivity;
import com.kobe.light.R;
import com.kobe.light.request.SubmitRequest2;

public class WorkSheetDetailActivity extends BaseActivity<WorkSheetDetailContract.presenter> implements View.OnClickListener, WorkSheetDetailContract.view {

    private ImageView mIvBack;
    private TextView mTitle;

    @Override
    public void onClick(View v) {
        SubmitRequest2 submitRequest2 = new SubmitRequest2();
        submitRequest2.billCode = "6F6BEDD462504D029EB092EC6700AE55";
        submitRequest2.detailPics = "";
        submitRequest2.detailMsg = "换了一个新灯具";
        mPresenter.submit(submitRequest2);
    }

    @Override
    public void submitSuccess() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_work_info_detail;
    }

    @Override
    protected void initViews() {
        mIvBack = findViewById(R.id.iv_back);
        mTitle = findViewById(R.id.tv_title);
    }

    @Override
    protected void initData() {
        mTitle.setText("工单详情");
    }

    @Override
    protected void registerListener() {

    }

    @Override
    public WorkSheetDetailContract.presenter initPresenter() {
        return new WorkSheetDetailPresenter(this);
    }
}
