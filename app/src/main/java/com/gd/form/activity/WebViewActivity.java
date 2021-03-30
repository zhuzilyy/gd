package com.gd.form.activity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

import com.gd.form.R;
import com.gd.form.base.BaseActivity;

import butterknife.BindView;

public class WebViewActivity extends BaseActivity {
    @BindView(R.id.web_view)
    WebView mWebView;
    private String fileUrl;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if(intent!=null){
            fileUrl = intent.getStringExtra("filePath");

        }
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("https://view.officeapps.live.com/op/view.aspx?src="+fileUrl);
    }

    @Override
    protected void setStatusBar() {

    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_webview;
    }
}
