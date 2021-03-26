package com.gd.form.adapter;

import android.util.SparseArray;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.recyclerview.widget.RecyclerView;

/**
 * <p>类描述：RecyclerView.ViewHolder减少findViewById方法类
 * <p>创建人：wh
 * <p>创建时间：2018/6/21
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {
    //使用SparseArray以取代HashMap<Integer,Object>
    private SparseArray<View> views;
    
   public BaseViewHolder(View itemView) {
        super(itemView);
        this.views=new SparseArray<>();
   }

    /**
     * 简单初始化控件
     * @param viewId 控件id
     * @param <T> 泛型T继承自View
     * @return 控件
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(@IdRes int viewId) {
        //先从控件集合中根据id获取控件
        View view = views.get(viewId);
        if (view == null) {
            //如果没有，findViewById并且将控件和id存入控件集合
            view = itemView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    public View getContentView() {
        return itemView;
    }
}
