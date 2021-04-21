package com.gd.form.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
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
    private String formId;
    private String token, userId;
    private MarkerOptions markerOption;
    private AMap aMap;
    private PhotoAdapter photoAdapter;
    private List<String> path;
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
        tvTitle.setText("重车碾压调查表");
        // 此方法必须重写
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
        Net.create(Api.class).getWeightCarDetail(token, params)
                .enqueue(new NetCallback<WeightCarDetailModel>(this, true) {
                    @Override
                    public void onResponse(WeightCarDetailModel model) {
                        if (model != null) {
                            WeightCarDetail dataDetail = model.getDatadetail();
                            tvDepartmentName.setText(dataDetail.getDepartmentid());
                            if(!TextUtils.isEmpty(model.getPipeString())){
                                tvPipeName.setText(model.getPipeString().split(":")[1]);
                            }
                            if (!TextUtils.isEmpty(model.getStakeString())) {
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
                            if (!TextUtils.isEmpty(location)) {
                                String[] locationArr = location.split(",");
                                LatLng latLng = new LatLng(Double.parseDouble(locationArr[1]), Double.parseDouble(locationArr[0]));
                                markerOption = new MarkerOptions().icon(BitmapDescriptorFactory
                                        .defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                                        .position(latLng)
                                        .draggable(true);
                                aMap.addMarker(markerOption);
                            }
                            //上传的文件
                            if (model.getDataupload() != null) {
                                if ("00".equals(model.getDataupload().getFilepath())) {
                                    tvFileName.setText("无");
                                } else {
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
                            String approval = model.getDatapproval().getEmployid();
                            if (!TextUtils.isEmpty(approval)) {
                                tvSpr.setText(approval.split(":")[1]);
                            }
                            //审批状态，0-表示批复不同意，1-表示批复同意，3-表示未批复
                            tvApproveStatus.setText(Util.getApprovalStatus(model.getDatapproval().getApprovalresult()));
                            //显示审批图片
                            if (!TextUtils.isEmpty(model.getDatapproval().getSignfilepath())) {
                                Glide.with(ApproveWeightCarActivity.this).
                                        load(model.getDatapproval().getSignfilepath()).
                                        into(ivApproveStatus);
                            }
                        }
                    }
                });
    }

    @OnClick({R.id.iv_back,
            R.id.btn_approve})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_approve:
                openActivity(ApproveFormActivity.class);
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
