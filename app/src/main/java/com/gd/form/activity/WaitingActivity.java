package com.gd.form.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.gd.form.R;
import com.gd.form.base.BaseActivity;
import com.gd.form.fragment.WaitingApproveFragment;
import com.gd.form.fragment.WaitingHandleFragment;
import com.jaeger.library.StatusBarUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class WaitingActivity extends BaseActivity {
    private WaitingHandleFragment waitingHandleFragment;
    private WaitingApproveFragment waitingApproveFragment;
    private FragmentManager fragmentManager;
    private Fragment currentFragment;
    @BindView(R.id.tv_toDo)
    TextView tvTodo;
    @BindView(R.id.view_toDo)
    View viewToDo;
    @BindView(R.id.tv_approve)
    TextView tvApprove;
    @BindView(R.id.view_approve)
    View viewApprove;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_waiting;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("待办任务");
        currentFragment = new Fragment();
        fragmentManager = getSupportFragmentManager();
        waitingHandleFragment = new WaitingHandleFragment();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        addOrShowFra(ft, waitingHandleFragment);
    }

    @OnClick({R.id.ll_toDo, R.id.ll_approve,R.id.iv_back})
    public void click(View view) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_toDo:
                if (waitingHandleFragment == null) {
                    waitingHandleFragment = new WaitingHandleFragment();
                }
                tvTodo.setTextColor(Color.parseColor("#FF52A7F9"));
                viewToDo.setVisibility(View.VISIBLE);
                tvApprove.setTextColor(Color.parseColor("#000000"));
                viewApprove.setVisibility(View.GONE);
                addOrShowFra(fragmentTransaction, waitingHandleFragment);
                break;
            case R.id.ll_approve:
                if (waitingApproveFragment == null) {
                    waitingApproveFragment = new WaitingApproveFragment();
                }
                tvApprove.setTextColor(Color.parseColor("#FF52A7F9"));
                viewApprove.setVisibility(View.VISIBLE);
                tvTodo.setTextColor(Color.parseColor("#000000"));
                viewToDo.setVisibility(View.GONE);
                addOrShowFra(fragmentTransaction, waitingApproveFragment);
                break;
        }
    }

    /***
     * 显示隐藏Fragment
     *
     * @param ft
     * @param fragment
     */
    private void addOrShowFra(FragmentTransaction ft, Fragment fragment) {
        if (currentFragment == fragment) {
            return;
        }
        if (!fragment.isAdded()) {
            ft.hide(currentFragment).add(R.id.main_switch, fragment).commitAllowingStateLoss();

        } else {
            ft.hide(currentFragment).show(fragment).commitAllowingStateLoss();

        }
        currentFragment = fragment;
    }
}
