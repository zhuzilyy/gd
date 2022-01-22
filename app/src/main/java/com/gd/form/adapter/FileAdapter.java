package com.gd.form.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.gd.form.R;
import com.gd.form.model.StandardFileModel;

import java.util.List;

/**
 * <p>类描述：一句话描述下这个类的作用
 * <p>创建人：wh
 * <p>创建时间：2020/5/11
 */
public class FileAdapter extends BaseRecyclerViewAdapter<StandardFileModel> {
    /**
     * @param context  {@link Context}
     * @param formModelList     数据集合
     * @param layoutId RecyclerView item布局ID
     */
    public FileAdapter(Context context, List<StandardFileModel> formModelList, int layoutId) {
        super(context, formModelList, layoutId);
    }

    @Override
    protected void bindData(BaseViewHolder viewHolder, StandardFileModel standardFileModel, int position) {
        TextView tvFileType = viewHolder.getView(R.id.tv_fileType);
        tvFileType.setText(standardFileModel.getDesc());
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
