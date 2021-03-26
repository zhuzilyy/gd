package com.gd.form.activity;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import com.gd.form.R;
import com.gd.form.base.BaseActivity;
import com.gd.form.model.Jobs;
import com.gd.form.model.LoginBean;
import com.gd.form.model.LoginBeanObj;
import com.gd.form.model.ResultMsg;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.net.Result;
import com.gd.form.utils.ToastUtil;
import com.gd.form.view.LoadView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.jaeger.library.StatusBarUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends BaseActivity {
    String uniqueId="";
    private LoadView loading;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageView(this, 0, null);
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_login;
    }

    @OnClick({
            R.id.bt_login,
    })
    public void onClick(View view){
        switch (view.getId()){
            case R.id.bt_login:

                Bundle bundle=new Bundle();
                //bundle.putString("msg",loginBeanObj.getRealName());
                openActivity(MainActivity.class, bundle, true);
                break;

        }
    }


}
