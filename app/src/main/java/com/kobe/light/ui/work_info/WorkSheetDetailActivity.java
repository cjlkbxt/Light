package com.kobe.light.ui.work_info;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.callback.SelectCallback;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.jakewharton.rxbinding3.view.RxView;
import com.kobe.lib_base.BaseActivity;
import com.kobe.light.R;
import com.kobe.light.adapter.UploadAdapter;
import com.kobe.light.engine.GlideEngine;
import com.kobe.light.manager.ImageLoaderManager;
import com.kobe.light.request.BindRequest;
import com.kobe.light.request.SubmitRequest2;
import com.kobe.light.response.UploadResponse;
import com.kobe.light.response.WorkSheetBean;
import com.kobe.light.ui.check.CheckActivity;
import com.kobe.light.ui.check.PicPreviewActivity;
import com.kobe.light.utils.MapUtils;
import com.kobe.light.utils.ToastUtil;
import com.zackratos.ultimatebarx.ultimatebarx.UltimateBarX;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class WorkSheetDetailActivity extends BaseActivity<WorkSheetDetailContract.presenter> implements View.OnClickListener, WorkSheetDetailContract.view {

    private ImageView mIvBack;
    private TextView mTitle;

    private TextView tv_light_tool;//灯具
    private TextView tv_lamp_pole;//灯杆
    private TextView tv_control_box;//控制箱
    private TextView tv_fix_status;//维修状态
    private TextView tv_road;//道路
    private TextView tv_jingweidu;//经纬度
    private TextView tv_daohang;//导航

    private TextView tv_fault_type;//故障类型
    private EditText et_solve_method;//解决方法

    private LinearLayout ll_upload;

    private RecyclerView mRecyclerView;

    private TextView mTvSubmit;
    private LinearLayout ll_container;

    private WorkSheetBean mWorkSheetBean;

    private UploadAdapter mAdapter;

    private final List<String> mFilePathList = new ArrayList<>();

    private String mImageUrl;

    private LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UltimateBarX.with(this)
                .fitWindow(true)
                .color(Color.WHITE)
                .light(true)
                .applyStatusBar();
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_daohang:
                if (!TextUtils.isEmpty(mWorkSheetBean.baiduLatitude)) {
                    if (!mLocationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
//                        ToastUtil.showShort(this, "请开启GPS");
                        // 转到手机设置界面，用户设置GPS
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    } else {
                        MapUtils.startGuide2(this, mWorkSheetBean.baiduLatitude, mWorkSheetBean.baiduLongitude);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void submitSuccess() {
        setResult(RESULT_OK);
        finish();
        ToastUtil.showShort(this, "提交成功");
    }

    @Override
    public void showToast(String msg) {
        ToastUtil.showShort(this, msg);
    }

    @Override
    public void uploadSuccess(UploadResponse uploadResponse) {
        for (String url : uploadResponse.data.url) {
            mImageUrl += url + ",";
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_work_sheet_detail;
    }

    @Override
    protected void initViews() {
        mIvBack = findViewById(R.id.iv_back);
        mTitle = findViewById(R.id.tv_title);

        tv_light_tool = findViewById(R.id.tv_light_tool);
        tv_lamp_pole = findViewById(R.id.tv_lamp_pole);
        tv_control_box = findViewById(R.id.tv_control_box);
        tv_fix_status = findViewById(R.id.tv_fix_status);
        tv_road = findViewById(R.id.tv_road);
        tv_jingweidu = findViewById(R.id.tv_jingweidu);
        tv_daohang = findViewById(R.id.tv_daohang);
        tv_fault_type = findViewById(R.id.tv_fault_type);
        et_solve_method = findViewById(R.id.et_solve_method);

        mTvSubmit = findViewById(R.id.tv_submit);
        ll_container = findViewById(R.id.ll_container);
        mRecyclerView = findViewById(R.id.rv_upload);
        ll_upload = findViewById(R.id.ll_upload);
    }

    @Override
    protected void initData() {
        mTitle.setText("工单详情");
        mWorkSheetBean = (WorkSheetBean) getIntent().getSerializableExtra("workSheetBean");

        if (TextUtils.isEmpty(mWorkSheetBean.field5Name)) {
            tv_light_tool.setText("");
        } else {
            tv_light_tool.setText(mWorkSheetBean.field5Name);
        }
        if (TextUtils.isEmpty(mWorkSheetBean.field4Name)) {
            tv_lamp_pole.setText("");
        } else {
            tv_lamp_pole.setText(mWorkSheetBean.field4Name);
        }
        if (TextUtils.isEmpty(mWorkSheetBean.field3Name)) {
            tv_control_box.setText("");
        } else {
            tv_control_box.setText(mWorkSheetBean.field3Name);
        }
        if (TextUtils.equals(mWorkSheetBean.checkStatus, "0")) {
            tv_fix_status.setText("待维修");
            mTvSubmit.setVisibility(View.VISIBLE);
            et_solve_method.setEnabled(true);
            mRecyclerView.setVisibility(View.VISIBLE);
            ll_upload.setVisibility(View.VISIBLE);
        } else {
            tv_fix_status.setText("已维修");
            mTvSubmit.setVisibility(View.GONE);
            et_solve_method.setEnabled(false);
            mRecyclerView.setVisibility(View.GONE);
            ll_upload.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(mWorkSheetBean.field1Name)) {
            tv_road.setText("");
        } else {
            tv_road.setText(mWorkSheetBean.field1Name);
        }
        if (TextUtils.isEmpty(mWorkSheetBean.baiduLongitude)) {
            tv_jingweidu.setText("");
        } else {
            tv_jingweidu.setText(mWorkSheetBean.baiduLongitude + "\n" + mWorkSheetBean.baiduLatitude);
        }
        if (TextUtils.isEmpty(mWorkSheetBean.billCause)) {
            tv_fault_type.setText("");
        } else {
            tv_fault_type.setText(mWorkSheetBean.billCause);
        }

        if (!TextUtils.isEmpty(mWorkSheetBean.billRemarks)) {
            ll_container.removeAllViews();
            for (String url : mWorkSheetBean.billRemarks.split(",")) {
                ImageView imageView = new ImageView(this);
                ImageLoaderManager.getInstance().displayImageForView(imageView, url);
                ll_container.addView(imageView);
            }
        }

        et_solve_method.setText(mWorkSheetBean.remarks);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mAdapter = new UploadAdapter(mFilePathList, this);
        mAdapter.setMaxImages(8);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mAdapter.setOnItemClickListener(new UploadAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(WorkSheetDetailActivity.this, PicPreviewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url", mFilePathList.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onItemDeleteClick(View view, int position) {
                mFilePathList.remove(position);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onItemAddClick(View view, int position) {
                EasyPhotos.createAlbum(WorkSheetDetailActivity.this, true, false, GlideEngine.getInstance())
                        .setFileProviderAuthority("com.kobe.light2.fileprovider")
                        .setCount(8)
                        .start(new SelectCallback() {
                            @Override
                            public void onResult(ArrayList<Photo> photos, boolean isOriginal) {
                                List<MultipartBody.Part> partList = new ArrayList<>();
                                for (int i = 0; i < photos.size(); i++) {
                                    File file = new File(photos.get(i).path);
                                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                                    MultipartBody.Part filePart = MultipartBody.Part.createFormData("files", file.getName(), requestBody);
                                    partList.add(filePart);
                                    mFilePathList.add(photos.get(i).path);
                                }
                                mAdapter.notifyDataSetChanged();
                                mPresenter.upLoad(partList);
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
            }
        });
    }

    @SuppressLint("CheckResult")
    @Override
    protected void registerListener() {
        mIvBack.setOnClickListener(this);
        tv_daohang.setOnClickListener(this);
        RxView.clicks(mTvSubmit).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(unit -> {
                    SubmitRequest2 submitRequest = new SubmitRequest2();
                    submitRequest.billCode = mWorkSheetBean.billCode;
                    if (TextUtils.isEmpty(mImageUrl)) {
                        submitRequest.detailPics = "";
                    } else {
                        submitRequest.detailPics = mImageUrl.substring(0, mImageUrl.length() - 2);
                    }
                    submitRequest.detailMsg = et_solve_method.getEditableText().toString();
                    mPresenter.submitWorkSheet(submitRequest);
                });
        et_solve_method.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    mTvSubmit.setEnabled(false);
                } else {
                    mTvSubmit.setEnabled(true);
                }
            }
        });
    }

    @Override
    public WorkSheetDetailContract.presenter initPresenter() {
        return new WorkSheetDetailPresenter(this);
    }


}
