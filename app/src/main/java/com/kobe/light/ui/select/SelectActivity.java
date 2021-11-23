package com.kobe.light.ui.select;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding3.view.RxView;
import com.kobe.lib_base.BaseActivity;
import com.kobe.lib_base.IBasePresenter;
import com.kobe.lib_zxing.zxing.activity.CaptureActivity;
import com.kobe.light.R;
import com.kobe.light.ui.pole_info.PoleInfoActivity;
import com.kobe.light.ui.work_info.WorkSheetListActivity;
import com.zackratos.ultimatebarx.ultimatebarx.UltimateBarX;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class SelectActivity extends BaseActivity {
    private final int REQUEST_CODE_SCAN = 101;

    private ImageView mIvBack;
    private TextView mTvTitle;
    private TextView mTvScanControl;//扫码控制
    private TextView mTvCheckWorkSheet;//查看工单

    @SuppressLint("CheckResult")
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
        return R.layout.activity_select;
    }

    @Override
    protected void initViews() {
        mIvBack = findViewById(R.id.iv_back);
        mTvTitle = findViewById(R.id.tv_title);
        mTvScanControl = findViewById(R.id.tv_scan_control);
        mTvCheckWorkSheet = findViewById(R.id.tv_check_work_sheet);
    }

    @Override
    protected void initData() {
        mIvBack.setVisibility(View.GONE);
        mTvTitle.setText("选择类型");
    }

    @SuppressLint("CheckResult")
    @Override
    protected void registerListener() {
        RxView.clicks(mTvScanControl).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(unit -> {
                    if (!checkPermission(Manifest.permission.CAMERA)) {
                        String[] permissions = new String[]{Manifest.permission.CAMERA};
                        requestPermission(permissions, new OnPermissionsResultListener() {
                            @Override
                            public void OnSuccess() {
                                Intent intent = new Intent(SelectActivity.this, CaptureActivity.class);
                                startActivityForResult(intent, 0);
                            }

                            @Override
                            public void OnFail(List<String> failedPermissionList) {
                                Toast.makeText(SelectActivity.this, "扫描功能需要开启相机权限", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Intent intent = new Intent(SelectActivity.this, CaptureActivity.class);
                        startActivityForResult(intent, REQUEST_CODE_SCAN);
                    }
                });
        RxView.clicks(mTvCheckWorkSheet).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(unit -> {
                    Intent intent = new Intent(this, WorkSheetListActivity.class);
                    startActivity(intent);
                });
    }

    @Override
    public IBasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_SCAN) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    String result = bundle.getString("result");
                    String str1 = result.substring(0, result.indexOf("="));
                    String str2 = result.substring(str1.length() + 1);
                    Intent intent = new Intent(this, PoleInfoActivity.class);
                    intent.putExtra("poleCode", str2);
                    startActivity(intent);
                }
            }
        }
    }

}