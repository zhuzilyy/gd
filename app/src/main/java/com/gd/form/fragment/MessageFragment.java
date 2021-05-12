package com.gd.form.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.gd.form.R;
import com.gd.form.activity.NoApproveActivity;
import com.gd.form.activity.OverTimeTaskActivity;
import com.gd.form.activity.RefuseTaskActivity;
import com.gd.form.activity.WaitingActivity;
import com.gd.form.base.BaseFragment;
import com.gd.form.model.TaskCountModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.SPUtil;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;


public class MessageFragment extends BaseFragment {
    @BindView(R.id.iv_waitingHandle)
    ImageView ivWaitingHandle;
    @BindView(R.id.rl_waiting_handle)
    RelativeLayout rlWaitingHandle;
    @BindView(R.id.rl_over_time)
    RelativeLayout rlOverTime;
    @BindView(R.id.rl_no_approve)
    RelativeLayout rlNoApprove;
    @BindView(R.id.ll_waiting)
    LinearLayout llWaiting;
    List<Badge> badges;
    private String token, userId;
    private MyReceiver myReceiver;

    @Override
    protected void initView(Bundle bundle) {
        token = (String) SPUtil.get(getActivity(), "token", "");
        userId = (String) SPUtil.get(getActivity(), "userId", "");
        badges = new ArrayList<>();
        badges.add(new QBadgeView(getActivity()).bindTarget(rlWaitingHandle));
        badges.add(new QBadgeView(getActivity()).bindTarget(rlOverTime));
        badges.add(new QBadgeView(getActivity()).bindTarget(rlNoApprove));
        myReceiver = new MyReceiver();
        IntentFilter filterWaitingTask = new IntentFilter();
        filterWaitingTask.addAction("com.action.update.waitingTask");
        getActivity().registerReceiver(myReceiver, filterWaitingTask);

        IntentFilter filterApprove = new IntentFilter();
        filterApprove.addAction("com.action.updateApprove");
        getActivity().registerReceiver(myReceiver, filterApprove);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    public void onResume() {
        super.onResume();
        getTaskCount();
    }

    private void getTaskCount() {
        JsonObject params = new JsonObject();
        params.addProperty("employid", userId);
        Net.create(Api.class).getTaskCount(token, params)
                .enqueue(new NetCallback<TaskCountModel>(getActivity(), false) {
                    @Override
                    public void onResponse(TaskCountModel model) {
                        getTaskTotal();
                    }
                });
    }

    private void getTaskTotal() {
        JsonObject params = new JsonObject();
        params.addProperty("empid", userId);
        Net.create(Api.class).getTaskTotal(token, params)
                .enqueue(new NetCallback<TaskCountModel>(getActivity(), false) {
                    @Override
                    public void onResponse(TaskCountModel model) {
                        int waitingTask = model.getApproval1() + model.getWaitCount();
                        if (waitingTask > 0) {
                            badges.get(0).setShowShadow(false).setBadgeNumber(waitingTask);
                        }

                        int overTimeTask = model.getOverCount();
                        if (overTimeTask > 0) {
                            badges.get(1).setShowShadow(false).setBadgeNumber(overTimeTask);
                        }
                        int noApprove = model.getApproval2();
                        if (noApprove > 0) {
                            badges.get(2).setShowShadow(false).setBadgeNumber(noApprove);
                        }

                    }
                });
    }

    @OnClick({
            R.id.ll_waiting,
            R.id.ll_overTime,
            R.id.ll_noApprove,
            R.id.ll_refuse,
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
            case R.id.ll_refuse:
                openActivity(RefuseTaskActivity.class);
                break;
        }
    }

    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("com.action.update.waitingTask") || action.equals("com.action.updateApprove")) {
                getTaskCount();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (myReceiver != null) {
            getActivity().unregisterReceiver(myReceiver);
        }
    }
}
