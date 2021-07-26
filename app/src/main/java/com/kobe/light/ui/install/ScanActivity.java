package com.kobe.light.ui.install;

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
import com.zackratos.ultimatebarx.ultimatebarx.UltimateBarX;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ScanActivity extends BaseActivity implements View.OnClickListener {
    private final int REQUEST_CODE_SCAN = 101;
    private final int REQUEST_CODE_INSTALL = 102;

    private ImageView mIvBack;
    private TextView mTvTitle;
    private TextView mTvScan;

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_scan2;
    }

    @Override
    protected void initViews() {
        mIvBack = findViewById(R.id.iv_back);
        mTvTitle = findViewById(R.id.tv_title);
        mTvScan = findViewById(R.id.tv_scan);
    }

    @Override
    protected void initData() {
        mTvTitle.setText("安装施工");
    }

    @SuppressLint("CheckResult")
    @Override
    protected void registerListener() {
        mIvBack.setOnClickListener(this);
        RxView.clicks(mTvScan).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(unit -> {
                    if (!checkPermission(Manifest.permission.CAMERA)) {
                        String[] permissions = new String[]{Manifest.permission.CAMERA};
                        requestPermission(permissions, new OnPermissionsResultListener() {
                            @Override
                            public void OnSuccess() {
                                Intent intent = new Intent(ScanActivity.this, CaptureActivity.class);
                                startActivityForResult(intent, 0);
                            }

                            @Override
                            public void OnFail(List<String> failedPermissionList) {
                                Toast.makeText(ScanActivity.this, "扫描功能需要开启相机权限", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Intent intent = new Intent(ScanActivity.this, CaptureActivity.class);
                        startActivityForResult(intent, REQUEST_CODE_SCAN);
                    }
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
                    String str = bundle.getString("result");
                    String str1 = str.substring(0, str.indexOf("="));
                    String str2 = str.substring(str1.length() + 1);

                    Intent intent = new Intent(this, InstallActivity.class);
                    intent.putExtra("result", str2);
                    startActivityForResult(intent, REQUEST_CODE_INSTALL);
                }
            }
        }
    }
}
