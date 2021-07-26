package com.kobe.light.ui.install;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding3.view.RxView;
import com.kobe.lib_base.BaseActivity;
import com.kobe.light.R;
import com.kobe.light.request.BindRequest;
import com.kobe.light.response.DeviceResponse;
import com.zackratos.ultimatebarx.ultimatebarx.UltimateBarX;

import java.util.concurrent.TimeUnit;

public class InstallActivity extends BaseActivity<InstallContract.presenter> implements InstallContract.view, View.OnClickListener {

    private ImageView mIvBack;
    private TextView mTvPole; //灯杆
    private TextView mTvRoad; //道路
    private TextView mTvDetailAddress; //详细地址
    private TextView mTvRoadDirect; //道路方向

    private TextView mTvSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UltimateBarX.with(this)
                .fitWindow(true)
                .color(Color.WHITE)
                .light(true)
                .applyStatusBar();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_install;
    }

    @Override
    protected void initViews() {
        mIvBack = findViewById(R.id.iv_back);
        mTvPole = findViewById(R.id.tv_pole);
        mTvRoad = findViewById(R.id.tv_road);
        mTvDetailAddress = findViewById(R.id.tv_detail_address);
        mTvRoadDirect = findViewById(R.id.tv_road_direct);
        mTvSubmit = findViewById(R.id.tv_submit);
    }

    @Override
    protected void initData() {
        String result = getIntent().getStringExtra("result");
        mPresenter.getPoleInfo(result);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void registerListener() {
        mIvBack.setOnClickListener(this);
        RxView.clicks(mTvSubmit).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(unit -> {
                    BindRequest bindRequest = new BindRequest();
                    mPresenter.bind(bindRequest);
                });
    }

    @Override
    public InstallContract.presenter initPresenter() {
        return new InstallPresenter(this);
    }

    @Override
    public void showPoleInfo(DeviceResponse deviceResponse) {
        mTvPole.setText(deviceResponse.data.road.roadName);
        mTvRoad.setText(deviceResponse.data.lampPoleName);
        mTvDetailAddress.setText(deviceResponse.data.concretePosition);
        mTvRoadDirect.setText(deviceResponse.data.lampPoleTypeDictText);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
