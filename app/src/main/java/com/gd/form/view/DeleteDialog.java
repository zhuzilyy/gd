package com.gd.form.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.gd.form.R;

public class DeleteDialog extends Dialog {
    private TextView tvTitle;
    private Button btnCancel;
    private Button btnConfirm;
    private TextView tvContent;
    private String title,content;
    private OnClickBottomListener onClickBottomListener;
    private Context context;
    public DeleteDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_delete);
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.CENTER;
            params.width = (int) ((context.getResources().getDisplayMetrics().widthPixels) * 0.7); // 宽度// 宽度
            window.setAttributes(params);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        tvTitle = findViewById(R.id.dialog_title);
        tvContent = findViewById(R.id.content);
        btnCancel = findViewById(R.id.btn_cancel);
        btnConfirm = findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickBottomListener!=null){
                    onClickBottomListener.onPositiveClick();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickBottomListener!=null){
                    onClickBottomListener.onNegativeClick();
                }
            }
        });
        //初始化界面数据
        refreshView();

    }

    private void refreshView() {
        //如果用户自定了title和message
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }
        if (!TextUtils.isEmpty(content)) {
            tvContent.setText(content);
        }
    }

    public  void setOnClickBottomListener(OnClickBottomListener onClickBottomListener) {
        this.onClickBottomListener = onClickBottomListener;
    }
    public interface OnClickBottomListener{
        /**
         * 点击确定按钮事件
         */
        public void onPositiveClick();
        /**
         * 点击取消按钮事件
         */
        public void onNegativeClick();
    }

    public DeleteDialog setTitle(String title) {
        this.title = title;
        return this;
    }
    public DeleteDialog setContent(String content) {
        this.content = content;
        return this;
    }

}
