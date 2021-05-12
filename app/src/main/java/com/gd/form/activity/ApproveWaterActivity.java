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

import com.amap.api.maps.MapView;
import com.bumptech.glide.Glide;
import com.gd.form.R;
import com.gd.form.adapter.PhotoAdapter;
import com.gd.form.base.BaseActivity;
import com.gd.form.model.WaterDetail;
import com.gd.form.model.WaterDetailModel;
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

public class ApproveWaterActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_stationNo)
    TextView tvStationNo;
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
    @BindView(R.id.et_distance)
    EditText etDistance;
    @BindView(R.id.et_location)
    EditText etLocation;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_BuildingCompany)
    EditText etBuildingCompany;
    @BindView(R.id.et_weather)
    EditText etWeather;
    @BindView(R.id.rg_isHidden)
    RadioGroup rgIsHidden;
    @BindView(R.id.rb_yes)
    RadioButton rbYes;
    @BindView(R.id.rb_no)
    RadioButton rbNo;
    @BindView(R.id.et_problem)
    EditText etProblem;
    @BindView(R.id.et_advice)
    EditText etAdvice;
    @BindView(R.id.rg_handle)
    RadioGroup rgHandle;
    @BindView(R.id.rb_mouth)
    RadioButton rbMouth;
    @BindView(R.id.rb_mouthUpload)
    RadioButton rbMouthUpload;
    @BindView(R.id.rb_writeUpload)
    RadioButton rbWriteUpload;
    @BindView(R.id.et_manager)
    EditText etManager;
    @BindView(R.id.et_process)
    EditText etProcess;
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
        return R.layout.activity_approve_water;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.activityList.add(this);
        tvTitle.setText("水工施工检查日志");
        token = (String) SPUtil.get(ApproveWaterActivity.this, "token", "");
        userId = (String) SPUtil.get(ApproveWaterActivity.this, "userId", "");
        llLocation.setVisibility(View.GONE);
        mapView.setVisibility(View.GONE);
        viewLocation.setVisibility(View.GONE);
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
        Log.i("tag", "params===" + params);
        Net.create(Api.class).getWaterDetail(token, params)
                .enqueue(new NetCallback<WaterDetailModel>(this, true) {
                    @Override
                    public void onResponse(WaterDetailModel model) {
                        if (model != null) {
                            WaterDetail dataDetail = model.getDatadetail();
                            if (!TextUtils.isEmpty(model.getStakeString()) && model.getStakeString().contains(":")) {
                                tvStationNo.setText(model.getStakeString().split(":")[1]);
                            }
                            etDistance.setText(dataDetail.getStakefrom());
                            etName.setText(dataDetail.getProtectname());
                            etLocation.setText(dataDetail.getLocate());
                            etBuildingCompany.setText(dataDetail.getProtectunit());
                            etWeather.setText(dataDetail.getWeathers());
                            etProblem.setText(dataDetail.getCol1());
                            etAdvice.setText(dataDetail.getCol1handle());
                            if (dataDetail.getHandlemode().equals("头通知施工单位整改")) {
                                rbMouth.setChecked(true);
                            }else if(dataDetail.getHandlemode().equals("头上报输气管理处")){
                                rbMouthUpload.setChecked(true);
                            }else{
                                rbWriteUpload.setChecked(true);
                            }

                            if (dataDetail.getCol3().equals("是")) {
                                rbYes.setChecked(true);
                            }else{
                                rbNo.setChecked(true);
                            }
                            etManager.setText(dataDetail.getConstructionhandler());
                            etProcess.setText(dataDetail.getCol2());

                            //上传的图片
                            if (!TextUtils.isEmpty(dataDetail.getCol1picture())) {
                                if ("00".equals(dataDetail.getCol1picture())) {
                                    tvPhoto.setText("无");
                                } else {
                                    String[] photoArr = dataDetail.getCol1picture().split(";");
                                    for (int i = 0; i < photoArr.length; i++) {
                                        path.add(photoArr[i]);
                                        photoAdapter.notifyDataSetChanged();
                                    }
                                }
                            } else {
                                tvPhoto.setText("无");
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
                            //审批人
                            if (model.getDatapproval() != null) {
                                String approval = model.getDatapproval().getEmployid();
                                if (!TextUtils.isEmpty(approval) && approval.contains(":")) {
                                    tvSpr.setText(approval.split(":")[1]);
                                }
                                //审批状态，0-表示批复不同意，1-表示批复同意，3-表示未批复
                                tvApproveStatus.setText(Util.getApprovalStatus(model.getDatapproval().getApprovalresult()));
                                if (!TextUtils.isEmpty(model.getDatapproval().getApprovalcomment())) {
                                    llApproveAdvice.setVisibility(View.VISIBLE);
                                    tvApproveAdvice.setText(model.getDatapproval().getApprovalcomment());
                                }
                                //显示审批图片
                                if (!TextUtils.isEmpty(model.getDatapproval().getSignfilepath())) {
                                    Glide.with(ApproveWaterActivity.this).
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Util.activityList.remove(this);
    }
}
