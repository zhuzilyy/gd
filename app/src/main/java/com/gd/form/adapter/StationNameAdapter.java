package com.gd.form.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.gd.form.R;

import java.util.List;

/**
 * <p>类描述：一句话描述下这个类的作用
 * <p>创建人：wh
 * <p>创建时间：2020/5/11
 */
public class StationNameAdapter extends BaseRecyclerViewAdapter<String> {
    /**
     * @param context  {@link Context}
     * @param list     数据集合
     * @param layoutId RecyclerView item布局ID
     */
    public StationNameAdapter(Context context, List<String> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected void bindData(BaseViewHolder viewHolder, String value, int position) {
        TextView tvStationName = viewHolder.getView(R.id.tv_stationName);
        TextView tvStationNo = viewHolder.getView(R.id.tv_stationNo);
        if(!TextUtils.isEmpty(value)){
            String[] stationArr = value.split(":");
            tvStationName.setText(stationArr[0]);
            tvStationNo.setText(stationArr[1]);
        }else{
            tvStationName.setText("暂无");
            tvStationNo.setText("暂无");
        }
        viewHolder.getContentView().setOnClickListener(view -> {
            if(onItemClickListener!=null){
                onItemClickListener.onItemClickListener(view,viewHolder.getLayoutPosition());
            }
        });
    }
}
