package com.gd.form.adapter;

import android.content.Context;
import android.os.Build;
import android.widget.ProgressBar;
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
        TextView tvProgress = viewHolder.getView(R.id.tv_progress);
        ProgressBar progressBar = viewHolder.getView(R.id.progressBar);
        TextView tvStatus = viewHolder.getView(R.id.tv_status);
        TextView tvName = viewHolder.getView(R.id.tv_name);
        tvName.setText(progress.getProjectname());
        tvProgress.setText("当前进度"+progress.getConstructionprocess()+"%");
        if(progress.getProjectstatus() == 1){
            progressBar.setProgressDrawable(context.getDrawable(R.drawable.progress_bg));
        }else if(progress.getProjectstatus() == 2){
            progressBar.setProgressDrawable(context.getDrawable(R.drawable.progress_bg1));
        }else{
            progressBar.setProgressDrawable(context.getDrawable(R.drawable.progress_bg2));
        }
        progressBar.setProgress(Integer.parseInt(progress.getConstructionprocess()));
//        TextView tvCondition = viewHolder.getView(R.id.tv_condition);
//        TextView tvTime = viewHolder.getView(R.id.tv_time);
//        TextView tvCount = viewHolder.getView(R.id.tv_count);
//        LinearLayout llOperate = viewHolder.getView(R.id.ll_operate);
//        tvName.setText(ticketModel.getCouponType());
//        tvCondition.setText(ticketModel.getCouponMeta());
//        tvCount.setText(ticketModel.getRemainder() + "张");
//        String startTime = TimeUtil.longToFormatTime(ticketModel.getDrawStartDate());
//        String endTime = TimeUtil.longToFormatTime(ticketModel.getDrawEndDate());
//        tvTime.setText(startTime + "至" + endTime);
//        if(ticketModel.isCanShowOperation()){
//            llOperate.setVisibility(View.VISIBLE);
//        }else{
//            llOperate.setVisibility(View.GONE);
//        }
//        viewHolder.getView(R.id.ll_container).setOnClickListener(view -> {
//            onItemClickListener.onItemClickListener(view, viewHolder.getLayoutPosition());
//        });
//        viewHolder.getView(R.id.ll_operate).setOnClickListener(view -> {
//            onItemClickListener.onItemClickListener(view, viewHolder.getLayoutPosition());
//        });
//        viewHolder.getView(R.id.btn_donate).setOnClickListener(view -> {
//            if (onItemClickListener != null) {
//                onItemClickListener.onItemClickListener(view, viewHolder.getLayoutPosition());
//            }
//        });
        viewHolder.getContentView().setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClickListener(view, viewHolder.getLayoutPosition());
            }
        });
    }
}
