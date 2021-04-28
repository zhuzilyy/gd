package com.gd.form.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.gd.form.R;
import com.gd.form.model.OverTimeModel;

import java.util.List;

/**
 * <p>类描述：一句话描述下这个类的作用
 * <p>创建人：wh
 * <p>创建时间：2020/5/11
 */
public class OVerTimeAdapter extends BaseRecyclerViewAdapter<OverTimeModel> {
    /**
     * @param context  {@link Context}
     * @param list     数据集合
     * @param layoutId RecyclerView item布局ID
     */
    public OVerTimeAdapter(Context context, List<OverTimeModel> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected void bindData(BaseViewHolder viewHolder, OverTimeModel model, int position) {
        TextView tvFormType = viewHolder.getView(R.id.tv_formType);
        TextView tvDepartmentName = viewHolder.getView(R.id.tv_departmentName);
        TextView tvNotice = viewHolder.getView(R.id.tv_notice);
        TextView tvTime = viewHolder.getView(R.id.tv_time);
        tvFormType.setText(model.getFormname());
        tvDepartmentName.setText(model.getDeptname());
        tvNotice.setText(model.getEmployname());
        tvTime.setText(model.getExpireTimeString());
        viewHolder.getContentView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener(view, viewHolder.getLayoutPosition());
                }
            }
        });
    }
}
