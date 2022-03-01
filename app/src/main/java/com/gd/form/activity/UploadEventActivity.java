package com.gd.form.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.gd.form.model.EventDetailModel;
import com.gd.form.model.GlideImageLoader;
import com.gd.form.model.ServerModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.NumberUtil;
import com.gd.form.utils.SPUtil;
import com.gd.form.utils.TimeUtil;
import com.gd.form.utils.ToastUtil;
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

public class UploadEventActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_stationNo)
    TextView tvStationNo;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_pic)
    TextView tvPic;
    @BindView(R.id.et_eventName)
    EditText etEventName;
    @BindView(R.id.et_distance)
    EditText etDistance;
    @BindView(R.id.et_desc)
    EditText etDesc;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.ll_stationNo)
    LinearLayout llStationNo;
    @BindView(R.id.ll_address)
    LinearLayout llAddress;
    @BindView(R.id.ll_selectPic)
    LinearLayout llSelectPic;
    @BindView(R.id.rvResultPhoto)
    RecyclerView rvResultPhoto;
    private String token, userId, tag, formId;
    private int pipeId, stakeId;
    private int SELECT_STATION = 103;
    private int SELECT_AREA = 104;
    private List<String> nameList;
    private PhotoAdapter photoAdapter;
    private List<String> path;
    private OSSCredentialProvider ossCredentialProvider;
    private OSS oss;
    private Dialog mWeiboDialog;
    private int PERMISSIONS_REQUEST_READ_CONTACTS = 8;
    private IHandlerCallBack iHandlerCallBack;
    private GalleryConfig galleryConfig;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_upload_event;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("事件记录");
        if (getIntent() != null) {
            formId = getIntent().getStringExtra("formId");
            tag = getIntent().getStringExtra("tag");
            if ("update".equals(tag)) {
                tvRight.setVisibility(View.VISIBLE);
                tvRight.setText("添加记录");
            } else if ("detail".equals(tag)) {
                etDesc.setEnabled(false);
                etDistance.setEnabled(false);
                etEventName.setEnabled(false);
                llStationNo.setEnabled(false);
                llAddress.setEnabled(false);
                llSelectPic.setEnabled(false);
                btnCommit.setVisibility(View.GONE);
                tvRight.setVisibility(View.VISIBLE);
                tvRight.setText("历史记录");
            }
            getEventDetail(formId);
        }
        token = (String) SPUtil.get(UploadEventActivity.this, "token", "");
        userId = (String) SPUtil.get(UploadEventActivity.this, "userId", "");
        nameList = new ArrayList<>();
        path = new ArrayList<>();
        initGallery();
        initConfig();
        ossCredentialProvider = new OSSPlainTextAKSKCredentialProvider(Constant.ACCESSKEYID, Constant.ACCESSKEYSECRET);
        oss = new OSSClient(mContext.getApplicationContext(), Constant.ENDPOINT, ossCredentialProvider);
    }

    private void getEventDetail(String formId) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("eventid", formId);
        Log.i("tag", "jsonObject==" + jsonObject);
        Net.create(Api.class).getEventDetail(token, jsonObject)
                .enqueue(new NetCallback<EventDetailModel>(this, true) {
                    @Override
                    public void onResponse(EventDetailModel result) {
                        if (result != null) {
                            pipeId = result.getPipeid();
                            stakeId = result.getStakeid();
                            etEventName.setText(result.getName());
                            tvStationNo.setText(result.getStakename());
                            etDistance.setText(result.getDistance());
                            etDesc.setText(result.getEventdesc());
                            tvAddress.setText(result.getLocate());
                            if (!TextUtils.isEmpty(result.getPicturepath())) {
                                if (result.getPicturepath().equals("00")) {
                                    rvResultPhoto.setVisibility(View.GONE);
                                } else {
                                    rvResultPhoto.setVisibility(View.VISIBLE);
                                    String[] photoArr = result.getPicturepath().split(";");
                                    for (int i = 0; i < photoArr.length; i++) {
                                        path.add(photoArr[i]);
                                        photoAdapter.notifyDataSetChanged();
                                    }
                                }

                            }
                        }
                    }
                });
    }

    @OnClick({
            R.id.iv_back,
            R.id.ll_stationNo,
            R.id.ll_address,
            R.id.btn_commit,
            R.id.ll_selectPic,
            R.id.tv_right,
    })
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                Bundle bundle = new Bundle();
                bundle.putString("eventId", formId);
                if (tag.equals("update")) {
                    openActivity(AddEventRecordActivity.class, bundle);
                } else {
                    openActivity(EventHistoryActivity.class, bundle);
                }

                break;
            case R.id.ll_selectPic:
                initPermissions();
                break;
            case R.id.btn_commit:
                if (paramsComplete()) {
                    if (tag.equals("add")) {
                        commit();
                    } else if (tag.equals("update")) {
                        update();
                    }
                }
                break;
            case R.id.ll_address:
                Intent intentArea = new Intent(this, MapActivity.class);
                startActivityForResult(intentArea, SELECT_AREA);
                break;
            case R.id.ll_stationNo:
                Intent intentStartStation = new Intent(this, UploadEventStakeActivity.class);
                startActivityForResult(intentStartStation, SELECT_STATION);
                break;
        }
    }

    private void update() {
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
        jsonObject.addProperty("formid", formId);
        jsonObject.addProperty("name", etEventName.getText().toString());
        jsonObject.addProperty("pipeid", pipeId);
        jsonObject.addProperty("stakeid", stakeId);
        jsonObject.addProperty("distance", etDistance.getText().toString());
        jsonObject.addProperty("eventdesc", etDesc.getText().toString());
        if (!TextUtils.isEmpty(photoSb.toString())) {
            jsonObject.addProperty("picturepath", photoSb.toString());
        } else {
            jsonObject.addProperty("picturepath", "00");
        }
        jsonObject.addProperty("eventstatus", 0);
        jsonObject.addProperty("locate", tvAddress.getText().toString());
        jsonObject.addProperty("creator", userId);
        jsonObject.addProperty("creatime", TimeUtil.getCurrentTime());
        Log.i("tag", "jsonObject==" + jsonObject);
        Net.create(Api.class).UpdateUploadEvent(token, jsonObject)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel result) {
                        ToastUtil.show(result.getMsg());
                        if (result.getCode() == Constant.SUCCESS_CODE) {
                            finish();
                        }

                    }
                });


    }

    private boolean paramsComplete() {
        if (TextUtils.isEmpty(etEventName.getText().toString())) {
            ToastUtil.show("事件名称不能为空");
            return false;
        }
        if (TextUtils.isEmpty(tvStationNo.getText().toString())) {
            ToastUtil.show("桩号不能为空");
            return false;
        }

        if (TextUtils.isEmpty(etDistance.getText().toString())) {
            ToastUtil.show("距离不能为空");
            return false;
        }
        if (!NumberUtil.isNumber((etDistance.getText().toString()))) {
            ToastUtil.show("距离合适输入不正确");
            return false;
        }

        if (TextUtils.isEmpty(etDesc.getText().toString())) {
            ToastUtil.show("事件描述不能为空");
            return false;
        }
        if (TextUtils.isEmpty(tvAddress.getText().toString())) {
            ToastUtil.show("地址描述不能为空");
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
        jsonObject.addProperty("name", etEventName.getText().toString());
        jsonObject.addProperty("pipeid", pipeId);
        jsonObject.addProperty("stakeid", stakeId);
        jsonObject.addProperty("distance", etDistance.getText().toString());
        jsonObject.addProperty("eventdesc", etDesc.getText().toString());
        jsonObject.addProperty("picturepath", photoSb.toString());
        if (!TextUtils.isEmpty(photoSb.toString())) {
            jsonObject.addProperty("picturepath", photoSb.toString());
        } else {
            jsonObject.addProperty("picturepath", "00");
        }
        jsonObject.addProperty("eventstatus", 0);
        jsonObject.addProperty("locate", tvAddress.getText().toString());
        jsonObject.addProperty("creator", userId);
        jsonObject.addProperty("creatime", TimeUtil.getCurrentTime());
        Log.i("tag", "jsonObject==" + jsonObject);
        Net.create(Api.class).addUploadEvent(token, jsonObject)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel result) {
                        ToastUtil.show(result.getMsg());
                        if (result.getCode() == Constant.SUCCESS_CODE) {
                            finish();
                        }

                    }
                });


    }

    private void initGallery() {
        iHandlerCallBack = new IHandlerCallBack() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(List<String> photoList) {
                rvResultPhoto.setVisibility(View.VISIBLE);
                path.clear();
                for (String s : photoList) {
                    path.add(s);
                }
                photoAdapter.notifyDataSetChanged();
                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(UploadEventActivity.this, "加载中...");
                mWeiboDialog.getWindow().setDimAmount(0f);
                for (int i = 0; i < path.size(); i++) {
                    String suffix = path.get(i).substring(path.get(i).length() - 4);
                    uploadFiles("revent/" + userId + "_" + TimeUtil.getFileNameTime() + "_" + i + suffix, path.get(i));
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

    // 授权管理
    private void initPermissions() {
        if (ContextCompat.checkSelfPermission(UploadEventActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(UploadEventActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(mContext, "请在 设置-应用管理 中开启此应用的储存授权。", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(UploadEventActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {
            GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(UploadEventActivity.this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(UploadEventActivity.this);
            } else {
                Log.i("tag", "拒绝授权");
            }
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == SELECT_STATION) {
            stakeId = Integer.parseInt(data.getStringExtra("stationId"));
            pipeId = Integer.parseInt(data.getStringExtra("lineId"));
            String stationName = data.getStringExtra("stationName");
            tvStationNo.setText(stationName);
        } else if (requestCode == SELECT_AREA) {
            String area = data.getStringExtra("area");
            tvAddress.setText(area);
        }
    }
}
