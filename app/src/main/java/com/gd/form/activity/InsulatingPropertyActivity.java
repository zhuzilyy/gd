package com.gd.form.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.gd.form.R;
import com.gd.form.adapter.PhotoAdapter;
import com.gd.form.base.BaseActivity;
import com.gd.form.constants.Constant;
import com.gd.form.model.Department;
import com.gd.form.model.GlideImageLoader;
import com.gd.form.model.Pipelineinfo;
import com.gd.form.model.Pipemploys;
import com.gd.form.model.ServerModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.NumberUtil;
import com.gd.form.utils.SPUtil;
import com.gd.form.utils.TimeUtil;
import com.gd.form.utils.ToastUtil;
import com.gd.form.utils.WeiboDialogUtils;
import com.gd.form.view.ListDialog;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;
import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;
import com.yancy.gallerypick.inter.IHandlerCallBack;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class InsulatingPropertyActivity extends BaseActivity {
    private List<Department> departmentList;
    private ListDialog dialog;
    private int FILE_REQUEST_CODE = 100;
    private int SELECT_APPROVER = 102;
    private int PERMISSIONS_REQUEST_READ_CONTACTS = 8;
    private int SELECT_ADDRESS = 103;
    private int SELECT_STATION_NAME = 104;
    private String approverName;
    private String approverId;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.tv_spr)
    TextView tvSpr;
    @BindView(R.id.tv_fileName)
    TextView tvFileName;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.et_no)
    EditText etNo;
    @BindView(R.id.et_room)
    EditText etRoom;
    @BindView(R.id.et_lineElectricity)
    EditText etLineElectricity;
    @BindView(R.id.et_distraction_electricity)
    EditText etDistractionElectricity;
    @BindView(R.id.et_position)
    EditText etPosition;
    @BindView(R.id.et_ground)
    EditText etGround;
    @BindView(R.id.et_blank_position)
    EditText etBlankPosition;
    @BindView(R.id.et_lightning_protection)
    EditText etLightningProtection;
    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.tv_pipeName)
    TextView tvPipeName;
    @BindView(R.id.tv_stationName)
    TextView tvStationName;
    @BindView(R.id.rg_isProperty)
    RadioGroup rg_isProperty;
    @BindView(R.id.rg_isBury)
    RadioGroup rg_isBury;
    @BindView(R.id.rvResultPhoto)
    RecyclerView rvResultPhoto;
    @BindView(R.id.ll_scfj)
    LinearLayout llSelectFile;
    @BindView(R.id.view_file)
    View viewFile;
    private List<Pipelineinfo> pipeLineinfoList;
    private Dialog mWeiboDialog;
    private OSSCredentialProvider ossCredentialProvider;
    private OSS oss;
    private String token, userId;
    private String ossFilePath;
    private String selectFileName;
    private String selectFilePath;
    private String location;
    private IHandlerCallBack iHandlerCallBack;
    private List<String> path;
    private GalleryConfig galleryConfig;
    private PhotoAdapter photoAdapter;
    private List<String> nameList;
    private int departmentId, pipeId;
    private String col6 = "正常";
    private String col8 = "正常";

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_insulating_property;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("绝缘件性能测试");
        dialog = new ListDialog(this);
        pipeLineinfoList = new ArrayList<>();
        path = new ArrayList<>();
        nameList = new ArrayList<>();
        dialog = new ListDialog(this);
        llSelectFile.setVisibility(View.GONE);
        viewFile.setVisibility(View.GONE);
        token = (String) SPUtil.get(this, "token", "");
        userId = (String) SPUtil.get(this, "userId", "");
        ossCredentialProvider = new OSSPlainTextAKSKCredentialProvider(Constant.ACCESSKEYID, Constant.ACCESSKEYSECRET);
        oss = new OSSClient(mContext.getApplicationContext(), Constant.ENDPOINT, ossCredentialProvider);
        pipeDepartmentInfoGetList();
        initListener();
        getPipelineInfoListRequest();
        initGallery();
        initConfig();
    }
    private void getPipelineInfoListRequest() {

        Net.create(Api.class).pipelineinfosget(token)
                .enqueue(new NetCallback<List<Pipelineinfo>>(this, false) {
                    @Override
                    public void onResponse(List<Pipelineinfo> list) {
                        pipeLineinfoList = list;
                    }
                });
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
                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(InsulatingPropertyActivity.this, "加载中...");
                mWeiboDialog.getWindow().setDimAmount(0f);
                for (int i = 0; i < path.size(); i++) {
                    String suffix = path.get(i).substring(path.get(i).length() - 4);
                    uploadFiles(userId + "_" + TimeUtil.getFileNameTime() + "_" + i + suffix, path.get(i));
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
        rg_isProperty.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesProperty:
                        col6 = "正常";
                        break;
                    case R.id.rb_noProperty:
                        col6 = "问题";
                        break;
                }
            }
        });
        rg_isBury.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesBury:
                        col8 = "正常";
                        break;
                    case R.id.rb_noBury:
                        col8 = "问题";
                        break;
                }
            }
        });
    }

    private void pipeDepartmentInfoGetList() {
        Net.create(Api.class).pipedepartmentinfoGetList(token)
                .enqueue(new NetCallback<List<Department>>(this, false) {
                    @Override
                    public void onResponse(List<Department> list) {
                        departmentList = list;
                    }
                });
    }

    @OnClick({R.id.iv_back,
            R.id.ll_area,
            R.id.ll_location,
            R.id.ll_scfj,
            R.id.btn_commit,
            R.id.ll_selectPic,
            R.id.ll_pipeName,
            R.id.ll_stationName,
    })
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_stationName:
                getStations();
                break;
            case R.id.ll_pipeName:
                List<String> pipeNameList = new ArrayList<>();
                List<Integer> pipeIdList = new ArrayList<>();
                if (pipeLineinfoList != null && pipeLineinfoList.size() > 0) {
                    for (int i = 0; i < pipeLineinfoList.size(); i++) {
                        pipeNameList.add(pipeLineinfoList.get(i).getName());
                        pipeIdList.add(pipeLineinfoList.get(i).getId());
                    }
                }
                dialog.setData(pipeNameList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvPipeName.setText(pipeNameList.get(positionM));
                    pipeId = pipeIdList.get(positionM);
                    dialog.dismiss();
                });
                break;
            case R.id.ll_selectPic:
                initPermissions();
                break;
            case R.id.btn_commit:
                if (paramsComplete()) {
                    commit();
                }
                break;
            case R.id.ll_area:
                List<String> areaList = new ArrayList<>();
                List<Integer> departmentIdList = new ArrayList<>();
                if (departmentList != null && departmentList.size() > 0) {
                    for (int i = 0; i < departmentList.size(); i++) {
                        areaList.add(departmentList.get(i).getName());
                        departmentIdList.add(departmentList.get(i).getId());
                    }
                }
                dialog.setData(areaList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvArea.setText(areaList.get(positionM));
                    departmentId = departmentIdList.get(positionM);
                    dialog.dismiss();
                });
                break;
            case R.id.ll_location:
                Intent intent = new Intent(this, MapActivity.class);
                startActivityForResult(intent, SELECT_ADDRESS);
                break;
            case R.id.ll_spr:
                Intent intentApprover = new Intent(this, ApproverActivity.class);
                startActivityForResult(intentApprover, SELECT_APPROVER);
                break;
            case R.id.ll_scfj:
                Intent intentAddress = new Intent(this, SelectFileActivity.class);
                startActivityForResult(intentAddress, FILE_REQUEST_CODE);
                break;
        }
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
        jsonObject.addProperty("departmentid", departmentId);
        jsonObject.addProperty("pipeid", pipeId);
        jsonObject.addProperty("stationdesc", tvStationName.getText().toString());
        jsonObject.addProperty("col1", etLineElectricity.getText().toString());
        jsonObject.addProperty("col2", etDistractionElectricity.getText().toString());
        jsonObject.addProperty("col3", etPosition.getText().toString());
        jsonObject.addProperty("col4", etGround.getText().toString());
        jsonObject.addProperty("col5", etBlankPosition.getText().toString());
        jsonObject.addProperty("col6", col6);
        jsonObject.addProperty("col7", etLightningProtection.getText().toString());
        jsonObject.addProperty("col8", col8);
        jsonObject.addProperty("remarks", etRemark.getText().toString());
        jsonObject.addProperty("locate", location);
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
        Net.create(Api.class).commitProperty(token, jsonObject)
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

    private void getStations() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("empid", userId);
        Net.create(Api.class).getStations(token, jsonObject)
                .enqueue(new NetCallback<List<String>>(this, true) {
                    @Override
                    public void onResponse(List<String> list) {
                        if (list != null && list.size() > 0) {
                            Intent intent = new Intent(InsulatingPropertyActivity.this, StationNameActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putStringArrayList("stations", (ArrayList<String>) list);
                            bundle.putString("tag", "selectStation");
                            intent.putExtras(bundle);
                            startActivityForResult(intent, SELECT_STATION_NAME);
                        } else {
                            ToastUtil.show("暂无数据");
                        }
                    }
                });
    }
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

    private boolean paramsComplete() {
        if (TextUtils.isEmpty(tvStationName.getText().toString())) {
            ToastUtil.show("请输入场站名称");
            return false;
        }
        if (TextUtils.isEmpty(etLineElectricity.getText().toString())) {
            ToastUtil.show("请输入管道电位");
            return false;
        }
        if (!NumberUtil.isNumber(etLineElectricity.getText().toString())) {
            ToastUtil.show("管道电位格式不正确");
            return false;
        }
        if (TextUtils.isEmpty(etDistractionElectricity.getText().toString())) {
            ToastUtil.show("请输入管道干扰电压");
            return false;
        }
        if (!NumberUtil.isNumber(etDistractionElectricity.getText().toString())) {
            ToastUtil.show("管道干扰电压格式不正确");
            return false;
        }
        if (TextUtils.isEmpty(etPosition.getText().toString())) {
            ToastUtil.show("请输入接地侧电位");
            return false;
        }
        if (!NumberUtil.isNumber(etPosition.getText().toString())) {
            ToastUtil.show("接地侧电位格式不正确");
            return false;
        }
        if (TextUtils.isEmpty(etGround.getText().toString())) {
            ToastUtil.show("请输入接地侧干扰电压");
            return false;
        }
        if (!NumberUtil.isNumber(etGround.getText().toString())) {
            ToastUtil.show("入接地侧干扰电压格式不正确");
            return false;
        }
        if (TextUtils.isEmpty(etBlankPosition.getText().toString())) {
            ToastUtil.show("请输入放空侧电位");
            return false;
        }
        if (!NumberUtil.isNumber(etBlankPosition.getText().toString())) {
            ToastUtil.show("放空侧电位格式不正确");
            return false;
        }
        if (TextUtils.isEmpty(etLightningProtection.getText().toString())) {
            ToastUtil.show("请输入管道避雷器电压");
            return false;
        }
        if (!NumberUtil.isNumber(etLightningProtection.getText().toString())) {
            ToastUtil.show("管道避雷器电压格式不正确");
            return false;
        }
        if (TextUtils.isEmpty(etRemark.getText().toString())) {
            ToastUtil.show("请输入备注");
            return false;
        }
        if (TextUtils.isEmpty(tvLocation.getText().toString())) {
            ToastUtil.show("请输入填报位置");
            return false;
        }
        if (TextUtils.isEmpty(tvSpr.getText().toString())) {
            ToastUtil.show("请选择审批人");
            return false;
        }
        return true;
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
            mWeiboDialog = WeiboDialogUtils.createLoadingDialog(this, "加载中...");
            mWeiboDialog.getWindow().setDimAmount(0f);
            uploadOffice(userId + "_" + TimeUtil.getFileNameTime() + "_" + selectFileName, selectFilePath);
        } else if (requestCode == SELECT_ADDRESS) {
            String latitude = data.getStringExtra("latitude");
            String longitude = data.getStringExtra("longitude");
            location = longitude + "," + latitude;
            if (!TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude)) {
                tvLocation.setText("经度:" + longitude + "   纬度:" + latitude);
            }
        } else if (requestCode == SELECT_APPROVER) {
            approverName = data.getStringExtra("name");
            approverId = data.getStringExtra("id");
            if (!TextUtils.isEmpty(approverName)) {
                tvSpr.setText(approverName);
            }
        } else if (requestCode == SELECT_STATION_NAME) {
            String content = data.getStringExtra("content");
            if (!TextUtils.isEmpty(content)) {
                String[] stationArr = content.split(":");
                tvStationName.setText(stationArr[0]);
            }
            getDefaultManager();
        }

    }
    private void getDefaultManager() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("empid", userId);
        Net.create(Api.class).getTunnelDefaultManager(token, jsonObject)
                .enqueue(new NetCallback<Pipemploys>(this, true) {
                    @Override
                    public void onResponse(Pipemploys pipemploys) {
                        approverId = pipemploys.getId();
                        tvSpr.setText(pipemploys.getName());
                    }
                });
    }
    //上传阿里云文件
    public void uploadFiles(String fileName, String filePath) {
        PutObjectRequest put = new PutObjectRequest(Constant.BUCKETSTRING, fileName, filePath);
        // 此处调用异步上传方法
        OSSAsyncTask ossAsyncTask = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
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

    public void uploadOffice(String fileName, String filePath) {
        PutObjectRequest put = new PutObjectRequest(Constant.BUCKETSTRING, fileName, filePath);
        // 此处调用异步上传方法
        OSSAsyncTask ossAsyncTask = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                ossFilePath = fileName;
                WeiboDialogUtils.closeDialog(mWeiboDialog);
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
