package com.gd.form.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.gd.form.R;
import com.gd.form.base.BaseActivity;
import com.jaeger.library.StatusBarUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class StandingBookActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.tv_pipeTagBaseInfo)
    TextView tvPipeTagBaseInfo;
    @BindView(R.id.tv_pipePerson)
    TextView tvPipePerson;
    @BindView(R.id.tv_station)
    TextView tvStation;
    @BindView(R.id.tv_highZone)
    TextView tvHighZone;
    @BindView(R.id.tv_building)
    TextView tvBuilding;
    @BindView(R.id.tv_tunnel)
    TextView tvTunnel;
    @BindView(R.id.tv_windVane)
    TextView tvWindVane;
    @BindView(R.id.tv_advocacyBoard)
    TextView tvAdvocacyBoard;
    @BindView(R.id.tv_videoMonitoring)
    TextView tvVideoMonitoring;
    @BindView(R.id.tv_water)
    TextView tvWater;
    @BindView(R.id.tv_well)
    TextView tvWell;
    @BindView(R.id.tv_other)
    TextView tvOther;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_standing_book;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("管道及附属设施台账");
        ivRight.setVisibility(View.VISIBLE);
    }

    @OnClick({
            R.id.iv_back,
            R.id.iv_right,
            R.id.btn_infoCheck,
            R.id.btn_infoChange,
            R.id.btn_personCheck,
            R.id.btn_personChange,
            R.id.btn_stationCheck,
            R.id.btn_stationChange,
            R.id.btn_highZoneCheck,
            R.id.btn_highZoneChange,
            R.id.btn_buildingCheck,
            R.id.btn_buildingChange,
            R.id.btn_tunnelCheck,
            R.id.btn_tunnelChange,
            R.id.btn_windVaneCheck,
            R.id.btn_windVaneChange,
            R.id.btn_advocacyBoardCheck,
            R.id.btn_advocacyBoardChange,
            R.id.btn_videoCheck,
            R.id.btn_videoChange,
            R.id.btn_waterCheck,
            R.id.btn_waterChange,
            R.id.btn_wellCheck,
            R.id.btn_wellChange,
            R.id.btn_otherCheck,
            R.id.btn_otherChange,
    })
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            //管道标识基础信息查看
            case R.id.btn_infoCheck:
                bundle.putString("tag", "check");
                openActivity(PipeTagActivity.class, bundle);
                break;
            //管道标识基础信息维护
            case R.id.btn_infoChange:
            case R.id.iv_right:
                bundle.putString("tag", "add");
                openActivity(PipeTagActivity.class, bundle);
                break;
            //管道责任人查看
            case R.id.btn_personCheck:
                openActivity(PipePrincipalActivity.class);
                break;
            //管道责任人查看维护
            case R.id.btn_personChange:
                openActivity(AddPrincipalActivity.class);
                break;
            //站场或阀室查看
            case R.id.btn_stationCheck:
                openActivity(PipeStandingActivity.class);
                break;
            //站场或阀室维护
            case R.id.btn_stationChange:
                openActivity(AddStandingActivity.class);
                break;
            //高后果区信息查看
            case R.id.btn_highZoneCheck:
                bundle.putString("tag", "check");
                openActivity(PipeHighZoneActivity.class, bundle);
                break;
            //高后果区信息维护
            case R.id.btn_highZoneChange:
                bundle.putString("tag", "add");
                openActivity(PipeHighZoneActivity.class, bundle);
                break;
            //违章建筑信息查看
            case R.id.btn_buildingCheck:
                bundle.putString("tag", "check");
                openActivity(PipeBuildingActivity.class, bundle);
                break;
            //违章建筑信息维护
            case R.id.btn_buildingChange:
                bundle.putString("tag", "add");
                openActivity(PipeBuildingActivity.class, bundle);
                break;
            //隧道信息查看
            case R.id.btn_tunnelCheck:
                bundle.putString("tag", "check");
                openActivity(PipeTunnelActivity.class, bundle);
                break;
            //隧道信息维护
            case R.id.btn_tunnelChange:
                bundle.putString("tag", "add");
                openActivity(PipeTunnelActivity.class, bundle);
                break;
            //风向标信息查看
            case R.id.btn_windVaneCheck:
                openActivity(PipeWindVaneActivity.class);
                break;
            //风向标信息维护
            case R.id.btn_windVaneChange:
                openActivity(AddWindVaneActivity.class);
                break;
            //宣传栏信息查看
            case R.id.btn_advocacyBoardCheck:
                openActivity(PipeAdvocacyBoardActivity.class);
                break;
            //宣传栏信息维护
            case R.id.btn_advocacyBoardChange:
                openActivity(AddAdvocacyBoardActivity.class);
                break;
            //视频监控信息查看
            case R.id.btn_videoCheck:
                openActivity(PipeVideoActivity.class);
                break;
            //视频监控信息维护
            case R.id.btn_videoChange:
                openActivity(AddVideoActivity.class);
                break;
            //水工保护信息查看
            case R.id.btn_waterCheck:
                openActivity(PipeWaterActivity.class);
                break;
            //水工保护信息维护
            case R.id.btn_waterChange:
                openActivity(AddWaterActivity.class);
                break;
            //人井(盘缆点)桩
            case R.id.btn_wellCheck:
                openActivity(PipeWellActivity.class);
                break;
            //人井(盘缆点)桩
            case R.id.btn_wellChange:
                openActivity(AddWellActivity.class);
                break;
            //其他(隧道、地震监测等设备设施)
            case R.id.btn_otherCheck:
                openActivity(PipeOtherActivity.class);
                break;
            //其他(隧道、地震监测等设备设施)
            case R.id.btn_otherChange:
                openActivity(AddOtherActivity.class);
                break;
            case R.id.iv_back:
                finish();
                break;

        }
    }
}
