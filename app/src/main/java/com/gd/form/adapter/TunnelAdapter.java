package com.gd.form.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.gd.form.R;
import com.gd.form.model.TunnelModel;

import java.util.List;

/**
 * <p>类描述：一句话描述下这个类的作用
 * <p>创建人：wh
 * <p>创建时间：2020/5/11
 */
public class TunnelAdapter extends BaseRecyclerViewAdapter<TunnelModel> {
    /**
     * @param context           {@link Context}
     * @param tunnelModels 数据集合
     * @param layoutId          RecyclerView item布局ID
     */
    public TunnelAdapter(Context context, List<TunnelModel> tunnelModels, int layoutId) {
        super(context, tunnelModels, layoutId);
    }

    @Override
    protected void bindData(BaseViewHolder viewHolder, TunnelModel tunnelModel, int position) {
        TextView tvPipeName = viewHolder.getView(R.id.tv_pipeName);
        tvPipeName.setText(tunnelModel.getPipename());
        TextView tvTunnelName = viewHolder.getView(R.id.tv_tunnelName);
        tvTunnelName.setText(tunnelModel.getPipename());
        TextView tvLocation = viewHolder.getView(R.id.tv_location);
        tvLocation.setText(tunnelModel.getLocation());
        viewHolder.getContentView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener(view, viewHolder.getLayoutPosition());
                }
            }
        });
    }
}
