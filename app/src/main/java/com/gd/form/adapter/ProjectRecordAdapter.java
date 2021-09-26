package com.gd.form.adapter;

import android.content.Context;
import android.os.Build;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.gd.form.R;
import com.gd.form.model.ProgressModel;
import com.gd.form.utils.TimeUtil;

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
        TextView tvProgress = viewHolder.getView(R.id.tv_progress);
        TextView tvDetail = viewHolder.getView(R.id.tv_detail);
        tvTime.setText(TimeUtil.longToFormatTime(progress.getRecorddate().getTime()));
        tvDetail.setText(progress.getProcessdesc());
        tvProgress.setText(progress.getConstructionprocess()+"%");
        viewHolder.getContentView().setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClickListener(view, viewHolder.getLayoutPosition());
            }
        });
    }
}
