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

import com.amap.api.maps.MapView;
import com.bumptech.glide.Glide;
import com.gd.form.R;
import com.gd.form.adapter.PhotoAdapter;
import com.gd.form.base.BaseActivity;
import com.gd.form.model.DeviceDetail;
import com.gd.form.model.DeviceDetailModel;
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

public class ApproveDeviceActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_pipeName)
    TextView tvPipeName;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.tv_stationNo)
    TextView tvStationNo;
    @BindView(R.id.tv_pipeElectricity)
    TextView tvPipeElectricity;
    @BindView(R.id.tv_pipePressure)
    TextView tvPipePressure;
    @BindView(R.id.tv_groundElectricity)
    TextView tvGroundElectricity;
    @BindView(R.id.tv_groundPressure)
    TextView tvGroundPressure;
    @BindView(R.id.tv_ac)
    TextView tvAc;
    @BindView(R.id.tv_resistance)
    TextView tvResistance;
    @BindView(R.id.tv_dc)
    TextView tvDc;
    @BindView(R.id.tv_material)
    TextView tvMaterial;
    @BindView(R.id.tv_location)
    TextView tvLocation;
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
    private String filePath;
    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_approve_device;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("去耦合器测试");
        mapView.setVisibility(View.GONE);
        llLocation.setVisibility(View.GONE);
        viewLocation.setVisibility(View.GONE);
        token = (String) SPUtil.get(ApproveDeviceActivity.this, "token", "");
        userId = (String) SPUtil.get(ApproveDeviceActivity.this, "userId", "");
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

    private void getDetail(String formId) {
        JsonObject params = new JsonObject();
        params.addProperty("formid", formId);
        Net.create(Api.class).getDeviceDetail(token, params)
                .enqueue(new NetCallback<DeviceDetailModel>(this, true) {
                    @Override
                    public void onResponse(DeviceDetailModel model) {
                        if (model != null) {
                            DeviceDetail dataDetail = model.getDatadetail();
                            if (!TextUtils.isEmpty(model.getDeptString())) {
                                tvArea.setText(model.getDeptString().split(":")[1]);
                            }
                            if (!TextUtils.isEmpty(model.getPipeString())) {
                                tvPipeName.setText(model.getPipeString().split(":")[1]);
                            }
                            tvStationNo.setText(dataDetail.getStakeid());
                            tvPipeElectricity.setText(dataDetail.getCol1());
                            tvPipePressure.setText(dataDetail.getCol2());
                            tvGroundElectricity.setText(dataDetail.getCol3());
                            tvGroundPressure.setText(dataDetail.getCol4());
                            tvAc.setText(dataDetail.getCol5());
                            tvDc.setText(dataDetail.getCol6());
                            tvMaterial.setText(dataDetail.getLandmaterial());
                            tvLocation.setText(dataDetail.getLocate());
                            tvResistance.setText(dataDetail.getLandresis());
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
                                Glide.with(ApproveDeviceActivity.this).
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
}
