package com.gd.form.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gd.form.R;
import com.gd.form.activity.PipeTagActivity;
import com.gd.form.model.StakeModel;

import java.util.List;

/**
 * <p>类描述：一句话描述下这个类的作用
 * <p>创建人：wh
 * <p>创建时间：2020/5/11
 */
public class PipeBaseInfoAdapter extends BaseRecyclerViewAdapter<StakeModel> {
    /**
     * @param context  {@link Context}
     * @param list     数据集合
     * @param layoutId RecyclerView item布局ID
     */
    public PipeBaseInfoAdapter(Context context, List<StakeModel> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected void bindData(BaseViewHolder viewHolder, StakeModel model, int position) {
        TextView tvDepartmentName = viewHolder.getView(R.id.tv_departmentName);
        LinearLayout llBottom = viewHolder.getView(R.id.ll_bottom);
        TextView tvPipeName = viewHolder.getView(R.id.tv_pipeName);
        TextView tvStationNo = viewHolder.getView(R.id.tv_stationNo);
        tvStationNo.setText(model.getName());
        tvDepartmentName.setText(model.getDeptname());
        tvPipeName.setText(model.getLinename());
        if("select".equals(model.getSelect())){
            llBottom.setVisibility(View.GONE);
        }else{
            llBottom.setVisibility(View.VISIBLE);
        }
        Button btnUpdate = viewHolder.getView(R.id.btn_update);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PipeTagActivity.class);
                intent.putExtra("tag","update");
                intent.putExtra("id",model.getId()+"");
                intent.putExtra("lineId",model.getLineid()+"");
                context.startActivity(intent);
            }
        });
        viewHolder.getContentView().setOnClickListener(view -> {
            if(onItemClickListener!=null){
                onItemClickListener.onItemClickListener(view,viewHolder.getLayoutPosition());
            }
        });
    }
}
