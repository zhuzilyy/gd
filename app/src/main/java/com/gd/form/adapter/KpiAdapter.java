package com.gd.form.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gd.form.R;
import com.gd.form.model.KpiModel;
import com.gd.form.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>类描述：一句话描述下这个类的作用
 * <p>创建人：wh
 * <p>创建时间：2020/5/11
 */
public class KpiAdapter extends BaseRecyclerViewAdapter<KpiModel> {
    /**
     * @param context  {@link Context}
     * @param modelList     数据集合
     * @param layoutId RecyclerView item布局ID
     */
    public KpiAdapter(Context context, List<KpiModel> modelList, int layoutId) {
        super(context, modelList, layoutId);
    }

    @Override
    protected void bindData(BaseViewHolder viewHolder, KpiModel kpiModel, int position) {
        TextView tvTime = viewHolder.getView(R.id.tv_time);
        LinearLayout llOperate = viewHolder.getView(R.id.ll_operate);
        TextView tvBusinessType = viewHolder.getView(R.id.tv_businessType);
        TextView etScore = viewHolder.getView(R.id.et_score);
        TextView etReason = viewHolder.getView(R.id.et_reason);
        TextView tvNoPic = viewHolder.getView(R.id.tv_noPic);
        TextView tvApprove = viewHolder.getView(R.id.tv_approve);
        if(kpiModel.getMainflag() == 0){
            llOperate.setVisibility(View.GONE);
        }else{
            llOperate.setVisibility(View.VISIBLE);
        }
        RecyclerView rvResultPhoto = viewHolder.getView(R.id.rvResultPhoto);
        if (kpiModel.getUploadpicture().equals("00") || TextUtils.isEmpty(kpiModel.getUploadpicture())) {
            tvNoPic.setVisibility(View.VISIBLE);
        }else{
            tvNoPic.setVisibility(View.GONE);
            String[] photoArr = kpiModel.getUploadpicture().split(";");
            List<String> path = new ArrayList<>();
            for (int i = 0; i < photoArr.length; i++) {
                path.add(photoArr[i]);
            }
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
            gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            rvResultPhoto.setLayoutManager(gridLayoutManager);
            PhotoAdapter photoAdapter = new PhotoAdapter(context, path);
            rvResultPhoto.setAdapter(photoAdapter);
        }
        tvTime.setText(TimeUtil.longToFormatTime(kpiModel.getCreatedate().getTime()));
        tvBusinessType.setText(kpiModel.getBusinesstype());
        etScore.setText(kpiModel.getScoreinput()+"");
        etReason.setText(kpiModel.getScorereason());
        tvApprove.setText(kpiModel.getCreatorname());
        viewHolder.getView(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClickListener!=null){
                    onItemClickListener.onItemClickListener(view,viewHolder.getLayoutPosition());
                }
            }
        });
        viewHolder.getView(R.id.btn_check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClickListener!=null){
                    onItemClickListener.onItemClickListener(view,viewHolder.getLayoutPosition());
                }
            }
        });

    }
}
