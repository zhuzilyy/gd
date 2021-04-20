package com.gd.form.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.gd.form.R;
import com.gd.form.base.BaseActivity;
import com.gd.form.view.SignatureView;
import com.jaeger.library.StatusBarUtil;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

public class SignActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.view_signature)
    SignatureView signatureView;

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
        tvTitle.setText("签名");
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
                String fileName = System.currentTimeMillis() + ".jpg";
                try {
                    signatureView.getSignBitmap("/sdcard/" + fileName, true, 10, SignActivity.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_clear:
                signatureView.clear();
                break;

        }
    }
}
