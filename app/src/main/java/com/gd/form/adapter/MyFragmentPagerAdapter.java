package com.gd.form.adapter;


import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.gd.form.activity.MainActivity;
import com.gd.form.fragment.MessageFragment;
import com.gd.form.fragment.UserFragment;
import com.gd.form.fragment.WorkFragment;

/**
 * Created by Jay on 2015/8/31 0031.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private final int PAGER_COUNT = 3;
    private WorkFragment workFragment = null;
    private MessageFragment messageFragment = null;
    private UserFragment userFragment = null;


    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        messageFragment = new MessageFragment();
        workFragment = new WorkFragment();
        userFragment = new UserFragment();
    }


    @Override
    public int getCount() {
        return PAGER_COUNT;
    }

    @Override
    public Object instantiateItem(ViewGroup vg, int position) {
        return super.instantiateItem(vg, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        System.out.println("position Destory" + position);
        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case MainActivity.PAGE_ONE:
                fragment = messageFragment;
                break;
            case MainActivity.PAGE_TWO:
                fragment = workFragment;
                break;
            case MainActivity.PAGE_THREE:
                fragment = userFragment;
                break;
        }
        return fragment;
    }


}

