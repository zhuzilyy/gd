package com.gd.form.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.bumptech.glide.Glide;
import com.gd.form.R;
import com.gd.form.adapter.PhotoAdapter;
import com.gd.form.base.BaseActivity;
import com.gd.form.model.TunnelDataDetail;
import com.gd.form.model.TunnelDetailModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.SPUtil;
import com.gd.form.utils.Util;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ApproveTunnelActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_departmentName)
    TextView tvDepartmentName;
    @BindView(R.id.tv_pipeName)
    TextView tvPipeName;
    @BindView(R.id.tv_tunnelLocation)
    TextView tvTunnelLocation;
    @BindView(R.id.tv_pipeLength)
    TextView tvPipeLength;
    @BindView(R.id.tv_illegal)
    TextView tvIllegal;
    @BindView(R.id.tv_illegal_des)
    TextView tvIllegalDes;
    @BindView(R.id.tv_third)
    TextView tvThird;
    @BindView(R.id.tv_third_problem)
    TextView tvThirdProblem;
    @BindView(R.id.tv_suspicious)
    TextView tvSuspicious;
    @BindView(R.id.tv_suspicious_problem)
    TextView tvSuspiciousProblem;
    @BindView(R.id.tv_seal)
    TextView tvSeal;
    @BindView(R.id.tv_seal_problem)
    TextView tvSealProblem;
    @BindView(R.id.tv_cave)
    TextView tvCave;
    @BindView(R.id.tv_cave_problem)
    TextView tvCaveProblem;
    @BindView(R.id.tv_water)
    TextView tvWater;
    @BindView(R.id.tv_water_problem)
    TextView tvWaterProblem;
    @BindView(R.id.tv_transition)
    TextView tvTransition;
    @BindView(R.id.tv_transition_problem)
    TextView tvTransitionProblem;
    @BindView(R.id.tv_smell)
    TextView tvSmell;
    @BindView(R.id.tv_smell_problem)
    TextView tvSmellProblem;
    @BindView(R.id.tv_gas)
    TextView tvGas;
    @BindView(R.id.tv_gas_problem)
    TextView tvGasProblem;
    @BindView(R.id.tv_arm)
    TextView tvArm;
    @BindView(R.id.tv_arm_problem)
    TextView tvArmProblem;
    @BindView(R.id.tv_other_problem)
    TextView tvOtherProblem;
    @BindView(R.id.tv_fileName)
    TextView tvFileName;
    @BindView(R.id.tv_spr)
    TextView tvSpr;
    @BindView(R.id.tv_approveStatus)
    TextView tvApproveStatus;
    @BindView(R.id.tv_photo)
    TextView tvPhoto;
    @BindView(R.id.iv_approveStatus)
    ImageView ivApproveStatus;
    @BindView(R.id.btn_approve)
    Button btnApprove;
    @BindView(R.id.map)
    MapView mapView;
    @BindView(R.id.rvResultPhoto)
    RecyclerView rvResultPhoto;
    @BindView(R.id.tv_approveAdvice)
    TextView tvApproveAdvice;
    @BindView(R.id.ll_approveAdvice)
    LinearLayout llApproveAdvice;
    private String formId;
    private String token, userId;
    private MarkerOptions markerOption;
    private AMap aMap;
    private PhotoAdapter photoAdapter;
    private List<String> path;
    private String filePath;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_approval_tunnel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.activityList.add(this);
        tvTitle.setText("隧道外部检查表");
        // 此方法必须重写
        mapView.onCreate(savedInstanceState);
        initMap();
        token = (String) SPUtil.get(ApproveTunnelActivity.this, "token", "");
        userId = (String) SPUtil.get(ApproveTunnelActivity.this, "userId", "");
        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            String tag = bundle.getString("tag");
            if (tag.equals("detail")) {
                btnApprove.setVisibility(View.GONE);
            }
            formId = bundle.getString("formId");
        }
        path = new ArrayList<>();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvResultPhoto.setLayoutManager(gridLayoutManager);
        photoAdapter = new PhotoAdapter(this, path);
        rvResultPhoto.setAdapter(photoAdapter);
        getDetail(formId);
    }

    /**
     * 初始化AMap对象
     */
    private void initMap() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
    }

    private void getDetail(String formId) {
        JsonObject params = new JsonObject();
        params.addProperty("formid", formId);
        Net.create(Api.class).getTunnelDetail(token, params)
                .enqueue(new NetCallback<TunnelDetailModel>(this, true) {
                    @Override
                    public void onResponse(TunnelDetailModel model) {
                        if (model != null) {
                            if (!TextUtils.isEmpty(model.getDeptString())) {
                                tvDepartmentName.setText(model.getDeptString().split(":")[1]);
                            }

                            if (!TextUtils.isEmpty(model.getPipeString())) {
                                tvPipeName.setText(model.getPipeString().split(":")[1]);
                            }

                            if (!TextUtils.isEmpty(model.getStakeString())) {
                                tvTunnelLocation.setText(model.getStakeString().split(":")[1]);
                            }
                            TunnelDataDetail dataDetail = model.getDatadetail();
                            tvPipeLength.setText(dataDetail.getPipelength());
                            tvIllegal.setText(dataDetail.getCol1());
                            tvIllegalDes.setText(dataDetail.getCol1desc());
                            tvThird.setText(dataDetail.getCol2());
                            tvThirdProblem.setText(dataDetail.getCol2desc());
                            tvSuspicious.setText(dataDetail.getCol3());
                            tvSuspiciousProblem.setText(dataDetail.getCol3desc());
                            tvSeal.setText(dataDetail.getCol4());
                            tvSealProblem.setText(dataDetail.getCol4desc());
                            tvCave.setText(dataDetail.getCol5());
                            tvCaveProblem.setText(dataDetail.getCol5desc());
                            tvWater.setText(dataDetail.getCol6());
                            tvWaterProblem.setText(dataDetail.getCol6desc());
                            tvTransition.setText(dataDetail.getCol7());
                            tvTransitionProblem.setText(dataDetail.getCol7desc());
                            tvSmell.setText(dataDetail.getCol8());
                            tvSmellProblem.setText(dataDetail.getCol8desc());
                            tvGas.setText(dataDetail.getCol9());
                            tvGasProblem.setText(dataDetail.getCol9desc());
                            tvArm.setText(dataDetail.getCol10());
                            tvArmProblem.setText(dataDetail.getCol10desc());
                            tvOtherProblem.setText(dataDetail.getCol11());
                            String location = model.getDatadetail().getLocate();
                            if (!TextUtils.isEmpty(location) && location.contains(",")) {
                                String[] locationArr = location.split(",");
                                LatLng latLng = new LatLng(Double.parseDouble(locationArr[1]), Double.parseDouble(locationArr[0]));
                                markerOption = new MarkerOptions().icon(BitmapDescriptorFactory
                                        .defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                                        .position(latLng)
                                        .draggable(true);
                                aMap.addMarker(markerOption);
                                aMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                                        new CameraPosition(latLng, 11f, 0, 0)));
                            }
                            //上传的文件
                            if (model.getDataupload() != null) {
                                if ("00".equals(model.getDataupload().getFilepath())) {
                                    tvFileName.setText("无");
                                } else {
                                    tvFileName.setText(model.getDataupload().getFilename());
                                    filePath = model.getDataupload().getFilepath();
                                }
                            } else {
                                tvFileName.setText("无");
                            }

                            //上传的图片
                            if (model.getDataupload() != null) {
                                if ("00".equals(model.getDataupload().getPicturepath())) {
                                    tvPhoto.setText("无");
                                } else {
                                    String[] photoArr = model.getDataupload().getPicturepath().split(";");
                                    for (int i = 0; i < photoArr.length; i++) {
                                        path.add(photoArr[i]);
                                        photoAdapter.notifyDataSetChanged();
                                    }
                                }
                            } else {
                                tvPhoto.setText("无");
                            }

                            //审批人
                            if (model.getDatapproval() != null) {
                                String approval = model.getDatapproval().getEmployid();
                                if (!TextUtils.isEmpty(approval) && approval.contains(":")) {
                                    tvSpr.setText(approval.split(":")[1]);
                                }
                                //审批状态，0-表示批复不同意，1-表示批复同意，3-表示未批复
                                tvApproveStatus.setText(Util.getApprovalStatus(model.getDatapproval().getApprovalresult()));
                                if(!TextUtils.isEmpty(model.getDatapproval().getApprovalcomment())){
                                    llApproveAdvice.setVisibility(View.VISIBLE);
                                    tvApproveAdvice.setText(model.getDatapproval().getApprovalcomment());
                                }
                                //显示审批图片
                                if (!TextUtils.isEmpty(model.getDatapproval().getSignfilepath())) {
                                    Glide.with(ApproveTunnelActivity.this).
                                            load(model.getDatapproval().getSignfilepath()).
                                            into(ivApproveStatus);
                                }
                            }

                        }
                    }
                });
    }

    @OnClick({R.id.iv_back,
            R.id.ll_file,
            R.id.btn_approve})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_approve:
                Bundle bundle = new Bundle();
                bundle.putString("formid", formId);
                openActivity(ApproveFormActivity.class, bundle);
                break;
            case R.id.ll_file:
                if (!TextUtils.isEmpty(filePath)) {
                    Uri uri = Uri.parse(filePath);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
                break;

        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Util.activityList.remove(this);
    }
}
