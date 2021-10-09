package com.kobe.light.ui.work_info;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kobe.lib_base.BaseActivity;
import com.kobe.light.R;
import com.kobe.light.response.BaseResponse;
import com.kobe.light.response.DeviceResponse;
import com.kobe.light.response.DictResponse;
import com.kobe.light.ui.pole_info.PoleInfoContract;
import com.kobe.light.ui.pole_info.PoleInfoPresenter;
import com.zackratos.ultimatebarx.ultimatebarx.UltimateBarX;

public class WorkInfoActivity extends BaseActivity<WorkInfoContract.presenter> implements View.OnClickListener, WorkInfoContract.view {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UltimateBarX.with(this)
                .fitWindow(false)
                .light(true)
                .applyStatusBar();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_scan;
    }

    @Override
    protected void initViews() {
        mRecyclerView = findViewById(R.id.rv_upload);
    }

    @Override
    protected void initData() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
    }

    @SuppressLint("CheckResult")
    @Override
    protected void registerListener() {
//        mIvBack.setOnClickListener(this);
    }

    @Override
    public WorkInfoContract.presenter initPresenter() {
        return new WorkInfoPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void listSuccess() {

    }

    @Override
    public void submitSuccess() {

    }
}