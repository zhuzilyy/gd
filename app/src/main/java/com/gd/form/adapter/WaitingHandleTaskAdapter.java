package com.gd.form.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.gd.form.R;
import com.gd.form.model.WaitingHandleTaskModel;
import com.gd.form.utils.TimeUtil;

import java.util.List;

/**
 * <p>类描述：一句话描述下这个类的作用
 * <p>创建人：wh
 * <p>创建时间：2020/5/11
 */
public class WaitingHandleTaskAdapter extends BaseRecyclerViewAdapter<WaitingHandleTaskModel> {
    /**
     * @param context  {@link Context}
     * @param list     数据集合
     * @param layoutId RecyclerView item布局ID
     */
    public WaitingHandleTaskAdapter(Context context, List<WaitingHandleTaskModel> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected void bindData(BaseViewHolder viewHolder, WaitingHandleTaskModel model, int position) {
        TextView tvSendName = viewHolder.getView(R.id.tv_sendName);
        TextView tvReceiveName = viewHolder.getView(R.id.tv_receiveName);
        TextView tvEndTime = viewHolder.getView(R.id.tv_endTime);
        TextView tvFinishTime = viewHolder.getView(R.id.tv_finishTime);
        tvSendName.setText(model.getCreatorname());
        tvReceiveName.setText(model.getRecipientname());
        if (model.getPlantime() != null) {
            tvEndTime.setText(TimeUtil.longToFormatTime(model.getPlantime().getTime()));
        }
        if (model.getFinishtime() != null) {
            tvFinishTime.setText(TimeUtil.longToFormatTime(model.getFinishtime().getTime()));
        }

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
