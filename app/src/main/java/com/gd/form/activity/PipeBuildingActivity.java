package com.gd.form.activity;

import android.os.Bundle;
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
import com.jaeger.library.StatusBarUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class PipeBuildingActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.et_pipeName)
    EditText etPipeName;
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
    @BindView(R.id.ll_search)
    LinearLayout llSearch;

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
        tvTitle.setText("违规违建");
        if (getIntent() != null) {
            String tag = getIntent().getExtras().getString("tag");
            if ("add".equals(tag)) {
                btnCommit.setVisibility(View.VISIBLE);
                llSearch.setVisibility(View.VISIBLE);
                etPipeName.setEnabled(true);
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
                etPipeName.setEnabled(false);
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
