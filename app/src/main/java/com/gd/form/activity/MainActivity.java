package com.gd.form.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.gd.form.BuildConfig;
import com.gd.form.R;
import com.gd.form.adapter.MyFragmentPagerAdapter;
import com.gd.form.base.BaseActivity;
import com.gd.form.constants.Constant;
import com.gd.form.model.OssModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.SPUtil;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements  ViewPager.OnPageChangeListener {
    @BindView(R.id.rg_tab_bar)
    RadioGroup rg_tab_bar;

    @BindView(R.id.rb_work)
    RadioButton rb_work;
    @BindView(R.id.rb_message)
    RadioButton rb_message;
    @BindView(R.id.rb_user)
    RadioButton rb_user;
    @BindView(R.id.vpager)
    ViewPager vpager;


    private MyFragmentPagerAdapter mAdapter;

    //几个代表页面的常量
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;
    private String token, userId;
    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext,R.color.colorFF52A7F9));
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
        getOssData();
//        updateApp();
    }

    private void updateApp() {

//        UpdateConfiguration configuration = new UpdateConfiguration()
//                //输出错误日志
//                .setEnableLog(true)
//                //设置自定义的下载
//                //.setHttpManager()
//                //下载完成自动跳动安装页面
//                .setJumpInstallPage(true)
//                //设置对话框背景图片 (图片规范参照demo中的示例图)
//                //.setDialogImage(R.drawable.ic_dialog)
//                //设置按钮的颜色
//                //.setDialogButtonColor(Color.parseColor("#E743DA"))
//                //设置对话框强制更新时进度条和文字的颜色
//                .setDialogProgressBarColor(Color.parseColor("#ff895b"))
//                //设置按钮的文字颜色
//                .setDialogButtonTextColor(Color.WHITE)
//                //设置是否显示通知栏进度
//                .setShowNotification(true)
//                //设置是否提示后台下载toast
//                .setShowBgdToast(false)
//                //设置强制更新
//                .setForcedUpgrade(false);
//        设置对话框按钮的点击监听
        int updateVersionCode = BuildConfig.VERSION_CODE + 1;
//        DownloadManager manager = DownloadManager.getInstance();
//        manager.setApkName("map.apk")
//                .setApkUrl(appDownload)
//                .setSmallIcon(R.mipmap.logo)
//                .setShowNewerToast(false)
//                .setConfiguration(configuration)
//                .setApkVersionCode(updateVersionCode)
//                .setApkVersionName(appVersion)
//                .setApkDescription(stringBuilder.toString())
//                .download();
    }
    private void getOssData() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("empid", userId);
        Net.create(Api.class).getOssData(token)
                .enqueue(new NetCallback<OssModel>(this, true) {
                    @Override
                    public void onResponse(OssModel ossModel) {
                      if(ossModel!=null){
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
    public void onClick(View view){
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



}
