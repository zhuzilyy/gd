package com.gd.form.adapter;

import android.view.View;

/**
 * <p>类描述：RecyclerView长按事件接口
 * <p>创建人：wh
 * <p>创建时间：2019/6/21
 */
public interface OnItemLongClickListener {
    /**
     * 长按事件
     * @param v 点击控件
     * @param position 下标
     */
    void onItemLongClickListener(View v, int position);
}
