package com.gd.form.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
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
import com.gd.form.utils.Util;
import com.gd.form.utils.WeiboDialogUtils;
import com.gd.form.view.SignatureView;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class SignActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.view_signature)
    SignatureView signatureView;
    private OSS oss;
    private OSSCredentialProvider ossCredentialProvider;
    private Dialog mWeiboDialog;
    private String formId, advice;
    private String token, userId;
    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_sign;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("签字");
        token = (String) SPUtil.get(this, "token", "");
        userId = (String) SPUtil.get(this, "userId", "");
        ossCredentialProvider = new OSSPlainTextAKSKCredentialProvider(Constant.ACCESSKEYID, Constant.ACCESSKEYSECRET);
        oss = new OSSClient(mContext.getApplicationContext(), Constant.ENDPOINT, ossCredentialProvider);
        if (getIntent() != null) {
            formId = getIntent().getExtras().getString("formId");
            advice = getIntent().getExtras().getString("advice");
        }
    }

    @OnClick({
            R.id.iv_back,
            R.id.btn_confirm,
            R.id.btn_clear,
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_confirm:
                if(!signatureView.getTouched()){
                    ToastUtil.show("请先签字");
                    return;
                }
                Bitmap bitmap = signatureView.getSignBitmap(true, 10);
                byte[] bitmapByte = Util.getBitmapByte(bitmap);
                //首先上传图片
                String fileName = userId + "_" + TimeUtil.getFileNameTime();
                uploadFiles(fileName, bitmapByte);
                break;
            case R.id.btn_clear:
                signatureView.clear();
                break;
        }
    }

    //上传阿里云文件
    public void uploadFiles(String fileName, byte[] bitmapByte) {

        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(SignActivity.this, "加载中...");
        mWeiboDialog.getWindow().setDimAmount(0f);

        PutObjectRequest put = new PutObjectRequest(Constant.BUCKETSTRING, fileName, bitmapByte);
        try {
            PutObjectResult putResult = oss.putObject(put);
            WeiboDialogUtils.closeDialog(mWeiboDialog);
            approve(fileName);
            Log.d("PutObject", "UploadSuccess");
            Log.d("ETag", putResult.getETag());
            Log.d("RequestId", putResult.getRequestId());
        } catch (ClientException e) {
            // 本地异常，如网络异常等。
            WeiboDialogUtils.closeDialog(mWeiboDialog);
            e.printStackTrace();
        } catch (ServiceException e) {
            // 服务异常。
            WeiboDialogUtils.closeDialog(mWeiboDialog);
            Log.e("RequestId", e.getRequestId());
            Log.e("ErrorCode", e.getErrorCode());
            Log.e("HostId", e.getHostId());
            Log.e("RawMessage", e.getRawMessage());
        }
    }

    private void approve(String fileName) {
        JsonObject params = new JsonObject();
        params.addProperty("formid", formId);
        params.addProperty("approvalresult", 1);
        params.addProperty("approvalcomment", advice);
        params.addProperty("signfilepath", fileName);
        params.addProperty("creatime", TimeUtil.longToFormatTimeHMS(System.currentTimeMillis()));
        Log.i("tag","params=="+params);
        Net.create(Api.class).approve(token, params)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel result) {
                        Log.i("tag","==="+result.getCode());
                        if (result.getCode() == Constant.SUCCESS_CODE) {
                            Intent intent = new Intent();
                            intent.setAction("com.action.updateApprove");
                            sendBroadcast(intent);
                            Util.finishAll();
                            finish();
                        }
                    }
                });
    }
}
