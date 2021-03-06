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
import com.gd.form.model.WeightCarDetail;
import com.gd.form.model.WeightCarDetailModel;
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

public class ApproveWeightCarActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_departmentName)
    TextView tvDepartmentName;
    @BindView(R.id.tv_pipeName)
    TextView tvPipeName;
    @BindView(R.id.tv_stationNo)
    TextView tvStationNo;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_roadName)
    TextView tvRoadName;
    @BindView(R.id.tv_roadWidth)
    TextView tvRoadWidth;
    @BindView(R.id.tv_pipeWidth)
    TextView tvPipeWidth;
    @BindView(R.id.tv_minDepth)
    TextView tvMinDepth;
    @BindView(R.id.tv_rate)
    TextView tvRate;
    @BindView(R.id.tv_isProtect)
    TextView tvIsProtect;
    @BindView(R.id.tv_remark)
    TextView tvRemark;
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
        return R.layout.activity_approve_weight_car;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.activityList.add(this);
        tvTitle.setText("?????????????????????");
        // ?????????????????????
        mapView.onCreate(savedInstanceState);
        initMap();
        token = (String) SPUtil.get(ApproveWeightCarActivity.this, "token", "");
        userId = (String) SPUtil.get(ApproveWeightCarActivity.this, "userId", "");
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
     * ?????????AMap??????
     */
    private void initMap() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
    }
    private void getDetail(String formId) {
        JsonObject params = new JsonObject();
        params.addProperty("formid", formId);
        Net.create(Api.class).getWeightCarDetail(token, params)
                .enqueue(new NetCallback<WeightCarDetailModel>(this, true) {
                    @Override
                    public void onResponse(WeightCarDetailModel model) {
                        if (model != null) {
                            WeightCarDetail dataDetail = model.getDatadetail();
                            tvDepartmentName.setText(dataDetail.getDepartmentid());
                            if(!TextUtils.isEmpty(model.getPipeString()) && model.getPipeString().contains(":")){
                                tvPipeName.setText(model.getPipeString().split(":")[1]);
                            }
                            if (!TextUtils.isEmpty(model.getStakeString()) && model.getStakeString().contains(":")) {
                                tvStationNo.setText(model.getStakeString().split(":")[1]);
                            }
                            tvAddress.setText(dataDetail.getAdmposition());
                            tvRoadName.setText(dataDetail.getRoadname());
                            tvRoadWidth.setText(dataDetail.getRoadwidth()+"");
                            tvPipeWidth.setText(dataDetail.getDiameter()+"");
                            tvMinDepth.setText(dataDetail.getMindepth()+"");
                            tvRate.setText(dataDetail.getRollfreq());
                            tvIsProtect.setText(dataDetail.getPipeprotect());
                            tvRemark.setText(dataDetail.getRemarks());
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
                            //???????????????
                            if (model.getDataupload() != null) {
                                if ("00".equals(model.getDataupload().getFilepath())) {
                                    tvFileName.setText("???");
                                } else {
                                    tvFileName.setText(model.getDataupload().getFilename());
                                    filePath = model.getDataupload().getFilepath();
                                }
                            } else {
                                tvFileName.setText("???");
                            }

                            //???????????????
                            if (model.getDataupload() != null) {
                                if ("00".equals(model.getDataupload().getPicturepath())) {
                                    tvPhoto.setText("???");
                                } else {
                                    String[] photoArr = model.getDataupload().getPicturepath().split(";");
                                    for (int i = 0; i < photoArr.length; i++) {
                                        path.add(photoArr[i]);
                                        photoAdapter.notifyDataSetChanged();
                                    }
                                }
                            } else {
                                tvPhoto.setText("???");
                            }

                            //?????????
                            if(model.getDatapproval()!=null){
                                String approval = model.getDatapproval().getEmployid();
                                if (!TextUtils.isEmpty(approval) && approval.contains(":")) {
                                    tvSpr.setText(approval.split(":")[1]);
                                }
                                //???????????????0-????????????????????????1-?????????????????????3-???????????????
                                tvApproveStatus.setText(Util.getApprovalStatus(model.getDatapproval().getApprovalresult()));
                                if(!TextUtils.isEmpty(model.getDatapproval().getApprovalcomment())){
                                    llApproveAdvice.setVisibility(View.VISIBLE);
                                    tvApproveAdvice.setText(model.getDatapproval().getApprovalcomment());
                                }
                                //??????????????????
                                if (!TextUtils.isEmpty(model.getDatapproval().getSignfilepath())) {
                                    Glide.with(ApproveWeightCarActivity.this).
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
                bundle.putString("formid",formId);
                openActivity(ApproveFormActivity.class,bundle);
                break;
            case R.id.ll_file:
                if(!TextUtils.isEmpty(filePath)){
                    Uri uri = Uri.parse(filePath);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
                break;

        }
    }

    /**
     * ??????????????????
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * ??????????????????
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * ??????????????????
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
