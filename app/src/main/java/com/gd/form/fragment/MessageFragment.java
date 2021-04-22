package com.gd.form.fragment;

import android.os.Bundle;
import android.view.View;

import com.gd.form.R;
import com.gd.form.activity.NoApproveActivity;
import com.gd.form.activity.OverTimeTaskActivity;
import com.gd.form.activity.WaitingActivity;
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

    @OnClick({
            R.id.ll_waiting,
            R.id.ll_overTime,
            R.id.ll_noApprove,
    })
    public void click(View view) {
        switch (view.getId()) {
            case R.id.ll_waiting:
                openActivity(WaitingActivity.class);
                break;
            case R.id.ll_overTime:
                openActivity(OverTimeTaskActivity.class);
                break;
            case R.id.ll_noApprove:
                openActivity(NoApproveActivity.class);
                break;
        }
    }
}
