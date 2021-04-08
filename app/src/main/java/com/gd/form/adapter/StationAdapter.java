package com.gd.form.adapter;

import android.content.Context;
import android.widget.TextView;

import com.gd.form.R;
import com.gd.form.model.StationNoModel;

import java.util.List;

/**
 * <p>类描述：一句话描述下这个类的作用
 * <p>创建人：wh
 * <p>创建时间：2020/5/11
 */
public class StationAdapter extends BaseRecyclerViewAdapter<StationNoModel> {
    /**
     * @param context  {@link Context}
     * @param list     数据集合
     * @param layoutId RecyclerView item布局ID
     */
    public StationAdapter(Context context, List<StationNoModel> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected void bindData(BaseViewHolder viewHolder, StationNoModel stationNoModel, int position) {
        TextView tvStation = viewHolder.getView(R.id.tv_station);
        tvStation.setText(stationNoModel.getName());
        viewHolder.getContentView().setOnClickListener(view -> {
            if(onItemClickListener!=null){
                onItemClickListener.onItemClickListener(view,viewHolder.getLayoutPosition());
            }
        });
    }
}
