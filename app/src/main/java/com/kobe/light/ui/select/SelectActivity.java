package com.kobe.light.ui.select;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding3.view.RxView;
import com.kobe.lib_base.BaseActivity;
import com.kobe.lib_base.IBasePresenter;
import com.kobe.light.R;
import com.kobe.light.ui.check.CheckActivity;
import com.kobe.light.ui.install.ScanActivity;
import com.zackratos.ultimatebarx.ultimatebarx.UltimateBarX;

import java.util.concurrent.TimeUnit;

public class SelectActivity extends BaseActivity {

    private ImageView mIvBack;
    private TextView mTvTitle;
    private TextView mTvCheck;
    private TextView mTvInstall;

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
        return R.layout.activity_select_work_type;
    }

    @Override
    protected void initViews() {
        mIvBack = findViewById(R.id.iv_back);
        mTvTitle = findViewById(R.id.tv_title);
        mTvCheck = findViewById(R.id.tv_check);
        mTvInstall = findViewById(R.id.tv_install);
    }

    @Override
    protected void initData() {
        mIvBack.setVisibility(View.GONE);
        mTvTitle.setText("选择工作类型");
    }

    @SuppressLint("CheckResult")
    @Override
    protected void registerListener() {
        RxView.clicks(mTvCheck).throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(unit -> {
                    Intent intent = new Intent(this, CheckActivity.class);
                    startActivity(intent);
                });
        RxView.clicks(mTvInstall).throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(unit -> {
                    Intent intent = new Intent(this, ScanActivity.class);
                    startActivity(intent);
                });
    }

    @Override
    public IBasePresenter initPresenter() {
        return null;
    }

}