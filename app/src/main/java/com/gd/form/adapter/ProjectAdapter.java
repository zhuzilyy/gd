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
//        progressBar.setProgress(Integer.parseInt(progress.getConstructionprocess()));
        viewHolder.getContentView().setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClickListener(view, viewHolder.getLayoutPosition());
            }
        });
    }
}
