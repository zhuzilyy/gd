package com.gd.form.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gd.form.R;
import com.gd.form.activity.UploadEventActivity;
import com.gd.form.model.UploadEventModel;
import com.gd.form.utils.TimeUtil;

import java.util.List;

/**
 * <p>类描述：一句话描述下这个类的作用
 * <p>创建人：wh
 * <p>创建时间：2020/5/11
 */
public class UploadEventAdapter extends BaseRecyclerViewAdapter<UploadEventModel> {
    public OnFinishListener onFinishListener;
    /**
     * @param context           {@link Context}
     * @param highZoneModelList 数据集合
     * @param layoutId          RecyclerView item布局ID
     */
    public UploadEventAdapter(Context context, List<UploadEventModel> highZoneModelList, int layoutId) {
        super(context, highZoneModelList, layoutId);
    }

    @Override
    protected void bindData(BaseViewHolder viewHolder, UploadEventModel model, int position) {
        TextView tvEventName = viewHolder.getView(R.id.tv_eventName);
        TextView tvStationNo = viewHolder.getView(R.id.tv_stationNo);
        TextView tvTime = viewHolder.getView(R.id.tv_time);
        LinearLayout llTime = viewHolder.getView(R.id.ll_time);
        View viewTime = viewHolder.getView(R.id.view_time);
        TextView tvEmploy = viewHolder.getView(R.id.tv_employ);
        LinearLayout llBottom = viewHolder.getView(R.id.ll_bottom);
        Button btnCheck = viewHolder.getView(R.id.btn_check);
        Button btnFinish = viewHolder.getView(R.id.btn_finish);
        tvEventName.setText(model.getFormname());
        tvStationNo.setText(model.getFormtype());
        tvTime.setText(TimeUtil.longToFormatTime(model.getCreatime().getTime()));
        tvEmploy.setText(model.getEmployname());
        if (model.getStatus() == 0) {
            llTime.setVisibility(View.VISIBLE);
            viewTime.setVisibility(View.VISIBLE);
            llBottom.setVisibility(View.VISIBLE);
        } else {
            llTime.setVisibility(View.GONE);
            viewTime.setVisibility(View.GONE);
            llBottom.setVisibility(View.GONE);
        }
        viewHolder.getContentView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener(view, viewHolder.getLayoutPosition());
                }
            }
        });
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UploadEventActivity.class);
                intent.putExtra("formId",model.getFormid());
                intent.putExtra("tag","update");
                context.startActivity(intent);
            }
        });
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onFinishListener!=null){
                    onFinishListener.onFinishClickListener(position);
                }
            }
        });

    }


    public interface OnFinishListener{
       void onFinishClickListener(int position);
    }

    public void setOnFinishListener(OnFinishListener listener) {
        this.onFinishListener = listener;
    }
}