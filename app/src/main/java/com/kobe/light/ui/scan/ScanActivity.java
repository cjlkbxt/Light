package com.kobe.light.ui.scan;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.callback.SelectCallback;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.jakewharton.rxbinding3.view.RxView;
import com.kobe.lib_base.BaseActivity;
import com.kobe.lib_base.CustomBottomDialog;
import com.kobe.lib_zxing.zxing.activity.CaptureActivity;
import com.kobe.light.LightApplication;
import com.kobe.light.R;
import com.kobe.light.adapter.UploadAdapter;
import com.kobe.light.constants.DictType;
import com.kobe.light.engine.GlideEngine;
import com.kobe.light.location.LocationService;
import com.kobe.light.request.SubmitRequest;
import com.kobe.light.response.DeviceResponse;
import com.kobe.light.response.DictBean;
import com.kobe.light.response.DictResponse;
import com.kobe.light.response.SubmitResponse;
import com.kobe.light.response.UploadResponse;
import com.kobe.light.utils.ToastUtil;
import com.zackratos.ultimatebarx.ultimatebarx.UltimateBarX;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ScanActivity extends BaseActivity<ScanContract.presenter> implements View.OnClickListener, ScanContract.view {
    private final int REQUEST_CODE_SCAN = 101;
    private final int REQUEST_CODE_OPEN_GPS = 102;
    private static final double EARTH_RADIUS = 6378137.0;
    private ImageView mIvBack;
    private ImageView mIvScan;
    private TextView tv_device_road;//设备道路
    private TextView tv_device_num;//设备编号
    private TextView tv_road_range;//道路范围
    private EditText et_detail_address;//详细地址
    private TextView tv_light_port;//灯杆
    private TextView tv_to_position;//GPS精准定位
    private TextView tv_road_direct;//道路方向
    private TextView tv_pole_position;//灯杆位置
    private TextView tv_shine_road;//照射道路
    private TextView tv_pole_num;//灯头数量设置
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
    private EditText et_remark;//备注
    private TextView mTvSubmit;
    private TextView tv_gps_address;

    private CustomBottomDialog mRoadDirectDialog;//道路方向
    private CustomBottomDialog mPolePositionDialog;//灯杆位置
    private CustomBottomDialog mShineRoadDialog;//照射道路

    private CustomBottomDialog mPoleNumDialog;
    private CustomBottomDialog mLightSetDialog;
    private RecyclerView mRecyclerView;

    private final List<String> mFilePathList = new ArrayList<>();

    private String mPoleCode;
    private int mLampPoleType;
    private String mLampPoleName;

    private String mGpsLatitude;//纬度
    private String mGpsLongitude;//经度

    //道路方向
    private String mRoadDirectStr;
    private int mRoadDirectCode;
    private final List<DictBean> mRoadDirectDictList = new ArrayList<>();

    //灯杆位置
    private String mPolePositionStr;
    private int mPolePositionCode;
    private final List<DictBean> mPolePositionDictList = new ArrayList<>();

    //照射道路
    private String mShineRoadStr;
    private int mShineRoadCode;
    private final List<DictBean> mShineRoadDictList = new ArrayList<>();

    //灯头数量
    private int mPoleNum = 1;
    private final List<Integer> mPoleNumList = new ArrayList<>();
    private final List<DictBean> mShineCarRoadDictList = new ArrayList<>();
    private final List<DictBean> mShineDirectDictList = new ArrayList<>();
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

    private final List<String> mImageUrlList = new ArrayList<>();
    private UploadAdapter mAdapter;
    private LocationManager mLocationManager;

    private LocationService mLocationService;

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
        return R.layout.activity_scan;
    }

    @Override
    protected void initViews() {
        mIvBack = findViewById(R.id.iv_back);
        mIvScan = findViewById(R.id.iv_scan);
        tv_device_road = findViewById(R.id.tv_device_road);
        tv_device_num = findViewById(R.id.tv_device_num);
        tv_road_range = findViewById(R.id.tv_road_range);
        et_detail_address = findViewById(R.id.et_detail_address);
        tv_light_port = findViewById(R.id.tv_light_port);
        tv_to_position = findViewById(R.id.tv_to_position);
        tv_to_position.setOnClickListener(this);
        tv_road_direct = findViewById(R.id.tv_road_direct);
        tv_pole_position = findViewById(R.id.tv_pole_position);
        tv_shine_road = findViewById(R.id.tv_shine_road);
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
        et_remark = findViewById(R.id.et_remark);
        mRecyclerView = findViewById(R.id.rv_upload);
        mTvSubmit = findViewById(R.id.tv_submit);
        tv_gps_address = findViewById(R.id.tv_gps_address);
    }

    @Override
    protected void initData() {
        mPresenter.getDict(DictType.ROAD_DIRECTION_TYPE);//道路方向
        mPresenter.getDict(DictType.IRRADIATE_ROAD);//照射车道
        mPresenter.getDict(DictType.SHINE_DIRECTION);//照射方向
        mPresenter.getDict(DictType.IRRADIATE_ROAD_TYPE);//照射道路
        mPresenter.getDict(DictType.LAMP_POLE_POSITION);//灯杆位置
        mPoleNumList.add(1);
        mPoleNumList.add(2);
        mPoleNumList.add(3);
        mPoleNumList.add(4);
        mPoleNumList.add(5);
        mPoleNumList.add(6);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mAdapter = new UploadAdapter(mFilePathList, this);
        mAdapter.setMaxImages(8);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mAdapter.setOnItemClickListener(new UploadAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(ScanActivity.this, PicPreviewActivity.class);
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
                EasyPhotos.createAlbum(ScanActivity.this, true, false, GlideEngine.getInstance())
                        .setFileProviderAuthority("com.kobe.light.fileprovider")
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
        tv_road_direct.setOnClickListener(this);
        tv_pole_position.setOnClickListener(this);
        tv_shine_road.setOnClickListener(this);
        tv_pole_num.setOnClickListener(this);
        tv_a.setOnClickListener(this);
        tv_b.setOnClickListener(this);
        tv_c.setOnClickListener(this);
        tv_d.setOnClickListener(this);
        tv_e.setOnClickListener(this);
        tv_f.setOnClickListener(this);
        RxView.clicks(mIvScan).throttleFirst(1, TimeUnit.SECONDS)
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
        RxView.clicks(mTvSubmit).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(unit -> {
                    String lampHolderSetting = "";
                    if (mPoleNum == 1) {
                        lampHolderSetting = mRoadTypeACode + "|" + mDirectionACode;
                    } else if (mPoleNum == 2) {
                        lampHolderSetting = mRoadTypeACode + "|" + mDirectionACode + ","
                                + mRoadTypeBCode + "|" + mDirectionBCode;
                    } else if (mPoleNum == 3) {
                        lampHolderSetting = mRoadTypeACode + "|" + mDirectionACode + ","
                                + mRoadTypeBCode + "|" + mDirectionBCode + ","
                                + mRoadTypeCCode + "|" + mDirectionCCode;
                    } else if (mPoleNum == 4) {
                        lampHolderSetting = mRoadTypeACode + "|" + mDirectionACode + ","
                                + mRoadTypeBCode + "|" + mDirectionBCode + ","
                                + mRoadTypeCCode + "|" + mDirectionCCode + ","
                                + mRoadTypeDCode + "|" + mDirectionDCode;
                    } else if (mPoleNum == 5) {
                        lampHolderSetting = mRoadTypeACode + "|" + mDirectionACode + ","
                                + mRoadTypeBCode + "|" + mDirectionBCode + ","
                                + mRoadTypeCCode + "|" + mDirectionCCode + ","
                                + mRoadTypeDCode + "|" + mDirectionDCode + ","
                                + mRoadTypeECode + "|" + mDirectionECode;
                    } else if (mPoleNum == 6) {
                        lampHolderSetting = mRoadTypeACode + "|" + mDirectionACode + ","
                                + mRoadTypeBCode + "|" + mDirectionBCode + ","
                                + mRoadTypeCCode + "|" + mDirectionCCode + ","
                                + mRoadTypeDCode + "|" + mDirectionDCode + ","
                                + mRoadTypeECode + "|" + mDirectionECode + ","
                                + mRoadTypeFCode + "|" + mDirectionFCode;
                    }
                    SubmitRequest submitRequest = new SubmitRequest();
                    submitRequest.lampPoleName = mLampPoleName;
                    submitRequest.addressInfo = et_detail_address.getEditableText().toString();
                    submitRequest.concretePosition = tv_road_range.getText().toString();
                    submitRequest.gpsLatitude = mGpsLatitude;
                    submitRequest.gpsLongitude = mGpsLongitude;
                    submitRequest.lampPoleType = mLampPoleType;
                    submitRequest.roadDirectType = mRoadDirectCode;
                    submitRequest.pic = mImageUrlList.toString();
                    submitRequest.poleCode = mPoleCode;
                    submitRequest.lampHolderSetting = lampHolderSetting;
                    submitRequest.remarks = et_remark.getEditableText().toString();
                    submitRequest.lampNums = mPoleNum;
                    submitRequest.roadType = mShineRoadCode;
                    submitRequest.polePosition = mPolePositionCode;
                    mPresenter.submit(submitRequest);
                });
    }

    @Override
    public ScanContract.presenter initPresenter() {
        return new ScanPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_to_position:
                // 判断GPS模块是否开启，如果没有则开启
                if (!mLocationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
                    ToastUtil.showShort(ScanActivity.this, "请开启GPS");
                    // 转到手机设置界面，用户设置GPS
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, REQUEST_CODE_OPEN_GPS); // 设置完成后返回到原来的界面
                } /*else {
                    startLocation();
                }*/

                break;
            case R.id.tv_road_direct:
                showRoadDirectDialog();
                break;
            case R.id.tv_pole_position:
                showPolePositionDialog();
                break;
            case R.id.tv_shine_road:
                showShineRoadDialog();
                break;
            case R.id.tv_pole_num:
                showPoleNumDialog();
                break;
            case R.id.tv_a:
                showLightSetDialog("A");
                break;
            case R.id.tv_b:
                showLightSetDialog("B");
                break;
            case R.id.tv_c:
                showLightSetDialog("C");
                break;
            case R.id.tv_d:
                showLightSetDialog("D");
                break;
            case R.id.tv_e:
                showLightSetDialog("E");
                break;
            case R.id.tv_f:
                showLightSetDialog("F");
                break;
            default:
                break;
        }
    }

    //道路方向弹窗
    private void showRoadDirectDialog() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_device, null);
        TextView tvConfirm = contentView.findViewById(R.id.tv_confirm);
        TagFlowLayout layoutTag = contentView.findViewById(R.id.id_flowlayout);
        TagAdapter tagAdapter = new TagAdapter(mRoadDirectDictList) {
            @Override
            public View getView(FlowLayout parent, int position, Object o) {
                TextView tv = (TextView) View.inflate(ScanActivity.this, R.layout.item_select_tag_view, null);
                tv.setText(mRoadDirectDictList.get(position).dictLabel);
                return tv;
            }
        };
        if (!TextUtils.isEmpty(mRoadDirectStr)) {
            for (int i = 0; i < mRoadDirectDictList.size(); i++) {
                if (TextUtils.equals(mRoadDirectStr, mRoadDirectDictList.get(i).dictLabel)) {
                    //预先设置选中
                    tagAdapter.setSelectedList(i);
                    tvConfirm.setEnabled(true);
                    break;
                }
            }
        } else {
            tvConfirm.setEnabled(false);
        }

        layoutTag.setAdapter(tagAdapter);
        layoutTag.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                List<Integer> selectPosList = new ArrayList<>(selectPosSet);
                if (selectPosSet.size() == 1) {
                    mRoadDirectStr = mRoadDirectDictList.get(selectPosList.get(0)).dictLabel;
                    mRoadDirectCode = mRoadDirectDictList.get(selectPosList.get(0)).dictCode;
                    tvConfirm.setEnabled(true);
                } else if (selectPosSet.size() == 0) {
                    tvConfirm.setEnabled(false);
                }
            }
        });
        mRoadDirectDialog = new CustomBottomDialog.Builder(this)
                .setContentView(contentView)
                .setText(R.id.tv_title, "道路方向")
                .setGravity(Gravity.BOTTOM)
                .setStyle(R.style.bottom_dialog)
                .setClickListener(R.id.iv_close, v1 -> mRoadDirectDialog.dismiss())
                .setClickListener(R.id.tv_confirm, v2 -> {
                    tv_road_direct.setText(mRoadDirectStr);
                    mRoadDirectDialog.dismiss();
                })
                .setCanCancelable(true)
                .build();
        mRoadDirectDialog.show();
    }

    //灯杆位置弹窗
    private void showPolePositionDialog() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_device, null);
        TextView tvConfirm = contentView.findViewById(R.id.tv_confirm);
        TagFlowLayout layoutTag = contentView.findViewById(R.id.id_flowlayout);
        TagAdapter tagAdapter = new TagAdapter(mPolePositionDictList) {
            @Override
            public View getView(FlowLayout parent, int position, Object o) {
                TextView tv = (TextView) View.inflate(ScanActivity.this, R.layout.item_select_tag_view, null);
                tv.setText(mPolePositionDictList.get(position).dictLabel);
                return tv;
            }
        };
        if (!TextUtils.isEmpty(mPolePositionStr)) {
            for (int i = 0; i < mPolePositionDictList.size(); i++) {
                if (TextUtils.equals(mPolePositionStr, mPolePositionDictList.get(i).dictLabel)) {
                    //预先设置选中
                    tagAdapter.setSelectedList(i);
                    tvConfirm.setEnabled(true);
                    break;
                }
            }
        } else {
            tvConfirm.setEnabled(false);
        }
        layoutTag.setAdapter(tagAdapter);
        layoutTag.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                List<Integer> selectPosList = new ArrayList<>(selectPosSet);
                if (selectPosSet.size() == 1) {
                    mPolePositionStr = mPolePositionDictList.get(selectPosList.get(0)).dictLabel;
                    mPolePositionCode = mPolePositionDictList.get(selectPosList.get(0)).dictCode;
                    tvConfirm.setEnabled(true);
                } else if (selectPosSet.size() == 0) {
                    tvConfirm.setEnabled(false);
                }
            }
        });
        mPolePositionDialog = new CustomBottomDialog.Builder(this)
                .setContentView(contentView)
                .setText(R.id.tv_title, "灯杆位置")
                .setGravity(Gravity.BOTTOM)
                .setStyle(R.style.bottom_dialog)
                .setClickListener(R.id.iv_close, v1 -> mPolePositionDialog.dismiss())
                .setClickListener(R.id.tv_confirm, v2 -> {
                    tv_pole_position.setText(mPolePositionStr);
                    mPolePositionDialog.dismiss();
                })
                .setCanCancelable(true)
                .build();
        mPolePositionDialog.show();
    }

    //照射道路弹窗
    private void showShineRoadDialog() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_device, null);
        TextView tvConfirm = contentView.findViewById(R.id.tv_confirm);
        TagFlowLayout layoutTag = contentView.findViewById(R.id.id_flowlayout);
        TagAdapter tagAdapter = new TagAdapter(mShineRoadDictList) {
            @Override
            public View getView(FlowLayout parent, int position, Object o) {
                TextView tv = (TextView) View.inflate(ScanActivity.this, R.layout.item_select_tag_view, null);
                tv.setText(mShineRoadDictList.get(position).dictLabel);
                return tv;
            }
        };
        if (!TextUtils.isEmpty(mShineRoadStr)) {
            for (int i = 0; i < mShineRoadDictList.size(); i++) {
                if (TextUtils.equals(mShineRoadStr, mShineRoadDictList.get(i).dictLabel)) {
                    //预先设置选中
                    tagAdapter.setSelectedList(i);
                    tvConfirm.setEnabled(true);
                    break;
                }
            }
        } else {
            tvConfirm.setEnabled(false);
        }
        layoutTag.setAdapter(tagAdapter);
        layoutTag.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                List<Integer> selectPosList = new ArrayList<>(selectPosSet);
                if (selectPosSet.size() == 1) {
                    mShineRoadStr = mShineRoadDictList.get(selectPosList.get(0)).dictLabel;
                    mShineRoadCode = mShineRoadDictList.get(selectPosList.get(0)).dictCode;
                    tvConfirm.setEnabled(true);
                } else if (selectPosSet.size() == 0) {
                    tvConfirm.setEnabled(false);
                }
            }
        });
        mShineRoadDialog = new CustomBottomDialog.Builder(this)
                .setContentView(contentView)
                .setText(R.id.tv_title, "照射道路")
                .setGravity(Gravity.BOTTOM)
                .setStyle(R.style.bottom_dialog)
                .setClickListener(R.id.iv_close, v1 -> mShineRoadDialog.dismiss())
                .setClickListener(R.id.tv_confirm, v2 -> {
                    tv_shine_road.setText(mShineRoadStr);
                    mShineRoadDialog.dismiss();
                })
                .setCanCancelable(true)
                .build();
        mShineRoadDialog.show();
    }

    //灯头数量设置弹窗
    private void showPoleNumDialog() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_device, null);
        TextView tvConfirm = contentView.findViewById(R.id.tv_confirm);
        TagFlowLayout layoutTag = contentView.findViewById(R.id.id_flowlayout);
        TagAdapter tagAdapter = new TagAdapter(mPoleNumList) {
            @Override
            public View getView(FlowLayout parent, int position, Object o) {
                TextView tv = (TextView) View.inflate(ScanActivity.this, R.layout.item_select_tag_view, null);
                tv.setText(mPoleNumList.get(position) + "");
                return tv;
            }
        };
        //预先设置选中
        tagAdapter.setSelectedList(mPoleNum - 1);
        layoutTag.setAdapter(tagAdapter);
        layoutTag.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                List<Integer> selectPosList = new ArrayList<>(selectPosSet);
                if (selectPosSet.size() == 1) {
                    mPoleNum = mPoleNumList.get(selectPosList.get(0));
                    tvConfirm.setEnabled(true);
                } else if (selectPosSet.size() == 0) {
                    tvConfirm.setEnabled(false);
                }
            }
        });
        mPoleNumDialog = new CustomBottomDialog.Builder(this)
                .setContentView(contentView)
                .setText(R.id.tv_title, "灯头数量")
                .setGravity(Gravity.BOTTOM)
                .setStyle(R.style.bottom_dialog)
                .setClickListener(R.id.iv_close, v1 -> mPoleNumDialog.dismiss())
                .setClickListener(R.id.tv_confirm, v2 -> {
                    if (mPoleNum == 1) {
                        ll_a.setVisibility(View.VISIBLE);
                        ll_b.setVisibility(View.GONE);
                        ll_c.setVisibility(View.GONE);
                        ll_d.setVisibility(View.GONE);
                        ll_e.setVisibility(View.GONE);
                        ll_f.setVisibility(View.GONE);
                    } else if (mPoleNum == 2) {
                        ll_a.setVisibility(View.VISIBLE);
                        ll_b.setVisibility(View.VISIBLE);
                        ll_c.setVisibility(View.GONE);
                        ll_d.setVisibility(View.GONE);
                        ll_e.setVisibility(View.GONE);
                        ll_f.setVisibility(View.GONE);
                    } else if (mPoleNum == 3) {
                        ll_a.setVisibility(View.VISIBLE);
                        ll_b.setVisibility(View.VISIBLE);
                        ll_c.setVisibility(View.VISIBLE);
                        ll_d.setVisibility(View.GONE);
                        ll_e.setVisibility(View.GONE);
                        ll_f.setVisibility(View.GONE);
                    } else if (mPoleNum == 4) {
                        ll_a.setVisibility(View.VISIBLE);
                        ll_b.setVisibility(View.VISIBLE);
                        ll_c.setVisibility(View.VISIBLE);
                        ll_d.setVisibility(View.VISIBLE);
                        ll_e.setVisibility(View.GONE);
                        ll_f.setVisibility(View.GONE);
                    } else if (mPoleNum == 5) {
                        ll_a.setVisibility(View.VISIBLE);
                        ll_b.setVisibility(View.VISIBLE);
                        ll_c.setVisibility(View.VISIBLE);
                        ll_d.setVisibility(View.VISIBLE);
                        ll_e.setVisibility(View.VISIBLE);
                        ll_f.setVisibility(View.GONE);
                    } else if (mPoleNum == 6) {
                        ll_a.setVisibility(View.VISIBLE);
                        ll_b.setVisibility(View.VISIBLE);
                        ll_c.setVisibility(View.VISIBLE);
                        ll_d.setVisibility(View.VISIBLE);
                        ll_e.setVisibility(View.VISIBLE);
                        ll_f.setVisibility(View.VISIBLE);
                    }
                    tv_pole_num.setText(mPoleNum + "");
                    mPoleNumDialog.dismiss();
                })
                .setCanCancelable(true)
                .build();
        mPoleNumDialog.show();
    }

    private void showLightSetDialog(String poleType) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_light_set, null);
        TagFlowLayout layoutTag = contentView.findViewById(R.id.id_flowlayout);
        TagAdapter tagAdapter = new TagAdapter(mShineCarRoadDictList) {
            @Override
            public View getView(FlowLayout parent, int position, Object o) {
                TextView tv = (TextView) View.inflate(ScanActivity.this, R.layout.item_select_tag_view, null);
                tv.setText(mShineCarRoadDictList.get(position).dictLabel);
                return tv;
            }
        };
        //预先设置选中
