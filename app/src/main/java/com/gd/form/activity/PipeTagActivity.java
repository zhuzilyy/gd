package com.gd.form.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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

public class PipeTagActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;

    @BindView(R.id.ll_area)
    LinearLayout llArea;
    @BindView(R.id.ll_pipeName)
    LinearLayout llPipeName;
    @BindView(R.id.ll_groundTagType)
    LinearLayout llGroundTagType;
    @BindView(R.id.ll_landForm)
    LinearLayout llLandForm;
    @BindView(R.id.ll_location)
    LinearLayout llLocation;
    @BindView(R.id.et_stationNo)
    EditText etStationNo;
    @BindView(R.id.et_kgInfo)
    EditText etKgInfo;
    @BindView(R.id.et_corner)
    EditText etCorner;
    @BindView(R.id.et_longitude)
    EditText etLongitude;
    @BindView(R.id.et_latitude)
    EditText etLatitude;
    @BindView(R.id.et_depth)
    EditText etDepth;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_remark)
    EditText etRemark;
    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_pipe_tag;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("管道标识");
        if(getIntent()!=null){
            String tag = getIntent().getExtras().getString("tag");
            if(!TextUtils.isEmpty(tag)){
                //查看
                if(tag.equals("check")){
                    llArea.setEnabled(false);
                    llPipeName.setEnabled(false);
                    llGroundTagType.setEnabled(false);
                    llLandForm.setEnabled(false);
                    llLocation.setEnabled(false);
                    etStationNo.setEnabled(false);
                    etKgInfo.setEnabled(false);
                    etCorner.setEnabled(false);
                    etLongitude.setEnabled(false);
                    etLatitude.setEnabled(false);
                    etDepth.setEnabled(false);
                    etName.setEnabled(false);
                    etPhone.setEnabled(false);
                    etRemark.setEnabled(false);
                }else if(tag.equals("add")){
                    tvRight.setVisibility(View.VISIBLE);
                    tvRight.setText("测量");
                    llArea.setEnabled(true);
                    llPipeName.setEnabled(true);
                    llGroundTagType.setEnabled(true);
                    llLandForm.setEnabled(true);
                    llLocation.setEnabled(true);
                    etStationNo.setEnabled(true);
                    etKgInfo.setEnabled(true);
                    etCorner.setEnabled(true);
                    etLongitude.setEnabled(true);
                    etLatitude.setEnabled(true);
                    etDepth.setEnabled(true);
                    etName.setEnabled(true);
                    etPhone.setEnabled(true);
                    etRemark.setEnabled(true);
                }
            }
        }

    }

    @OnClick({
            R.id.iv_back,
            R.id.tv_right,
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:

                openActivity(PipeMeasureActivity.class);
                break;

        }
    }
}
