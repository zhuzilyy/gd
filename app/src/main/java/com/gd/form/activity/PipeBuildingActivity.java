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

public class PipeBuildingActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_commit)
    Button btnCommit;

    @BindView(R.id.et_startStationNo)
    EditText etStartStationNo;
    @BindView(R.id.et_location)
    EditText etLocation;
    @BindView(R.id.et_pipeProperty)
    EditText etPipeProperty;
    @BindView(R.id.et_type)
    EditText etType;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_time)
    EditText etTime;
    @BindView(R.id.rg_isHighZone)
    RadioGroup rgIsHighZone;
    @BindView(R.id.rb_yes)
    RadioButton rbYes;
    @BindView(R.id.rb_no)
    RadioButton rbNo;
    @BindView(R.id.et_missLength)
    EditText etMissLength;
    @BindView(R.id.et_minDistance)
    EditText etMinDistance;
    @BindView(R.id.et_missArea)
    EditText etMissArea;
    @BindView(R.id.et_personActivity)
    EditText etPersonActivity;
    @BindView(R.id.et_des)
    EditText etDes;
    @BindView(R.id.et_riskEvaluate)
    EditText etRiskEvaluate;
    @BindView(R.id.et_riskType)
    EditText etRiskType;
    @BindView(R.id.et_beforeChangeMethod)
    EditText etBeforeChangeMethod;
    @BindView(R.id.et_changeMethod)
    EditText etChangeMethod;
    @BindView(R.id.tv_pipeName)
    TextView tvPipeName;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.ll_pipeName)
    LinearLayout llPipeName;
    private String isHighZone = "是";
    private List<Pipelineinfo> pipelineInfoList;
    private ListDialog dialog;
    private int pipeId;
    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_building;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initListener();
        getPipelineInfoListRequest();
        tvTitle.setText("违规违建");
        if (getIntent() != null) {
            String tag = getIntent().getExtras().getString("tag");
            if ("add".equals(tag)) {
                btnCommit.setVisibility(View.VISIBLE);
                llSearch.setVisibility(View.VISIBLE);
                llPipeName.setEnabled(true);
                etStartStationNo.setEnabled(true);
                etLocation.setEnabled(true);
                etPipeProperty.setEnabled(true);
                etType.setEnabled(true);
                etName.setEnabled(true);
                etTime.setEnabled(true);
                rgIsHighZone.setEnabled(true);
                etMissLength.setEnabled(true);
                etMinDistance.setEnabled(true);
                etMissArea.setEnabled(true);
                etPersonActivity.setEnabled(true);
                etDes.setEnabled(true);
                etRiskEvaluate.setEnabled(true);
                etRiskType.setEnabled(true);
                etBeforeChangeMethod.setEnabled(true);
                etChangeMethod.setEnabled(true);
                rbNo.setEnabled(true);
                rbYes.setEnabled(true);
            } else if (("check".equals(tag))) {
                btnCommit.setVisibility(View.GONE);
                llSearch.setVisibility(View.GONE);
                llPipeName.setEnabled(false);
                etStartStationNo.setEnabled(false);
                etLocation.setEnabled(false);
                etPipeProperty.setEnabled(false);
                etType.setEnabled(false);
                etName.setEnabled(false);
                etTime.setEnabled(false);
                rgIsHighZone.setEnabled(false);
                etMissLength.setEnabled(false);
                etMinDistance.setEnabled(false);
                etMissArea.setEnabled(false);
                etPersonActivity.setEnabled(false);
                etDes.setEnabled(false);
                etRiskEvaluate.setEnabled(false);
                etRiskType.setEnabled(false);
                etBeforeChangeMethod.setEnabled(false);
                etChangeMethod.setEnabled(false);
                rbNo.setEnabled(false);
                rbYes.setEnabled(false);
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
        rgIsHighZone.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yes:
                        isHighZone = "是";
                        break;
                    case R.id.rb_no:
                        isHighZone = "否";
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
            case R.id.bt_confirm:
                paramsComplete();
                break;
        }
    }

    private boolean paramsComplete() {
        if (TextUtils.isEmpty(tvPipeName.getText().toString())) {
            ToastUtil.show("请选择管道名称");
            return false;
        }
        if (TextUtils.isEmpty(etStartStationNo.getText().toString())) {
            ToastUtil.show("请输入起始桩号");
            return false;
        }
        if (TextUtils.isEmpty(etLocation.getText().toString())) {
            ToastUtil.show("请输入行政位置");
            return false;
        }
        if (TextUtils.isEmpty(etPipeProperty.getText().toString())) {
            ToastUtil.show("请输入占据性质");
            return false;
        }
        if (TextUtils.isEmpty(etType.getText().toString())) {
            ToastUtil.show("请输入占压类型");
            return false;
        }
        if (TextUtils.isEmpty(etName.getText().toString())) {
            ToastUtil.show("请输入占压名称");
            return false;
        }
        if (TextUtils.isEmpty(etTime.getText().toString())) {
            ToastUtil.show("请输入形成时间");
            return false;
        }
        if (TextUtils.isEmpty(etMissLength.getText().toString())) {
            ToastUtil.show("请输入占压或安全距离不足长度(m)");
            return false;
        }
        if (TextUtils.isEmpty(etMinDistance.getText().toString())) {
            ToastUtil.show("请输入与管道最小间距(m)");
            return false;
        }
        if (TextUtils.isEmpty(etMissArea.getText().toString())) {
            ToastUtil.show("请输入占压或安全距离不足面积(㎡)");
            return false;
        }
        if (TextUtils.isEmpty(etPersonActivity.getText().toString())) {
            ToastUtil.show("请输入人员活动情况");
            return false;
        }
        if (TextUtils.isEmpty(etDes.getText().toString())) {
            ToastUtil.show("请输入隐患内容描述");
            return false;
        }
        if (TextUtils.isEmpty(etRiskEvaluate.getText().toString())) {
            ToastUtil.show("请输入风险评价");
            return false;
        }
        if (TextUtils.isEmpty(etRiskType.getText().toString())) {
            ToastUtil.show("请输入隐患类型");
            return false;
        }
        if (TextUtils.isEmpty(etBeforeChangeMethod.getText().toString())) {
            ToastUtil.show("请输入整改前控制措施");
            return false;
        }
        if (TextUtils.isEmpty(etChangeMethod.getText().toString())) {
            ToastUtil.show("请输入整改控制措施");
            return false;
        }
        return true;
    }
}
