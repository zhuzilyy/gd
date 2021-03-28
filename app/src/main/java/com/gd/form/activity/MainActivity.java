package com.gd.form.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.gd.form.R;
import com.gd.form.adapter.MyFragmentPagerAdapter;
import com.gd.form.base.BaseActivity;
import com.jaeger.library.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;

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
