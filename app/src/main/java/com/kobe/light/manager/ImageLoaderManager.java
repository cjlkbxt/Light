package com.kobe.light.manager;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;

/**
 * 图处加载类，外界唯一调用类,直持为view,notifaication,appwidget加载图片
 */
public class ImageLoaderManager {

    private ImageLoaderManager() {
    }

    public static ImageLoaderManager getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static ImageLoaderManager instance = new ImageLoaderManager();
    }

    public void displayImageForView(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .asBitmap()
                .load(url)
                .apply(initCommonRequestOption())
                .transition(withCrossFade())
                .into(imageView);
    }

    public void displayImageForView(ImageView imageView, String url, int placeHolderResId, int errorResId) {
        Glide.with(imageView.getContext())
                .asBitmap()
                .load(url)
                .apply(initCommonRequestOption(placeHolderResId, errorResId))
                .transition(withCrossFade())
                .into(imageView);
    }

    public void displayImageForView(ImageView imageView, Object obj) {
        Glide.with(imageView.getContext())
                .asBitmap()
                .load(obj)
                .apply(initCommonRequestOption())
                .transition(withCrossFade())
                .into(imageView);
    }

    public void displayGifImageForView(ImageView imageView, Object obj) {
        Glide.with(imageView.getContext())
                .asGif()
                .load(obj)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .apply(initCommonRequestOption())
                .into(imageView);
    }

    public void displayImageForView(ImageView imageView, Object obj, int placeHolderResId, int errorResId) {
        Glide.with(imageView.getContext())
                .asBitmap()
                .load(obj)
                .apply(initCommonRequestOption(placeHolderResId, errorResId))
                .transition(withCrossFade())
                .into(imageView);
    }

    public void displayImageForCircleView(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .asBitmap()
                .load(url)
                .apply(initCircleRequestOption())
                .transition(withCrossFade())
                .into(imageView);
    }

    public void displayImageForCircleView(ImageView imageView, String url, int placeHolderResId, int errorResId) {
        Glide.with(imageView.getContext())
                .asBitmap()
                .load(url)
                .apply(initCircleRequestOption(placeHolderResId, errorResId))
                .transition(withCrossFade())
                .into(imageView);
    }

    public void displayImageForCircleView(ImageView imageView, Object obj) {
        Glide.with(imageView.getContext())
                .asBitmap()
                .load(obj)
                .apply(initCircleRequestOption())
                .transition(withCrossFade())
                .into(imageView);
    }

    public void displayImageForCircleView(ImageView imageView, Object obj, int placeHolderResId, int errorResId) {
        Glide.with(imageView.getContext())
                .asBitmap()
                .load(obj)
                .apply(initCircleRequestOption(placeHolderResId, errorResId))
                .transition(withCrossFade())
                .into(imageView);
    }

    private RequestOptions initCommonRequestOption(int placeHolderResId, int errorResId) {
        RequestOptions options = new RequestOptions();
        options.placeholder(placeHolderResId)
                .error(errorResId)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .skipMemoryCache(false)
                .priority(Priority.NORMAL);
        return options;
    }

    private RequestOptions initCommonRequestOption() {
        RequestOptions options = new RequestOptions();
        options.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .skipMemoryCache(false)
                .priority(Priority.NORMAL);
        return options;
    }

    private RequestOptions initCircleRequestOption(int placeHolderResId, int errorResId) {
        RequestOptions options = new RequestOptions();
        options.placeholder(placeHolderResId)
                .error(errorResId)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .skipMemoryCache(false)
                .priority(Priority.NORMAL)
                .circleCrop();
        return options;
    }

    private RequestOptions initCircleRequestOption() {
        RequestOptions options = new RequestOptions();
        options.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .skipMemoryCache(false)
                .priority(Priority.NORMAL)
                .circleCrop();
        return options;
    }
}
