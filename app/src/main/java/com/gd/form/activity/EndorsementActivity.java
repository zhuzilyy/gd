package com.gd.form.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.gd.form.model.BuildingModel;
import com.gd.form.model.DepartmentPerson;
import com.gd.form.model.GlideImageLoader;
import com.gd.form.model.SearchBuildingModel;
import com.gd.form.model.ServerModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.ContentUriUtil;
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
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class EndorsementActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_stationNo)
    TextView tvStationNo;
    @BindView(R.id.tv_fileName)
    TextView tv_fileName;
    @BindView(R.id.tv_location)
    TextView tv_location;
    @BindView(R.id.tv_spr)
    TextView tv_spr;
    @BindView(R.id.rvResultPhoto)
    RecyclerView rvResultPhoto;
    @BindView(R.id.et_des)
    EditText etDes;
    @BindView(R.id.et_record)
    EditText etRecord;
    @BindView(R.id.ll_location)
    LinearLayout llLocation;
    private int FILE_REQUEST_CODE = 100;
    private int SELECT_STATION = 101;
    private int SELECT_ADDRESS = 102;
    private int SELECT_APPROVER = 103;
    private int SELECT_BUILDING = 104;
    private int PERMISSIONS_REQUEST_READ_CONTACTS = 8;
    private String approverName;
    private String approverId;
    private Dialog mWeiboDialog;
    private OSSCredentialProvider ossCredentialProvider;
    private OSS oss;
    private String token, userId;
    private String ossFilePath;
    private String selectFileName;
    private String selectFilePath;
    private String stationId, pipeId, location, buildId, stakeId;
    private String isProtection = "是";
    private IHandlerCallBack iHandlerCallBack;
    private List<String> path;
    private GalleryConfig galleryConfig;
    private PhotoAdapter photoAdapter;
    private List<String> nameList;
    private ListDialog dialog;
    private String formId;
    private boolean selectInit = true;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_endorsement;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("现有违章违建记录");
        path = new ArrayList<>();
        nameList = new ArrayList<>();
        llLocation.setVisibility(View.GONE);
        initGallery();
        initConfig();
        token = (String) SPUtil.get(this, "token", "");
        userId = (String) SPUtil.get(this, "userId", "");
        if (getIntent().getExtras() != null) {
            formId = getIntent().getExtras().getString("formId");
            if (!TextUtils.isEmpty(formId)) {
                getBuildingData(formId);
            }
        }
        ossCredentialProvider = new OSSPlainTextAKSKCredentialProvider(Constant.ACCESSKEYID, Constant.ACCESSKEYSECRET);
        oss = new OSSClient(mContext.getApplicationContext(), Constant.ENDPOINT, ossCredentialProvider);
        getDefaultManager();
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
                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(EndorsementActivity.this, "加载中...");
                mWeiboDialog.getWindow().setDimAmount(0f);
                for (int i = 0; i < path.size(); i++) {
                    String suffix = path.get(i).substring(path.get(i).length() - 4);
                    uploadFiles("W005/" + userId + "_" + TimeUtil.getFileNameTime() + "_" + i + suffix, path.get(i));
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

    @OnClick({R.id.iv_back,
            R.id.ll_stationNo,
            R.id.ll_selectPic,
            R.id.ll_location,
            R.id.ll_scfj,
            R.id.btn_commit,
            R.id.ll_spr,
    })
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_commit:
                if (paramsComplete()) {
                    commit();
                }
                break;
            case R.id.ll_scfj:
//                Intent intentAddress = new Intent(EndorsementActivity.this, SelectFileActivity.class);
//                startActivityForResult(intentAddress, FILE_REQUEST_CODE);
//                Intent intentAddress = new Intent(Intent.ACTION_GET_CONTENT);
//                intentAddress.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
//                intentAddress.addCategory(Intent.CATEGORY_OPENABLE);
//                startActivityForResult(intentAddress,FILE_REQUEST_CODE);
                getPermission();
                break;
            case R.id.ll_spr:
                selectInit = false;
                getDefaultManager();
                break;
            case R.id.ll_location:
                Intent intent = new Intent(this, MapActivity.class);
                startActivityForResult(intent, SELECT_ADDRESS);
                break;
            case R.id.ll_selectPic:
                initPermissions();
                break;
            case R.id.ll_stationNo:
                getBuildings();
//                if (resultSearchPipeModel.getLlegalCount() > 0) {
//                    bundle.putSerializable("buildings", (Serializable) resultSearchPipeModel.getLlegaList());
//                }
//                openActivity(BuildingListActivity.class, bundle);
//                Intent intentStation = new Intent(this, StationActivity.class);
//                startActivityForResult(intentStation, SELECT_STATION);
                break;
        }
    }
    private void getPermission(){
        AndPermission
                .with(this)
                .permission(Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE)
                .onDenied(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        //拒绝权限
                        ToastUtil.show("请赋予必要权限");
                    }
                }).onGranted(new Action() {
            @Override
            public void onAction(List<String> permissions) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent,FILE_REQUEST_CODE);
            }
        }).start();
    }

    private void getBuildings() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("empid", userId);
        Log.i("tag", "jsonObject===" + jsonObject);
        Net.create(Api.class).getBuildings(token, jsonObject)
                .enqueue(new NetCallback<List<SearchBuildingModel>>(this, true) {
                    @Override
                    public void onResponse(List<SearchBuildingModel> list) {
                        if (list != null && list.size() > 0) {
                            List<SearchBuildingModel> searchBuildingModelList = new ArrayList<>();
                            for (int i = 0; i < list.size(); i++) {
                                SearchBuildingModel searchBuildingModel = list.get(i);
                                searchBuildingModel.setSelect("select");
                                searchBuildingModelList.add(searchBuildingModel);
                            }
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("buildings", (Serializable) searchBuildingModelList);
                            Intent intent = new Intent(EndorsementActivity.this, BuildingListActivity.class);
                            intent.putExtras(bundle);
                            startActivityForResult(intent, SELECT_BUILDING);
                        } else {
                            ToastUtil.show("暂无数据");
                        }
                    }
                });
    }


    private void getBuildingData(String id) {
        JsonObject params = new JsonObject();
        params.addProperty("id", id);
        Log.i("tag", "params====" + params);
        Net.create(Api.class).getBuildingData(token, params)
                .enqueue(new NetCallback<List<BuildingModel>>(this, true) {
                    @Override
                    public void onResponse(List<BuildingModel> result) {
                        if (result != null && result.size() > 0) {
                            BuildingModel buildingModel = result.get(0);
                            tvStationNo.setText(buildingModel.getOvername());
                            etDes.setText(buildingModel.getDangerdesc());
                            buildId = buildingModel.getId() + "";
                            stakeId = buildingModel.getStakeid() + "";
                        }
                    }
                });
    }

    private boolean paramsComplete() {
        if (TextUtils.isEmpty(tvStationNo.getText().toString())) {
            ToastUtil.show("请选择桩号");
            return false;
        }
        if (TextUtils.isEmpty(etDes.getText().toString())) {
            ToastUtil.show("请输入问题描述");
            return false;
        }
        if (TextUtils.isEmpty(etRecord.getText().toString())) {
            ToastUtil.show("请输入处理记录");
            return false;
        }
//        if (TextUtils.isEmpty(tv_location.getText().toString())) {
//            ToastUtil.show("请选择坐标");
//            return false;
//        }
        if (TextUtils.isEmpty(tv_spr.getText().toString())) {
            ToastUtil.show("请选择审批人");
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
        jsonObject.addProperty("pipeid", Integer.valueOf(buildId));
        jsonObject.addProperty("stakeid", Integer.valueOf(stakeId));
        jsonObject.addProperty("conditiondesc", etDes.getText().toString());
        jsonObject.addProperty("solutionrecord", etRecord.getText().toString());
        jsonObject.addProperty("locate", "00");
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
        Log.i("tag", "jsonObject===" + jsonObject);
        Net.create(Api.class).commitIllegalBuilding(token, jsonObject)
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
        if (ContextCompat.checkSelfPermission(EndorsementActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(EndorsementActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(mContext, "请在 设置-应用管理 中开启此应用的储存授权。", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(EndorsementActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {
            GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(EndorsementActivity.this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(EndorsementActivity.this);
            } else {
                Log.i("tag", "拒绝授权");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if (null != uri) {
                selectFilePath = ContentUriUtil.getPath(this, uri);
                String[] splitPath = selectFilePath.split("/");
                selectFileName = splitPath[splitPath.length-1];
                tv_fileName.setText(selectFileName);
                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(this, "加载中...");
                mWeiboDialog.getWindow().setDimAmount(0f);
                uploadOffice("W005/" + userId + "_" + TimeUtil.getFileNameTime() + "_" + selectFileName, selectFilePath);
            }

            //选择桩号
        } else if (requestCode == SELECT_STATION) {
            stationId = data.getStringExtra("stationId");
            pipeId = data.getStringExtra("pipeId");
            String stationName = data.getStringExtra("stationName");
            tvStationNo.setText(stationName);
        } else if (requestCode == SELECT_ADDRESS) {
            String latitude = data.getStringExtra("latitude");
            String longitude = data.getStringExtra("longitude");
            location = longitude + "," + latitude;
            if (!TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude)) {
                tv_location.setText("经度:" + longitude + "   纬度:" + latitude);
            }
        } else if (requestCode == SELECT_APPROVER) {
            approverName = data.getStringExtra("name");
            approverId = data.getStringExtra("id");
            if (!TextUtils.isEmpty(approverName)) {
                tv_spr.setText(approverName);
            }
        } else if (requestCode == SELECT_BUILDING) {
            String illegalName = data.getStringExtra("name");
            buildId = data.getStringExtra("buildId");
            stakeId = data.getStringExtra("stakeId");
            tvStationNo.setText(illegalName);
            getBuildingDesc(buildId);
        }

    }

    private void getDefaultManager() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("empid", userId);
        Net.create(Api.class).getTunnelDefaultManager(token, jsonObject)
                .enqueue(new NetCallback<List<DepartmentPerson>>(this, true) {
                    @Override
                    public void onResponse(List<DepartmentPerson> list) {
                        if(list.size()>0){
                            List<String> nameList = new ArrayList<>();
                            List<String> idList = new ArrayList<>();
                            if (list != null && list.size() > 0) {
                                for (int i = 0; i < list.size(); i++) {
                                    DepartmentPerson departmentPerson = list.get(i);
                                    nameList.add(departmentPerson.getName());
                                    idList.add(departmentPerson.getId());
                                }
                                if (selectInit) {
                                    tv_spr.setText(nameList.get(0));
                                    approverId = idList.get(0);
                                } else {
                                    if (dialog == null) {
                                        dialog = new ListDialog(mContext);
                                    }
                                    dialog.setData(nameList);
                                    dialog.show();
                                    dialog.setListItemClick(positionM -> {
                                        tv_spr.setText(nameList.get(positionM));
                                        approverId = idList.get(positionM);
                                        dialog.dismiss();
                                    });
                                }

                            }
                        }else{
                            ToastUtil.show("暂无审批人");
                        }
                    }
                });
    }

    private void getBuildingDesc(String stationId) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", stationId);
        Net.create(Api.class).getBuildingDees(token, jsonObject)
                .enqueue(new NetCallback<BuildingModel>(this, true) {
                    @Override
                    public void onResponse(BuildingModel buildingModel) {
                        etDes.setText(buildingModel.getDangerdesc());
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
