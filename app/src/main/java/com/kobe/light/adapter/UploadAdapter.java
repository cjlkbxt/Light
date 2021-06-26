package com.kobe.light.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.kobe.light.R;
import com.kobe.light.manager.ImageLoaderManager;

import java.util.List;


public class UploadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int ITEM_PIC = 1;
    private int maxImages = 10;
    private final List<String> datas;
    private final Context context;

    public UploadAdapter(List<String> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView;
        if (viewType == ITEM_PIC) {
            rootView = LayoutInflater.from(context).inflate(R.layout.add_image_grid_view_item, parent, false);
            return new FeedBackHolder(rootView);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FeedBackHolder) {
            if (datas != null && position < datas.size()) {
                ImageLoaderManager.getInstance().displayImageForView(((FeedBackHolder) holder).ivUpload, datas.get(position));
                ((FeedBackHolder) holder).ivDel.setVisibility(View.VISIBLE);
                ((FeedBackHolder) holder).ivDel.setOnClickListener(v -> mOnItemClickListener.onItemDeleteClick(v, position));
                ((FeedBackHolder) holder).ivUpload.setOnClickListener(v -> mOnItemClickListener.onItemClick(v, position));
            } else {
                /**代表+号的需要+号图片显示图片**/
                ImageLoaderManager.getInstance().displayImageForView(((FeedBackHolder) holder).ivUpload, R.drawable.icon_add_picture);
                ((FeedBackHolder) holder).ivDel.setVisibility(View.GONE);
                ((FeedBackHolder) holder).ivUpload.setOnClickListener(v -> mOnItemClickListener.onItemAddClick(v, position));
            }
        }
    }

    @Override
    public int getItemCount() {
        int count = datas == null ? 1 : datas.size() + 1;
        if (count > maxImages) {
            return datas.size();
        } else {
            return count;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_PIC;
    }

    public class FeedBackHolder extends RecyclerView.ViewHolder {

        private ImageView ivUpload;
        private ImageView ivDel;

        public FeedBackHolder(View itemView) {
            super(itemView);
            ivUpload = itemView.findViewById(R.id.iv_upload);
            ivDel = itemView.findViewById(R.id.iv_delete);
        }
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemDeleteClick(View view, int position);

        void onItemAddClick(View view, int position);
    }

    /**
     * 获取最大上传张数
     *
     * @return
     */
    public int getMaxImages() {
        return maxImages;
    }

    /**
     * 设置最大上传张数
     *
     * @param maxImages
     */
    public void setMaxImages(int maxImages) {
        this.maxImages = maxImages;
    }
}
