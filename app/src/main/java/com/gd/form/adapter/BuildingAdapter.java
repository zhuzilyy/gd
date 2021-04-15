package com.gd.form.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gd.form.R;
import com.gd.form.model.BuildingModel;

import java.util.List;

/**
 * <p>类描述：一句话描述下这个类的作用
 * <p>创建人：wh
 * <p>创建时间：2020/5/11
 */
public class BuildingAdapter extends BaseRecyclerViewAdapter<BuildingModel> {
    /**
     * @param context  {@link Context}
     * @param buildingModelList     数据集合
     * @param layoutId RecyclerView item布局ID
     */
    public BuildingAdapter(Context context, List<BuildingModel> buildingModelList, int layoutId) {
        super(context, buildingModelList, layoutId);
    }

    @Override
    protected void bindData(BaseViewHolder viewHolder, BuildingModel model, int position) {
        TextView tvPipeName = viewHolder.getView(R.id.tv_pipeName);
        if(!TextUtils.isEmpty((model.getPipedesc()))){
            tvPipeName.setText(model.getPipedesc().split(":")[0]);
        }else{
            tvPipeName.setText("暂无");
        }
        TextView tvOverName = viewHolder.getView(R.id.tv_overName);
        tvOverName.setText(model.getOvername());
        TextView tvLocation = viewHolder.getView(R.id.tv_location);
        tvLocation.setText(model.getLocationdesc());
        TextView tvTime = viewHolder.getView(R.id.tv_time);
        tvTime.setText(model.getGenernaldate());
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
