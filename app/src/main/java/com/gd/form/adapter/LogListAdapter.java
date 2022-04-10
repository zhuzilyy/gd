package com.gd.form.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gd.form.R;
import com.gd.form.model.LogBean;
import com.gd.form.utils.TimeUtil;

import java.util.List;

/**
 * <p>类描述：一句话描述下这个类的作用
 * <p>创建人：wh
 * <p>创建时间：2020/5/11
 */
public class LogListAdapter extends BaseRecyclerViewAdapter<LogBean> {
    /**
     * @param context  {@link Context}
     * @param list     数据集合
     * @param layoutId RecyclerView item布局ID
     */
    public LogListAdapter(Context context, List<LogBean> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected void bindData(BaseViewHolder viewHolder, LogBean model, int position) {
        TextView tvTime = viewHolder.getView(R.id.tv_time);
        TextView tvName = viewHolder.getView(R.id.tv_name);
        Button btnDelete = viewHolder.getView(R.id.btn_delete);
        tvTime.setText(TimeUtil.longToFormatTime(model.getCreatime().getTime()));
        tvName.setText(model.getCreatorname());
        if (model.getMainflag() == 1) {
            btnDelete.setVisibility(View.VISIBLE);
        }else{
            btnDelete.setVisibility(View.GONE);
        }
        viewHolder.getContentView().findViewById(R.id.btn_delete).setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClickListener(view, viewHolder.getLayoutPosition());
            }
        });
        viewHolder.getContentView().findViewById(R.id.btn_check).setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClickListener(view, viewHolder.getLayoutPosition());
            }
        });
    }
}
