package com.gd.form.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.gd.form.R;
import com.gd.form.model.ProjectModel;

import java.util.List;

/**
 * <p>类描述：一句话描述下这个类的作用
 * <p>创建人：wh
 * <p>创建时间：2019/12/25
 */
public class ProjectAdapter extends BaseRecyclerViewAdapter<ProjectModel> {

    /**
     * @param context  {@link Context}
     * @param list     数据集合
     * @param layoutId RecyclerView item布局ID
     */
    public ProjectAdapter(Context context, List<ProjectModel> list, int layoutId) {
        super(context, list, layoutId);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void bindData(BaseViewHolder viewHolder, ProjectModel progress, int position) {
        View viewStatus = viewHolder.getView(R.id.view_status);
        LinearLayout llBg = viewHolder.getView(R.id.ll_bg);
        TextView tvStatus = viewHolder.getView(R.id.tv_status);
        TextView tvDistance = viewHolder.getView(R.id.tv_distance);
        TextView tvProgressData = viewHolder.getView(R.id.tv_progressData);
        TextView tvName = viewHolder.getView(R.id.tv_name);
        tvName.setText(progress.getProjectname());
        tvDistance.setText(progress.getPipedistance());
        tvProgressData.setText(progress.getConstructionprocess()+"%");
        if(progress.getProjectstatus() == 1){
            viewStatus.setBackgroundColor(Color.parseColor("#8000ff00"));
            llBg.setBackgroundColor(Color.parseColor("#4000ff00"));
            tvStatus.setText("正在施工");
        }else if(progress.getProjectstatus() == 2){
            viewStatus.setBackgroundColor(Color.parseColor("#80ff0000"));
            llBg.setBackgroundColor(Color.parseColor("#40ff0000"));
            tvStatus.setText("完成");
        }else{
            viewStatus.setBackgroundColor(Color.parseColor("#80ffff00"));
            llBg.setBackgroundColor(Color.parseColor("#40ffff00"));
            tvStatus.setText("暂停");
        }
        viewHolder.getContentView().setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClickListener(view, viewHolder.getLayoutPosition());
            }
        });
    }
}
