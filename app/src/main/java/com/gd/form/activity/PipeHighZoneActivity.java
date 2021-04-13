package com.gd.form.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.gd.form.R;
import com.gd.form.base.BaseActivity;
import com.gd.form.model.Pipelineinfo;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.ToastUtil;
import com.gd.form.view.ListDialog;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PipeHighZoneActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.tv_pipeName)
    TextView tvPipeName;
    @BindView(R.id.et_highZoneName)
    EditText etHighZoneName;
    @BindView(R.id.et_location)
    EditText etLocation;
    @BindView(R.id.et_length)
    EditText etLength;
    @BindView(R.id.et_level)
    EditText etLevel;
    @BindView(R.id.et_desc)
    EditText etDesc;
    @BindView(R.id.et_controlMethod)
    EditText etControlMethod;
    @BindView(R.id.et_recognise)
    EditText etRecognise;
    @BindView(R.id.et_locationLevel)
    EditText etLocationLevel;
    @BindView(R.id.et_riskLevel)
    EditText etRiskLevel;
    @BindView(R.id.et_ply)
    EditText etPly;
    @BindView(R.id.et_relativeAreaLevel)
    EditText etRelativeAreaLevel;
    @BindView(R.id.et_death)
    EditText etDeath;
    @BindView(R.id.et_influence)
    EditText etInfluence;
    @BindView(R.id.et_type)
    EditText etType;
    @BindView(R.id.rg_isAdd)
    RadioGroup rgIsAdd;
    @BindView(R.id.rb_yes)
    RadioButton rbYes;
    @BindView(R.id.rb_no)
    RadioButton rbNo;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.ll_pipeName)
    LinearLayout llPipeName;
    private String isAdd = "是";
    private List<Pipelineinfo> pipelineInfoList;
    private ListDialog dialog;
    private int pipeId;
    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_pipe_high_zone;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initListener();
        getPipelineInfoListRequest();
        dialog = new ListDialog(this);
        tvTitle.setText("高后果区");
        if (getIntent() != null) {
            String tag = getIntent().getExtras().getString("tag");
            if ("add".equals(tag)) {
                btnCommit.setVisibility(View.VISIBLE);
                llSearch.setVisibility(View.VISIBLE);
                etHighZoneName.setEnabled(true);
                etLocation.setEnabled(true);
                etLength.setEnabled(true);
                etLevel.setEnabled(true);
                etDesc.setEnabled(true);
                etControlMethod.setEnabled(true);
                etRecognise.setEnabled(true);
                etLocationLevel.setEnabled(true);
                etRiskLevel.setEnabled(true);
                etPly.setEnabled(true);
                etRelativeAreaLevel.setEnabled(true);
                etDeath.setEnabled(true);
                etInfluence.setEnabled(true);
                rbYes.setEnabled(true);
                rbNo.setEnabled(true);
                rgIsAdd.setEnabled(true);
                etType.setEnabled(true);
                llPipeName.setEnabled(true);
            } else if (("check".equals(tag))) {
                btnCommit.setVisibility(View.GONE);
                llSearch.setVisibility(View.GONE);
                etHighZoneName.setEnabled(false);
                etLocation.setEnabled(false);
                etLength.setEnabled(false);
                etLevel.setEnabled(false);
                etDesc.setEnabled(false);
                etControlMethod.setEnabled(false);
                etRecognise.setEnabled(false);
                etLocationLevel.setEnabled(false);
                etRiskLevel.setEnabled(false);
                etPly.setEnabled(false);
                etRelativeAreaLevel.setEnabled(false);
                etDeath.setEnabled(false);
                etInfluence.setEnabled(false);
                rbYes.setEnabled(false);
                rbNo.setEnabled(false);
                rgIsAdd.setEnabled(false);
                etType.setEnabled(false);
                etType.setEnabled(false);
                llPipeName.setEnabled(false);
            }
        }
    }
    private void getPipelineInfoListRequest() {

        Net.create(Api.class).pipelineinfosget()
                .enqueue(new NetCallback<List<Pipelineinfo>>(this, false) {
                    @Override
                    public void onResponse(List<Pipelineinfo> list) {
                        pipelineInfoList = list;
                    }
                });
    }
    private void initListener() {
        rgIsAdd.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yes:
                        isAdd = "是";
                        break;
                    case R.id.rb_no:
                        isAdd = "否";
                        break;
                }
            }
        });
    }

    @OnClick({
            R.id.iv_back,
            R.id.btn_commit,
            R.id.ll_pipeName,
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_commit:
                paramsComplete();
                break;
            case R.id.ll_pipeName:
                List<String> pipeList = new ArrayList<>();
                List<Integer> pipeIdList = new ArrayList<>();
                if (pipelineInfoList != null && pipelineInfoList.size() > 0) {
                    for (int i = 0; i < pipelineInfoList.size(); i++) {
                        pipeList.add(pipelineInfoList.get(i).getName());
                        pipeIdList.add(pipelineInfoList.get(i).getId());
                    }
                }
                dialog.setData(pipeList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvPipeName.setText(pipeList.get(positionM));
                    pipeId = pipeIdList.get(positionM);
                    dialog.dismiss();
                });
                break;
        }
    }

    private boolean paramsComplete() {
        if (TextUtils.isEmpty(tvPipeName.getText().toString())) {
            ToastUtil.show("请选择管道名称");
            return false;
        }
        if (TextUtils.isEmpty(etHighZoneName.getText().toString())) {
            ToastUtil.show("请输入高后果区名称");
            return false;
        }
        if (TextUtils.isEmpty(etLocation.getText().toString())) {
            ToastUtil.show("请输入所在区域");
            return false;
        }
        if (TextUtils.isEmpty(etLength.getText().toString())) {
            ToastUtil.show("请输入长度");
            return false;
        }
        if (TextUtils.isEmpty(etLevel.getText().toString())) {
            ToastUtil.show("请输入高后果区等级");
            return false;
        }
        if (TextUtils.isEmpty(etType.getText().toString())) {
            ToastUtil.show("请输入高后果区类型");
            return false;
        }
        if (TextUtils.isEmpty(etDesc.getText().toString())) {
            ToastUtil.show("请输入高后果区描述");
            return false;
        }
        if (TextUtils.isEmpty(etControlMethod.getText().toString())) {
            ToastUtil.show("请输入高后果区管控措施");
            return false;
        }
        if (TextUtils.isEmpty(etRecognise.getText().toString())) {
            ToastUtil.show("请输入高后果区识别项");
            return false;
        }
        if (TextUtils.isEmpty(etLocationLevel.getText().toString())) {
            ToastUtil.show("请输入地区等级");
            return false;
        }
        if (TextUtils.isEmpty(etRiskLevel.getText().toString())) {
            ToastUtil.show("请输入风险等级");
            return false;
        }
        if (TextUtils.isEmpty(etPly.getText().toString())) {
            ToastUtil.show("请输入管道壁厚");
            return false;
        }
        if (TextUtils.isEmpty(etRelativeAreaLevel.getText().toString())) {
            ToastUtil.show("请输入对应设计地区等级");
            return false;
        }
        if (TextUtils.isEmpty(etRelativeAreaLevel.getText().toString())) {
            ToastUtil.show("请输入对应设计地区等级");
            return false;
        }
        if (TextUtils.isEmpty(etDeath.getText().toString())) {
            ToastUtil.show("请输入致死半径区域");
            return false;
        }
        if (TextUtils.isEmpty(etInfluence.getText().toString())) {
            ToastUtil.show("请输入潜在影响区域");
            return false;
        }
        return true;
    }
}
