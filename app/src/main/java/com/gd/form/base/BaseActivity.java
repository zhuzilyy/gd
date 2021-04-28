package com.gd.form.base;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gd.form.R;
import com.gd.form.utils.ActivityCollectorUtil;
import com.gd.form.utils.StatusBarUtil;
import com.gd.form.view.LoadView;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * <p>类描述：Activity基类
 * <p>创建人：wh
 * <p>创建时间：2019/6/21
 */
public abstract class BaseActivity extends AppCompatActivity {

    public Context mContext;
    private Unbinder unbinder;
    private LoadView loading;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置状态栏的颜色
        setStatusColor();
        //设置白底黑字
        setTitleBarColor();
        mContext=this;
        ActivityCollectorUtil.addActivity(this);
        setContentView(getActLayoutId());
        unbinder= ButterKnife.bind(this);

        setStatusBar();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    protected abstract void setStatusBar();


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(unbinder!=null){
            unbinder.unbind();
        }
        ActivityCollectorUtil.removeActivity(this);
    }

    /**
     * 获取activity布局.
     *
     * @return layout
     */
    @LayoutRes
    protected abstract int getActLayoutId();

    /*
     * 以下为startActivity优化
     */
    /**
     * @param target 要跳转的Activity.class
     */
    protected void openActivity(Class<?> target) {
        openActivity(target, null);
    }

    protected void openActivity(Class<?> target, Bundle bundle) {
        openActivity(target, bundle, false);
    }

    protected void openActivity(Class<?> target, Bundle bundle, boolean isFinish) {
        Intent intent = new Intent(this, target);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        this.startActivity(intent);
        if (isFinish) {
            this.finish();
        }
    }

    private void setTitleBarColor() {
        //实现状态栏 黑字白底 6.0以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_IMMERSIVE |View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.write);
        }else {
            StatusBarUtil.transparencyBar(this); //设置状态栏全透明
            //StatusBarUtil.StatusBarLightMode(this); //设置白底黑字
        }
    }
    public void setStatusColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus();
        }
    }
    private void setTranslucentStatus() {
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        final int status = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        params.flags |= status;
        window.setAttributes(params);
    }


    public void showLoading(Context activity, String loadText) {
        if (loading == null) {
            loading = new LoadView(activity);
            loading.setCancelable(false);
            loading.setCanceledOnTouchOutside(false);
        }
        loading.loading(loadText);
        loading.getWindow().setDimAmount(0f);
        loading.show();
    }

    public void dismissLoading() {
        if (loading != null && loading.isShowing()) {
            loading.dismiss();
        }
    }
}
