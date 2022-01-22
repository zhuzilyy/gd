package com.gd.form.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.gd.form.R;
import com.gd.form.model.ScoreModel;

import java.util.List;

/**
 * <p>类描述：一句话描述下这个类的作用
 * <p>创建人：wh
 * <p>创建时间：2020/5/11
 */
public class ScoreAdapter extends BaseRecyclerViewAdapter<ScoreModel> {
    /**
     * @param context  {@link Context}
     * @param formModelList     数据集合
     * @param layoutId RecyclerView item布局ID
     */
    public ScoreAdapter(Context context, List<ScoreModel> formModelList, int layoutId) {
        super(context, formModelList, layoutId);
    }

    @Override
    protected void bindData(BaseViewHolder viewHolder, ScoreModel scoreModel, int position) {
        TextView tvDepartmentName = viewHolder.getView(R.id.tv_departmentName);
        TextView tvScore = viewHolder.getView(R.id.tv_score);
        tvDepartmentName.setText(scoreModel.getDepartmentname());
        tvScore.setText(scoreModel.getTotalscore()+"");
        viewHolder.getView(R.id.btn_check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null){
                    onItemClickListener.onItemClickListener(v,viewHolder.getLayoutPosition());
                }
            }
        });

    }
}
