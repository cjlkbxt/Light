package com.kobe.light.ui.work_info;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jakewharton.rxbinding3.view.RxView;
import com.kobe.lib_base.BaseLayLoadFragment;
import com.kobe.lib_base.IBasePresenter;
import com.kobe.light.R;
import com.kobe.light.response.WorkSheetBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WorkSheetListFragment extends BaseLayLoadFragment {

    private RecyclerView mRecyclerView;
    private List<WorkSheetBean> mList = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.fragment_work_sheet_list;
    }

    @Override
    public IBasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initViews() {
        mRecyclerView = convertView.findViewById(R.id.recycle_view);
    }

    @Override
    public void registerListener() {

    }

    @Override
    public void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mList = (List<WorkSheetBean>) bundle.getSerializable("list");
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        CommonAdapter<WorkSheetBean> mAdapter = new CommonAdapter<WorkSheetBean>(getActivity(), R.layout.item_work_sheet, mList) {
            @SuppressLint("CheckResult")
            @Override
            protected void convert(ViewHolder holder, WorkSheetBean workSheetBean, int position) {
                TextView tv_sheet_no = holder.getView(R.id.tv_sheet_no);
                TextView tv_status = holder.getView(R.id.tv_status);
                TextView tv_address = holder.getView(R.id.tv_address);
                TextView tv_light = holder.getView(R.id.tv_light);
                TextView tv_date = holder.getView(R.id.tv_date);

                tv_sheet_no.setText("维修单号:" + workSheetBean.viewCode);
                if (TextUtils.equals(workSheetBean.checkStatus, "0")) {
                    tv_status.setText("待维修");
                } else {
                    tv_status.setText("已维修");
                }
                if (TextUtils.isEmpty(workSheetBean.field1Name)) {
                    tv_address.setText("所在道路:");
                } else {
                    tv_address.setText("所在道路:" + workSheetBean.field1Name);
                }
                if (TextUtils.isEmpty(workSheetBean.field4Name)) {
                    tv_light.setText("所在灯杆:");
                } else {
                    tv_light.setText("所在灯杆:" + workSheetBean.field4Name);
                }
                if (TextUtils.isEmpty(workSheetBean.billDate)) {
                    tv_date.setText("报修时间:");
                } else {
                    tv_date.setText("报修时间:" + workSheetBean.billDate);
                }

                RxView.clicks(holder.itemView)
                        .throttleFirst(1, TimeUnit.SECONDS)
                        .subscribe(unit -> {
                            Intent intent = new Intent(getActivity(), WorkSheetDetailActivity.class);
                            intent.putExtra("workSheetBean", workSheetBean);
                            startActivityForResult(intent, 111);
                        });
            }
        };
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void showLoadingDialog(String msg) {

    }

    @Override
    public void dismissLoadingDialog() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 111) {

            }
        }
    }
}
