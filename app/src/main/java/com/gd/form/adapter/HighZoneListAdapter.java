package com.gd.form.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gd.form.R;
import com.gd.form.activity.PipeHighZoneActivity;
import com.gd.form.model.SearchHighZoneModel;

import java.util.List;

/**
 * <p>类描述：一句话描述下这个类的作用
 * <p>创建人：wh
 * <p>创建时间：2020/5/11
 */
public class HighZoneListAdapter extends BaseRecyclerViewAdapter<SearchHighZoneModel> {
    /**
     * @param context  {@link Context}
     * @param list     数据集合
     * @param layoutId RecyclerView item布局ID
     */
    public HighZoneListAdapter(Context context, List<SearchHighZoneModel> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected void bindData(BaseViewHolder viewHolder, SearchHighZoneModel model, int position) {
        TextView tvHighZoneName = viewHolder.getView(R.id.tv_highZoneName);
        TextView tvStartStationNo = viewHolder.getView(R.id.tv_startStationNo);
        TextView tvEndStationNo = viewHolder.getView(R.id.tv_endStationNo);
        TextView tvPipeManager = viewHolder.getView(R.id.tv_pipeManager);
        tvHighZoneName.setText(model.getHighname());
        tvStartStationNo.setText(model.getStakename());
        tvEndStationNo.setText(model.getEndstakename());
        tvPipeManager.setText(model.getOwnername());
        Button btnCheck = viewHolder.getView(R.id.btn_check);
        Button btnUpdate = viewHolder.getView(R.id.btn_update);
//        if(model.getMaintain().equals("1")){
//            btnUpdate.setVisibility(View.VISIBLE);
//        }else if(model.getMaintain().equals("0")){
//            btnUpdate.setVisibility(View.GONE);
//        }
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,PipeHighZoneActivity.class);
                intent.putExtra("tag","check");
                intent.putExtra("highZoneId",model.getId()+"");
                context.startActivity(intent);
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,PipeHighZoneActivity.class);
                intent.putExtra("tag","update");
                intent.putExtra("highZoneId",model.getId()+"");
                context.startActivity(intent);
            }
        });
//        tvRecord.setText(value);
        viewHolder.getContentView().setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClickListener(view, viewHolder.getLayoutPosition());
            }
        });
    }
}
