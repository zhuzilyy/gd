package com.gd.form.adapter;

import android.content.Context;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.gd.form.R;
import com.gd.form.model.BottomDialogBean;
import com.gd.form.utils.Util;

import java.util.List;

/**
 * <p>类描述：底部弹出框适配器
 */
public class BottomDialogAdapter extends BaseRecyclerViewAdapter<BottomDialogBean> {

    /**
     * @param context  {@link Context}
     * @param list     数据集合
     * @param layoutId RecyclerView item布局ID
     */
    public BottomDialogAdapter(Context context, List<BottomDialogBean> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected void bindData(BaseViewHolder viewHolder, BottomDialogBean bottomDialogBean, int position) {
        TextView parentLeftTextView=viewHolder.getView(R.id.parentLeftTextView);
        TextView parentRightTextView=viewHolder.getView(R.id.parentRightTextView);
        RecyclerView parentRecyclerView=viewHolder.getView(R.id.parentRecyclerView);
        parentLeftTextView.setText(bottomDialogBean.getTitle());
        if(!Util.isEmpty(bottomDialogBean.getRightText())){
            parentRightTextView.setText(bottomDialogBean.getRightText());
        }
        parentRecyclerView.setLayoutManager(new GridLayoutManager(context,bottomDialogBean.getNumColumns()));
        if(bottomDialogBean.getType()==3){
            parentRecyclerView.setAdapter(new BottomDialogChildAdapter(context,bottomDialogBean.getList(),R.layout.dialog_bottom_text,position,true));
        }else{
            parentRecyclerView.setAdapter(new BottomDialogChildAdapter(context,bottomDialogBean.getList(),R.layout.dialog_bottom_text,position,false));
        }
    }



}
