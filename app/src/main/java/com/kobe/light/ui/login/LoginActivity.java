package com.kobe.light.ui.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding3.view.RxView;
import com.kobe.lib_base.BaseActivity;
import com.kobe.light.ui.scan.ScanActivity;
import com.kobe.light.R;
import com.kobe.light.request.LoginRequest;
import com.kobe.light.response.LoginResponse;
import com.kobe.light.utils.ToastUtil;
import com.zackratos.ultimatebarx.ultimatebarx.UltimateBarX;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends BaseActivity<LoginContract.presenter> implements LoginContract.view {

    private EditText mEtAccount;
    private EditText mEtPsw;
    private TextView mTvLogin;

    @SuppressLint("CheckResult")
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
        return R.layout.activity_login;
    }

    @Override
    protected void initViews() {
        mEtAccount = findViewById(R.id.et_account);
        mEtPsw = findViewById(R.id.et_psw);
        mTvLogin = findViewById(R.id.tv_login);
    }

    @Override
    protected void initData() {

    }

    @SuppressLint("CheckResult")
    @Override
    protected void registerListener() {
        RxView.clicks(mTvLogin).throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(unit -> {
                    LoginRequest loginBean = new LoginRequest();
                    loginBean.username = mEtAccount.getEditableText().toString();
                    loginBean.password = mEtPsw.getEditableText().toString();
                    mPresenter.login(loginBean);
                });
    }

    @Override
    public LoginContract.presenter initPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    public void loginSuccess(LoginResponse loginResponse) {
        ToastUtil.showShort(this, loginResponse.msg);
        Intent intent = new Intent(this, ScanActivity.class);
        startActivity(intent);
    }

    @Override
    public void loginFailed(LoginResponse loginResponse) {
        ToastUtil.showShort(this, loginResponse.msg);
    }

    @Override
    public void showError() {
        ToastUtil.showShort(this, "登录失败");
    }
}