//                tagAdapter.setSelectedList(new HashSet<>(selectList));
        layoutTag.setAdapter(tagAdapter);
        layoutTag.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                List<Integer> selectPosList = new ArrayList<>(selectPosSet);
                if (selectPosSet.size() == 1) {
                    if (TextUtils.equals(poleType, "A")) {
                        mRoadTypeA = mShineCarRoadDictList.get(selectPosList.get(0)).dictLabel;
                        mRoadTypeACode = mShineCarRoadDictList.get(selectPosList.get(0)).dictCode;
                    } else if (TextUtils.equals(poleType, "B")) {
                        mRoadTypeB = mShineCarRoadDictList.get(selectPosList.get(0)).dictLabel;
                        mRoadTypeBCode = mShineCarRoadDictList.get(selectPosList.get(0)).dictCode;
                    } else if (TextUtils.equals(poleType, "C")) {
                        mRoadTypeC = mShineCarRoadDictList.get(selectPosList.get(0)).dictLabel;
                        mRoadTypeCCode = mShineCarRoadDictList.get(selectPosList.get(0)).dictCode;
                    } else if (TextUtils.equals(poleType, "D")) {
                        mRoadTypeD = mShineCarRoadDictList.get(selectPosList.get(0)).dictLabel;
                        mRoadTypeDCode = mShineCarRoadDictList.get(selectPosList.get(0)).dictCode;
                    } else if (TextUtils.equals(poleType, "E")) {
                        mRoadTypeE = mShineCarRoadDictList.get(selectPosList.get(0)).dictLabel;
                        mRoadTypeECode = mShineCarRoadDictList.get(selectPosList.get(0)).dictCode;
                    } else if (TextUtils.equals(poleType, "F")) {
                        mRoadTypeF = mShineCarRoadDictList.get(selectPosList.get(0)).dictLabel;
                        mRoadTypeFCode = mShineCarRoadDictList.get(selectPosList.get(0)).dictCode;
                    }
                } else if (selectPosSet.size() == 0) {
                    if (TextUtils.equals(poleType, "A")) {
                        mRoadTypeA = "";
                    } else if (TextUtils.equals(poleType, "B")) {
                        mRoadTypeB = "";
                    } else if (TextUtils.equals(poleType, "C")) {
                        mRoadTypeC = "";
                    } else if (TextUtils.equals(poleType, "D")) {
                        mRoadTypeD = "";
                    } else if (TextUtils.equals(poleType, "E")) {
                        mRoadTypeE = "";
                    } else if (TextUtils.equals(poleType, "F")) {
                        mRoadTypeF = "";
                    }
                }
            }
        });

        TagFlowLayout layoutTag2 = contentView.findViewById(R.id.id_flowlayout_second);
        TagAdapter tagAdapter2 = new TagAdapter(mShineDirectDictList) {
            @Override
            public View getView(FlowLayout parent, int position, Object o) {
                TextView tv = (TextView) View.inflate(ScanActivity.this, R.layout.item_select_tag_view2, null);
                tv.setText(mShineDirectDictList.get(position).dictLabel);
                return tv;
            }
        };
        //预先设置选中
