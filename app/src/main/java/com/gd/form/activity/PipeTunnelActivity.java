package com.gd.form.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.gd.form.R;
import com.gd.form.base.BaseActivity;
import com.jaeger.library.StatusBarUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class PipeTunnelActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.et_belongPipe)
    EditText et_belongPipe;
    @BindView(R.id.et_stationNo)
    EditText et_stationNo;
    @BindView(R.id.et_pipeName)
    EditText et_pipeName;
    @BindView(R.id.et_pipeDepth)
    EditText et_pipeDepth;
    @BindView(R.id.et_method)
    EditText et_method;
    @BindView(R.id.et_status)
    EditText et_status;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;

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
        if (getIntent() != null) {
            String tag = getIntent().getExtras().getString("tag");
            if ("add".equals(tag)) {
                et_belongPipe.setEnabled(true);
                et_stationNo.setEnabled(true);
                et_pipeName.setEnabled(true);
                et_pipeDepth.setEnabled(true);
                et_method.setEnabled(true);
                et_status.setEnabled(true);
                btnCommit.setVisibility(View.VISIBLE);
                llSearch.setVisibility(View.VISIBLE);
            } else if ("check".equals(tag)) {
                et_belongPipe.setEnabled(false);
                et_stationNo.setEnabled(false);
                et_pipeName.setEnabled(false);
                et_pipeDepth.setEnabled(false);
                et_method.setEnabled(false);
                et_status.setEnabled(false);
                btnCommit.setVisibility(View.GONE);
                llSearch.setVisibility(View.GONE);
            }
        }
    }
    @OnClick({
            R.id.iv_back,
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
