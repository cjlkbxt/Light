package com.kobe.light.ui.work_info;

import androidx.recyclerview.widget.RecyclerView;

import com.kobe.lib_base.BaseLayLoadFragment;

public class WorkInfoFragment extends BaseLayLoadFragment<WorkInfoContract.presenter> implements WorkInfoContract.view{

    private RecyclerView mRecyclerView;

    @Override
    public void listSuccess() {

    }

    @Override
    public void submitSuccess() {

    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public WorkInfoContract.presenter initPresenter() {
        return null;
    }

    @Override
    public void initViews() {

    }

    @Override
    public void registerListener() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void showLoadingDialog(String msg) {

    }

    @Override
    public void dismissLoadingDialog() {

    }
}
