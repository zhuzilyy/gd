package com.gd.form.fragment;

import android.os.Bundle;
import android.view.View;

import com.gd.form.R;
import com.gd.form.activity.TaskStatusActivity;
import com.gd.form.base.BaseFragment;

import butterknife.OnClick;


public class MessageFragment extends BaseFragment {

    @Override
    protected void initView(Bundle bundle) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message;
    }
    @OnClick({R.id.ll_dbrw})
    public void click(View view){
        switch (view.getId()){
            case R.id.ll_dbrw:
                openActivity(TaskStatusActivity.class);
                break;
        }
    }
}
