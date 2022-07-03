package com.gd.form.view_test;

import android.util.Log;

import com.gd.form.base.BaseActivity;

public class TestView1 extends BaseActivity {
    @Override
    protected void setStatusBar() {
        Log.i("tag","测试");
    }

    @Override
    protected int getActLayoutId() {
        return 0;
    }
}
