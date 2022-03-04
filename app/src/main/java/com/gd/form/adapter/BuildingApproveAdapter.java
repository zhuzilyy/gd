package com.gd.form.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gd.form.R;
import com.gd.form.model.BuildingApproveModel;
import com.gd.form.utils.TimeUtil;

import java.util.List;

/**
 * <p>类描述：一句话描述下这个类的作用
 * <p>创建人：wh
 * <p>创建时间：2020/5/11
 */
public class BuildingApproveAdapter extends BaseRecyclerViewAdapter<BuildingApproveModel> {
    /**
     * @param context  {@link Context}
     * @param list     数据集合
     * @param layoutId RecyclerView item布局ID
     */
    public BuildingApproveAdapter(Context context, List<BuildingApproveModel> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected void bindData(BaseViewHolder viewHolder, BuildingApproveModel model, int position) {
        TextView tvStationNo = viewHolder.getView(R.id.tv_stationNo);
        TextView tvBuildingName = viewHolder.getView(R.id.tv_buildingName);
        TextView tvApplyName = viewHolder.getView(R.id.tv_applyName);
        TextView tvApplyOperation = viewHolder.getView(R.id.tv_applyOperation);
        TextView tvApplyTime = viewHolder.getView(R.id.tv_applyTime);
        Button btnAgree = viewHolder.getView(R.id.btn_agree);
        Button btnDisagree = viewHolder.getView(R.id.btn_disagree);
        tvStationNo.setText(model.getStakename());
        tvBuildingName.setText(model.getOtherdevname());
        tvApplyName.setText(model.getCreatorname());
        tvApplyOperation.setText(model.getOpername());
        tvApplyTime.setText(TimeUtil.longToFormatTime(model.getCreatime().getTime()));
        if(model.getOperauthority() == 0){
            btnAgree.setVisibility(View.GONE);
            btnDisagree.setVisibility(View.GONE);
        }else{
            btnAgree.setVisibility(View.VISIBLE);
            btnDisagree.setVisibility(View.VISIBLE);
        }
        viewHolder.getContentView().findViewById(R.id.btn_agree).setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClickListener(view, viewHolder.getLayoutPosition());
            }
        });
        viewHolder.getContentView().findViewById(R.id.btn_disagree).setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClickListener(view, viewHolder.getLayoutPosition());
            }
        });
        viewHolder.getContentView().findViewById(R.id.btn_detail).setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClickListener(view, viewHolder.getLayoutPosition());
            }
        });
    }
}
