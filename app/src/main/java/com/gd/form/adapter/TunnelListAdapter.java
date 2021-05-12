package com.gd.form.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gd.form.R;
import com.gd.form.activity.PipeTunnelActivity;
import com.gd.form.model.SearchPipeInfoModel;

import java.util.List;

/**
 * <p>类描述：一句话描述下这个类的作用
 * <p>创建人：wh
 * <p>创建时间：2020/5/11
 */
public class TunnelListAdapter extends BaseRecyclerViewAdapter<SearchPipeInfoModel> {
    /**
     * @param context  {@link Context}
     * @param list     数据集合
     * @param layoutId RecyclerView item布局ID
     */
    public TunnelListAdapter(Context context, List<SearchPipeInfoModel> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected void bindData(BaseViewHolder viewHolder, SearchPipeInfoModel model, int position) {
        LinearLayout llShowButton = viewHolder.getView(R.id.ll_showButton);
        TextView tvName = viewHolder.getView(R.id.tv_name);
        TextView tvStartStationNo = viewHolder.getView(R.id.tv_startStationNo);
        TextView tvEndStationNo = viewHolder.getView(R.id.tv_endStationNo);
        tvName.setText(model.getPipename());
        tvStartStationNo.setText(model.getStakename());
        tvEndStationNo.setText(model.getEndstakename());
        Button btnCheck = viewHolder.getView(R.id.btn_check);
        Button btnUpdate = viewHolder.getView(R.id.btn_update);
        if(model.getMaintain().equals("1")){
            btnUpdate.setVisibility(View.VISIBLE);
        }else if(model.getMaintain().equals("0")){
            btnUpdate.setVisibility(View.GONE);
        }
        if("select".equals(model.getType())){
            llShowButton.setVisibility(View.GONE);
        }else{
            llShowButton.setVisibility(View.VISIBLE);
        }
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PipeTunnelActivity.class);
                intent.putExtra("tag","check");
                intent.putExtra("tunnelId",model.getId()+"");
                context.startActivity(intent);
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PipeTunnelActivity.class);
                intent.putExtra("tag","update");
                intent.putExtra("tunnelId",model.getId()+"");
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
