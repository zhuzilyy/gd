package com.gd.form.adapter;

import android.view.View;

/**
 * <p>类描述：RecyclerView点击事件接口
 * <p>创建人：wh
 * <p>创建时间：2019/6/21
 */
public interface OnItemClickListener {
    /**
     * 点击事件
     * @param v 点击控件
     * @param position 下标
     */
    void onItemClickListener(View v, int position);
}
