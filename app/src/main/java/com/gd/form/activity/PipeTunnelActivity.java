package com.gd.form.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.gd.form.R;
import com.gd.form.base.BaseActivity;
import com.gd.form.constants.Constant;
import com.gd.form.model.ServerModel;
import com.gd.form.model.TunnelModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.SPUtil;
import com.gd.form.utils.ToastUtil;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PipeTunnelActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.et_belongPipe)
    EditText etBelongPipe;
    @BindView(R.id.et_stationNo)
    EditText etStationNo;
    @BindView(R.id.et_pipeName)
    EditText etPipeName;
    @BindView(R.id.et_pipeDepth)
    EditText etPipeDepth;
    @BindView(R.id.et_method)
    EditText etMethod;
    @BindView(R.id.et_status)
    EditText etStatus;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    private String token, userId, stationId, pipeId, pipeName, stationName, tunnelId;
    private final int SEARCH_TUNNEL = 100;
    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_tunnel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("隧道");
        token = (String) SPUtil.get(PipeTunnelActivity.this, "token", "");
        userId = (String) SPUtil.get(PipeTunnelActivity.this, "userId", "");
        if (getIntent() != null) {
            String tag = getIntent().getExtras().getString("tag");
            tunnelId = getIntent().getExtras().getString("tunnelId");
            stationId = getIntent().getExtras().getString("stationId");
            pipeId = getIntent().getExtras().getString("pipeId");
            String pipeName = getIntent().getExtras().getString("pipeName");
            etBelongPipe.setText(pipeName);
            if ("add".equals(tag)) {
                etBelongPipe.setEnabled(true);
                etStationNo.setEnabled(true);
                etPipeName.setEnabled(true);
                etPipeDepth.setEnabled(true);
                etMethod.setEnabled(true);
                etStatus.setEnabled(true);
                btnCommit.setVisibility(View.VISIBLE);
                llSearch.setVisibility(View.VISIBLE);
            } else if ("check".equals(tag)) {
                etBelongPipe.setEnabled(false);
                etStationNo.setEnabled(false);
                etPipeName.setEnabled(false);
                etPipeDepth.setEnabled(false);
                etMethod.setEnabled(false);
                etStatus.setEnabled(false);
                btnCommit.setVisibility(View.GONE);
                llSearch.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(tunnelId)) {
                    getTunnelData(tunnelId);
                }
            }
        }
    }

    private void getTunnelData(String tunnelId) {
        JsonObject params = new JsonObject();
        params.addProperty("id", Double.parseDouble(tunnelId));
        Net.create(Api.class).getTunnelData(token, params)
                .enqueue(new NetCallback<List<TunnelModel>>(this, true) {
                    @Override
                    public void onResponse(List<TunnelModel> result) {
                        if (result != null && result.size() > 0) {
                            TunnelModel tunnelModel = result.get(0);
                            etStationNo.setText(tunnelModel.getLocation());
                            etPipeName.setText(tunnelModel.getPipename());
                            etPipeDepth.setText(tunnelModel.getPipelength() + "");
                            etStatus.setText(tunnelModel.getPipesituation());
                            etMethod.setText(tunnelModel.getSetupmode());
                        }
                    }
                });
    }

    @OnClick({
            R.id.iv_back,
            R.id.btn_commit,
            R.id.ll_search,
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_commit:
                if (paramsComplete()) {
                    addTunnel();
                }
                break;
            case R.id.ll_search:
                Intent intent = new Intent(PipeTunnelActivity.this,SearchTunnelActivity.class);
                startActivityForResult(intent,SEARCH_TUNNEL);
                break;
        }
    }

    private void addTunnel() {
        JsonObject params = new JsonObject();
        params.addProperty("id", 0);
        params.addProperty("stakeid", Integer.valueOf(stationId));
        params.addProperty("pipeid", Integer.valueOf(pipeId));
        params.addProperty("location", etStationNo.getText().toString());
        params.addProperty("pipename", etPipeName.getText().toString());
        params.addProperty("pipelength", Double.parseDouble(etPipeDepth.getText().toString()));
        params.addProperty("setupmode", etMethod.getText().toString());
        params.addProperty("pipesituation", etStatus.getText().toString());
        Net.create(Api.class).addTunnel(token, params)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel result) {
                        if (result.getCode() == Constant.SUCCESS_CODE) {
                            ToastUtil.show("保存成功");
                            Intent intent = new Intent();
                            intent.setAction("com.action.update");
                            sendBroadcast(intent);
                            finish();
                        } else {
                            ToastUtil.show("保存失败");
                        }
                    }
                });
    }

    private boolean paramsComplete() {
        if (TextUtils.isEmpty(etStationNo.getText().toString())) {
            ToastUtil.show("请输入桩号位置");
            return false;
        }
        if (TextUtils.isEmpty(etPipeName.getText().toString())) {
            ToastUtil.show("请输入隧道名称");
            return false;
        }
        if (TextUtils.isEmpty(etPipeDepth.getText().toString())) {
            ToastUtil.show("请输入隧道长度");
            return false;
        }
        if (TextUtils.isEmpty(etMethod.getText().toString())) {
            ToastUtil.show("请输入管道在隧道内的架设方式");
            return false;
        }
        if (TextUtils.isEmpty(etStatus.getText().toString())) {
            ToastUtil.show("请输入隧道及隧道口情况");
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data==null){
            return;
        }
        if(requestCode == SEARCH_TUNNEL){
            TunnelModel tunnelModel = (TunnelModel)data.getSerializableExtra("tunnel");
            etStationNo.setText(tunnelModel.getLocation());
            etPipeName.setText(tunnelModel.getPipename());
            etPipeDepth.setText(tunnelModel.getPipelength() + "");
            etStatus.setText(tunnelModel.getPipesituation());
            etMethod.setText(tunnelModel.getSetupmode());
        }
    }
}
