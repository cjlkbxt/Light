package com.kobe.light.ui.pole_info;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.kobe.lib_base.BaseActivity;
import com.kobe.light.R;
import com.kobe.light.request.BrightRequest;
import com.kobe.light.request.SwitchRequest;
import com.kobe.light.response.BaseResponse;
import com.kobe.light.response.DeviceResponse;
import com.kobe.light.response.DictResponse;
import com.kobe.light.response.LampInfoResponse;
import com.kobe.light.response.PoleInfoResponse;
import com.kobe.light.ui.check.CheckActivity;
import com.zackratos.ultimatebarx.ultimatebarx.UltimateBarX;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PoleInfoActivity extends BaseActivity<PoleInfoContract.presenter> implements View.OnClickListener, PoleInfoContract.view {

    private TextView tv_pole_no;//灯杆编号
    private TextView tv_device_road;//设备道路
    private TextView tv_address;//地址
    private TextView tv_jingweidu;//经纬度
    private TextView tv_condition;//状态

    private TagFlowLayout mTagFlowLayout;

    private TextView tv_submit;

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
        return R.layout.activity_pole_info;
    }

    @Override
    protected void initViews() {
        tv_pole_no = findViewById(R.id.tv_pole_no);
        tv_device_road = findViewById(R.id.tv_device_road);
        tv_address = findViewById(R.id.tv_address);
        tv_jingweidu = findViewById(R.id.tv_jingweidu);
        tv_condition = findViewById(R.id.tv_condition);
        mTagFlowLayout = findViewById(R.id.id_flowlayout);
        tv_submit = findViewById(R.id.tv_submit);
    }

    @Override
    protected void initData() {
        String poleCode = getIntent().getStringExtra("poleCode");
        mPresenter.getPoleInfo2(poleCode);

    }

    @SuppressLint("CheckResult")
    @Override
    protected void registerListener() {
        tv_pole_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwitchRequest switchRequest = new SwitchRequest();
                switchRequest.lampCodes = "3BC25911EB1F4711BFE5372E5147FB5D";
                switchRequest.switchVal = 2;
                mPresenter.switchOnOff(switchRequest);
            }
        });
        tv_device_road.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BrightRequest brightRequest = new BrightRequest();
                brightRequest.lampCodes = "3BC25911EB1F4711BFE5372E5147FB5D";
                brightRequest.brightness = 90;
                mPresenter.dimming(brightRequest);
            }
        });
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
    public void showPoleInfo(PoleInfoResponse poleInfoResponse) {
        tv_pole_no.setText(poleInfoResponse.data.hlLampPole.lampPoleName);
        tv_device_road.setText(poleInfoResponse.data.hlLampPole.lampPoleName);
        tv_address.setText(poleInfoResponse.data.hlLampPole.lampPoleName);
        tv_jingweidu.setText(poleInfoResponse.data.hlLampPole.baiduLongitude + "," + poleInfoResponse.data.hlLampPole.baiduLatitude);


        TagAdapter tagAdapter = new TagAdapter(poleInfoResponse.data.lamplist) {
            @Override
            public View getView(FlowLayout parent, int position, Object o) {
                TextView tv = (TextView) View.inflate(PoleInfoActivity.this, R.layout.item_select_tag_view, null);
                tv.setText(poleInfoResponse.data.lamplist.get(position).lampName);
                return tv;
            }
        };
        tagAdapter.setSelectedList(0);
        mPresenter.getLampInfo2(poleInfoResponse.data.lamplist.get(0).lampCode);
        mTagFlowLayout.setAdapter(tagAdapter);
        mTagFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                List<Integer> selectPosList = new ArrayList<>(selectPosSet);
                if (selectPosSet.size() == 1) {
                    mPresenter.getLampInfo2(poleInfoResponse.data.lamplist.get(selectPosList.get(0)).lampCode);
                }
            }
        });
    }

    @Override
    public void showLampInfo(LampInfoResponse lampInfoResponse) {

    }


    @Override
    public void switchOnOff(BaseResponse baseResponse) {

    }

    @Override
    public void dimming(BaseResponse baseResponse) {

    }

}