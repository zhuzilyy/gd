package com.gd.form.adapter;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.gd.form.R;
import com.gd.form.model.Jobs;
import com.gd.form.model.JobsMy;

import java.util.List;


public class JobsListAdapter extends BaseRecyclerViewAdapter<Jobs> {
    private Dialog detailDialog;

    /**
     * @param context  {@link Context}
     * @param list     数据集合
     * @param layoutId RecyclerView item布局ID
     */
    public JobsListAdapter(Context context, List<Jobs> list, int layoutId) {
        super(context, list, layoutId);
    }


    @Override
    protected void bindData(BaseViewHolder viewHolder, Jobs job, int position) {
        TextView tv_job=viewHolder.getView(R.id.tv_job);
        tv_job.setText(job.getName()+"("+job.getId()+")");


        viewHolder.getContentView().setOnClickListener(view -> {
            if(onItemClickListener!=null){
                onItemClickListener.onItemClickListener(view,viewHolder.getLayoutPosition());
            }
        });
    }
}
