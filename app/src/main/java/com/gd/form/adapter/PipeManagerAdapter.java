package com.gd.form.adapter;

import android.content.Context;
import android.widget.TextView;

import com.gd.form.R;
import com.gd.form.model.SearchOwnerModel;

import java.util.List;

/**
 * <p>类描述：一句话描述下这个类的作用
 * <p>创建人：wh
 * <p>创建时间：2020/5/11
 */
public class PipeManagerAdapter extends BaseRecyclerViewAdapter<SearchOwnerModel> {
    /**
     * @param context  {@link Context}
     * @param list     数据集合
     * @param layoutId RecyclerView item布局ID
     */
    public PipeManagerAdapter(Context context, List<SearchOwnerModel> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected void bindData(BaseViewHolder viewHolder, SearchOwnerModel model, int position) {
        TextView tvManagerName = viewHolder.getView(R.id.tv_managerName);
        TextView tvStartStationNo = viewHolder.getView(R.id.tv_startStationNo);
        TextView tvEndStationNo = viewHolder.getView(R.id.tv_endStationNo);
        tvManagerName.setText(model.getOwnername());
        tvStartStationNo.setText(model.getStakename());
        tvEndStationNo.setText(model.getEndstakename());
//        tvRecord.setText(value);
        viewHolder.getContentView().setOnClickListener(view -> {
            if(onItemClickListener!=null){
                onItemClickListener.onItemClickListener(view,viewHolder.getLayoutPosition());
            }
        });
    }
}
