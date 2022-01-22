package com.gd.form.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gd.form.R;
import com.gd.form.activity.KpiListActivity;
import com.gd.form.adapter.OnItemClickListener;
import com.gd.form.adapter.ScoreAdapter;
import com.gd.form.base.BaseFragment;
import com.gd.form.model.ScoreModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.SPUtil;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class AverageSeasonFragment extends BaseFragment {
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    @BindView(R.id.rv_score)
    RecyclerView rvScore;
    private List<ScoreModel> scoreModelList;
    private String token, userId;
    private ScoreAdapter scoreAdapter;
    private MyReceiver myReceiver;
    @Override
    protected void initView(Bundle bundle) {
        scoreModelList = new ArrayList<>();
        token = (String) SPUtil.get(getActivity(), "token", "");
        userId = (String) SPUtil.get(getActivity(), "userId", "");
        getData();
        rvScore.setLayoutManager(new LinearLayoutManager(getActivity()));
        scoreAdapter = new ScoreAdapter(getActivity(), scoreModelList, R.layout.adapter_item_score);
        rvScore.setAdapter(scoreAdapter);
        scoreAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                if (v.getId() == R.id.btn_check) {
                    Bundle bundle = new Bundle();
                    bundle.putString("seasonNo","5");
                    bundle.putString("departmentId",scoreModelList.get(position).getDepartmentid()+"");
                    openActivity(KpiListActivity.class,bundle);
                }
            }
        });

        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.action.delete.kpi.success");
        getActivity().registerReceiver(myReceiver,intentFilter);
    }

    private void getData() {
        JsonObject params = new JsonObject();
        params.addProperty("seqNo", 5);
        params.addProperty("appempid", userId);
        Net.create(Api.class).getSeasonScore(token, params)
                .enqueue(new NetCallback<List<ScoreModel>>(getActivity(), true) {
                    @Override
                    public void onResponse(List<ScoreModel> result) {
                        if (result != null && result.size() > 0) {
                            scoreModelList.addAll(result);
                            scoreAdapter.notifyDataSetChanged();
                            rvScore.setVisibility(View.VISIBLE);
                            llNoData.setVisibility(View.GONE);
                        } else {
                            rvScore.setVisibility(View.GONE);
                            llNoData.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_first_season;
    }

    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.action.delete.kpi.success")){
                scoreModelList.clear();
                getData();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(myReceiver!=null){
            getActivity().unregisterReceiver(myReceiver);
        }
    }
}
