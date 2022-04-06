package com.gd.form.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gd.form.R;
import com.gd.form.activity.LookBigPicActivity;
import com.gd.form.model.EaluationPicBean;
import com.gd.form.model.ProgressModel;
import com.gd.form.utils.TimeUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>类描述：一句话描述下这个类的作用
 * <p>创建人：wh
 * <p>创建时间：2019/12/25
 */
public class ProjectRecordAdapter extends BaseRecyclerViewAdapter<ProgressModel> {

    /**
     * @param context  {@link Context}
     * @param list     数据集合
     * @param layoutId RecyclerView item布局ID
     */
    public ProjectRecordAdapter(Context context, List<ProgressModel> list, int layoutId) {
        super(context, list, layoutId);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void bindData(BaseViewHolder viewHolder, ProgressModel progress, int position) {
        TextView tvTime = viewHolder.getView(R.id.tv_time);
        TextView tvCreator = viewHolder.getView(R.id.tv_creator);
        TextView tvProgress = viewHolder.getView(R.id.tv_progress);
        TextView tvDetail = viewHolder.getView(R.id.tv_detail);
        LinearLayout llSelectImages = viewHolder.getView(R.id.ll_selectImage);
        RecyclerView rvResultPhoto = viewHolder.getView(R.id.rvResultPhoto);
        LinearLayout llUpload = viewHolder.getView(R.id.ll_upload);
        TextView tvFileName = viewHolder.getView(R.id.tv_fileName);
        tvCreator.setText(progress.getCreatorame());
        if (progress.getUploadpicture().equals("00") || TextUtils.isEmpty(progress.getUploadpicture())) {
            llSelectImages.setVisibility(View.GONE);
        }else{
            String[] photoArr = progress.getUploadpicture().split(";");
            List<String> path = new ArrayList<>();
            for (int i = 0; i < photoArr.length; i++) {
                path.add(photoArr[i]);
            }
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
            gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            rvResultPhoto.setLayoutManager(gridLayoutManager);
            PhotoAdapter photoAdapter = new PhotoAdapter(context, path);
            rvResultPhoto.setAdapter(photoAdapter);
            photoAdapter.setPicClickListener(new PhotoAdapter.picClickListener() {
                @Override
                public void click(int position) {
                    Intent intent = new Intent(context, LookBigPicActivity.class);
                    List<EaluationPicBean> list = new ArrayList<>();
                    EaluationPicBean ealuationPicBean = new EaluationPicBean();
                    ealuationPicBean.imageUrl = path.get(position);
                    list.add(ealuationPicBean);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(LookBigPicActivity.PICDATALIST, (Serializable)list);
                    intent.putExtra("CURRENTITEM",0);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }
        if(progress.getUploadfile().equals("00") || TextUtils.isEmpty(progress.getUploadfile())){
            llUpload.setVisibility(View.GONE);
        }else{
            String[] fileName = progress.getFilename().split("_");
            tvFileName.setText(fileName[fileName.length-1]);
        }
        tvTime.setText(TimeUtil.longToFormatTime(progress.getRecorddate().getTime()));
        tvDetail.setText(progress.getProcessdesc());
        tvProgress.setText(progress.getConstructionprocess() + "%");
        viewHolder.getContentView().setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClickListener(view, viewHolder.getLayoutPosition());
            }
        });
        viewHolder.getContentView().findViewById(R.id.ll_upload).setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClickListener(view, viewHolder.getLayoutPosition());
            }
        });
    }
}
