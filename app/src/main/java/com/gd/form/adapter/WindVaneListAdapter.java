package com.gd.form.adapter;

import android.content.Context;
import android.widget.TextView;

import com.gd.form.R;
import com.gd.form.model.WindModel;

import java.util.List;

/**
 * <p>类描述：一句话描述下这个类的作用
 * <p>创建人：wh
 * <p>创建时间：2020/5/11
 */
public class WindVaneListAdapter extends BaseRecyclerViewAdapter<WindModel> {
    /**
     * @param context  {@link Context}
     * @param list     数据集合
     * @param layoutId RecyclerView item布局ID
     */
    public WindVaneListAdapter(Context context, List<WindModel> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected void bindData(BaseViewHolder viewHolder, WindModel model, int position) {
        TextView tvStationNo = viewHolder.getView(R.id.tv_stationNo);
        TextView tvDistance = viewHolder.getView(R.id.tv_distance);
        tvStationNo.setText(model.getStakename());
        tvDistance.setText(model.getDistance());
        viewHolder.getContentView().findViewById(R.id.btn_delete).setOnClickListener(view -> {
            if(onItemClickListener!=null){
                onItemClickListener.onItemClickListener(view,viewHolder.getLayoutPosition());
            }
        });
    }
}
