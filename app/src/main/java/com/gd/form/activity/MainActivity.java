package com.gd.form.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.azhon.appupdate.BuildConfig;
import com.azhon.appupdate.config.UpdateConfiguration;
import com.azhon.appupdate.manager.DownloadManager;
import com.gd.form.R;
import com.gd.form.adapter.MyFragmentPagerAdapter;
import com.gd.form.base.BaseActivity;
import com.gd.form.constants.Constant;
import com.gd.form.model.OssModel;
import com.gd.form.model.UpdateModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.SPUtil;
import com.gd.form.view.AppRadioButton;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import q.rorbin.badgeview.Badge;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    @BindView(R.id.rg_tab_bar)
    RadioGroup rg_tab_bar;
    @BindView(R.id.rb_work)
    AppRadioButton rb_work;
    @BindView(R.id.rb_message)
    AppRadioButton rb_message;
    @BindView(R.id.rb_user)
    AppRadioButton rb_user;
    @BindView(R.id.vpager)
    ViewPager vpager;
    private MyFragmentPagerAdapter mAdapter;
    private DownloadManager manager;
    //几个代表页面的常量
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;
    private String token, userId;
    private UpdateConfiguration configuration;
    private long exitTime = 0;//根据连续点击之间时间判断 是否退出
    private MyReceiver myReceiver;
    private List<Badge> badges;
    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }
    @Override
    protected int getActLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        rb_work.setChecked(true);
        vpager.setAdapter(mAdapter);
        vpager.setCurrentItem(PAGE_TWO);
        vpager.addOnPageChangeListener(this);
        token = (String) SPUtil.get(this, "token", "");
        manager = DownloadManager.getInstance(getApplicationContext());
        getOssData();
        updateApp();

        myReceiver = new MyReceiver();
        IntentFilter filterShowDot = new IntentFilter();
        filterShowDot.addAction("com.action.showMessageDot");
        registerReceiver(myReceiver,filterShowDot);

        IntentFilter filterHideDot = new IntentFilter();
        filterHideDot.addAction("com.action.hideMessageDot");
        registerReceiver(myReceiver,filterHideDot);


    }
    private void updateApp() {
        configuration = new UpdateConfiguration()
                //输出错误日志
                .setEnableLog(true)
                //设置自定义的下载
                //.setHttpManager()
                //下载完成自动跳动安装页面
                .setJumpInstallPage(true)
                //设置对话框背景图片 (图片规范参照demo中的示例图)
                //.setDialogImage(R.drawable.ic_dialog)
                //设置按钮的颜色
                //.setDialogButtonColor(Color.parseColor("#E743DA"))
                //设置对话框强制更新时进度条和文字的颜色
                //.setDialogProgressBarColor(Color.parseColor("#E743DA"))
                //设置按钮的文字颜色
                .setDialogButtonTextColor(Color.WHITE)
                //设置是否显示通知栏进度
                .setShowNotification(true)
                //设置是否提示后台下载toast
                .setShowBgdToast(false)
                //设置是否上报数据
                .setUsePlatform(true);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", 1);
        Net.create(Api.class).appUpdate(token, jsonObject)
                .enqueue(new NetCallback<UpdateModel>(this, true) {
                    @Override
                    public void onResponse(UpdateModel model) {
                        if (model != null) {
                            if (BuildConfig.VERSION_CODE < model.getVersioncode()) {
                                down(model.getDownloadpath(), model.getUpdatecomment(), model.getAppversion(), model.getVersioncode());
                            }
                        }
                    }
                });
    }

    private void down(String apkUrl, String content, String appVersion, int versionCode) {
        StringBuilder stringBuilder = new StringBuilder();
        if (!TextUtils.isEmpty(content)) {
            String[] split = content.split(",");
            for (int i = 0; i < split.length; i++) {
                stringBuilder.append(split[i] + "\n");
            }
        }
        manager.setApkName("gd.apk")
                .setApkUrl(apkUrl)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setShowNewerToast(true)
                .setConfiguration(configuration)
                .setApkVersionCode(versionCode)
                .setApkVersionName(appVersion)
                .setApkDescription(content)
                .download();

    }

    private void getOssData() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("empid", userId);
        Net.create(Api.class).getOssData(token)
                .enqueue(new NetCallback<OssModel>(this, true) {
                    @Override
                    public void onResponse(OssModel ossModel) {
                        if (ossModel != null) {
                            Constant.ACCESSKEYID = ossModel.getAccessKeyId();
                            Constant.ACCESSKEYSECRET = ossModel.getAccessKeySecret();
                            Constant.BUCKETSTRING = ossModel.getBucketString();
                            Constant.ENDPOINT = ossModel.getEndpoint();
                        }
                    }
                });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //state的状态有三个，0表示什么都没做，1正在滑动，2滑动完毕
        if (state == 2) {
            switch (vpager.getCurrentItem()) {
                case PAGE_ONE:
                    rb_message.setChecked(true);
                    break;
                case PAGE_TWO:
                    rb_work.setChecked(true);
                    break;
                case PAGE_THREE:
                    rb_user.setChecked(true);
                    break;

            }
        }
    }


    @OnClick({
            R.id.rb_work,
            R.id.rb_message,
            R.id.rb_user,
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rb_work:
                vpager.setCurrentItem(PAGE_TWO);
                break;
            case R.id.rb_message:
                vpager.setCurrentItem(PAGE_ONE);
                break;
            case R.id.rb_user:
                vpager.setCurrentItem(PAGE_THREE);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
   /*
    根据点击时间判断
    */
    private void exit() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText(getApplicationContext(), "再点击一次退出", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }
    class MyReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent!=null){
                if(intent.getAction().equals("com.action.showMessageDot")){
                    rb_message.setShowSmallDot(true);
                }else if(intent.getAction().equals("com.action.hideMessageDot")){
                    rb_message.setShowSmallDot(false);
                }
            }
        }
    }
}
