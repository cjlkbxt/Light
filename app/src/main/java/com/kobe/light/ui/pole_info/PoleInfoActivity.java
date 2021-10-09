package com.kobe.light.ui.pole_info;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kobe.lib_base.BaseActivity;
import com.kobe.light.R;
import com.kobe.light.response.BaseResponse;
import com.kobe.light.response.DeviceResponse;
import com.kobe.light.response.DictResponse;
import com.zackratos.ultimatebarx.ultimatebarx.UltimateBarX;

public class PoleInfoActivity extends BaseActivity<PoleInfoContract.presenter> implements View.OnClickListener, PoleInfoContract.view {

    private TextView tv_pole_no;//灯杆编号
    private TextView tv_device_road;//设备道路
    private TextView tv_address;//地址
    private TextView tv_jingweidu;//经纬度
    private TextView tv_condition;//状态

    private TextView tv_submit;

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
        return R.layout.activity_pole_info;
    }

    @Override
    protected void initViews() {
        tv_pole_no = findViewById(R.id.tv_pole_no);
        tv_device_road = findViewById(R.id.tv_device_road);
        tv_address = findViewById(R.id.tv_address);
        tv_jingweidu = findViewById(R.id.tv_jingweidu);
        tv_condition = findViewById(R.id.tv_condition);
        tv_submit = findViewById(R.id.tv_submit);
    }

    @Override
    protected void initData() {
    }

    @SuppressLint("CheckResult")
    @Override
    protected void registerListener() {
//        mIvBack.setOnClickListener(this);
    }

    @Override
    public PoleInfoContract.presenter initPresenter() {
        return new PoleInfoPresenter(this);
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
    public void showPoleInfo(DeviceResponse deviceResponse) {
    }

    @Override
    public void showLampInfo(DictResponse dictResponse) {

    }

    @Override
    public void switchOnOff(BaseResponse baseResponse) {

    }

    @Override
    public void dimming(BaseResponse baseResponse) {

    }

}