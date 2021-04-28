package com.gd.form.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.gd.form.R;
import com.gd.form.activity.PipeHighZoneActivity;

import java.util.List;

/**
 * <p>类描述：一句话描述下这个类的作用
 * <p>创建人：wh
 * <p>创建时间：2020/5/11
 */
public class HighZoneListAdapter extends BaseRecyclerViewAdapter<String> {
    /**
     * @param context  {@link Context}
     * @param list     数据集合
     * @param layoutId RecyclerView item布局ID
     */
    public HighZoneListAdapter(Context context, List<String> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected void bindData(BaseViewHolder viewHolder, String value, int position) {
        Button btnCheck = viewHolder.getView(R.id.btn_check);
        Button btnUpdate = viewHolder.getView(R.id.btn_update);
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,PipeHighZoneActivity.class);
                intent.putExtra("tag","check");
                intent.putExtra("highZoneId","");
                context.startActivity(intent);
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,PipeHighZoneActivity.class);
                intent.putExtra("tag","update");
                intent.putExtra("highZoneId","");
                context.startActivity(intent);
            }
        });
//        tvRecord.setText(value);
        viewHolder.getContentView().setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClickListener(view, viewHolder.getLayoutPosition());
            }
        });
    }
}
