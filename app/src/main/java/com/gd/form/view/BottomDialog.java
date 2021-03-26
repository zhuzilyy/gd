package com.gd.form.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gd.form.R;
import com.gd.form.adapter.BottomDialogAdapter;
import com.gd.form.model.BottomDialogBean;
import com.gd.form.utils.GsonUtil;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


/**
 * <p>类描述：底部弹出框
 */

public class BottomDialog extends Dialog {

    private boolean isAnimation = false;
    private List<BottomDialogBean> list;
    private List<BottomDialogBean> lastList;
    private LinearLayout mRootView;
    private OnClickListener mListener;
    private OnBottomShowDetailListener detailListener;
    private TextView titleView;
    private RecyclerView dialogBottomRecyclerView;
    private boolean isSure = false;
    private BottomDialogAdapter adapter;


    public BottomDialog(Context context, boolean isShow) {
        super(context);
        EventBus.getDefault().register(this);
        init(context,isShow);
    }


    private void init(Context context,boolean isShow) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mRootView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.dialog_bottom_layout, null, false);
        setContentView(mRootView);

        titleView = mRootView.findViewById(R.id.dialogBottomTitleTextView);
        TextView dialogBottomSubmitTextView = mRootView.findViewById(R.id.dialogBottomSubmitTextView);
        dialogBottomRecyclerView = mRootView.findViewById(R.id.dialogBottomRecyclerView);
        TextView dialogBottomTextView=mRootView.findViewById(R.id.dialogBottomTextView);
        ViewSwitcher dialogBottomViewSwitcher=mRootView.findViewById(R.id.dialogBottomViewSwitcher);
        if(isShow){
            dialogBottomViewSwitcher.setVisibility(View.VISIBLE);
        }else {
            dialogBottomViewSwitcher.setVisibility(View.GONE);
        }
        dialogBottomSubmitTextView.setOnClickListener(view1 -> {
            dismiss();
        });
        dialogBottomTextView.setOnClickListener(view -> {
            if(detailListener!=null&&view.getVisibility()== View.VISIBLE){
                detailListener.showDetail();
                dismiss();
            }
        });
        slideToUp();
        Window dialogWindow = getWindow();
        if (dialogWindow != null) {
            WindowManager.LayoutParams params = dialogWindow.getAttributes();
            dialogWindow.setGravity(Gravity.BOTTOM);
            params.x = 0; // 新位置X坐标
            params.y = 0; // 新位置Y坐标
            mRootView.measure(0, 0);
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            dialogWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogWindow.setAttributes(params);
        }
    }


    public void setData(String titleText, List<BottomDialogBean> list) {
        isSure = false;
        String sList = GsonUtil.toString(list);
        this.list = GsonUtil.fromJson(sList, new TypeToken<List<BottomDialogBean>>() {
        }.getType());
        this.lastList = GsonUtil.fromJson(sList, new TypeToken<List<BottomDialogBean>>() {
        }.getType());
        titleView.setText(titleText);
        dialogBottomRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        if (adapter == null) {
            adapter = new BottomDialogAdapter(getContext(), this.list, R.layout.dialog_bottom_parent);
            dialogBottomRecyclerView.setAdapter(adapter);
        } else {
            adapter.updateList(this.list);
        }
    }


    public void setListener(OnClickListener listener) {
        mListener = listener;
    }
    public void setDetailListener(OnBottomShowDetailListener detailListener){
        this.detailListener=detailListener;
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        if (isAnimation) {
            return;
        }
        isAnimation = true;
        slideToDown(() -> {
            isAnimation = false;
            BottomDialog.super.dismiss();
            if (mListener != null) {
                if (isSure) {
                    //确定传递最新值
                    mListener.click(this.list, true);
                } else {
                    //之前传递的值
                    mListener.click(this.lastList, false);
                }
            }
            EventBus.getDefault().unregister(this);
        });
    }

    public interface OnClickListener {
        void click(List<BottomDialogBean> list, boolean dataChange);
    }

    public interface OnBottomShowDetailListener{
        void showDetail();
    }

    interface AnimationListener {
        void onFinish();
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


    }

    /**
     * 隐藏动画
     * @param listener 动画监听
     */
    private void slideToDown(AnimationListener listener) {
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


    /**
     * tag @Subscribe 注解必须要写，线程需要指定
     * 方法名可随意
     *
     * @param event 接收数据
     */
//    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
//    public void onMessageEvent(EventMessage event) {
//        if (event.getToView().equals(Constant.BOTTOMDIALOG)) {
//            isSure=true;
//            this.list.get(event.getParentPosition()).getList().get(event.getPosition()).setCheck(event.isChecked());
//        }
//        EventBus.getDefault().removeStickyEvent(event);
//    }
}
