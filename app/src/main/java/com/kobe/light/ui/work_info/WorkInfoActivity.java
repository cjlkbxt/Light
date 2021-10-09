package com.kobe.light.ui.work_info;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.kobe.lib_base.BaseActivity;
import com.kobe.light.R;
import com.kobe.light.adapter.MyFragmentPagerAdapter;
import com.kobe.light.request.SubmitRequest2;
import com.kobe.light.response.BaseResponse;
import com.kobe.light.response.DeviceResponse;
import com.kobe.light.response.DictResponse;
import com.kobe.light.response.ListResponse;
import com.kobe.light.response.WorkSheetBean;
import com.kobe.light.ui.pole_info.PoleInfoContract;
import com.kobe.light.ui.pole_info.PoleInfoPresenter;
import com.zackratos.ultimatebarx.ultimatebarx.UltimateBarX;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WorkInfoActivity extends BaseActivity<WorkInfoContract.presenter> implements View.OnClickListener, WorkInfoContract.view {

    private ImageView mIvBack;
    private TextView mTitle;
    private SlidingTabLayout mTabLayout;
    private ViewPager mViewPager;

    private final ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private final ArrayList<String> mFragmentTitleList = new ArrayList<>();
    private WorkInfoFragment mAllWorkInfoFragment;
    private WorkInfoFragment mWorkInfoFragment;
    private WorkInfoFragment mWorkInfoFragment2;


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
        return R.layout.activity_work_info;
    }

    @Override
    protected void initViews() {
        mIvBack = findViewById(R.id.iv_back);
        mTitle = findViewById(R.id.tv_title);
        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_pager);
    }

    @Override
    protected void initData() {
        mTitle.setText("工单详情");
        mPresenter.list();

    }

    @SuppressLint("CheckResult")
    @Override
    protected void registerListener() {
//        mIvBack.setOnClickListener(this);
    }

    @Override
    public WorkInfoContract.presenter initPresenter() {
        return new WorkInfoPresenter(this);
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
    public void showListResponse(ListResponse listResponse) {
        mFragmentList.clear();
        List<WorkSheetBean> list = new ArrayList<>();
        List<WorkSheetBean> list2 = new ArrayList<>();
        for (WorkSheetBean workSheetBean : listResponse.data.list) {
            if (TextUtils.equals(workSheetBean.checkStatus, "1")) {
                list.add(workSheetBean);
            } else {
                list2.add(workSheetBean);
            }
        }
        if (mAllWorkInfoFragment == null) {
            mAllWorkInfoFragment = new WorkInfoFragment();
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("list", (Serializable) listResponse.data.list);
        mAllWorkInfoFragment.setArguments(bundle);
        addTabFragment(mAllWorkInfoFragment, "全部");


        if (mWorkInfoFragment == null) {
            mWorkInfoFragment = new WorkInfoFragment();
        }
        Bundle bundle2 = new Bundle();
        bundle2.putSerializable("list", (Serializable) list);
        mWorkInfoFragment.setArguments(bundle2);
        addTabFragment(mWorkInfoFragment, "待维修");


        if (mWorkInfoFragment2 == null) {
            mWorkInfoFragment2 = new WorkInfoFragment();
        }
        Bundle bundle3 = new Bundle();
        bundle3.putSerializable("list", (Serializable) list2);
        mWorkInfoFragment2.setArguments(bundle3);
        addTabFragment(mWorkInfoFragment2, "已维修");

        MyFragmentPagerAdapter myPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragmentTitleList, mFragmentList);
        mViewPager.setAdapter(myPagerAdapter);
        mTabLayout.setViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(2);
    }

    private void addTabFragment(Fragment fragment, String fragmentTitle) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(fragmentTitle);
    }
}