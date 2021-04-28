package com.gd.form.adapter;

import android.content.Context;

import com.gd.form.R;

import java.util.List;

/**
 * <p>类描述：一句话描述下这个类的作用
 * <p>创建人：wh
 * <p>创建时间：2020/5/11
 */
public class AdvocacyBoardListAdapter extends BaseRecyclerViewAdapter<String> {
    /**
     * @param context  {@link Context}
     * @param list     数据集合
     * @param layoutId RecyclerView item布局ID
     */
    public AdvocacyBoardListAdapter(Context context, List<String> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected void bindData(BaseViewHolder viewHolder, String value, int position) {
//        TextView tvRecord = viewHolder.getView(R.id.tv_value);
//        tvRecord.setText(value);
        viewHolder.getContentView().findViewById(R.id.btn_delete).setOnClickListener(view -> {
            if(onItemClickListener!=null){
                onItemClickListener.onItemClickListener(view,viewHolder.getLayoutPosition());
            }
        });
    }
}
