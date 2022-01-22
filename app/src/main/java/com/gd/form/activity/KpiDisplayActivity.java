package com.gd.form.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.gd.form.R;
import com.gd.form.base.BaseActivity;
import com.gd.form.fragment.AverageSeasonFragment;
import com.gd.form.fragment.FirstSeasonFragment;
import com.gd.form.fragment.FourthSeasonFragment;
import com.gd.form.fragment.SecondSeasonFragment;
import com.gd.form.fragment.ThirdSeasonFragment;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class KpiDisplayActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_firstSeason)
    TextView tvFirstSeason;
    @BindView(R.id.view_firstSeason)
    View viewFirstSeason;
    @BindView(R.id.tv_secondSeason)
    TextView tvSecondSeason;
    @BindView(R.id.view_secondSeason)
    View viewSecondSeason;
    @BindView(R.id.tv_thirdSeason)
    TextView tvThirdSeason;
    @BindView(R.id.view_thirdSeason)
    View viewThirdSeason;
    @BindView(R.id.tv_fourthSeason)
    TextView tvFourthSeason;
    @BindView(R.id.view_fourthSeason)
    View viewFourthSeason;
    @BindView(R.id.tv_average)
    TextView tvAverage;
    @BindView(R.id.view_average)
    View viewAverage;
    private FragmentManager fragmentManager;
    private Fragment currentFragment;
    private FirstSeasonFragment firstSeasonFragment;
    private SecondSeasonFragment secondSeasonFragment;
    private ThirdSeasonFragment thirdSeasonFragment;
    private FourthSeasonFragment fourthSeasonFragment;
    private AverageSeasonFragment averageSeasonFragment;
    private List<TextView> textViews;
    private List<View> views;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_kpi_display;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("考核公示");
        currentFragment = new Fragment();
        fragmentManager = getSupportFragmentManager();
        firstSeasonFragment = new FirstSeasonFragment();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        addOrShowFra(ft, firstSeasonFragment);
        textViews = new ArrayList<>();
        views = new ArrayList<>();
        textViews.add(tvFirstSeason);
        textViews.add(tvSecondSeason);
        textViews.add(tvThirdSeason);
        textViews.add(tvFourthSeason);
        textViews.add(tvAverage);
        views.add(viewFirstSeason);
        views.add(viewSecondSeason);
        views.add(viewThirdSeason);
        views.add(viewFourthSeason);
        views.add(viewAverage);
    }

    @OnClick({
            R.id.ll_firstSeason,
            R.id.ll_secondSeason,
            R.id.ll_thirdSeason,
            R.id.ll_fourthSeason,
            R.id.ll_average,
            R.id.iv_back
    })
    public void click(View view) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_firstSeason:
                if (firstSeasonFragment == null) {
                    firstSeasonFragment = new FirstSeasonFragment();
                }
                changeSelectStatus(0);
                addOrShowFra(fragmentTransaction, firstSeasonFragment);
                break;
            case R.id.ll_secondSeason:
                if (secondSeasonFragment == null) {
                    secondSeasonFragment = new SecondSeasonFragment();
                }
                changeSelectStatus(1);
                addOrShowFra(fragmentTransaction, secondSeasonFragment);
                break;
            case R.id.ll_thirdSeason:
                if (thirdSeasonFragment == null) {
                    thirdSeasonFragment = new ThirdSeasonFragment();
                }
                changeSelectStatus(2);
                addOrShowFra(fragmentTransaction, thirdSeasonFragment);
                break;
            case R.id.ll_fourthSeason:
                if (fourthSeasonFragment == null) {
                    fourthSeasonFragment = new FourthSeasonFragment();
                }
                changeSelectStatus(3);
                addOrShowFra(fragmentTransaction, fourthSeasonFragment);
                break;
            case R.id.ll_average:
                if (averageSeasonFragment == null) {
                    averageSeasonFragment = new AverageSeasonFragment();
                }
                changeSelectStatus(4);
                addOrShowFra(fragmentTransaction, averageSeasonFragment);
                break;
        }
    }

    private void changeSelectStatus(int index) {
        for (int i = 0; i < textViews.size(); i++) {
            if (i == index) {
                textViews.get(i).setTextColor(Color.parseColor("#FF52A7F9"));
                views.get(i).setVisibility(View.VISIBLE);
            } else {
                textViews.get(i).setTextColor(Color.parseColor("#000000"));
                views.get(i).setVisibility(View.INVISIBLE);
            }
        }
    }

    /***
     * 显示隐藏Fragment
     *
     * @param ft
     * @param fragment
     */
    private void addOrShowFra(FragmentTransaction ft, Fragment fragment) {
        if (currentFragment == fragment) {
            return;
        }
        if (!fragment.isAdded()) {
            ft.hide(currentFragment).add(R.id.main_switch, fragment).commitAllowingStateLoss();

        } else {
            ft.hide(currentFragment).show(fragment).commitAllowingStateLoss();

        }
        currentFragment = fragment;
    }
}
