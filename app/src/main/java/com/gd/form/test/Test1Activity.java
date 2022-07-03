package com.gd.form.test;

import android.util.Log;

import com.gd.form.base.BaseActivity;

public class Test1Activity extends BaseActivity {
    @Override
    protected void setStatusBar() {
        Log.i("tag","222222222");
    }

    @Override
    protected int getActLayoutId() {
        return 0;
    }
}
