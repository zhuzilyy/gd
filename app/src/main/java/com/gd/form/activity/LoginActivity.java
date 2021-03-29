package com.gd.form.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.gd.form.R;
import com.gd.form.base.BaseActivity;
import com.gd.form.view.LoadView;
import com.jaeger.library.StatusBarUtil;

import butterknife.OnClick;


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
