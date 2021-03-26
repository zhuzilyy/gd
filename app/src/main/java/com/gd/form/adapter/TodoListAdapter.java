package com.gd.form.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.gd.form.R;
import com.gd.form.activity.HandOverActivity;
import com.gd.form.utils.Util;

import java.util.List;

/**
 * <p>类描述：一句话描述下这个类的作用
 * <p>创建人：wh
 * <p>创建时间：2020/5/11
 */
public class TodoListAdapter extends BaseRecyclerViewAdapter<String> {
    private Dialog detailDialog;

    /**
     * @param context  {@link Context}
     * @param list     数据集合
     * @param layoutId RecyclerView item布局ID
     */
    public TodoListAdapter(Context context, List<String> list, int layoutId) {
        super(context, list, layoutId);
    }



    @Override
    protected void bindData(BaseViewHolder viewHolder, String s, int position) {

      //  TextView num=viewHolder.getView(R.id.num);
      //  num.setText(""+position);

        Button bt_view=viewHolder.getView(R.id.bt_view);
        Button bt_do=viewHolder.getView(R.id.bt_do);
        bt_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailDialog = new Dialog(context, R.style.BottomDialog);
                LinearLayout root = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.dialog_task_detail, null);
                TextView tv_confirm = root.findViewById(R.id.tv_confirm);
                tv_confirm.setOnClickListener(view -> {
                    if (detailDialog != null && detailDialog.isShowing()) {
                        detailDialog.dismiss();
                    }
                });
                //初始化视图
                detailDialog.setContentView(root);
                Window window = detailDialog.getWindow();
                if (window != null) {
                    WindowManager.LayoutParams params = window.getAttributes();
                    params.gravity = Gravity.CENTER;
                    params.width = context.getResources().getDisplayMetrics().widthPixels - Util.dpToPx(context, (int) context.getResources().getDimension(R.dimen.d_220)); // 宽度
                    window.setAttributes(params);
                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
                detailDialog.show();

            }
        });

        bt_do.setOnClickListener(v -> {
            Intent intent=new Intent(context, HandOverActivity.class);
            context.startActivity(intent);
        });

        viewHolder.getContentView().setOnClickListener(view -> {
            if(onItemClickListener!=null){
                onItemClickListener.onItemClickListener(view,viewHolder.getLayoutPosition());
            }
        });
    }
}
