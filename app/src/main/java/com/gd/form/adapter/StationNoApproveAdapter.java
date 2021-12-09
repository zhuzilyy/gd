package com.gd.form.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gd.form.R;
import com.gd.form.model.StationNoApproveModel;
import com.gd.form.utils.TimeUtil;

import java.util.List;

/**
 * <p>类描述：一句话描述下这个类的作用
 * <p>创建人：wh
 * <p>创建时间：2020/5/11
 */
public class StationNoApproveAdapter extends BaseRecyclerViewAdapter<StationNoApproveModel> {
    /**
     * @param context  {@link Context}
     * @param list     数据集合
     * @param layoutId RecyclerView item布局ID
     */
    public StationNoApproveAdapter(Context context, List<StationNoApproveModel> list, int layoutId) {
        super(context, list, layoutId);
    }
    @Override
    protected void bindData(BaseViewHolder viewHolder, StationNoApproveModel model, int position) {
        TextView tvStationNo = viewHolder.getView(R.id.tv_stationNo);
        TextView tvApplyName = viewHolder.getView(R.id.tv_applyName);
        TextView tvApplyOperation = viewHolder.getView(R.id.tv_apply_operation);
        TextView tvApplyTime = viewHolder.getView(R.id.tv_apply_time);
        Button btnAgree = viewHolder.getView(R.id.btn_agree);
        Button btnDisagree = viewHolder.getView(R.id.btn_disagree);
        if(model.getOperauthority() == 0){
            btnAgree.setVisibility(View.INVISIBLE);
            btnDisagree.setVisibility(View.INVISIBLE);
        }else{
            btnAgree.setVisibility(View.VISIBLE);
            btnDisagree.setVisibility(View.VISIBLE);
        }
        tvStationNo.setText(model.getStakename());
        tvApplyName.setText(model.getCreatorname());
        tvApplyOperation.setText(model.getOpername());
        tvApplyTime.setText(TimeUtil.longToFormatTime(model.getCreatime().getTime()));
        viewHolder.getContentView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener(view, viewHolder.getLayoutPosition());
                }
            }
        });
        viewHolder.getContentView().findViewById(R.id.btn_detail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener(v, viewHolder.getLayoutPosition());
                }
            }
        });
        viewHolder.getContentView().findViewById(R.id.btn_agree).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener(v, viewHolder.getLayoutPosition());
                }
            }
        });
        viewHolder.getContentView().findViewById(R.id.btn_disagree).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener(v, viewHolder.getLayoutPosition());
                }
            }
        });

    }
}
