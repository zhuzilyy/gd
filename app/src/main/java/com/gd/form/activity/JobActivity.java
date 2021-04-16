package com.gd.form.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.gd.form.R;
import com.gd.form.base.BaseActivity;
import com.gd.form.model.ResultMsg;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.ToastUtil;
import com.gd.form.utils.Util;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class JobActivity extends BaseActivity {
    @BindView(R.id.et_job_name)
    EditText et_job_name;
    @BindView(R.id.et_job_code)
    EditText et_job_code;
    private String name;
    private int code;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                    ToastUtil.show("岗位名称不能为空");
                    break;
                }
                if (Util.isEmpty(et_job_code.getText().toString())){
                    ToastUtil.show("岗位职称编号不能为空");
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
        Net.create(Api.class).professionalinfoAdd(jsonObject)
                .enqueue(new NetCallback<ResultMsg>(this,true) {
                    @Override
                    public void onResponse(ResultMsg resultMsg) {
                        Log.i("--------",resultMsg.getMsg()+"");
                        Log.i("--------",resultMsg.getCode()+"");
                        if (resultMsg.getCode()==200){
                            if (!Util.isEmpty(name)){
                                ToastUtil.show("修改成功");
                            }else {
                                ToastUtil.show("新增成功");
                            }
                            finish();
                        }else {
                            if (!Util.isEmpty(name)){
                                ToastUtil.show("修改失败");
                            }else {
                                ToastUtil.show("新增失败");
                            }
                        }
                    }
                });
    }
}