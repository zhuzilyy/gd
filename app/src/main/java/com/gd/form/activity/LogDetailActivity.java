package com.gd.form.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.gd.form.R;
import com.gd.form.base.BaseActivity;
import com.gd.form.model.LogBean;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.SPUtil;
import com.gd.form.utils.TimeUtil;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class LogDetailActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.et_content)
    EditText etContent;
    private String token;
    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_log_detail;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("日志详情");
        token = (String) SPUtil.get(LogDetailActivity.this, "token", "");
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            String creatorName = extras.getString("creatorName");
            String time = extras.getString("time");
            int departmentId = extras.getInt("departmentId");
            getDetail(creatorName,time,departmentId);
        }
    }

    private void getDetail(String creatorName, String time, int departmentId) {
        JsonObject params = new JsonObject();
        params.addProperty("departmentid",departmentId);
        params.addProperty("creator",creatorName);
        params.addProperty("creatime",time);
        Net.create(Api.class).logDetail(token, params)
                .enqueue(new NetCallback<LogBean>(this, true) {
                    @Override
                    public void onResponse(LogBean result) {
                        if (result!=null) {
                            tvName.setText(result.getCreatorname());
                            tvTime.setText(TimeUtil.longToFormatTime(result.getCreatime().getTime()));
                            etContent.setText(result.getDailycontent());
                        }
                    }
                });
    }
    @OnClick({
            R.id.iv_back,
    })
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
