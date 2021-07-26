package com.kobe.light.ui.install;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding3.view.RxView;
import com.kobe.lib_base.BaseActivity;
import com.kobe.lib_zxing.zxing.activity.CaptureActivity;
import com.kobe.light.R;
import com.kobe.light.constants.DictType;
import com.kobe.light.engine.GlideEngine;
import com.kobe.light.manager.ImageLoaderManager;
import com.kobe.light.request.BindRequest;
import com.kobe.light.response.DeviceResponse;
import com.kobe.light.response.DictBean;
import com.kobe.light.response.DictResponse;
import com.kobe.light.response.SubmitResponse;
import com.kobe.light.ui.check.CheckActivity;
import com.kobe.light.utils.ToastUtil;
import com.zackratos.ultimatebarx.ultimatebarx.UltimateBarX;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class InstallActivity extends BaseActivity<InstallContract.presenter> implements InstallContract.view, View.OnClickListener {
    private final int REQUEST_CODE_SCAN = 101;
    private ImageView mIvBack;
    private TextView mTvTitle;
    private TextView mTvPole; //灯杆
    private TextView mTvRoad; //道路
    private TextView mTvDetailAddress; //详细地址
    private TextView mTvRoadDirect; //道路方向
    private TextView tv_pole_num; //灯头数量
    private TextView tv_a;//灯头设置A
    private TextView tv_b;//灯头设置B
    private TextView tv_c;//灯头设置C
    private TextView tv_d;//灯头设置D
    private TextView tv_e;//灯头设置E
    private TextView tv_f;//灯头设置F
    private LinearLayout ll_a;//灯头设置A
    private LinearLayout ll_b;//灯头设置B
    private LinearLayout ll_c;//灯头设置C
    private LinearLayout ll_d;//灯头设置D
    private LinearLayout ll_e;//灯头设置E
    private LinearLayout ll_f;//灯头设置F
    private LinearLayout mLlContainer;//灯控码
    private TextView tv_lamp_add;//灯控扫描

    private TextView mTvSubmit;

    //道路方向
    private int mRoadDirectCode;

    private String mLampPoleCode;

    //灯头设置A
    private String mRoadTypeA = "";
    private int mRoadTypeACode;
    private String mDirectionA = "";
    private int mDirectionACode;
    //灯头设置B
    private String mRoadTypeB = "";
    private int mRoadTypeBCode;
    private String mDirectionB = "";
    private int mDirectionBCode;
    //灯头设置C
    private String mRoadTypeC = "";
    private int mRoadTypeCCode;
    private String mDirectionC = "";
    private int mDirectionCCode;
    //灯头设置D
    private String mRoadTypeD = "";
    private int mRoadTypeDCode;
    private String mDirectionD = "";
    private int mDirectionDCode;
    //灯头设置E
    private String mRoadTypeE = "";
    private int mRoadTypeECode;
    private String mDirectionE = "";
    private int mDirectionECode;
    //灯头设置F
    private String mRoadTypeF = "";
    private int mRoadTypeFCode;
    private String mDirectionF = "";
    private int mDirectionFCode;

    private ImageView iv_lamp_pic;
    private CardView mCvPic;
    private TextView tv_lamp_code;
    private ImageView iv_scan;

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
        mTvTitle = findViewById(R.id.tv_title);
        mTvPole = findViewById(R.id.tv_pole);
        mTvRoad = findViewById(R.id.tv_road);
        mTvDetailAddress = findViewById(R.id.tv_detail_address);
        mTvRoadDirect = findViewById(R.id.tv_road_direct);
        tv_pole_num = findViewById(R.id.tv_pole_num);
        tv_a = findViewById(R.id.tv_a);
        tv_b = findViewById(R.id.tv_b);
        tv_c = findViewById(R.id.tv_c);
        tv_d = findViewById(R.id.tv_d);
        tv_e = findViewById(R.id.tv_e);
        tv_f = findViewById(R.id.tv_f);
        ll_a = findViewById(R.id.ll_a);
        ll_b = findViewById(R.id.ll_b);
        ll_c = findViewById(R.id.ll_c);
        ll_d = findViewById(R.id.ll_d);
        ll_e = findViewById(R.id.ll_e);
        ll_f = findViewById(R.id.ll_f);
        mLlContainer = findViewById(R.id.ll_container);
        mTvSubmit = findViewById(R.id.tv_submit);
        tv_lamp_add = findViewById(R.id.tv_lamp_add);
        iv_lamp_pic = findViewById(R.id.iv_lamp_pic);
        mCvPic = findViewById(R.id.cv_pic);
        tv_lamp_code = findViewById(R.id.tv_lamp_code);
        iv_scan = findViewById(R.id.iv_scan);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initData() {
        mTvTitle.setText("扫码结果");
        RxView.clicks(iv_scan).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(unit -> {
                    if (!checkPermission(Manifest.permission.CAMERA)) {
                        String[] permissions = new String[]{Manifest.permission.CAMERA};
                        requestPermission(permissions, new OnPermissionsResultListener() {
                            @Override
                            public void OnSuccess() {
                                Intent intent = new Intent(InstallActivity.this, CaptureActivity.class);
                                startActivityForResult(intent, 0);
                            }

                            @Override
                            public void OnFail(List<String> failedPermissionList) {
                                Toast.makeText(InstallActivity.this, "扫描功能需要开启相机权限", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Intent intent = new Intent(InstallActivity.this, CaptureActivity.class);
                        startActivityForResult(intent, REQUEST_CODE_SCAN);
                    }
                });
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
                    bindRequest.lampCode = tv_lamp_code.getText().toString();
                    bindRequest.lampPoleCode = mLampPoleCode;
                    mPresenter.bind(bindRequest);
                });
//        tv_lamp_add.setOnClickListener(this);
    }

    @Override
    public InstallContract.presenter initPresenter() {
        return new InstallPresenter(this);
    }

    private List<String> aa = new ArrayList<>();

    @Override
    public void showPoleInfo(DeviceResponse deviceResponse) {
        if (TextUtils.isEmpty(deviceResponse.data.lampPic)) {
            mCvPic.setVisibility(View.GONE);
        } else {
            mCvPic.setVisibility(View.VISIBLE);
            List<String> picList = Arrays.asList(deviceResponse.data.lampPic.replace("[", "").replace("]", "").split(","));
            ImageLoaderManager.getInstance().displayImageForView(iv_lamp_pic, picList.get(0));
        }
        mLampPoleCode = deviceResponse.data.poleCode;
        mTvPole.setText(deviceResponse.data.lampPoleName);
        mTvRoad.setText(deviceResponse.data.road.roadName);
        mTvDetailAddress.setText(deviceResponse.data.addressInfo);
        mRoadDirectCode = deviceResponse.data.roadDirectType;
        mPresenter.getDict(DictType.ROAD_DIRECTION_TYPE);//道路方向
        mPresenter.getDict(DictType.IRRADIATE_ROAD);//照射车道
        aa.addAll(Arrays.asList(deviceResponse.data.lampHolderSetting.split(",")));
        tv_pole_num.setText(aa.size() + "");
        if (aa.size() == 1) {
            for (int i = 0; i < aa.size(); i++) {
                mRoadTypeACode = Integer.parseInt(Arrays.asList(aa.get(0).split("\\|")).get(0));
                mDirectionACode = Integer.parseInt(Arrays.asList(aa.get(0).split("\\|")).get(1));
            }
            ll_a.setVisibility(View.VISIBLE);
            ll_b.setVisibility(View.GONE);
            ll_c.setVisibility(View.GONE);
            ll_d.setVisibility(View.GONE);
            ll_e.setVisibility(View.GONE);
            ll_f.setVisibility(View.GONE);
        } else if (aa.size() == 2) {
            for (int i = 0; i < aa.size(); i++) {
                mRoadTypeACode = Integer.parseInt(Arrays.asList(aa.get(0).split("\\|")).get(0));
                mDirectionACode = Integer.parseInt(Arrays.asList(aa.get(0).split("\\|")).get(1));
                mRoadTypeBCode = Integer.parseInt(Arrays.asList(aa.get(1).split("\\|")).get(0));
                mDirectionBCode = Integer.parseInt(Arrays.asList(aa.get(1).split("\\|")).get(1));
            }
            ll_a.setVisibility(View.VISIBLE);
            ll_b.setVisibility(View.VISIBLE);
            ll_c.setVisibility(View.GONE);
            ll_d.setVisibility(View.GONE);
            ll_e.setVisibility(View.GONE);
            ll_f.setVisibility(View.GONE);
        } else if (aa.size() == 3) {
            for (int i = 0; i < aa.size(); i++) {
                mRoadTypeACode = Integer.parseInt(Arrays.asList(aa.get(0).split("\\|")).get(0));
                mDirectionACode = Integer.parseInt(Arrays.asList(aa.get(0).split("\\|")).get(1));
                mRoadTypeBCode = Integer.parseInt(Arrays.asList(aa.get(1).split("\\|")).get(0));
                mDirectionBCode = Integer.parseInt(Arrays.asList(aa.get(1).split("\\|")).get(1));
                mRoadTypeCCode = Integer.parseInt(Arrays.asList(aa.get(2).split("\\|")).get(0));
                mDirectionCCode = Integer.parseInt(Arrays.asList(aa.get(2).split("\\|")).get(1));
            }
            ll_a.setVisibility(View.VISIBLE);
            ll_b.setVisibility(View.VISIBLE);
            ll_c.setVisibility(View.VISIBLE);
            ll_d.setVisibility(View.GONE);
            ll_e.setVisibility(View.GONE);
            ll_f.setVisibility(View.GONE);
        } else if (aa.size() == 4) {
            for (int i = 0; i < aa.size(); i++) {
                mRoadTypeACode = Integer.parseInt(Arrays.asList(aa.get(0).split("\\|")).get(0));
                mDirectionACode = Integer.parseInt(Arrays.asList(aa.get(0).split("\\|")).get(1));
                mRoadTypeBCode = Integer.parseInt(Arrays.asList(aa.get(1).split("\\|")).get(0));
                mDirectionBCode = Integer.parseInt(Arrays.asList(aa.get(1).split("\\|")).get(1));
                mRoadTypeCCode = Integer.parseInt(Arrays.asList(aa.get(2).split("\\|")).get(0));
                mDirectionCCode = Integer.parseInt(Arrays.asList(aa.get(2).split("\\|")).get(1));
                mRoadTypeDCode = Integer.parseInt(Arrays.asList(aa.get(3).split("\\|")).get(0));
                mDirectionDCode = Integer.parseInt(Arrays.asList(aa.get(3).split("\\|")).get(1));
            }
            ll_a.setVisibility(View.VISIBLE);
            ll_b.setVisibility(View.VISIBLE);
            ll_c.setVisibility(View.VISIBLE);
            ll_d.setVisibility(View.VISIBLE);
            ll_e.setVisibility(View.GONE);
            ll_f.setVisibility(View.GONE);
        } else if (aa.size() == 5) {
            for (int i = 0; i < aa.size(); i++) {
                mRoadTypeACode = Integer.parseInt(Arrays.asList(aa.get(0).split("\\|")).get(0));
                mDirectionACode = Integer.parseInt(Arrays.asList(aa.get(0).split("\\|")).get(1));
                mRoadTypeBCode = Integer.parseInt(Arrays.asList(aa.get(1).split("\\|")).get(0));
                mDirectionBCode = Integer.parseInt(Arrays.asList(aa.get(1).split("\\|")).get(1));
                mRoadTypeCCode = Integer.parseInt(Arrays.asList(aa.get(2).split("\\|")).get(0));
                mDirectionCCode = Integer.parseInt(Arrays.asList(aa.get(2).split("\\|")).get(1));
                mRoadTypeDCode = Integer.parseInt(Arrays.asList(aa.get(3).split("\\|")).get(0));
                mDirectionDCode = Integer.parseInt(Arrays.asList(aa.get(3).split("\\|")).get(1));
                mRoadTypeECode = Integer.parseInt(Arrays.asList(aa.get(4).split("\\|")).get(0));
                mDirectionECode = Integer.parseInt(Arrays.asList(aa.get(4).split("\\|")).get(1));
            }
            ll_a.setVisibility(View.VISIBLE);
            ll_b.setVisibility(View.VISIBLE);
            ll_c.setVisibility(View.VISIBLE);
            ll_d.setVisibility(View.VISIBLE);
            ll_e.setVisibility(View.VISIBLE);
            ll_f.setVisibility(View.GONE);
        } else if (aa.size() == 6) {
            for (int i = 0; i < aa.size(); i++) {
                mRoadTypeACode = Integer.parseInt(Arrays.asList(aa.get(0).split("\\|")).get(0));
                mDirectionACode = Integer.parseInt(Arrays.asList(aa.get(0).split("\\|")).get(1));
                mRoadTypeBCode = Integer.parseInt(Arrays.asList(aa.get(1).split("\\|")).get(0));
                mDirectionBCode = Integer.parseInt(Arrays.asList(aa.get(1).split("\\|")).get(1));
                mRoadTypeCCode = Integer.parseInt(Arrays.asList(aa.get(2).split("\\|")).get(0));
                mDirectionCCode = Integer.parseInt(Arrays.asList(aa.get(2).split("\\|")).get(1));
                mRoadTypeDCode = Integer.parseInt(Arrays.asList(aa.get(3).split("\\|")).get(0));
                mDirectionDCode = Integer.parseInt(Arrays.asList(aa.get(3).split("\\|")).get(1));
                mRoadTypeECode = Integer.parseInt(Arrays.asList(aa.get(4).split("\\|")).get(0));
                mDirectionECode = Integer.parseInt(Arrays.asList(aa.get(4).split("\\|")).get(1));
                mRoadTypeFCode = Integer.parseInt(Arrays.asList(aa.get(5).split("\\|")).get(0));
                mDirectionFCode = Integer.parseInt(Arrays.asList(aa.get(5).split("\\|")).get(1));
            }
            ll_a.setVisibility(View.VISIBLE);
            ll_b.setVisibility(View.VISIBLE);
            ll_c.setVisibility(View.VISIBLE);
            ll_d.setVisibility(View.VISIBLE);
            ll_e.setVisibility(View.VISIBLE);
            ll_f.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void showRoadDirectInfo(DictResponse dictResponse) {
        for (DictBean dictBean : dictResponse.data) {
            if (dictBean.dictCode == mRoadDirectCode) {
                mTvRoadDirect.setText(dictBean.dictLabel);
                break;
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void showShineCarRoadInfo(DictResponse dictResponse) {
        mPresenter.getDict(DictType.SHINE_DIRECTION);//照射方向
        for (DictBean dictBean : dictResponse.data) {
            if (dictBean.dictCode == mRoadTypeACode) {
                mRoadTypeA = dictBean.dictLabel;
            }
            if (dictBean.dictCode == mRoadTypeBCode) {
                mRoadTypeB = dictBean.dictLabel;
            }
            if (dictBean.dictCode == mRoadTypeCCode) {
                mRoadTypeC = dictBean.dictLabel;
            }
            if (dictBean.dictCode == mRoadTypeDCode) {
                mRoadTypeD = dictBean.dictLabel;
            }
            if (dictBean.dictCode == mRoadTypeECode) {
                mRoadTypeE = dictBean.dictLabel;
            }
            if (dictBean.dictCode == mRoadTypeFCode) {
                mRoadTypeF = dictBean.dictLabel;
            }
        }
    }

    @Override
    public void showShineDirectInfo(DictResponse dictResponse) {
        for (DictBean dictBean : dictResponse.data) {
            if (dictBean.dictCode == mDirectionACode) {
                mDirectionA = dictBean.dictLabel;
            }
            if (dictBean.dictCode == mDirectionBCode) {
                mDirectionB = dictBean.dictLabel;
            }
            if (dictBean.dictCode == mDirectionCCode) {
                mDirectionC = dictBean.dictLabel;
            }
            if (dictBean.dictCode == mDirectionDCode) {
                mDirectionD = dictBean.dictLabel;
            }
            if (dictBean.dictCode == mDirectionECode) {
                mDirectionE = dictBean.dictLabel;
            }
            if (dictBean.dictCode == mDirectionFCode) {
                mDirectionF = dictBean.dictLabel;
            }
        }
        tv_a.setText(mRoadTypeA + "/" + mDirectionA);
        tv_b.setText(mRoadTypeB + "/" + mDirectionB);
        tv_c.setText(mRoadTypeC + "/" + mDirectionC);
        tv_d.setText(mRoadTypeD + "/" + mDirectionD);
        tv_e.setText(mRoadTypeE + "/" + mDirectionE);
        tv_f.setText(mRoadTypeF + "/" + mDirectionF);
    }

    @Override
    public void bindSuccess(SubmitResponse submitResponse) {
        ToastUtil.showShort(this, submitResponse.msg);
        finish();
        setResult(666);
    }

    @Override
    public void bindError(SubmitResponse submitResponse) {
        ToastUtil.showShort(this, submitResponse.msg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_lamp_add:
                View view = LayoutInflater.from(this).inflate(R.layout.item_lamp, null);
                mLlContainer.addView(view);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_SCAN) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    String result = bundle.getString("result");
                    if (!TextUtils.isEmpty(result)) {
                        mTvSubmit.setEnabled(true);
                        tv_lamp_code.setText(result);
                    } else {
                        mTvSubmit.setEnabled(false);
                    }
                }
            }
        }
    }
}
