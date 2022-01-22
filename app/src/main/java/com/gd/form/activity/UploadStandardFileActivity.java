package com.gd.form.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

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
import com.gd.form.base.BaseActivity;
import com.gd.form.constants.Constant;
import com.gd.form.model.ServerModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.SPUtil;
import com.gd.form.utils.TimeUtil;
import com.gd.form.utils.ToastUtil;
import com.gd.form.utils.WeiboDialogUtils;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class UploadStandardFileActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_fileName)
    TextView tvFileName;
    @BindView(R.id.et_fileName)
    EditText etFileName;
    private int FILE_REQUEST_CODE = 100;
    private String token, userId;
    private OSSCredentialProvider ossCredentialProvider;
    private OSS oss;
    private String selectFileName, selectFilePath,type;
    private String ossFilePath;
    private Dialog mWeiboDialog;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_upload_standard_file;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getIntent()!=null){
            type = getIntent().getStringExtra("type");
        }
        tvTitle.setText("文件上传");
        token = (String) SPUtil.get(UploadStandardFileActivity.this, "token", "");
        userId = (String) SPUtil.get(UploadStandardFileActivity.this, "userId", "");
        ossCredentialProvider = new OSSPlainTextAKSKCredentialProvider(Constant.ACCESSKEYID, Constant.ACCESSKEYSECRET);
        oss = new OSSClient(mContext.getApplicationContext(), Constant.ENDPOINT, ossCredentialProvider);
    }

    @OnClick({
            R.id.iv_back,
            R.id.btn_commit,
            R.id.ll_selectFile
    })
    public void click(View view) {
        switch (view.getId()) {
            case R.id.ll_selectFile:
                Intent intentFile = new Intent(UploadStandardFileActivity.this, SelectFileActivity.class);
                startActivityForResult(intentFile, FILE_REQUEST_CODE);
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_commit:
                addFile();
                break;
        }
    }

    private void addFile() {
        JsonObject params = new JsonObject();
        params.addProperty("filetype", type);
        params.addProperty("filename", etFileName.getText().toString());
        params.addProperty("uploadfile", ossFilePath);
        params.addProperty("creator", userId);
        params.addProperty("creatime", TimeUtil.getCurrentTimeYYmmdd());
        Net.create(Api.class).addFile(token, params)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel result) {
                        if (result.getCode() == Constant.SUCCESS_CODE) {
                            Intent intent = new Intent();
                            intent.setAction("com.action.addFile.success");
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
            etFileName.setText(selectFileName);
            mWeiboDialog = WeiboDialogUtils.createLoadingDialog(this, "加载中...");
            mWeiboDialog.getWindow().setDimAmount(0f);
            uploadOffice("StandardFile/" + selectFileName, selectFilePath);
        }
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
