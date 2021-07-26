package com.kobe.light.ui.check;

import android.os.Bundle;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.kobe.lib_base.BaseActivity;
import com.kobe.lib_base.IBasePresenter;
import com.kobe.light.R;
import com.kobe.light.manager.ImageLoaderManager;

public class PicPreviewActivity extends BaseActivity {

    private PhotoView mPhotoView;
    private String mImageUrl;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pic_preview;
    }

    @Override
    protected void initViews() {
        mPhotoView = findViewById(R.id.photo);
    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mImageUrl = bundle.getString("url");
        }
        ImageLoaderManager.getInstance().displayImageForView(mPhotoView, mImageUrl, R.drawable.loading_pic, R.drawable.load_pic_failed);
        mPhotoView.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(ImageView view, float x, float y) {
                finish();
            }
        });
    }

    @Override
    protected void registerListener() {

    }

    @Override
    public IBasePresenter initPresenter() {
        return null;
    }

}
