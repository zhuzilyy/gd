package com.gd.form.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.gd.form.R;
import com.gd.form.adapter.BaseRecyclerViewAdapter;
import com.gd.form.adapter.BaseViewHolder;
import com.gd.form.utils.Util;

import java.util.List;

/**
 * <p>类描述：底部列表弹窗
 */
public class ListDialog extends Dialog {

    private boolean isAnimation = false;
    private LinearLayout mRootView;
    private RecyclerView dialogRecyclerView;
    private Adapter adapter;
    private OnDialogListItemClickListener listItemClick;

    public ListDialog(Context context) {
        super(context);
        init(context);
    }
    private void init(Context context) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mRootView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.dialog_list, null, false);
        setContentView(mRootView);
        dialogRecyclerView = mRootView.findViewById(R.id.dialogRecyclerView);
        dialogRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        Button dialogCancelButton = mRootView.findViewById(R.id.dialogCancelButton);
        dialogCancelButton.setOnClickListener(view1 ->
            dismiss()
        );
        slideToUp();
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.BOTTOM;
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(params);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    public interface OnDialogListItemClickListener{
        void listener(int position);
    }

    public void setListItemClick(OnDialogListItemClickListener listItemClick){
        this.listItemClick=listItemClick;
    }

    public void setData(List<String> list){
        if(!Util.isEmpty(list)){
            if(adapter==null){
                adapter=new Adapter(getContext(),list,R.layout.adapter_dialog_list);
                dialogRecyclerView.setAdapter(adapter);
            }else{
                adapter.updateList(list);
            }
            adapter.setOnItemClickListener((v, position) -> {
                if(listItemClick!=null){
                    listItemClick.listener(position);
                }
            });
        }
    }
    @Override
    public void dismiss() {
        if (isAnimation) {
            return;
        }
        isAnimation = true;
        slideToDown(() -> {
            isAnimation = false;
            ListDialog.super.dismiss();
        });
    }

    class Adapter extends BaseRecyclerViewAdapter<String> {

        /**
         * @param context  {@link Context}
         * @param list     数据集合
         * @param layoutId RecyclerView item布局ID
         */
        Adapter(Context context, List<String> list, int layoutId) {
            super(context, list, layoutId);
        }

        @Override
        protected void bindData(BaseViewHolder viewHolder, String s, int position) {
            Button dialogListButton=viewHolder.getView(R.id.dialogListButton);
            dialogListButton.setText(s);
            dialogListButton.setOnClickListener(view -> {
                if(onItemClickListener!=null){
                    onItemClickListener.onItemClickListener(view,position);
                }
            });
        }
    }

    /**
     * 显示动画
     */
    private void slideToUp() {
        Animation slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);

        slide.setDuration(400);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        mRootView.startAnimation(slide);

        slide.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    /**
     * 隐藏动画
     * @param listener 动画监听
     */
    private void slideToDown(BottomDialog.AnimationListener listener) {
        Animation slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 1.0f);

        slide.setDuration(400);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        mRootView.startAnimation(slide);

        slide.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (listener != null) {
                    listener.onFinish();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

}
