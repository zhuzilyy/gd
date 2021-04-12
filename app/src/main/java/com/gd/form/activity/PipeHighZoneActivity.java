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

public class PipeHighZoneActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.et_pipeName)
    EditText etPipeName;
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
    @BindView(R.id.rg_isAdd)
    RadioGroup rgIsAdd;
    @BindView(R.id.rb_yes)
    RadioButton rbYes;
    @BindView(R.id.rb_no)
    RadioButton rbNo;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;

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
        tvTitle.setText("违规违建");
        if (getIntent() != null) {
            String tag = getIntent().getExtras().getString("tag");
            if ("add".equals(tag)) {
                btnCommit.setVisibility(View.VISIBLE);
                llSearch.setVisibility(View.VISIBLE);
                etPipeName.setEnabled(true);
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

            } else if (("check".equals(tag))) {
                btnCommit.setVisibility(View.GONE);
                llSearch.setVisibility(View.GONE);
                etPipeName.setEnabled(false);
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
