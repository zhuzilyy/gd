package com.gd.form.activity;

import android.util.Log;

import com.gd.form.base.BaseActivity;

public class TestActivity extends BaseActivity {

    @Override
    protected void setStatusBar() {
        Log.i("tag","2222222222");
    }

    @Override
    protected int getActLayoutId() {
        return 0;
    }
}
