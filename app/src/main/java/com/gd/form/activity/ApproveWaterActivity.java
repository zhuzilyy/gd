package com.gd.form.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.amap.api.maps.MapView;
import com.bumptech.glide.Glide;
import com.gd.form.R;
import com.gd.form.adapter.PhotoAdapter;
import com.gd.form.base.BaseActivity;
import com.gd.form.constants.Constant;
import com.gd.form.model.GlideImageLoader;
import com.gd.form.model.ServerModel;
import com.gd.form.model.WaterDetail;
import com.gd.form.model.WaterDetailModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.SPUtil;
import com.gd.form.utils.TimeUtil;
import com.gd.form.utils.ToastUtil;
import com.gd.form.utils.Util;
import com.gd.form.utils.WeiboDialogUtils;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;
import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;
import com.yancy.gallerypick.inter.IHandlerCallBack;

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
    @BindView(R.id.ll_chooseImages)
    LinearLayout llChooseImages;
    @BindView(R.id.ll_approveStatus)
    LinearLayout llApproveStatus;
    private String formId;
    private String token, userId;
    private PhotoAdapter photoAdapter;
    private List<String> path;
    private String filePath;
    private String isHidden, handleMethod;
    private String tag;
    private int FILE_REQUEST_CODE = 100;
    private int PERMISSIONS_REQUEST_READ_CONTACTS = 8;
    private String ossFilePath;
    private String selectFileName;
    private String selectFilePath;
    private IHandlerCallBack iHandlerCallBack;
    private GalleryConfig galleryConfig;
    private List<String> nameList;
    private Dialog mWeiboDialog;
    private OSSCredentialProvider ossCredentialProvider;
    private OSS oss;
    private int stakeId;
    private String approverId;
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
        tvTitle.setText("????????????????????????");
        token = (String) SPUtil.get(ApproveWaterActivity.this, "token", "");
        userId = (String) SPUtil.get(ApproveWaterActivity.this, "userId", "");
        llLocation.setVisibility(View.GONE);
        mapView.setVisibility(View.GONE);
        viewLocation.setVisibility(View.GONE);
        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            tag = bundle.getString("tag");
            Log.i("tag","tag==="+tag);
            if (tag.equals("detail")) {
                btnApprove.setVisibility(View.GONE);
            }else if(tag.equals("update")){
                btnApprove.setText("??????");
                ivApproveStatus.setVisibility(View.GONE);
                llApproveStatus.setVisibility(View.GONE);
            }else if(tag.equals("approve")){
                llApproveStatus.setVisibility(View.GONE);
                llApproveAdvice.setVisibility(View.GONE);
            }
            if(tag.equals("detail")||tag.equals("approve")){
                etWeather.setEnabled(false);
                etProblem.setEnabled(false);
                etAdvice.setEnabled(false);
                rbMouth.setEnabled(false);
                rbMouthUpload.setEnabled(false);
                rbWriteUpload.setEnabled(false);
                rbNo.setEnabled(false);
                rbYes.setEnabled(false);
                etManager.setEnabled(false);
                etProcess.setEnabled(false);
                llChooseImages.setEnabled(false);
            }else{
                etWeather.setEnabled(true);
                etProblem.setEnabled(true);
                etAdvice.setEnabled(true);
                rbMouth.setEnabled(true);
                rbMouthUpload.setEnabled(true);
                rbWriteUpload.setEnabled(true);
                rbNo.setEnabled(true);
                rbYes.setEnabled(true);
                etManager.setEnabled(true);
                etProcess.setEnabled(true);
                llChooseImages.setEnabled(true);
            }
            formId = bundle.getString("formId");
        }
        path = new ArrayList<>();
        nameList = new ArrayList<>();
        ossCredentialProvider = new OSSPlainTextAKSKCredentialProvider(Constant.ACCESSKEYID, Constant.ACCESSKEYSECRET);
        oss = new OSSClient(mContext.getApplicationContext(), Constant.ENDPOINT, ossCredentialProvider);
        getDetail(formId);
        initGallery();
        initConfig();
        initListener();
    }

    private void initConfig() {
        galleryConfig = new GalleryConfig.Builder()
                .imageLoader(new GlideImageLoader())    // ImageLoader ????????????????????????
                .iHandlerCallBack(iHandlerCallBack)     // ????????????????????????
                .provider("com.gd.form.fileprovider")   // provider(??????)
                .pathList(path)                         // ?????????????????????
                .multiSelect(true)                      // ????????????   ?????????false
                .multiSelect(true, 9)                   // ??????????????????????????? ??????????????????   ?????????false ??? 9
                .maxSize(9)                             // ??????????????? ??????????????????    ?????????9
                .crop(false)                             // ??????????????????????????????????????? ??????????????????????????????
                .crop(false, 1, 1, 500, 500)             // ??????????????????????????????   ?????????????????? 1:1
                .isShowCamera(true)                     // ????????????????????????  ?????????false
                .filePath("/Gallery/Pictures")// ??????????????????
                .build();


        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvResultPhoto.setLayoutManager(gridLayoutManager);
        photoAdapter = new PhotoAdapter(this, path);
        rvResultPhoto.setAdapter(photoAdapter);

    }


    private void initGallery() {
        iHandlerCallBack = new IHandlerCallBack() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(List<String> photoList) {
                path.clear();
                for (String s : photoList) {
                    path.add(s);
                }
                photoAdapter.notifyDataSetChanged();
                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(ApproveWaterActivity.this, "?????????...");
                mWeiboDialog.getWindow().setDimAmount(0f);
                for (int i = 0; i < path.size(); i++) {
                    String suffix = path.get(i).substring(path.get(i).length() - 4);
                    uploadFiles("W015/" + userId + "_" + TimeUtil.getFileNameTime() + "_" + i + suffix, path.get(i));
                }
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onError() {

            }
        };
    }

    private void initListener() {
        rgIsHidden.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yes:
                        isHidden = "???";
                        break;
                    case R.id.rb_no:
                        isHidden = "???";
                        break;
                }
            }
        });
        rgHandle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_mouth:
                        handleMethod = "??????????????????????????????";
                        break;
                    case R.id.rb_mouthUpload:
                        handleMethod = "???????????????????????????";
                        break;
                    case R.id.rb_writeUpload:
                        handleMethod = "???????????????????????????";
                        break;
                }
            }
        });
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

                            handleMethod = dataDetail.getHandlemode();
                            stakeId = dataDetail.getStakeid();
                            etDistance.setText(dataDetail.getStakefrom());
                            etName.setText(dataDetail.getProtectname());
                            etLocation.setText(dataDetail.getLocate());
                            etBuildingCompany.setText(dataDetail.getProtectunit());
                            etWeather.setText(dataDetail.getWeathers());
                            etProblem.setText(dataDetail.getCol1());
                            etAdvice.setText(dataDetail.getCol1handle());
                            if (dataDetail.getHandlemode().equals("???????????????????????????")) {
                                rbMouth.setChecked(true);
                            } else if (dataDetail.getHandlemode().equals("????????????????????????")) {
                                rbMouthUpload.setChecked(true);
                            } else {
                                rbWriteUpload.setChecked(true);
                            }
                            isHidden = dataDetail.getCol3();
                            if (dataDetail.getCol3().equals("???")) {
                                rbYes.setChecked(true);
                            } else {
                                rbNo.setChecked(true);
                            }
                            etManager.setText(dataDetail.getConstructionhandler());
                            etProcess.setText(dataDetail.getCol2());

                            //???????????????
                            if (model.getDataupload()!=null) {
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
                            //?????????
                            if (model.getDatapproval() != null) {
                                String approval = model.getDatapproval().getEmployid();
                                if (!TextUtils.isEmpty(approval) && approval.contains(":")) {
                                    tvSpr.setText(approval.split(":")[1]);
                                    approverId = approval.split(":")[0];
                                }
                                tvApproveStatus.setText(Util.getApprovalStatus(model.getDatapproval().getApprovalresult()));
                                if (!TextUtils.isEmpty(model.getDatapproval().getApprovalcomment())
                                        && (tag.equals("detail") || tag.equals("update"))
                                        && model.getDatapproval().getApprovalresult()!=3) {
                                    llApproveAdvice.setVisibility(View.VISIBLE);
                                    tvApproveAdvice.setText(model.getDatapproval().getApprovalcomment());
                                }
                                //??????????????????
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
            R.id.ll_chooseImages,
            R.id.btn_approve})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_chooseImages:
                initPermissions();
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
                if (tag.equals("update")) {
                    Intent intentAddress = new Intent(this, SelectFileActivity.class);
                    startActivityForResult(intentAddress, FILE_REQUEST_CODE);
                } else {
                    if (!TextUtils.isEmpty(filePath)) {
                        Uri uri = Uri.parse(filePath);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                }
                break;

        }
    }
    private boolean paramsComplete() {
        if (TextUtils.isEmpty(etWeather.getText().toString())) {
            ToastUtil.show("???????????????");
            return false;
        }
        if (TextUtils.isEmpty(etProblem.getText().toString())) {
            ToastUtil.show("??????????????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(etAdvice.getText().toString())) {
            ToastUtil.show("?????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(etManager.getText().toString())) {
            ToastUtil.show("????????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(etProcess.getText().toString())) {
            ToastUtil.show("?????????????????????");
            return false;
        }
        return true;
    }
    private void commit() {
        StringBuilder photoSb = new StringBuilder();
        if (nameList.size() > 0) {
            for (int i = 0; i < nameList.size(); i++) {
                if (i != nameList.size() - 1) {
                    photoSb.append(nameList.get(i) + ";");
                } else {
                    photoSb.append(nameList.get(i));
                }
            }
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("departmentid", 0);
        jsonObject.addProperty("id", formId);
        jsonObject.addProperty("stakeid",stakeId);
        jsonObject.addProperty("stakefrom", etDistance.getText().toString());
        jsonObject.addProperty("protectname", etName.getText().toString());
        jsonObject.addProperty("protectunit", etBuildingCompany.getText().toString());
        jsonObject.addProperty("locations", etLocation.getText().toString());
        jsonObject.addProperty("weathers", etWeather.getText().toString());
        jsonObject.addProperty("col1", etProblem.getText().toString());
        jsonObject.addProperty("col1handle", etAdvice.getText().toString());
        jsonObject.addProperty("handlemode", handleMethod);
        jsonObject.addProperty("constructionhandler", etManager.getText().toString());
        jsonObject.addProperty("col2", etProcess.getText().toString());
        jsonObject.addProperty("col3", isHidden);
        jsonObject.addProperty("creator", userId);
        jsonObject.addProperty("creatime", TimeUtil.getCurrentTime());
        jsonObject.addProperty("approvalid", approverId);
        if (!TextUtils.isEmpty(photoSb.toString())) {
            jsonObject.addProperty("picturepath", photoSb.toString());
        } else {
            jsonObject.addProperty("picturepath", "00");
        }
        if (!TextUtils.isEmpty(ossFilePath)) {
            jsonObject.addProperty("filepath", ossFilePath);
        } else {
            jsonObject.addProperty("filepath", "00");
        }
        Log.i("tag","jsonObject=="+jsonObject);
        Net.create(Api.class).updateWaterCheck(token, jsonObject)
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
    //????????????
    private void initPermissions() {
        if (ContextCompat.checkSelfPermission(ApproveWaterActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ApproveWaterActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(mContext, "?????? ??????-???????????? ????????????????????????????????????", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(ApproveWaterActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {
            GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(ApproveWaterActivity.this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(ApproveWaterActivity.this);
            } else {
                Log.i("tag", "????????????");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Util.activityList.remove(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == FILE_REQUEST_CODE) {
            selectFileName = data.getStringExtra("fileName");
            selectFilePath = data.getStringExtra("selectFilePath");
            tvFileName.setText(selectFileName);
            mWeiboDialog = WeiboDialogUtils.createLoadingDialog(this, "?????????...");
            mWeiboDialog.getWindow().setDimAmount(0f);
            uploadOffice("W015/" + userId + "_" + TimeUtil.getFileNameTime() + "_" + selectFileName, selectFilePath);
            //????????????
        }
    }

    //?????????????????????
    public void uploadFiles(String fileName, String filePath) {
        PutObjectRequest put = new PutObjectRequest(Constant.BUCKETSTRING, fileName, filePath);
        // ??????????????????????????????
        OSSAsyncTask ossAsyncTask = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                tvPhoto.setText("");
                nameList.add(fileName);
                if (nameList.size() == path.size()) {
                    WeiboDialogUtils.closeDialog(mWeiboDialog);
                }
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientException, ServiceException serviceException) {
                ToastUtil.show("?????????????????????");
                // ???????????????
                if (clientException != null) {
                    // ????????????????????????????????????
                }
                if (serviceException != null) {


                }
            }
        });

    }

    public void uploadOffice(String fileName, String filePath) {
        PutObjectRequest put = new PutObjectRequest(Constant.BUCKETSTRING, fileName, filePath);
        // ??????????????????????????????
        OSSAsyncTask ossAsyncTask = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                ossFilePath = fileName;
                WeiboDialogUtils.closeDialog(mWeiboDialog);
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientException, ServiceException serviceException) {
                ToastUtil.show("?????????????????????");
                // ???????????????
                if (clientException != null) {
                    // ????????????????????????????????????
                }
                if (serviceException != null) {


                }
            }
        });
    }
}
