package com.gd.form.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.gd.form.R;
import com.gd.form.model.EventHistoryModel;
import com.gd.form.utils.TimeUtil;

import java.util.List;

/**
 * <p>类描述：一句话描述下这个类的作用
 * <p>创建人：wh
 * <p>创建时间：2020/5/11
 */
public class EventHistoryAdapter extends BaseRecyclerViewAdapter<EventHistoryModel> {
    /**
     * @param context           {@link Context}
     * @param highZoneModelList 数据集合
     * @param layoutId          RecyclerView item布局ID
     */
    public EventHistoryAdapter(Context context, List<EventHistoryModel> highZoneModelList, int layoutId) {
        super(context, highZoneModelList, layoutId);
    }

    @Override
    protected void bindData(BaseViewHolder viewHolder, EventHistoryModel model, int position) {
        TextView tvEventName = viewHolder.getView(R.id.tv_eventName);
        TextView tvEmploy = viewHolder.getView(R.id.tv_employ);
        TextView tvTime = viewHolder.getView(R.id.tv_time);
        tvEventName.setText(model.getEventname());
        tvEmploy.setText(model.getCreatorname());
        tvTime.setText(TimeUtil.longToFormatTimeHMS(model.getCreatime().getTime()));
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
