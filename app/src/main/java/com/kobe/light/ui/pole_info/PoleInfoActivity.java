package com.kobe.light.ui.pole_info;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;

import com.jakewharton.rxbinding3.view.RxView;
import com.kobe.lib_base.BaseActivity;
import com.kobe.light.R;
import com.kobe.light.request.BindRequest;
import com.kobe.light.request.BrightRequest;
import com.kobe.light.request.SwitchRequest;
import com.kobe.light.response.BaseResponse;
import com.kobe.light.response.LampInfoResponse;
import com.kobe.light.response.PoleInfoResponse;
import com.kobe.light.utils.ToastUtil;
import com.zackratos.ultimatebarx.ultimatebarx.UltimateBarX;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 灯杆信息
 */
public class PoleInfoActivity extends BaseActivity<PoleInfoContract.presenter> implements View.OnClickListener, PoleInfoContract.view {

    private ImageView mIvBack;
    private TextView mTvTitle;

    private TextView tv_pole_no;//灯杆编号
    private TextView tv_device_road;//设备道路
    private TextView tv_address;//地址
    private TextView tv_jingweidu;//经纬度
    private TextView tv_control_box;//控制箱

    private TagFlowLayout mTagFlowLayout;//灯具

    private TextView tv_condition;//状态
    private TextView tv_voltage;//电压
    private TextView tv_current;//电流
    private TextView tv_power;//功率
    private TextView tv_model;//型号
    private TextView tv_open_light_time;//开灯时间
    private TextView tv_first_adjust_light_time;//第一次调灯时间
    private TextView tv_second_adjust_light_time;//第二次调灯时间
    private TextView tv_third_adjust_light_time;//第三次调灯时间
    private TextView tv_close_light_time;//关灯时间
    private SwitchCompat mSwitch;//亮灯开关
    private SeekBar mSeekBar;//亮度
    private TextView tv_percent;//亮度比例
    private TextView tv_confirm;//确认调光

    private String mLampPoleCode;
    private int mProgress;

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
        mIvBack = findViewById(R.id.iv_back);
        mTvTitle = findViewById(R.id.tv_title);

        tv_pole_no = findViewById(R.id.tv_pole_no);
        tv_device_road = findViewById(R.id.tv_device_road);
        tv_address = findViewById(R.id.tv_address);
        tv_jingweidu = findViewById(R.id.tv_jingweidu);
        tv_control_box = findViewById(R.id.tv_control_box);

        tv_condition = findViewById(R.id.tv_condition);
        tv_voltage = findViewById(R.id.tv_voltage);
        tv_current = findViewById(R.id.tv_current);
        tv_power = findViewById(R.id.tv_power);
        tv_model = findViewById(R.id.tv_model);
        tv_open_light_time = findViewById(R.id.tv_open_light_time);
        tv_first_adjust_light_time = findViewById(R.id.tv_first_adjust_light_time);
        tv_second_adjust_light_time = findViewById(R.id.tv_second_adjust_light_time);
        tv_third_adjust_light_time = findViewById(R.id.tv_third_adjust_light_time);
        tv_close_light_time = findViewById(R.id.tv_close_light_time);

