package com.gd.form.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.gd.form.R;
import com.gd.form.base.BaseActivity;
import com.jaeger.library.StatusBarUtil;

public class SplashActivity extends BaseActivity {
    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.bg_White));

    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(2000);//延时两秒跳转
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                openActivity(LoginActivity.class,null,true);
            }
        }.start();
    }
}
