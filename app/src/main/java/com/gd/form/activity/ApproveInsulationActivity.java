package com.gd.form.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.gd.form.constants.Constant;
import com.gd.form.model.InsulationDetail;
import com.gd.form.model.InsulationDetailModel;
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

public class ApproveInsulationActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_stationName)
    TextView tvStationName;
    @BindView(R.id.et_pipeElectricity)
    EditText etPipeElectricity;
    @BindView(R.id.et_pipePressure)
    EditText etPipePressure;
    @BindView(R.id.et_groundElectricity)
    EditText etGroundElectricity;
    @BindView(R.id.et_groundPressure)
    EditText etGroundPressure;
    @BindView(R.id.et_blankElectricity)
    EditText etBlankElectricity;
    @BindView(R.id.et_lightingPressure)
    EditText etLightingPressure;
    @BindView(R.id.et_remark)
    EditText etRemark;
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
    @BindView(R.id.ll_file)
    LinearLayout llFile;
    @BindView(R.id.ll_selectImages)
    LinearLayout llSelectImages;
    @BindView(R.id.ll_approveStatus)
    LinearLayout llApproveStatus;

    @BindView(R.id.rg_isProperty)
    RadioGroup rgIsProperty;
    @BindView(R.id.rb_yesProperty)
    RadioButton rbYesProperty;
    @BindView(R.id.rb_noProperty)
    RadioButton rbNoProperty;

    @BindView(R.id.rg_isBury)
    RadioGroup rgIsBury;
    @BindView(R.id.rb_yesBury)
    RadioButton rbYesBury;
    @BindView(R.id.rb_noBury)
    RadioButton rbNoBury;
    private String formId;
    private String token, userId;
    private MarkerOptions markerOption;
    private AMap aMap;
    private PhotoAdapter photoAdapter;
    private List<String> path;
    private String filePath;
    private String tag, location, approveId;
    private String property, lightingStatus;
    private int pipeId;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_approve_insulation;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.activityList.add(this);
        tvTitle.setText("绝缘件性能测试");
        llFile.setVisibility(View.GONE);
        llSelectImages.setVisibility(View.GONE);
        // 此方法必须重写
        mapView.onCreate(savedInstanceState);
        initMap();
        token = (String) SPUtil.get(ApproveInsulationActivity.this, "token", "");
        userId = (String) SPUtil.get(ApproveInsulationActivity.this, "userId", "");
        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            tag = bundle.getString("tag");
            if (tag.equals("detail")) {
                btnApprove.setVisibility(View.GONE);
            } else if (tag.equals("update")) {
                ivApproveStatus.setVisibility(View.GONE);
                llApproveStatus.setVisibility(View.GONE);
                btnApprove.setText("提交");
            } else if (tag.equals("approve")) {
                llApproveStatus.setVisibility(View.GONE);
                llApproveAdvice.setVisibility(View.GONE);
            }
            if (tag.equals("detail") || tag.equals("approve")) {
                etPipeElectricity.setEnabled(false);
                etPipePressure.setEnabled(false);
                etGroundElectricity.setEnabled(false);
                etGroundPressure.setEnabled(false);
                etBlankElectricity.setEnabled(false);
                etLightingPressure.setEnabled(false);
                etRemark.setEnabled(false);
                rbYesProperty.setEnabled(false);
                rbNoProperty.setEnabled(false);
                rbYesBury.setEnabled(false);
                rbNoBury.setEnabled(false);
            } else {
                etPipeElectricity.setEnabled(true);
                etPipePressure.setEnabled(true);
                etGroundElectricity.setEnabled(true);
                etGroundPressure.setEnabled(true);
                etBlankElectricity.setEnabled(true);
                etLightingPressure.setEnabled(true);
                etRemark.setEnabled(true);
                rbYesProperty.setEnabled(true);
                rbNoProperty.setEnabled(true);
                rbYesBury.setEnabled(true);
                rbNoBury.setEnabled(true);
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
        initListener();

    }

    private void initListener() {
        rgIsProperty.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesProperty:
                        property = "正常";
                        break;
                    case R.id.rb_noProperty:
                        property = "问题";
                        break;

                }
            }
        });
        rgIsBury.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesBury:
                        lightingStatus = "正常";
                        break;
                    case R.id.rb_noBury:
                        lightingStatus = "问题";
                        break;

                }
            }
        });
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
        Log.i("tag", "params===" + params);
        Net.create(Api.class).getInsulationDetail(token, params)
                .enqueue(new NetCallback<InsulationDetailModel>(this, true) {
                    @Override
                    public void onResponse(InsulationDetailModel model) {
                        if (model != null) {
                            InsulationDetail dataDetail = model.getDatadetail();
                            tvStationName.setText(dataDetail.getStationdesc());
                            etRemark.setText(dataDetail.getRemarks());
                            etPipeElectricity.setText(dataDetail.getCol1());
                            etPipePressure.setText(dataDetail.getCol2());
                            etGroundElectricity.setText(dataDetail.getCol3());
                            etGroundPressure.setText(dataDetail.getCol4());
                            etBlankElectricity.setText(dataDetail.getCol5());
                            etLightingPressure.setText(dataDetail.getCol7());
                            location = dataDetail.getLocate();
                            property = dataDetail.getCol6();
                            lightingStatus = dataDetail.getCol8();
                            if (property.equals("正常")) {
                                rbYesProperty.setChecked(true);
                            } else {
                                rbNoProperty.setChecked(true);
                            }
                            if (lightingStatus.equals("正常")) {
                                rbYesBury.setChecked(true);
                            } else {
                                rbNoBury.setChecked(true);
                            }
                            pipeId = dataDetail.getPipeid();
                            String location = model.getDatadetail().getLocate();
                            etRemark.setText(dataDetail.getRemarks());
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
                                    Glide.with(ApproveInsulationActivity.this).
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
                if (tag.equals("update")) {
                    if (paramsComplete()) {
                        commit();
                    }
                } else {
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
            ToastUtil.show("请输入管道电位");
            return false;
        }
        if (!NumberUtil.isNumber(etPipeElectricity.getText().toString())) {
            ToastUtil.show("管道电位格式不正确");
            return false;
        }
        if (TextUtils.isEmpty(etPipePressure.getText().toString())) {
            ToastUtil.show("请输入管道干扰电压");
            return false;
        }
        if (!NumberUtil.isNumber(etPipePressure.getText().toString())) {
            ToastUtil.show("管道干扰电压格式不正确");
            return false;
        }
        if (TextUtils.isEmpty(etGroundElectricity.getText().toString())) {
            ToastUtil.show("请输入接地侧电位");
            return false;
        }
        if (!NumberUtil.isNumber(etGroundElectricity.getText().toString())) {
            ToastUtil.show("接地侧电位格式不正确");
            return false;
        }
        if (TextUtils.isEmpty(etGroundPressure.getText().toString())) {
            ToastUtil.show("请输入接地侧干扰电压");
            return false;
        }
        if (!NumberUtil.isNumber(etGroundPressure.getText().toString())) {
            ToastUtil.show("入接地侧干扰电压格式不正确");
            return false;
        }
        if (TextUtils.isEmpty(etBlankElectricity.getText().toString())) {
            ToastUtil.show("请输入放空侧电位");
            return false;
        }
        if (!NumberUtil.isNumber(etBlankElectricity.getText().toString())) {
            ToastUtil.show("放空侧电位格式不正确");
            return false;
        }
        if (TextUtils.isEmpty(etLightingPressure.getText().toString())) {
            ToastUtil.show("请输入管道避雷器电压");
            return false;
        }
        if (!NumberUtil.isNumber(etLightingPressure.getText().toString())) {
            ToastUtil.show("管道避雷器电压格式不正确");
            return false;
        }
        if (TextUtils.isEmpty(etRemark.getText().toString())) {
            ToastUtil.show("请输入备注");
            return false;
        }
        return true;
    }

    private void commit() {
        etPipeElectricity.setEnabled(false);
        etPipePressure.setEnabled(false);
        etGroundElectricity.setEnabled(false);
        etGroundPressure.setEnabled(false);
        etBlankElectricity.setEnabled(false);
        etLightingPressure.setEnabled(false);
        etRemark.setEnabled(false);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", formId);
        jsonObject.addProperty("departmentid", 0);
        jsonObject.addProperty("pipeid", pipeId);
        jsonObject.addProperty("stationdesc", tvStationName.getText().toString());
        jsonObject.addProperty("col1", etPipeElectricity.getText().toString());
        jsonObject.addProperty("col2", etPipePressure.getText().toString());
        jsonObject.addProperty("col3", etGroundElectricity.getText().toString());
        jsonObject.addProperty("col4", etGroundPressure.getText().toString());
        jsonObject.addProperty("col5", etBlankElectricity.getText().toString());
        jsonObject.addProperty("col6", property);
        jsonObject.addProperty("col7", etLightingPressure.getText().toString());
        jsonObject.addProperty("col8", lightingStatus);
        jsonObject.addProperty("remarks", etRemark.getText().toString());
        jsonObject.addProperty("locate", location);
        jsonObject.addProperty("creator", userId);
        jsonObject.addProperty("creatime", TimeUtil.getCurrentTime());
        jsonObject.addProperty("approvalid", approveId);
        jsonObject.addProperty("filepath", "00");
        jsonObject.addProperty("picturepath", "00");
        Net.create(Api.class).updateProperty(token, jsonObject)
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
