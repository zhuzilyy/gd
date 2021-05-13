package com.gd.form.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.gd.form.model.TaskDetailModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.SPUtil;
import com.gd.form.utils.TimeUtil;
import com.gd.form.utils.ToastUtil;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class TaskDetailActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_finisStatus)
    EditText etFinisStatus;
    @BindView(R.id.et_require)
    EditText etRequire;
    @BindView(R.id.tv_plantFinishTime)
    TextView tvPlantFinishTime;
    @BindView(R.id.tv_realFinishTime)
    TextView tvRealFinishTime;
    @BindView(R.id.tv_sendPerson)
    TextView tvSendPerson;
    @BindView(R.id.ll_realFinishTime)
    LinearLayout llRealFinishTime;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.view_finish_time)
    View viewFinishTime;
    private String taskId;
    private String token, userId;
    private String tag;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_task_detail;

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("任务详情");
        token = (String) SPUtil.get(this, "token", "");
        userId = (String) SPUtil.get(this, "userId", "");
        if (getIntent() != null) {
            taskId = getIntent().getExtras().getString("taskId");
            tag = getIntent().getExtras().getString("tag");
            if (tag.equals("unFinish")) {
                llRealFinishTime.setVisibility(View.GONE);
            } else {
                llRealFinishTime.setVisibility(View.VISIBLE);
                viewFinishTime.setVisibility(View.VISIBLE);
                btnCommit.setVisibility(View.GONE);
                etFinisStatus.setEnabled(false);
            }
        }
        getTaskDetail(taskId);
    }

    private void getTaskDetail(String taskId) {
        JsonObject params = new JsonObject();
        params.addProperty("id", taskId);
        Net.create(Api.class).getTaskDetail(token, params)
                .enqueue(new NetCallback<TaskDetailModel>(this, true) {
                    @Override
                    public void onResponse(TaskDetailModel result) {
                        if (result != null) {
                            etRequire.setText(result.getTaskcontent());
                            tvPlantFinishTime.setText(TimeUtil.longToFormatTime(result.getPlantime().getTime()));
                            if (result.getFinishtime() != null) {
                                etFinisStatus.setText(result.getFinishcontent());
                                tvRealFinishTime.setText(TimeUtil.longToFormatTime(result.getFinishtime().getTime()));
                            }
                            tvSendPerson.setText(result.getCreatorname());
                        }
                    }
                });
    }

    @OnClick({
            R.id.iv_back,
            R.id.btn_commit,
    })
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_commit:
                if (TextUtils.isEmpty(etFinisStatus.getText().toString())) {
                    ToastUtil.show("请输入完成情况");
                    return;
                }
                commit();
                break;
        }
    }

    private void commit() {
        JsonObject params = new JsonObject();
        params.addProperty("id", Integer.parseInt(taskId));
        params.addProperty("finishcontent", etFinisStatus.getText().toString());
        params.addProperty("finishtime", TimeUtil.getCurrentTimeYYmmdd());
        Log.i("tag", "params===" + params);
        Net.create(Api.class).finishTask(token, params)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel result) {
                        if (result.getCode() == Constant.SUCCESS_CODE) {
                            ToastUtil.show("任务完成成功");
                            Intent intent = new Intent();
                            intent.setAction("com.action.update.task");
                            sendBroadcast(intent);
                            finish();
                        }
                    }
                });
    }
}
