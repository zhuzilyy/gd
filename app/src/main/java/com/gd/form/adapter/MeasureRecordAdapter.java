package com.gd.form.adapter;

import android.content.Context;
import android.widget.TextView;

import com.gd.form.R;
import com.gd.form.model.MeasureModel;
import com.gd.form.utils.TimeUtil;

import java.util.List;

/**
 * <p>类描述：一句话描述下这个类的作用
 * <p>创建人：wh
 * <p>创建时间：2020/5/11
 */
public class MeasureRecordAdapter extends BaseRecyclerViewAdapter<MeasureModel> {
    /**
     * @param context  {@link Context}
     * @param list     数据集合
     * @param layoutId RecyclerView item布局ID
     */
    public MeasureRecordAdapter(Context context, List<MeasureModel> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected void bindData(BaseViewHolder viewHolder, MeasureModel value, int position) {
        TextView tvTime = viewHolder.getView(R.id.tv_time);
        TextView tvDeep = viewHolder.getView(R.id.tv_deep);
        String deep =value.getPipedeep()+"米";
        tvTime.setText("测量时间: "+ TimeUtil.longToFormatTime(value.getMeasuredate().getTime()));
        tvDeep.setText("管道埋深: "+deep);
        viewHolder.getContentView().setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClickListener(view, viewHolder.getLayoutPosition());
            }
        });
    }
}
