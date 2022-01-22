package com.gd.form.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.gd.form.R;
import com.gd.form.model.FileDetailModel;

import java.util.List;

/**
 * <p>类描述：一句话描述下这个类的作用
 * <p>创建人：wh
 * <p>创建时间：2020/5/11
 */
public class FileDetailAdapter extends BaseRecyclerViewAdapter<FileDetailModel> {
    /**
     * @param context  {@link Context}
     * @param formModelList     数据集合
     * @param layoutId RecyclerView item布局ID
     */
    public FileDetailAdapter(Context context, List<FileDetailModel> formModelList, int layoutId) {
        super(context, formModelList, layoutId);
    }

    @Override
    protected void bindData(BaseViewHolder viewHolder, FileDetailModel fileDetailModel, int position) {
        TextView tvFileName = viewHolder.getView(R.id.tv_fileName);
        tvFileName.setText(fileDetailModel.getFilename());
        viewHolder.getView(R.id.btn_check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null){
                    onItemClickListener.onItemClickListener(v,viewHolder.getLayoutPosition());
                }
            }
        });
        viewHolder.getView(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener(v, viewHolder.getLayoutPosition());
                }
            }
        });
    }
}
