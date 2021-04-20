package com.gd.form.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.gd.form.R;
import com.gd.form.model.FormModel;
import com.gd.form.utils.TimeUtil;

import java.util.List;

/**
 * <p>类描述：一句话描述下这个类的作用
 * <p>创建人：wh
 * <p>创建时间：2020/5/11
 */
public class FormAdapter extends BaseRecyclerViewAdapter<FormModel> {
    /**
     * @param context  {@link Context}
     * @param formModelList     数据集合
     * @param layoutId RecyclerView item布局ID
     */
    public FormAdapter(Context context, List<FormModel> formModelList, int layoutId) {
        super(context, formModelList, layoutId);
    }

    @Override
    protected void bindData(BaseViewHolder viewHolder, FormModel value, int position) {
        TextView tvTime = viewHolder.getView(R.id.tv_time);
        TextView tvName = viewHolder.getView(R.id.tv_name);
        TextView tvType = viewHolder.getView(R.id.tv_type);
        tvTime.setText(TimeUtil.longToFormatTime(value.getCreatime().getTime()));
        tvName.setText(value.getEmployname());
        tvType.setText(value.getFormname());
        viewHolder.getContentView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClickListener!=null){
                    onItemClickListener.onItemClickListener(view,viewHolder.getLayoutPosition());
                }
            }
        });
    }
}