        mTagFlowLayout = findViewById(R.id.id_flowlayout);
        tv_confirm = findViewById(R.id.tv_confirm);
        mSwitch = findViewById(R.id.switch_tog);
        mSeekBar = findViewById(R.id.seekbar);
        tv_percent = findViewById(R.id.tv_percent);
    }

    @Override
    protected void initData() {
        mTvTitle.setText("灯杆信息");
        String poleCode = getIntent().getStringExtra("poleCode");
        mPresenter.getPoleInfo2(poleCode);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void registerListener() {
        mIvBack.setOnClickListener(this);
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int switchVal = isChecked ? 1 : 2;
                SwitchRequest switchRequest = new SwitchRequest();
                switchRequest.lampCodes = mLampPoleCode;
                switchRequest.switchVal = switchVal;
                mPresenter.switchOnOff(switchRequest);
            }
        });
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mProgress = progress;
                tv_percent.setText(mProgress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        RxView.clicks(tv_confirm).throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(unit -> {
                    BrightRequest brightRequest = new BrightRequest();
                    brightRequest.lampCodes = mLampPoleCode;
                    brightRequest.brightness = mProgress;
                    mPresenter.dimming(brightRequest);
                });
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
        tv_device_road.setText(poleInfoResponse.data.hlLampPole.road.roadName);
        tv_address.setText(poleInfoResponse.data.hlLampPole.concretePosition);
        tv_jingweidu.setText(poleInfoResponse.data.hlLampPole.baiduLongitude + "," + poleInfoResponse.data.hlLampPole.baiduLatitude);
        tv_control_box.setText(poleInfoResponse.data.hlLampPole.boxName);

        TagAdapter tagAdapter = new TagAdapter(poleInfoResponse.data.lamplist) {
            @Override
            public View getView(FlowLayout parent, int position, Object o) {
                TextView tv = (TextView) View.inflate(PoleInfoActivity.this, R.layout.item_select_tag_view, null);
                tv.setText(poleInfoResponse.data.lamplist.get(position).lampName);
                return tv;
            }
        };
        tagAdapter.setSelectedList(0);
        mLampPoleCode = poleInfoResponse.data.lamplist.get(0).lampCode;
        mPresenter.getLampInfo2(mLampPoleCode);
        mTagFlowLayout.setAdapter(tagAdapter);
        mTagFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                List<Integer> selectPosList = new ArrayList<>(selectPosSet);
                if (selectPosSet.size() == 1) {
                    mLampPoleCode = poleInfoResponse.data.lamplist.get(selectPosList.get(0)).lampCode;
                    mPresenter.getLampInfo2(mLampPoleCode);
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void showLampInfo(LampInfoResponse lampInfoResponse) {
        if (lampInfoResponse.data.hlDeviceData.switchState == 1) {
            tv_condition.setText("关灯");
        } else if (lampInfoResponse.data.hlDeviceData.switchState == 2) {
            tv_condition.setText("开灯");
        } else if (lampInfoResponse.data.hlDeviceData.switchState == 3) {
            tv_condition.setText("故障");
        }

        tv_voltage.setText(txfloat(lampInfoResponse.data.hlDeviceData.voltage, 10) + "V");
        tv_current.setText(txfloat(lampInfoResponse.data.hlDeviceData.curr, 1000) + "A");
        tv_power.setText(txfloat(lampInfoResponse.data.hlDeviceData.power, 10) + "W");

        tv_open_light_time.setText(lampInfoResponse.data.hlDeviceData.pm2d5 + ":" + lampInfoResponse.data.hlDeviceData.pm10);
        tv_first_adjust_light_time.setText(lampInfoResponse.data.hlDeviceData.humidity + "");
        tv_second_adjust_light_time.setText(lampInfoResponse.data.hlDeviceData.noise + "");
        tv_third_adjust_light_time.setText(lampInfoResponse.data.hlDeviceData.railfall + "");
        tv_close_light_time.setText(lampInfoResponse.data.hlDeviceData.windSpeed + ":" + lampInfoResponse.data.hlDeviceData.windDirection);

        mProgress = lampInfoResponse.data.hlDeviceData.brightness;
        mSeekBar.setProgress(mProgress);
        tv_percent.setText(mProgress + "%");
    }

    @Override
    public void switchOnOffSuccess(BaseResponse baseResponse) {
        ToastUtil.showShort(this, baseResponse.msg);
    }

    @Override
    public void dimmingSuccess(BaseResponse baseResponse) {
        ToastUtil.showShort(this, baseResponse.msg);
    }

    @Override
    public void showToast(String msg) {
        ToastUtil.showShort(this, msg);
    }

    private String txfloat(int a, int b) {
        DecimalFormat df = new DecimalFormat("0.00");//设置保留位数
        return df.format((float) a / b);
    }

}