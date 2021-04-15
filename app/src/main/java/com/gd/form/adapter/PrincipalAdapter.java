package com.gd.form.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.gd.form.R;
import com.gd.form.model.PipePrincial;

import java.util.List;

/**
 * <p>类描述：一句话描述下这个类的作用
 * <p>创建人：wh
 * <p>创建时间：2020/5/11
 */
public class PrincipalAdapter extends BaseRecyclerViewAdapter<PipePrincial> {
    /**
     * @param context  {@link Context}
     * @param pipeOwnerList     数据集合
     * @param layoutId RecyclerView item布局ID
     */
    public PrincipalAdapter(Context context, List<PipePrincial> pipeOwnerList, int layoutId) {
        super(context, pipeOwnerList, layoutId);
    }

    @Override
    protected void bindData(BaseViewHolder viewHolder, PipePrincial value, int position) {
        TextView tvRecord = viewHolder.getView(R.id.tv_record);
        TextView delete = viewHolder.getView(R.id.tv_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        tvRecord.setText(value.getDepartmentName()+"("+value.getUserName()+")");
//        viewHolder.getContentView().setOnClickListener(view -> {
//            if(onItemClickListener!=null){
//                onItemClickListener.onItemClickListener(view,viewHolder.getLayoutPosition());
//            }
//        });
        viewHolder.getContentView().findViewById(R.id.tv_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClickListener!=null){
                    onItemClickListener.onItemClickListener(view,viewHolder.getLayoutPosition());
                }
            }
        });
    }
}
