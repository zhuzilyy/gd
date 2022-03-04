package com.gd.form.adapter;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.TextView;

import com.gd.form.R;
import com.gd.form.model.Pipemploys;

import java.util.List;

/**
 * <p>类描述：一句话描述下这个类的作用
 * <p>创建人：wh
 * <p>创建时间：2020/5/11
 */
public class ApproveAdapter extends BaseRecyclerViewAdapter<Pipemploys> {
    /**
     * @param context  {@link Context}
     * @param list     数据集合
     * @param layoutId RecyclerView item布局ID
     */
    public ApproveAdapter(Context context, List<Pipemploys> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected void bindData(BaseViewHolder viewHolder, Pipemploys pipemploys, int position) {
        TextView tvDepartmentName = viewHolder.getView(R.id.tv_departmentName);
        CheckBox cbSelect = viewHolder.getView(R.id.cb_select);
        tvDepartmentName.setText(pipemploys.getDeptname()+":"+pipemploys.getName());
        if(pipemploys.isSelected()){
            cbSelect.setChecked(true);
        }else{
            cbSelect.setChecked(false);
        }
        viewHolder.getContentView().findViewById(R.id.ll_departmentName).setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClickListener(view, viewHolder.getLayoutPosition());
            }
        });
//        viewHolder.getContentView().findViewById(R.id.btn_check).setOnClickListener(view -> {
//            if (onItemClickListener != null) {
//                onItemClickListener.onItemClickListener(view, viewHolder.getLayoutPosition());
//            }
//        });
    }
}
