package com.kobe.light.ui.work_info;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class WorkInfoFragment extends BaseLayLoadFragment {

    private RecyclerView mRecyclerView;
    private List<WorkSheetBean> list = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.fragment_work_info;
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
            list = (List<WorkSheetBean>) bundle.getSerializable("list");
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        CommonAdapter<WorkSheetBean> mAdapter = new CommonAdapter<WorkSheetBean>(getActivity(), R.layout.item_work_sheet, list) {
            @SuppressLint("CheckResult")
            @Override
            protected void convert(ViewHolder holder, WorkSheetBean workSheetBean, int position) {
                TextView tv_pole_no = holder.getView(R.id.tv_pole_no);
                TextView tv_device_road = holder.getView(R.id.tv_device_road);

                tv_pole_no.setText(workSheetBean.billDate);
                tv_device_road.setText(workSheetBean.field1Name);

                RxView.clicks(holder.itemView)
                        .throttleFirst(1, TimeUnit.SECONDS)
                        .subscribe(unit -> {
                            Intent intent = new Intent(getActivity(), WorkSheetDetailActivity.class);
                            startActivity(intent);
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
}
