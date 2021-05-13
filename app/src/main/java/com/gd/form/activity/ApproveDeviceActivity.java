package com.gd.form.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.gd.form.constants.Constant;
import com.gd.form.model.DeviceDetail;
import com.gd.form.model.DeviceDetailModel;
import com.gd.form.model.ServerModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.NumberUtil;
import com.gd.form.utils.SPUtil;
import com.gd.form.utils.TimeUtil;
import com.gd.form.utils.ToastUtil;
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
    @BindView(R.id.tv_stationNo)
    TextView tvStationNo;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.et_pipeElectricity)
    EditText etPipeElectricity;
    @BindView(R.id.et_pipePressure)
    EditText etPipePressure;
    @BindView(R.id.et_groundElectricity)
    EditText etGroundElectricity;
    @BindView(R.id.et_groundPressure)
    EditText etGroundPressure;
    @BindView(R.id.et_ac)
    EditText etAc;
    @BindView(R.id.et_resistance)
    EditText etResistance;
    @BindView(R.id.et_dc)
    EditText etDc;
    @BindView(R.id.et_material)
    EditText etMaterial;
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
    @BindView(R.id.tv_approveAdvice)
    TextView tvApproveAdvice;
    @BindView(R.id.ll_approveAdvice)
    LinearLayout llApproveAdvice;
    @BindView(R.id.ll_file)
    LinearLayout llFile;
    @BindView(R.id.ll_selectImages)
    LinearLayout llSelectImages;
    @BindView(R.id.ll_approveStatus)
    LinearLayout llApproveStatus;
    private String formId;
    private String token, userId;
    private PhotoAdapter photoAdapter;
    private List<String> path;
    private String filePath;
    private String tag;
    private String pipeId,stakeId,approveId;
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
        Util.activityList.add(this);
        tvTitle.setText("去耦合器测试");
        mapView.setVisibility(View.GONE);
        llFile.setVisibility(View.GONE);
        llSelectImages.setVisibility(View.GONE);
        llLocation.setVisibility(View.GONE);
        token = (String) SPUtil.get(ApproveDeviceActivity.this, "token", "");
        userId = (String) SPUtil.get(ApproveDeviceActivity.this, "userId", "");
        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            tag = bundle.getString("tag");
            if (tag.equals("detail")) {
                btnApprove.setVisibility(View.GONE);
            }else if(tag.equals("update")){
                ivApproveStatus.setVisibility(View.GONE);
                llApproveStatus.setVisibility(View.GONE);
                btnApprove.setText("提交");
            }else if(tag.equals("approve")){
                llApproveStatus.setVisibility(View.GONE);
                llApproveAdvice.setVisibility(View.GONE);
            }
            if(tag.equals("detail") || tag.equals("approve")){
                etPipeElectricity.setEnabled(false);
                etPipePressure.setEnabled(false);
                etGroundElectricity.setEnabled(false);
                etGroundPressure.setEnabled(false);
                etAc.setEnabled(false);
                etDc.setEnabled(false);
                etMaterial.setEnabled(false);
                etResistance.setEnabled(false);
            }else{
                etPipeElectricity.setEnabled(true);
                etPipePressure.setEnabled(true);
                etGroundElectricity.setEnabled(true);
                etGroundPressure.setEnabled(true);
                etAc.setEnabled(true);
                etDc.setEnabled(true);
                etMaterial.setEnabled(true);
                etResistance.setEnabled(true);
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
                            tvStationNo.setText(dataDetail.getStakeid());
                            etPipeElectricity.setText(dataDetail.getCol1());
                            etPipePressure.setText(dataDetail.getCol2());
                            etGroundElectricity.setText(dataDetail.getCol3());
                            etGroundPressure.setText(dataDetail.getCol4());
                            etAc.setText(dataDetail.getCol5());
                            etDc.setText(dataDetail.getCol6());
                            etMaterial.setText(dataDetail.getLandmaterial());
                            etResistance.setText(dataDetail.getLandresis());
                            tvAddress.setText(dataDetail.getLocate());
                            pipeId = dataDetail.getPipeid();
                            stakeId = dataDetail.getStakeid();
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
                                if (!TextUtils.isEmpty(approval)) {
                                    tvSpr.setText(approval.split(":")[1]);
                                    approveId = approval.split(":")[0];
                                }
                                //审批状态，0-表示批复不同意，1-表示批复同意，3-表示未批复
                                tvApproveStatus.setText(Util.getApprovalStatus(model.getDatapproval().getApprovalresult()));
                                if (!TextUtils.isEmpty(model.getDatapproval().getApprovalcomment())
                                        && (tag.equals("detail") || tag.equals("update"))
                                        && model.getDatapproval().getApprovalresult()!=3) {
                                    llApproveAdvice.setVisibility(View.VISIBLE);
                                    tvApproveAdvice.setText(model.getDatapproval().getApprovalcomment());
                                }
                                //显示审批图片
                                if (!TextUtils.isEmpty(model.getDatapproval().getSignfilepath())) {
                                    Glide.with(ApproveDeviceActivity.this).
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
                if(tag.equals("update")){
                    if (paramsComplete()) {
                        commit();
                    }
                }else{
                    Bundle bundle = new Bundle();
                    bundle.putString("formid", formId);
                    openActivity(ApproveFormActivity.class, bundle);
                }
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
    private boolean paramsComplete() {
        if (TextUtils.isEmpty(etPipeElectricity.getText().toString())) {
            ToastUtil.show("请输入管地电位");
            return false;
        }
        if (!NumberUtil.isNumber(etPipeElectricity.getText().toString())) {
            ToastUtil.show("管地电位格式输入不正确");
            return false;
        }
        if (TextUtils.isEmpty(etPipePressure.getText().toString())) {
            ToastUtil.show("请输入交流干扰电压");
            return false;
        }
        if (!NumberUtil.isNumber(etPipePressure.getText().toString())) {
            ToastUtil.show("交流干扰电压格式输入不正确");
            return false;
        }
        if (TextUtils.isEmpty(etGroundElectricity.getText().toString())) {
            ToastUtil.show("请输入对地电位");
            return false;
        }
        if (!NumberUtil.isNumber(etGroundElectricity.getText().toString())) {
            ToastUtil.show("接地电位格式输入不正确");
            return false;
        }
        if (TextUtils.isEmpty(etGroundPressure.getText().toString())) {
            ToastUtil.show("请输入交流干扰电压");
            return false;
        }
        if (!NumberUtil.isNumber(etGroundPressure.getText().toString())) {
            ToastUtil.show("交流干扰电压格式输入不正确");
            return false;
        }
        if (TextUtils.isEmpty(etAc.getText().toString())) {
            ToastUtil.show("请输入交流电流");
            return false;
        }
        if (!NumberUtil.isNumber(etAc.getText().toString())) {
            ToastUtil.show("交流电流格式输入不正确");
            return false;
        }
        if (TextUtils.isEmpty(etDc.getText().toString())) {
            ToastUtil.show("请输入直流电流");
            return false;
        }
        if (!NumberUtil.isNumber(etDc.getText().toString())) {
            ToastUtil.show("直流电流格式输入不正确");
            return false;
        }
        if (TextUtils.isEmpty(etMaterial.getText().toString())) {
            ToastUtil.show("请输入接地材料");
            return false;
        }
        if (TextUtils.isEmpty(etResistance.getText().toString())) {
            ToastUtil.show("请输入接地电阻");
            return false;
        }
        if (!NumberUtil.isNumber(etResistance.getText().toString())) {
            ToastUtil.show("接地电阻格式输入不正确");
            return false;
        }
        return true;
    }
    private void commit() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", formId);
        jsonObject.addProperty("departmentid",0);
        jsonObject.addProperty("pipeid", pipeId);
        jsonObject.addProperty("stakeid", stakeId);
        jsonObject.addProperty("col1",etPipeElectricity.getText().toString());
        jsonObject.addProperty("col2", etPipePressure.getText().toString());
        jsonObject.addProperty("col3", etGroundElectricity.getText().toString());
        jsonObject.addProperty("col4", etGroundPressure.getText().toString());
        jsonObject.addProperty("col5", etAc.getText().toString());
        jsonObject.addProperty("col6", etDc.getText().toString());
        jsonObject.addProperty("landmaterial", etMaterial.getText().toString());
        jsonObject.addProperty("landresis", etResistance.getText().toString());
        jsonObject.addProperty("locate", tvAddress.getText().toString());
        jsonObject.addProperty("creator", userId);
        jsonObject.addProperty("creatime", TimeUtil.getCurrentTime());
        jsonObject.addProperty("approvalid", approveId);
        jsonObject.addProperty("picturepath", "00");
        jsonObject.addProperty("filepath", "00");
        Net.create(Api.class).updateDevice(token, jsonObject)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel result) {
                        ToastUtil.show(result.getMsg());
                        if (result.getCode() == Constant.SUCCESS_CODE) {
                            Intent intent = new Intent();
                            intent.setAction("com.action.update.waitingTask");
                            sendBroadcast(intent);
                            finish();
                        }

                    }
                });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Util.activityList.remove(this);
    }
}
