package com.gd.form.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.gd.form.R;
import com.gd.form.activity.JobsActivity;
import com.gd.form.activity.SgbhActivity;
import com.gd.form.activity.UsersActivity;
import com.gd.form.activity.XhglActivity;
import com.gd.form.base.BaseFragment;
import com.gd.form.model.Jobs;
import com.jaeger.library.StatusBarUtil;

import butterknife.OnClick;


public class UserFragment extends BaseFragment {



    @Override
    protected void initView(Bundle bundle) {
        StatusBarUtil.setTranslucentForImageView(getActivity(), 0, null);

    }
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user;
    }


    @SuppressLint("NonConstantResourceId")
    @OnClick({
            R.id.ll_gwwh,
            R.id.ll_rywh,


    })
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.ll_gwwh:
               openActivity(JobsActivity.class);
                break;
            case R.id.ll_rywh:
               openActivity(UsersActivity.class);
                break;

        }
    }
}
