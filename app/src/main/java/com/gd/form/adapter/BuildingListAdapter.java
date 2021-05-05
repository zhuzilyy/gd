package com.gd.form.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gd.form.R;
import com.gd.form.activity.PipeBuildingActivity;
import com.gd.form.model.SearchBuildingModel;

import java.util.List;

/**
 * <p>类描述：一句话描述下这个类的作用
 * <p>创建人：wh
 * <p>创建时间：2020/5/11
 */
public class BuildingListAdapter extends BaseRecyclerViewAdapter<SearchBuildingModel> {
    /**
     * @param context  {@link Context}
     * @param list     数据集合
     * @param layoutId RecyclerView item布局ID
     */
    public BuildingListAdapter(Context context, List<SearchBuildingModel> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected void bindData(BaseViewHolder viewHolder, SearchBuildingModel model, int position) {
        TextView tvName = viewHolder.getView(R.id.tv_name);
        TextView tvStationNo = viewHolder.getView(R.id.tv_stationNo);
        LinearLayout llUpdate = viewHolder.getView(R.id.ll_update);
        tvName.setText(model.getLlegalname());
        tvStationNo.setText(model.getStakename());
        Button btnUpdate = viewHolder.getView(R.id.btn_update);
        Button btnCheck = viewHolder.getView(R.id.btn_check);
//        if(model.getMaintain().equals("1")){
//            llUpdate.setVisibility(View.VISIBLE);
//        }else if(model.getMaintain().equals("0")){
//            llUpdate.setVisibility(View.GONE);
//        }
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PipeBuildingActivity.class);
                intent.putExtra("tag","add");
                intent.putExtra("buildingId",model.getId()+"");
                context.startActivity(intent);
            }
        });
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PipeBuildingActivity.class);
                intent.putExtra("tag", "check");
                intent.putExtra("buildingId",model.getId()+"");
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
