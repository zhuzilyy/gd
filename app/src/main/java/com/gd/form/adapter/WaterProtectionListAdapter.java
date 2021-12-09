package com.gd.form.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gd.form.R;
import com.gd.form.model.WaterModel;
import com.gd.form.utils.TimeUtil;

import java.util.List;

/**
 * <p>类描述：一句话描述下这个类的作用
 * <p>创建人：wh
 * <p>创建时间：2020/5/11
 */
public class WaterProtectionListAdapter extends BaseRecyclerViewAdapter<WaterModel> {
    /**
     * @param context  {@link Context}
     * @param list     数据集合
     * @param layoutId RecyclerView item布局ID
     */
    public WaterProtectionListAdapter(Context context, List<WaterModel> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected void bindData(BaseViewHolder viewHolder, WaterModel model, int position) {
        TextView tvStationNo = viewHolder.getView(R.id.tv_stationNo);
        TextView tvDistance = viewHolder.getView(R.id.tv_distance);
        TextView tvName = viewHolder.getView(R.id.tv_name);
        TextView tvTime = viewHolder.getView(R.id.tv_time);
        LinearLayout llUpdate = viewHolder.getView(R.id.ll_update);
        View viewUpdate = viewHolder.getView(R.id.view_update);
        tvStationNo.setText(model.getStakename());
        tvDistance.setText(model.getDistance());
        tvName.setText(model.getName());
        if (model.getMaintain().equals("1")) {
            llUpdate.setVisibility(View.VISIBLE);
        } else if (model.getMaintain().equals("0")) {
            llUpdate.setVisibility(View.GONE);
        }
        if ("select".equals(model.getType())) {
            tvTime.setText(TimeUtil.longToFormatTime(model.getCreatime().getTime()));
            llUpdate.setVisibility(View.GONE);

        } else {
            if(model.getCreatime()!=null){
                if(!TextUtils.isEmpty(model.getCreatime().getTime()+"")){
                    tvTime.setText(TimeUtil.longToFormatTime(model.getCreatime().getTime()));
                }
            }
            llUpdate.setVisibility(View.VISIBLE);
        }
        viewHolder.getContentView().setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClickListener(view, viewHolder.getLayoutPosition());
            }
        });
        viewHolder.getView(R.id.btn_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener(v, viewHolder.getLayoutPosition());
                }
            }
        });
    }
}
