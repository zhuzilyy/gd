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

import androidx.annotation.Nullable;

import com.gd.form.R;
import com.gd.form.activity.NoApproveActivity;
import com.gd.form.activity.OverTimeTaskActivity;
import com.gd.form.activity.RefuseTaskActivity;
import com.gd.form.activity.StationWaitingApproveActivity;
import com.gd.form.activity.WaitingActivity;
import com.gd.form.activity.WaitingHandleTaskActivity;
import com.gd.form.base.BaseFragment;
import com.gd.form.model.StationApproveModel;
import com.gd.form.model.TaskCountModel;
import com.gd.form.model.WaitingTakModel;
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
    @BindView(R.id.ll_task_waiting_handle)
    LinearLayout llTaskWaitingHandle;
    @BindView(R.id.rl_task_waiting_handle)
    RelativeLayout rlTaskWaitingHandle;
    @BindView(R.id.rl_refuse)
    RelativeLayout rlRefuse;
    @BindView(R.id.rl_station_approve)
    RelativeLayout rlStationApprove;
    List<Badge> badges;
    private String token, userId;
    private MyReceiver myReceiver;
    private int msgCount;

    @Override
    protected void initView(Bundle bundle) {
        token = (String) SPUtil.get(getActivity(), "token", "");
        userId = (String) SPUtil.get(getActivity(), "userId", "");
        badges = new ArrayList<>();
        badges.add(new QBadgeView(getActivity()).bindTarget(rlWaitingHandle));
        badges.add(new QBadgeView(getActivity()).bindTarget(rlOverTime));
        badges.add(new QBadgeView(getActivity()).bindTarget(rlNoApprove));
        badges.add(new QBadgeView(getActivity()).bindTarget(rlTaskWaitingHandle));
        badges.add(new QBadgeView(getActivity()).bindTarget(rlRefuse));
        badges.add(new QBadgeView(getActivity()).bindTarget(rlStationApprove));
        myReceiver = new MyReceiver();
        IntentFilter filterWaitingTask = new IntentFilter();
        filterWaitingTask.addAction("com.action.update.waitingTask");
        getActivity().registerReceiver(myReceiver, filterWaitingTask);

        IntentFilter filterApprove = new IntentFilter();
        filterApprove.addAction("com.action.updateApprove");
        getActivity().registerReceiver(myReceiver, filterApprove);

        IntentFilter filterTask = new IntentFilter();
        filterTask.addAction("com.action.update.task");
        getActivity().registerReceiver(myReceiver, filterTask);

        IntentFilter filterStationApprove = new IntentFilter();
        filterStationApprove.addAction("com.action.update.approve");
        getActivity().registerReceiver(myReceiver, filterStationApprove);
        getWaitingTaskCount();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message;
    }

    private void getWaitingTaskCount() {
        JsonObject params = new JsonObject();
        params.addProperty("employname", userId);
        params.addProperty("taskstatus", 1);
        Net.create(Api.class).getWaitingTaskCount(token, params)
                .enqueue(new NetCallback<WaitingTakModel>(getActivity(), false) {
                    @Override
                    public void onResponse(WaitingTakModel result) {
                        if (result.getMsg() > 0) {
                            msgCount += result.getMsg();
                            badges.get(3).setShowShadow(false).setBadgeNumber(result.getMsg());
                        } else {
                            badges.get(3).hide(true);
                        }
                        getNoApproveCount();
                    }
                });
    }

    private void getNoApproveCount() {
        JsonObject params = new JsonObject();
        params.addProperty("employid", userId);
        Net.create(Api.class).getTaskCount(token, params)
                .enqueue(new NetCallback<TaskCountModel>(getActivity(), false) {
                    @Override
                    public void onResponse(TaskCountModel model) {
                        int noApprove = model.getApproval2();
                        if (noApprove > 0) {
                            msgCount += noApprove;
                            badges.get(2).setShowShadow(false).setBadgeNumber(noApprove);
                        } else {
                            badges.get(2).hide(true);
                        }
                        getTaskTotal(model.getApproval1());
                    }
                });
    }

    private void getTaskTotal(int noApproveCount) {
        JsonObject params = new JsonObject();
        params.addProperty("empid", userId);
        Net.create(Api.class).getTaskTotal(token, params)
                .enqueue(new NetCallback<TaskCountModel>(getActivity(), false) {
                    @Override
                    public void onResponse(TaskCountModel model) {
                        int waitingTask = model.getWaitCount();
                        waitingTask += noApproveCount;
                        if (waitingTask > 0) {
                            msgCount += waitingTask;
                            badges.get(0).setShowShadow(false).setBadgeNumber(waitingTask);
                        } else {
                            badges.get(0).hide(true);
                        }
                        int overTimeTask = model.getOverCount();
                        if (overTimeTask > 0) {
                            msgCount +=overTimeTask;
                            badges.get(1).setShowShadow(false).setBadgeNumber(overTimeTask);
                        } else {
                            badges.get(1).hide(true);
                        }
                        getRefuseCount();
                    }
                });
    }

    private void getRefuseCount() {
        JsonObject params = new JsonObject();
        params.addProperty("employid", userId);
        params.addProperty("basecode", "ALL");
        params.addProperty("status", 0);
        Net.create(Api.class).refuseTaskCount(token, params)
                .enqueue(new NetCallback<WaitingTakModel>(getActivity(), false) {
                    @Override
                    public void onResponse(WaitingTakModel result) {
                        if (result.getMsg() > 0) {
                            msgCount+=result.getMsg();
                            badges.get(4).setShowShadow(false).setBadgeNumber(result.getMsg());
                        } else {
                            badges.get(4).hide(true);
                        }
                        getApproveStationCount();
                    }
                });
    }

    private void getApproveStationCount() {
        JsonObject params = new JsonObject();
        params.addProperty("appempid", userId);
        Net.create(Api.class).approveStationCount(token, params)
                .enqueue(new NetCallback<StationApproveModel>(getActivity(), false) {
                    @Override
                    public void onResponse(StationApproveModel result) {
                        if (result.getCount() > 0) {
                            msgCount+=result.getCount();
                            badges.get(5).setShowShadow(false).setBadgeNumber(result.getCount());
                        } else {
                            badges.get(5).hide(true);
                        }
                        sendMsgBroadCast(msgCount);
                    }
                });
    }

    private void sendMsgBroadCast(int msgCount) {
        Intent intent = new Intent();
        if(msgCount>0){
            intent.setAction("com.action.showMessageDot");
        }else{
            intent.setAction("com.action.hideMessageDot.=");
        }
        intent.putExtra("count",msgCount);
        getActivity().sendBroadcast(intent);
    }

    @OnClick({
            R.id.ll_waiting,
            R.id.ll_overTime,
            R.id.ll_noApprove,
            R.id.ll_task_waiting_handle,
            R.id.ll_refuse,
            R.id.ll_station_approve,
    })
    public void click(View view) {
        switch (view.getId()) {
            case R.id.ll_waiting:
                openActivity(WaitingActivity.class);
                break;
            case R.id.ll_station_approve:
                openActivity(StationWaitingApproveActivity.class);
                break;
            case R.id.ll_task_waiting_handle:
                openActivity(WaitingHandleTaskActivity.class);
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
            if (action.equals("com.action.update.waitingTask")
                    || action.equals("com.action.updateApprove")
                    || action.equals("com.action.update.task")
                    ||action.equals("com.action.update.approve")) {
                msgCount = 0;
                getWaitingTaskCount();
            }
//            else if (action.equals("com.action.update.approve")) {
//                getApproveStationCount();
//            }
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
