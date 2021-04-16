package com.gd.form.activity;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.gd.form.R;
import com.gd.form.base.BaseActivity;
import com.gd.form.fragment.DoneFragment;
import com.gd.form.fragment.TodoFragment;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class TaskActivity extends BaseActivity {
    @BindView(R.id.rb_todo)
    RadioButton rb_todo;
    @BindView(R.id.rb_done)
    RadioButton rb_done;
    @BindView(R.id.underline1)
    TextView underline1;
    @BindView(R.id.underline2)
    TextView underline2;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    private FragmentManager fragmentManager;
    private List<Fragment> fragmentList;
    private TodoFragment todoFragment;
    private DoneFragment doneFragment;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext,R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_task;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentList = new ArrayList<Fragment>();
        todoFragment=new TodoFragment();
        doneFragment=new DoneFragment();
        fragmentList.add(todoFragment);
        fragmentList.add(doneFragment);

        fragmentManager = getSupportFragmentManager();
        MyPagerAdapter adapter = new MyPagerAdapter(fragmentManager);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(0);
        setListeners();

        if (rb_todo.isChecked() ) {
            underline1.setBackgroundColor(getResources().getColor(R.color.colorFF52A7F9));
            underline2.setBackgroundColor(getResources().getColor(R.color.write));
        }
        if (rb_done.isChecked() ) {
            underline2.setBackgroundColor(getResources().getColor(R.color.colorFF52A7F9));
            underline1.setBackgroundColor(getResources().getColor(R.color.write));
        }
    }

    private void setListeners() {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override//开始滑动
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override//viewpager滑动
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        rb_todo.setChecked(true);
                        underline1.setBackgroundResource(R.color.colorFF52A7F9);
                        underline2.setBackgroundResource(R.color.write);
                        break;
                    case 1:
                        rb_done.setChecked(true);
                        underline2.setBackgroundColor(getResources().getColor(R.color.colorFF52A7F9));
                        underline1.setBackgroundColor(getResources().getColor(R.color.write));
                        break;
                }
            }

            @Override
            //结束滑动
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @OnClick({
            R.id.rb_todo,
            R.id.rb_done
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rb_todo:
                viewPager.setCurrentItem(0);
                underline1.setBackgroundColor(getResources().getColor(R.color.colorFF52A7F9));
                underline2.setBackgroundColor(getResources().getColor(R.color.write));
                break;
            case R.id.rb_done:
                viewPager.setCurrentItem(1);
                underline2.setBackgroundColor(getResources().getColor(R.color.colorFF52A7F9));
                underline1.setBackgroundColor(getResources().getColor(R.color.write));

                break;
        }
    }

    class MyPagerAdapter extends FragmentPagerAdapter {


        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int arg0) {
            return fragmentList.get(arg0);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

    }
}
