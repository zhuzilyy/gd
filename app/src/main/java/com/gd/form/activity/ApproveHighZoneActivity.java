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
import com.gd.form.model.HighZoneDetail;
import com.gd.form.model.HighZoneDetailModel;
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

public class ApproveHighZoneActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.tv_weather)
    TextView tvWeather;
    @BindView(R.id.tv_startStationNo)
    TextView tvStationNo;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_bare)
    TextView tvBare;
    @BindView(R.id.tv_machine)
    TextView tvMachine;
    @BindView(R.id.tv_suspicious)
    TextView tvSuspicious;
    @BindView(R.id.tv_new)
    TextView tvNew;
    @BindView(R.id.tv_illegal)
    TextView tvIllegal;
    @BindView(R.id.tv_complete)
    TextView tvComplete;
    @BindView(R.id.tv_water)
    TextView tvWater;
    @BindView(R.id.tv_relative)
    TextView tvRelative;
    @BindView(R.id.tv_change)
    TextView tvChange;
    @BindView(R.id.tv_add)
    TextView tvAdd;
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
    @BindView(R.id.tv_detail)
    TextView tvDetail;
    @BindView(R.id.tv_method)
    TextView tvMethod;
    @BindView(R.id.tv_result)
    TextView tvResult;
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
    @BindView(R.id.ll_location)
    LinearLayout llLocation;
    @BindView(R.id.view_location)
    View viewLocation;
    @BindView(R.id.rvResultPhoto)
    RecyclerView rvResultPhoto;
    private String formId;
    private String token, userId;
    private PhotoAdapter photoAdapter;
    private List<String> path;
    private MarkerOptions markerOption;
    private AMap aMap;
    private String filePath;
    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_approve_high_zone;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("高后果区徒步巡检表");
        // 此方法必须重写
        mapView.onCreate(savedInstanceState);
        initMap();
        token = (String) SPUtil.get(ApproveHighZoneActivity.this, "token", "");
        userId = (String) SPUtil.get(ApproveHighZoneActivity.this, "userId", "");
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
        Net.create(Api.class).getHighZoneDetail(token, params)
                .enqueue(new NetCallback<HighZoneDetailModel>(this, true) {
                    @Override
                    public void onResponse(HighZoneDetailModel model) {
                        if (model != null) {
                            HighZoneDetail dataDetail = model.getDatadetail();
                            if (!TextUtils.isEmpty(model.getDeptString())) {
                                tvArea.setText(model.getDeptString().split(":")[1]);
                            }
                            tvWeather.setText(dataDetail.getWeathers());
                            if (!TextUtils.isEmpty(model.getStakeString())) {
                                tvStationNo.setText(model.getStakeString().split(";")[0].split(":")[1]
                                        + "到" + model.getStakeString().split(";")[1].split(":")[1]);
                            }
                            tvBare.setText(dataDetail.getCol1());
                            tvMachine.setText(dataDetail.getCol2());
                            tvSuspicious.setText(dataDetail.getCol3());
                            tvNew.setText(dataDetail.getCol4());
                            tvIllegal.setText(dataDetail.getCol5());
                            tvComplete.setText(dataDetail.getCol6());
                            tvWater.setText(dataDetail.getCol7());
                            tvRelative.setText(dataDetail.getCol8());
                            tvChange.setText(dataDetail.getCol9());
                            tvAdd.setText(dataDetail.getCol10());
                            tvCar.setText(dataDetail.getCol11());
                            tvTimely.setText(dataDetail.getCol12());
                            tvWear.setText(dataDetail.getCol13());
                            tvRecord.setText(dataDetail.getCol14());
                            tvOther.setText(dataDetail.getCol15());
                            tvDetail.setText(dataDetail.getProblemdetail());
                            tvMethod.setText(dataDetail.getSolutions());
                            tvResult.setText(dataDetail.getResultdetail());
                            tvStatus.setText(dataDetail.getConditions());
                            String location = dataDetail.getLocate();
                            if (!TextUtils.isEmpty(location)) {
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
                            String approval = model.getDatapproval().getEmployid();
                            if (!TextUtils.isEmpty(approval)) {
                                tvSpr.setText(approval.split(":")[1]);
                            }
                            //审批状态，0-表示批复不同意，1-表示批复同意，3-表示未批复
                            tvApproveStatus.setText(Util.getApprovalStatus(model.getDatapproval().getApprovalresult()));
                            //显示审批图片
                            if (!TextUtils.isEmpty(model.getDatapproval().getSignfilepath())) {
                                Glide.with(ApproveHighZoneActivity.this).
                                        load(model.getDatapproval().getSignfilepath()).
                                        into(ivApproveStatus);
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
                openActivity(ApproveFormActivity.class);
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
}
