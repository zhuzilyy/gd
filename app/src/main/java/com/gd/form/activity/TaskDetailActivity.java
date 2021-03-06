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
import com.gd.form.model.GlideImageLoader;
import com.gd.form.model.ServerModel;
import com.gd.form.model.TaskDetailModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
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

public class TaskDetailActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_finisStatus)
    EditText etFinisStatus;
    @BindView(R.id.et_require)
    EditText etRequire;
    @BindView(R.id.tv_plantFinishTime)
    TextView tvPlantFinishTime;
    @BindView(R.id.tv_realFinishTime)
    TextView tvRealFinishTime;
    @BindView(R.id.tv_sendPerson)
    TextView tvSendPerson;
    @BindView(R.id.ll_realFinishTime)
    LinearLayout llRealFinishTime;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.view_finish_time)
    View viewFinishTime;
    @BindView(R.id.tv_fileName)
    TextView tvFileName;
    @BindView(R.id.rvResultPhoto)
    RecyclerView rvResultPhoto;
    @BindView(R.id.ll_selectImage)
    LinearLayout llSelectImage;
    @BindView(R.id.ll_scfj)
    LinearLayout llSelectFile;
    @BindView(R.id.tv_pic)
    TextView tvPic;
    private String taskId;
    private String token, userId;
    private String tag;
    private int FILE_REQUEST_CODE = 100;
    private int PERMISSIONS_REQUEST_READ_CONTACTS = 8;
    private String selectFileName;
    private String selectFilePath;
    private Dialog mWeiboDialog;
    private OSS oss;
    private OSSCredentialProvider ossCredentialProvider;
    private String ossFilePath;
    private List<String> path;
    private GalleryConfig galleryConfig;
    private PhotoAdapter photoAdapter;
    private List<String> nameList;
    private IHandlerCallBack iHandlerCallBack;
    private TaskDetailModel taskDetailModel;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_task_detail;

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("????????????");
        token = (String) SPUtil.get(this, "token", "");
        userId = (String) SPUtil.get(this, "userId", "");
        if (getIntent() != null) {
            taskId = getIntent().getExtras().getString("taskId");
            tag = getIntent().getExtras().getString("tag");
            if (tag.equals("unFinish")) {
                llRealFinishTime.setVisibility(View.GONE);
            } else {
                llRealFinishTime.setVisibility(View.VISIBLE);
                viewFinishTime.setVisibility(View.VISIBLE);
                llSelectImage.setEnabled(false);
                btnCommit.setVisibility(View.GONE);
                etFinisStatus.setEnabled(false);
            }
        }
        ossCredentialProvider = new OSSPlainTextAKSKCredentialProvider(Constant.ACCESSKEYID, Constant.ACCESSKEYSECRET);
        oss = new OSSClient(mContext.getApplicationContext(), Constant.ENDPOINT, ossCredentialProvider);
        path = new ArrayList<>();
        nameList = new ArrayList<>();
        initGallery();
        initConfig();
        getTaskDetail(taskId);
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
                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(TaskDetailActivity.this, "?????????...");
                mWeiboDialog.getWindow().setDimAmount(0f);
                for (int i = 0; i < path.size(); i++) {
                    String suffix = path.get(i).substring(path.get(i).length() - 4);
                    uploadFiles("worktask/" + userId + "_" + TimeUtil.getFileNameTime() + "_" + i + suffix, path.get(i));
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

    private void getTaskDetail(String taskId) {
        JsonObject params = new JsonObject();
        params.addProperty("id", taskId);
        Log.i("tag", "params===" + params);
        Net.create(Api.class).getTaskDetail(token, params)
                .enqueue(new NetCallback<TaskDetailModel>(this, true) {
                    @Override
                    public void onResponse(TaskDetailModel result) {
                        taskDetailModel = result;
                        if (result != null) {
                            etRequire.setText(result.getTaskcontent());
                            tvPlantFinishTime.setText(TimeUtil.longToFormatTime(result.getPlantime().getTime()));
                            if (result.getFinishtime() != null) {
                                etFinisStatus.setText(result.getFinishcontent());
                                tvRealFinishTime.setText(TimeUtil.longToFormatTime(result.getFinishtime().getTime()));
                            }
                            tvSendPerson.setText(result.getCreatorname());
                            if (result.getUploadpicture() != null && !tag.equals("unFinish")) {
                                if (!TextUtils.isEmpty(result.getUploadpicture()) &&
                                        !result.getUploadpicture().equals("00")) {
                                    if (result.getUploadpicture().contains(";")) {
                                        String[] pathArr = result.getUploadpicture().split(";");
                                        for (int i = 0; i < pathArr.length; i++) {
                                            path.add(pathArr[i]);
                                            photoAdapter.notifyDataSetChanged();
                                        }
                                    } else {
                                        path.add(result.getUploadpicture());
                                        photoAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    tvPic.setText("????????????");
                                }
                            }

                            if (result.getUploadfile() != null && !tag.equals("unFinish")) {
                                if (!TextUtils.isEmpty(result.getUploadfile()) &&
                                        !result.getUploadfile().equals("00")) {
                                    if (result.getFilename().contains("/")) {
                                        String subFileName = result.getFilename().split("/")[1];
                                        tvFileName.setText(subFileName);
                                    } else {
                                        tvFileName.setText(result.getFilename());
                                    }
                                } else {
                                    tvFileName.setText("????????????");
                                }
                            }
                        }
                    }
                });
    }

    @OnClick({
            R.id.iv_back,
            R.id.btn_commit,
            R.id.ll_scfj,
            R.id.ll_selectImage,
    })
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_scfj:
                if (tag.equals("unFinish")) {
                    Intent intentAddress = new Intent(this, SelectFileActivity.class);
                    startActivityForResult(intentAddress, FILE_REQUEST_CODE);
                } else {
                    if (!TextUtils.isEmpty(taskDetailModel.getUploadfile()) &&
                            !taskDetailModel.getUploadfile().equals("00")) {
                        Uri uri = Uri.parse(taskDetailModel.getUploadfile());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                }

                break;
            case R.id.ll_selectImage:
                initPermissions();
                break;
            case R.id.btn_commit:
                if (TextUtils.isEmpty(etFinisStatus.getText().toString())) {
                    ToastUtil.show("?????????????????????");
                    return;
                }
                commit();
                break;
        }
    }

    //????????????
    private void initPermissions() {
        if (ContextCompat.checkSelfPermission(TaskDetailActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(TaskDetailActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(mContext, "?????? ??????-???????????? ????????????????????????????????????", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(TaskDetailActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {
            GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(TaskDetailActivity.this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(TaskDetailActivity.this);
            } else {
                Log.i("tag", "????????????");
            }
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
        JsonObject params = new JsonObject();
        params.addProperty("id", Integer.parseInt(taskId));
        params.addProperty("finishcontent", etFinisStatus.getText().toString());
        params.addProperty("finishtime", TimeUtil.getCurrentTimeYYmmdd());
        if (!TextUtils.isEmpty(photoSb.toString())) {
            params.addProperty("uploadpicture", photoSb.toString());
        } else {
            params.addProperty("uploadpicture", "00");
        }
        if (!TextUtils.isEmpty(ossFilePath)) {
            params.addProperty("uploadfile", ossFilePath);
        } else {
            params.addProperty("uploadfile", "00");
        }
        Net.create(Api.class).finishTask(token, params)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel result) {
                        if (result.getCode() == Constant.SUCCESS_CODE) {
                            ToastUtil.show("??????????????????");
                            Intent intent = new Intent();
                            intent.setAction("com.action.update.task");
                            sendBroadcast(intent);
                            finish();
                        }
                    }
                });
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
            uploadOffice("worktask/" + userId + "_" + TimeUtil.getFileNameTime() + "_" + selectFileName, selectFilePath);
        }

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

    //?????????????????????
    public void uploadFiles(String fileName, String filePath) {
        PutObjectRequest put = new PutObjectRequest(Constant.BUCKETSTRING, fileName, filePath);
        // ??????????????????????????????
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