//                tagAdapter.setSelectedList(new HashSet<>(selectList));
        layoutTag2.setAdapter(tagAdapter2);
        layoutTag2.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                List<Integer> selectPosList = new ArrayList<>(selectPosSet);
                if (selectPosSet.size() == 1) {
                    if (TextUtils.equals(poleType, "A")) {
                        mDirectionA = mShineDirectDictList.get(selectPosList.get(0)).dictLabel;
                        mDirectionACode = mShineDirectDictList.get(selectPosList.get(0)).dictCode;
                    } else if (TextUtils.equals(poleType, "B")) {
                        mDirectionB = mShineDirectDictList.get(selectPosList.get(0)).dictLabel;
                        mDirectionBCode = mShineDirectDictList.get(selectPosList.get(0)).dictCode;
                    } else if (TextUtils.equals(poleType, "C")) {
                        mDirectionC = mShineDirectDictList.get(selectPosList.get(0)).dictLabel;
                        mDirectionCCode = mShineDirectDictList.get(selectPosList.get(0)).dictCode;
                    } else if (TextUtils.equals(poleType, "D")) {
                        mDirectionD = mShineDirectDictList.get(selectPosList.get(0)).dictLabel;
                        mDirectionDCode = mShineDirectDictList.get(selectPosList.get(0)).dictCode;
                    } else if (TextUtils.equals(poleType, "E")) {
                        mDirectionE = mShineDirectDictList.get(selectPosList.get(0)).dictLabel;
                        mDirectionECode = mShineDirectDictList.get(selectPosList.get(0)).dictCode;
                    } else if (TextUtils.equals(poleType, "F")) {
                        mDirectionF = mShineDirectDictList.get(selectPosList.get(0)).dictLabel;
                        mDirectionFCode = mShineDirectDictList.get(selectPosList.get(0)).dictCode;
                    }
                } else if (selectPosSet.size() == 0) {
                    if (TextUtils.equals(poleType, "A")) {
                        mDirectionA = "";
                    } else if (TextUtils.equals(poleType, "B")) {
                        mDirectionB = "";
                    } else if (TextUtils.equals(poleType, "C")) {
                        mDirectionC = "";
                    } else if (TextUtils.equals(poleType, "D")) {
                        mDirectionD = "";
                    } else if (TextUtils.equals(poleType, "E")) {
                        mDirectionE = "";
                    } else if (TextUtils.equals(poleType, "F")) {
                        mDirectionF = "";
                    }
                }
            }
        });
        mLightSetDialog = new CustomBottomDialog.Builder(this)
                .setContentView(contentView)
                .setGravity(Gravity.BOTTOM)
                .setStyle(R.style.bottom_dialog)
                .setClickListener(R.id.iv_close, v1 -> mLightSetDialog.dismiss())
                .setClickListener(R.id.tv_confirm, v2 -> {
                    if (TextUtils.equals(poleType, "A")) {
                        tv_a.setText(mRoadTypeA + "/" + mDirectionA);
                    } else if (TextUtils.equals(poleType, "B")) {
                        tv_b.setText(mRoadTypeB + "/" + mDirectionB);
                    } else if (TextUtils.equals(poleType, "C")) {
                        tv_c.setText(mRoadTypeC + "/" + mDirectionC);
                    } else if (TextUtils.equals(poleType, "D")) {
                        tv_d.setText(mRoadTypeD + "/" + mDirectionD);
                    } else if (TextUtils.equals(poleType, "E")) {
                        tv_e.setText(mRoadTypeE + "/" + mDirectionE);
                    } else if (TextUtils.equals(poleType, "F")) {
                        tv_f.setText(mRoadTypeF + "/" + mDirectionF);
                    }
                    mLightSetDialog.dismiss();
                })
                .setCanCancelable(true)
                .build();
        mLightSetDialog.show();
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
                    mPresenter.getPoleInfo(str2);
                }
            } else if (requestCode == REQUEST_CODE_OPEN_GPS) {
                if (!mLocationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
                    tv_to_position.setText("开启GPS");
                } else {
                    startLocation();
                }
            }
        }
    }

    @Override
    public void showPoleInfo(DeviceResponse deviceResponse) {
        tv_device_road.setText(deviceResponse.data.road.roadName);
        tv_device_num.setText(deviceResponse.data.lampPoleName);
        tv_road_range.setText(deviceResponse.data.concretePosition);
        tv_light_port.setText(deviceResponse.data.lampPoleTypeDictText);
        mPoleCode = deviceResponse.data.poleCode;
        mLampPoleType = deviceResponse.data.lampPoleType;
        mLampPoleName = deviceResponse.data.lampPoleName;
    }

    @Override
    public void showRoadDirectInfo(DictResponse dictResponse) {
        mRoadDirectDictList.addAll(dictResponse.data);
    }

    @Override
    public void showShineCarRoadInfo(DictResponse dictResponse) {
        mShineCarRoadDictList.addAll(dictResponse.data);
    }

    @Override
    public void showShineDirectInfo(DictResponse dictResponse) {
        mShineDirectDictList.addAll(dictResponse.data);
    }

    @Override
    public void showShineRoadInfo(DictResponse dictResponse) {
        mShineRoadDictList.addAll(dictResponse.data);
        mShineRoadStr = mShineRoadDictList.get(0).dictLabel;
        mShineRoadCode = mShineRoadDictList.get(0).dictCode;
        tv_shine_road.setText(mShineRoadStr);
    }

    @Override
    public void showPolePositionInfo(DictResponse dictResponse) {
        mPolePositionDictList.addAll(dictResponse.data);
    }

    @Override
    public void submitSuccess(SubmitResponse submitResponse) {
        ToastUtil.showShort(this, submitResponse.msg);
        tv_device_road.setText("");
        tv_device_num.setText("");
        tv_road_range.setText("");
        tv_light_port.setText("");
        mPoleCode = "";
        mLampPoleType = 0;
        mLampPoleName = "";

        mRoadDirectStr = "";
        mRoadDirectCode = 0;
        tv_road_direct.setText(mRoadDirectStr);

        mPolePositionStr = "";
        mPolePositionCode = 0;
        tv_pole_position.setText(mPolePositionStr);

        String mShineRoadStr = "";
        mShineRoadCode = 0;
        tv_shine_road.setText(mShineRoadStr);

        et_detail_address.setText("");
        et_remark.setText("");

        mRoadTypeA = "";
        mRoadTypeACode = 0;
        mDirectionA = "";
        mDirectionACode = 0;
        tv_a.setText("");

        mRoadTypeB = "";
        mRoadTypeBCode = 0;
        mDirectionB = "";
        mDirectionBCode = 0;
        tv_b.setText("");

        mRoadTypeC = "";
        mRoadTypeCCode = 0;
        mDirectionC = "";
        mDirectionCCode = 0;
        tv_c.setText("");

        mRoadTypeD = "";
        mRoadTypeDCode = 0;
        mDirectionD = "";
        mDirectionDCode = 0;
        tv_d.setText("");

        mRoadTypeE = "";
        mRoadTypeECode = 0;
        mDirectionE = "";
        mDirectionECode = 0;
        tv_e.setText("");

        mRoadTypeF = "";
        mRoadTypeFCode = 0;
        mDirectionF = "";
        mDirectionFCode = 0;
        tv_f.setText("");

        mGpsLatitude = "";
        mGpsLongitude = "";

        tv_pole_num.setText("1");
        ll_a.setVisibility(View.VISIBLE);
        ll_b.setVisibility(View.GONE);
        ll_c.setVisibility(View.GONE);
        ll_d.setVisibility(View.GONE);
        ll_e.setVisibility(View.GONE);
        ll_f.setVisibility(View.GONE);

//        tv_to_position.setTextColor(getResources().getColor(R.color.color_blue));
        mTvSubmit.setEnabled(false);
        mImageUrlList.clear();
        mFilePathList.clear();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void submitError(SubmitResponse submitResponse) {
        ToastUtil.showShort(this, submitResponse.msg);
    }

    @Override
    public void uploadSuccess(UploadResponse uploadResponse) {
        mImageUrlList.addAll(uploadResponse.data.url);
    }

    // 返回单位是米
    public double getDistance(double longitude1, double latitude1,
                              double longitude2, double latitude2) {
        double Lat1 = rad(latitude1);
        double Lat2 = rad(latitude2);
        double a = Lat1 - Lat2;
        double b = rad(longitude1) - rad(longitude2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(Lat1) * Math.cos(Lat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    private double rad(double d) {
        return d * Math.PI / 180.0;
    }


    private void startLocation() {
        //注册监听
        mLocationService.setLocationOption(mLocationService.getDefaultLocationClientOption());
        mLocationService.start();
    }

    /**
     * 实现定位回调
     */
    private int mTimes;
    private final BDAbstractLocationListener mListener = new BDAbstractLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                mTimes++;
//                Log.i("cjl", "onReceiveLocation:222 " + mTimes);
                //获取纬度信息
                String latitude = location.getLatitude() + "";
                //获取经度信息
                String longitude = location.getLongitude() + "";
                if (!TextUtils.equals(latitude, "4.9E-324") && !TextUtils.equals(longitude, "4.9E-324")) {
                    mGpsLatitude = latitude;
                    mGpsLongitude = longitude;
                    StringBuffer sb = new StringBuffer(256);
                    sb.append(location.getAddrStr());
                    sb.append(location.getLocationDescribe());
                    tv_gps_address.setVisibility(View.VISIBLE);
                    tv_gps_address.setText(sb.toString());
                    tv_to_position.setText("GPS已开启定位中");
                }
//                Log.i("cjl", "onReceiveLocation1: " + latitude);
//                Log.i("cjl", "onReceiveLocation2: " + longitude);

//                tv_to_position.setTextColor(getResources().getColor(R.color.color_grey));
                if (mTimes == 10) {
                    mTvSubmit.setEnabled(true);
                }
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        mLocationService = ((LightApplication) getApplication()).mLocationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        mLocationService.registerListener(mListener);

        //获取地理位置管理器
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // 判断GPS模块是否开启，如果没有则开启
        if (!mLocationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            tv_to_position.setText("开启GPS");
        } else {
            startLocation();
        }
    }

    /***
     * Stop location service
     */
    @Override
    protected void onStop() {
        mTimes = 0;
        mTvSubmit.setEnabled(false);
        mLocationService.unregisterListener(mListener); //注销掉监听
        mLocationService.stop(); //停止定位服务
        super.onStop();
    }
}