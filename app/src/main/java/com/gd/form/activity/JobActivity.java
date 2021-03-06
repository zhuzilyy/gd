package com.gd.form.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.gd.form.R;
import com.gd.form.base.BaseActivity;
import com.gd.form.model.ResultMsg;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.SPUtil;
import com.gd.form.utils.ToastUtil;
import com.gd.form.utils.Util;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class JobActivity extends BaseActivity {
    @BindView(R.id.et_job_name)
    EditText et_job_name;
    @BindView(R.id.et_job_code)
    EditText et_job_code;
    private String name;
    private int code;
    private String token, userId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        token = (String) SPUtil.get(this, "token", "");
        userId = (String) SPUtil.get(this, "userId", "");
        if(getIntent()!=null&&getIntent().getExtras()!=null){
            Bundle extra=getIntent().getExtras();
            name=extra.getString("name");
            code=extra.getInt("code");

        }
        if (!Util.isEmpty(name)){
            et_job_name.setText(name);
            et_job_code.setText(code+"");
        }
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext,R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_job;
    }

    @OnClick({
            R.id.iv_back,
            R.id.bt_confirm,

    })
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.bt_confirm:

                if (Util.isEmpty(et_job_name.getText().toString())){
                    ToastUtil.show("????????????????????????");
                    break;
                }
                if (Util.isEmpty(et_job_code.getText().toString())){
                    ToastUtil.show("??????????????????????????????");
                    break;
                }

                addRequest();
                break;

        }
    }


    private void addRequest(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", Integer.valueOf(et_job_code.getText().toString()));
        jsonObject.addProperty("name",et_job_name.getText().toString());
        Net.create(Api.class).professionalinfoAdd(token,jsonObject)
                .enqueue(new NetCallback<ResultMsg>(this,true) {
                    @Override
                    public void onResponse(ResultMsg resultMsg) {
                        if (resultMsg.getCode()==200){
                            if (!Util.isEmpty(name)){
                                ToastUtil.show("????????????");
                            }else {
                                ToastUtil.show("????????????");
                            }
                            finish();
                        }else {
                            if (!Util.isEmpty(name)){
                                ToastUtil.show("????????????");
                            }else {
                                ToastUtil.show("????????????");
                            }
                        }
                    }
                });
    }
}