package com.gd.form.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.gd.form.R;
import com.gd.form.model.HighZoneModel;

import java.util.List;

/**
 * <p>类描述：一句话描述下这个类的作用
 * <p>创建人：wh
 * <p>创建时间：2020/5/11
 */
public class HighZoneAdapter extends BaseRecyclerViewAdapter<HighZoneModel> {
    /**
     * @param context  {@link Context}
     * @param highZoneModelList     数据集合
     * @param layoutId RecyclerView item布局ID
     */
    public HighZoneAdapter(Context context, List<HighZoneModel> highZoneModelList, int layoutId) {
        super(context, highZoneModelList, layoutId);
    }

    @Override
    protected void bindData(BaseViewHolder viewHolder, HighZoneModel model, int position) {
        TextView tvPipeName = viewHolder.getView(R.id.tv_pipeName);
        tvPipeName.setText(model.getPipedesc());
        TextView tvHighZoneName = viewHolder.getView(R.id.tv_highZoneName);
        tvHighZoneName.setText(model.getName());
        TextView tvLocation = viewHolder.getView(R.id.tv_location);
        tvLocation.setText(model.getLocationdesc());
        viewHolder.getContentView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClickListener!=null){
                    onItemClickListener.onItemClickListener(view,viewHolder.getLayoutPosition());
                }
            }
        });
    }
}
