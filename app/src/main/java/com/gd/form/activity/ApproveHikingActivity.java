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
import com.gd.form.model.HikingDetail;
import com.gd.form.model.HikingDetailModel;
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

public class ApproveHikingActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_pipeName)
    TextView tvPipeName;
    @BindView(R.id.tv_startStationNo)
    TextView tvStartStationNo;
    @BindView(R.id.tv_endStationNo)
    TextView tvEndStationNo;
    @BindView(R.id.tv_allowPeople)
    TextView tvAllowPeople;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.tv_bare)
    TextView tvBare;
    @BindView(R.id.tv_machine)
    TextView tvMachine;
    @BindView(R.id.tv_suspicious)
    TextView tvSuspicious;
    @BindView(R.id.tv_new)
    TextView tvNew;
    @BindView(R.id.tv_complete)
    TextView tvComplete;
    @BindView(R.id.tv_useful)
    TextView tvUseful;
    @BindView(R.id.tv_correct)
    TextView tvCorrect;
    @BindView(R.id.tv_water)
    TextView tvWater;
    @BindView(R.id.tv_relative)
    TextView tvRelative;
    @BindView(R.id.tv_building)
    TextView tvBuilding;
    @BindView(R.id.tv_car)
    TextView tvCar;
    @BindView(R.id.tv_timely)
    TextView tvTimely;
    @BindView(R.id.tv_wear)
    TextView tvWear;
    @BindView(R.id.tv_record)
    TextView tvRecord;
    @BindView(R.id.tv_other)
    TextView tvOther;
    @BindView(R.id.tv_advice)
    TextView tvAdvice;
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
        return R.layout.activity_approve_hiking;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.activityList.add(this);
        tvTitle.setText("徒步巡检表");
        // 此方法必须重写
        mapView.onCreate(savedInstanceState);
        initMap();
        token = (String) SPUtil.get(ApproveHikingActivity.this, "token", "");
        userId = (String) SPUtil.get(ApproveHikingActivity.this, "userId", "");
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
        Net.create(Api.class).getHikingDetail(token, params)
                .enqueue(new NetCallback<HikingDetailModel>(this, true) {
                    @Override
                    public void onResponse(HikingDetailModel model) {
                        if (model != null) {
                            HikingDetail dataDetail = model.getDatadetail();
                            if (!TextUtils.isEmpty(model.getPipeString())) {
                                tvPipeName.setText(model.getPipeString().split(":")[1]);
                            }
                            if (!TextUtils.isEmpty(model.getStakeString())) {
                                tvStartStationNo.setText(model.getStakeString().split(";")[0].split(":")[1]);
                                tvEndStationNo.setText(model.getStakeString().split(";")[0].split(":")[1] + "至" + model.getStakeString().split(";")[1].split(":")[1]);
                            }
                            if (!TextUtils.isEmpty(model.getDeptString())) {
                                tvArea.setText(model.getDeptString().split(":")[1]);
                            }
                            tvAllowPeople.setText(dataDetail.getRouteinspect());
                            tvBare.setText(dataDetail.getCol1());
                            tvMachine.setText(dataDetail.getCol2());
                            tvSuspicious.setText(dataDetail.getCol3());
                            tvNew.setText(dataDetail.getCol4());
                            tvComplete.setText(dataDetail.getCol5());
                            tvCorrect.setText(dataDetail.getCol6());
                            tvUseful.setText(dataDetail.getCol7());
                            tvWater.setText(dataDetail.getCol8());
                            tvRelative.setText(dataDetail.getCol9());
                            tvBuilding.setText(dataDetail.getCol10());
                            tvCar.setText(dataDetail.getCol11());
                            tvTimely.setText(dataDetail.getCol12());
                            tvWear.setText(dataDetail.getCol13());
                            tvRecord.setText(dataDetail.getCol14());
                            tvOther.setText(dataDetail.getCol15());
                            tvAdvice.setText(dataDetail.getCol16());
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
                                    filePath = model.getDataupload().getFilepath();
                                    tvFileName.setText(model.getDataupload().getFilename());
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
                            if(model.getDatapproval()!=null){
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
                                    Glide.with(ApproveHikingActivity.this).
                                            load(model.getDatapproval().getSignfilepath()).
                                            into(ivApproveStatus);
                                }
                            }

                        }
                    }
                });
    }

    @OnClick({R.id.iv_back,
            R.id.btn_approve,
            R.id.ll_file,
    })
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_file:
                if(!TextUtils.isEmpty(filePath)){
                    Uri uri = Uri.parse(filePath);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
                break;
            case R.id.btn_approve:
                Bundle bundle = new Bundle();
                bundle.putString("formid",formId);
                openActivity(ApproveFormActivity.class,bundle);
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
