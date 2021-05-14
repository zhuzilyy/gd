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
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.MarkerOptions;
import com.bumptech.glide.Glide;
import com.gd.form.R;
import com.gd.form.adapter.PhotoAdapter;
import com.gd.form.base.BaseActivity;
import com.gd.form.constants.Constant;
import com.gd.form.model.GlideImageLoader;
import com.gd.form.model.ServerModel;
import com.gd.form.model.VideoDetail;
import com.gd.form.model.VideoDetailModel;
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

public class ApproveVideoActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_des)
    EditText etDes;
    @BindView(R.id.et_process)
    EditText etProcess;
    @BindView(R.id.ll_location)
    LinearLayout llLocation;
    @BindView(R.id.ll_selectImages)
    LinearLayout llSelectImages;
    @BindView(R.id.ll_file)
    LinearLayout llFile;
    @BindView(R.id.tv_imageName)
    TextView tvImageName;
    @BindView(R.id.tv_tip)
    TextView tvTip;
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
    @BindView(R.id.ll_approveStatus)
    LinearLayout llApproveStatus;
    @BindView(R.id.ll_chooseImages)
    LinearLayout llChooseImages;

    @BindView(R.id.rg_isThird)
    RadioGroup rgIsThird;
    @BindView(R.id.rb_yes)
    RadioButton rbYes;
    @BindView(R.id.rb_no)
    RadioButton rbNo;

    @BindView(R.id.rg_isMachine)
    RadioGroup rgIsMachine;
    @BindView(R.id.rb_yesMachine)
    RadioButton rbYesMachine;
    @BindView(R.id.rb_noMachine)
    RadioButton rbNoMachine;

    @BindView(R.id.rg_isComplete)
    RadioGroup rgIsComplete;
    @BindView(R.id.rb_yesComplete)
    RadioButton rbYesComplete;
    @BindView(R.id.rb_noComplete)
    RadioButton rbNoComplete;

    @BindView(R.id.rg_isCover)
    RadioGroup rgIsCover;
    @BindView(R.id.rb_yesCover)
    RadioButton rbYesCover;
    @BindView(R.id.rb_noCover)
    RadioButton rbNoCover;

    @BindView(R.id.rg_isBuilding)
    RadioGroup rgIsBuilding;
    @BindView(R.id.rb_yesBuild)
    RadioButton rbYesBuild;
    @BindView(R.id.rb_noBuild)
    RadioButton rbNoBuild;

    @BindView(R.id.rg_isClear)
    RadioGroup rgIsClear;
    @BindView(R.id.rb_yesClear)
    RadioButton rbYesClear;
    @BindView(R.id.rb_noClear)
    RadioButton rbNoClear;

    @BindView(R.id.rg_isNormal)
    RadioGroup rgIsNormal;
    @BindView(R.id.rb_yesNormal)
    RadioButton rbYesNormal;
    @BindView(R.id.rb_noNormal)
    RadioButton rbNoNormal;

    @BindView(R.id.rg_isOther)
    RadioGroup rgIsOther;
    @BindView(R.id.rb_yesOther)
    RadioButton rbYesOther;
    @BindView(R.id.rb_noOther)
    RadioButton rbNoOther;


    private String formId;
    private String token, userId;
    private MarkerOptions markerOption;
    private AMap aMap;
    private PhotoAdapter photoAdapter;
    private List<String> path;
    private String filePath;
    private String col1, col2, col3, col4, col5, col6, col7, col8;
    private String tag,approverId;
    private int PERMISSIONS_REQUEST_READ_CONTACTS = 8;
    private IHandlerCallBack iHandlerCallBack;
    private GalleryConfig galleryConfig;
    private List<String> nameList;
    private Dialog mWeiboDialog;
    private OSSCredentialProvider ossCredentialProvider;
    private OSS oss;
    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_approve_video;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.activityList.add(this);
        tvTitle.setText("视频监控查看记录");
        // 此方法必须重写
        mapView.onCreate(savedInstanceState);
        initMap();
        token = (String) SPUtil.get(ApproveVideoActivity.this, "token", "");
        userId = (String) SPUtil.get(ApproveVideoActivity.this, "userId", "");
        ossCredentialProvider = new OSSPlainTextAKSKCredentialProvider(Constant.ACCESSKEYID, Constant.ACCESSKEYSECRET);
        oss = new OSSClient(mContext.getApplicationContext(), Constant.ENDPOINT, ossCredentialProvider);
        llFile.setVisibility(View.GONE);
        llLocation.setVisibility(View.GONE);
        tvTip.setVisibility(View.VISIBLE);
        tvImageName.setText("监控截图");
        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            tag = bundle.getString("tag");
            if (tag.equals("detail")) {
                btnApprove.setVisibility(View.GONE);
            } else if (tag.equals("update")) {
                btnApprove.setText("提交");
                ivApproveStatus.setVisibility(View.GONE);
                llApproveStatus.setVisibility(View.GONE);
            }else if(tag.equals("approve")){
                llApproveStatus.setVisibility(View.GONE);
                llApproveAdvice.setVisibility(View.GONE);
            }
            if(tag.equals("detail") || tag.equals("approve")){
                rbYes.setEnabled(false);
                rbNo.setEnabled(false);
                rbYesMachine.setEnabled(false);
                rbNoMachine.setEnabled(false);
                rbYesComplete.setEnabled(false);
                rbNoComplete.setEnabled(false);
                rbYesCover.setEnabled(false);
                rbNoCover.setEnabled(false);
                rbYesBuild.setEnabled(false);
                rbNoBuild.setEnabled(false);
                rbYesClear.setEnabled(false);
                rbNoClear.setEnabled(false);
                rbYesNormal.setEnabled(false);
                rbNoNormal.setEnabled(false);
                rbYesOther.setEnabled(false);
                rbNoOther.setEnabled(false);
                etDes.setEnabled(false);
                etProcess.setEnabled(false);
                llChooseImages.setEnabled(false);
            }else{
                rbYes.setEnabled(true);
                rbNo.setEnabled(true);
                rbYesMachine.setEnabled(true);
                rbNoMachine.setEnabled(true);
                rbYesComplete.setEnabled(true);
                rbNoComplete.setEnabled(true);
                rbYesCover.setEnabled(true);
                rbNoCover.setEnabled(true);
                rbYesBuild.setEnabled(true);
                rbNoBuild.setEnabled(true);
                rbYesClear.setEnabled(true);
                rbNoClear.setEnabled(true);
                rbYesNormal.setEnabled(true);
                rbNoNormal.setEnabled(true);
                rbYesOther.setEnabled(true);
                rbNoOther.setEnabled(true);
                etDes.setEnabled(true);
                etProcess.setEnabled(true);
                llChooseImages.setEnabled(true);
            }
            formId = bundle.getString("formId");
        }
        path = new ArrayList<>();
        nameList = new ArrayList<>();
        getDetail(formId);
        initGallery();
        initConfig();
        iniListener();
    }
    private void initConfig() {
        galleryConfig = new GalleryConfig.Builder()
                .imageLoader(new GlideImageLoader())    // ImageLoader 加载框架（必填）
                .iHandlerCallBack(iHandlerCallBack)     // 监听接口（必填）
                .provider("com.gd.form.fileprovider")   // provider(必填)
                .pathList(path)                         // 记录已选的图片
                .multiSelect(true)                      // 是否多选   默认：false
                .multiSelect(true, 9)                   // 配置是否多选的同时 配置多选数量   默认：false ， 9
                .maxSize(9)                             // 配置多选时 的多选数量。    默认：9
                .crop(false)                             // 快捷开启裁剪功能，仅当单选 或直接开启相机时有效
                .crop(false, 1, 1, 500, 500)             // 配置裁剪功能的参数，   默认裁剪比例 1:1
                .isShowCamera(true)                     // 是否现实相机按钮  默认：false
                .filePath("/Gallery/Pictures")// 图片存放路径
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
                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(ApproveVideoActivity.this, "加载中...");
                mWeiboDialog.getWindow().setDimAmount(0f);
                for (int i = 0; i < path.size(); i++) {
                    String suffix = path.get(i).substring(path.get(i).length() - 4);
                    uploadFiles("W010/" + userId + "_" + TimeUtil.getFileNameTime() + "_" + i + suffix, path.get(i));
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
    private void iniListener() {
        rgIsThird.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yes:
                        col1 = "是";
                        break;
                    case R.id.rb_no:
                        col1 = "否";
                        break;
                }
            }
        });


        rgIsMachine.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesMachine:
                        col2 = "是";
                        break;
                    case R.id.rb_noMachine:
                        col2 = "否";
                        break;
                }
            }
        });

        rgIsComplete.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesComplete:
                        col3 = "是";
                        break;
                    case R.id.rb_noComplete:
                        col3 = "否";
                        break;
                }
            }
        });

        rgIsCover.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesCover:
                        col4 = "是";
                        break;
                    case R.id.rb_noCover:
                        col4 = "否";
                        break;
                }
            }
        });

        rgIsBuilding.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesBuild:
                        col5 = "是";
                        break;
                    case R.id.rb_noBuild:
                        col5 = "否";
                        break;
                }
            }
        });

        rgIsClear.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesClear:
                        col6 = "是";
                        break;
                    case R.id.rb_noClear:
                        col6 = "否";
                        break;
                }
            }
        });

        rgIsNormal.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesNormal:
                        col7 = "是";
                        break;
                    case R.id.rb_noNormal:
                        col7 = "否";
                        break;
                }
            }
        });

        rgIsOther.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesOther:
                        col8 = "是";
                        break;
                    case R.id.rb_noOther:
                        col8 = "否";
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
        Log.i("tag", "params==" + params);
        Net.create(Api.class).getVideoDetail(token, params)
                .enqueue(new NetCallback<VideoDetailModel>(this, true) {
                    @Override
                    public void onResponse(VideoDetailModel model) {
                        if (model != null) {
                            VideoDetail dataDetail = model.getDatadetail();
                            col1 = dataDetail.getCol1();
                            col2 = dataDetail.getCol2();
                            col3 = dataDetail.getCol3();
                            col4 = dataDetail.getCol4();
                            col5 = dataDetail.getCol5();
                            col6 = dataDetail.getCol6();
                            col7 = dataDetail.getCol7();
                            col8 = dataDetail.getCol8();
                            if (dataDetail.getCol1().equals("是")) {
                                rbYes.setChecked(true);
                            } else {
                                rbNo.setChecked(true);
                            }

                            if (dataDetail.getCol2().equals("是")) {
                                rbYesMachine.setChecked(true);
                            } else {
                                rbNoMachine.setChecked(true);
                            }

                            if (dataDetail.getCol3().equals("是")) {
                                rbYesComplete.setChecked(true);
                            } else {
                                rbNoComplete.setChecked(true);
                            }

                            if (dataDetail.getCol4().equals("是")) {
                                rbYesCover.setChecked(true);
                            } else {
                                rbNoCover.setChecked(true);
                            }

                            if (dataDetail.getCol5().equals("是")) {
                                rbYesBuild.setChecked(true);
                            } else {
                                rbNoBuild.setChecked(true);
                            }

                            if (dataDetail.getCol6().equals("是")) {
                                rbYesClear.setChecked(true);
                            } else {
                                rbNoClear.setChecked(true);
                            }

                            if (dataDetail.getCol7().equals("是")) {
                                rbYesNormal.setChecked(true);
                            } else {
                                rbNoNormal.setChecked(true);
                            }

                            if (dataDetail.getCol8().equals("是")) {
                                rbYesOther.setChecked(true);
                            } else {
                                rbNoOther.setChecked(true);
                            }
                            etDes.setText(dataDetail.getExceptiondesc());
                            etProcess.setText(dataDetail.getDealprocess());
//                            String location = model.getDatadetail().getLocate();
//                            if (!TextUtils.isEmpty(location) && location.contains(",")) {
//                                String[] locationArr = location.split(",");
//                                LatLng latLng = new LatLng(Double.parseDouble(locationArr[1]), Double.parseDouble(locationArr[0]));
//                                markerOption = new MarkerOptions().icon(BitmapDescriptorFactory
//                                        .defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
//                                        .position(latLng)
//                                        .draggable(true);
//                                aMap.addMarker(markerOption);
//                                aMap.moveCamera(CameraUpdateFactory.newCameraPosition(
//                                        new CameraPosition(latLng, 11f, 0, 0)));
//                            }
//                            //上传的文件
//                            if (model.getDataupload() != null) {
//                                if ("00".equals(model.getDataupload().getFilepath())) {
//                                    tvFileName.setText("无");
//                                } else {
//                                    tvFileName.setText(model.getDataupload().getFilename());
//                                    filePath = model.getDataupload().getFilepath();
//                                }
//                            } else {
//                                tvFileName.setText("无");
//                            }

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
                                    approverId = approval.split(":")[0];
                                }
                                //审批状态，0-表示批复不同意，1-表示批复同意，3-表示未批复
                                tvApproveStatus.setText(Util.getApprovalStatus(model.getDatapproval().getApprovalresult()));
                                if (!TextUtils.isEmpty(model.getDatapproval().getApprovalcomment())
                                && (tag.equals("detail") || tag.equals("update"))) {
                                    llApproveAdvice.setVisibility(View.VISIBLE);
                                    tvApproveAdvice.setText(model.getDatapproval().getApprovalcomment());
                                }
                                //显示审批图片
                                if (!TextUtils.isEmpty(model.getDatapproval().getSignfilepath())) {
                                    Glide.with(ApproveVideoActivity.this).
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
        if (TextUtils.isEmpty(etDes.getText().toString())) {
            ToastUtil.show("请输入异常描述");
            return false;
        }
        if (TextUtils.isEmpty(etProcess.getText().toString())) {
            ToastUtil.show("请输入处置过程");
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
        jsonObject.addProperty("id", formId);
        jsonObject.addProperty("exceptiondesc", etDes.getText().toString());
        jsonObject.addProperty("dealprocess", etProcess.getText().toString());
        jsonObject.addProperty("col1", col1);
        jsonObject.addProperty("col2", col2);
        jsonObject.addProperty("col3", col3);
        jsonObject.addProperty("col4", col4);
        jsonObject.addProperty("col5", col5);
        jsonObject.addProperty("col6", col6);
        jsonObject.addProperty("col7", col7);
        jsonObject.addProperty("col8", col8);
        jsonObject.addProperty("creator", userId);
        jsonObject.addProperty("creatime", TimeUtil.getCurrentTime());
        jsonObject.addProperty("approvalid",approverId);
        if (!TextUtils.isEmpty(photoSb.toString())) {
            jsonObject.addProperty("picturepath", photoSb.toString());
        } else {
            jsonObject.addProperty("picturepath", "00");
        }
        jsonObject.addProperty("filepath", "00");
        Log.i("tag", "1111=" + jsonObject.toString());
        Net.create(Api.class).updateVideoRecord(token, jsonObject)
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
    // 授权管理
    private void initPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(mContext, "请在 设置-应用管理 中开启此应用的储存授权。", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {
            GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(this);
            } else {
                Log.i("tag", "拒绝授权");
            }
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
    //上传阿里云文件
    public void uploadFiles(String fileName, String filePath) {
        PutObjectRequest put = new PutObjectRequest(Constant.BUCKETSTRING, fileName, filePath);
        // 此处调用异步上传方法
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
                ToastUtil.show("上传失败请重试");
                // 请求异常。
                if (clientException != null) {
                    // 本地异常，如网络异常等。
                }
                if (serviceException != null) {


                }
            }
        });

    }

}
