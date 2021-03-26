package com.gd.form.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * <p>类描述：RecyclerView适配器抽象类，ViewHolder使用BaseViewHolder
 * <p>创建人：wh
 * <p>创建时间：2018/6/21
 */
public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    //不使用private的原因是：继承之后的适配器可能存在需要使用context、list
    protected Context context;
    protected List<T> list;
    protected int layoutId;
    protected LayoutInflater inflater;
    //点击事件
    protected OnItemClickListener onItemClickListener;
    //长按事件
    protected OnItemLongClickListener onItemLongClickListener;

    /**
     * @param context {@link Context}
     * @param list 数据集合
     * @param layoutId RecyclerView item布局ID
     */
    public BaseRecyclerViewAdapter(Context context, List<T> list, int layoutId) {
        this.context = context;
        this.list = list;
        this.layoutId = layoutId;
        this.inflater= LayoutInflater.from(context);
    }

    /**
     * 适配器不为空调用此函数传递list数据集合
     * @param list 数据集合
     */
    public void updateList(List<T> list){
        this.list=list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View contentView=inflater.inflate(layoutId,parent,false);
        return new BaseViewHolder(contentView);
    }

    /**
     * 重写此方法
     * @param holder {@link BaseViewHolder}
     * @param position 下标
     */
    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        bindData(holder,list.get(position),position);
    }

    /**
     * 重写此方法
     * @return 数据集合size()
     */
    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    /**
     * <p>绑定数据
     * demo：TextView tv=viewHolder.getView(R.id.tv);
     * tv.setText(t.getName());
     * @param viewHolder {@link BaseViewHolder}
     * @param t list已经get(position)的Bean
     * @param position {@link #onBindViewHolder(BaseViewHolder, int)}
     */
    protected abstract void bindData(BaseViewHolder viewHolder, T t, int position);

    /**
     * RecyclerView点击事件
     * @param onItemClickListener 点击事件{@link OnItemClickListener}
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }

    /**
     * RecyclerView长按事件
     * <p>可能不使用但需要保留
     * @param onItemLongClickListener 长按事件{@link OnItemLongClickListener}
     */
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener){
        this.onItemLongClickListener=onItemLongClickListener;
    }

}
