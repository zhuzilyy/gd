package com.gd.form.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ViewSwitcher;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gd.form.R;
import com.gd.form.activity.FfglActivity;
import com.gd.form.activity.FxglActivity;
import com.gd.form.activity.GcglActivity;
import com.gd.form.activity.GdCheckFormActivity;
import com.gd.form.activity.GhgActivity;
import com.gd.form.activity.JobActivity;
import com.gd.form.activity.JobsActivity;
import com.gd.form.activity.SdwbActivity;
import com.gd.form.activity.SgbhActivity;
import com.gd.form.activity.StandingBookActivity;
import com.gd.form.activity.XhglActivity;
import com.gd.form.adapter.TodoListAdapter;
import com.gd.form.base.BaseFragment;
import com.gd.form.constants.Constant;
import com.gd.form.utils.MessageEvent;
import com.gd.form.utils.ToastUtil;
import com.gd.form.utils.Util;
import com.jaeger.library.StatusBarUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class WorkFragment extends BaseFragment {
  //  @BindView(R.id.ll_gd_check)
  //  LinearLayout ll_gd_check;


    @Override
    protected void initView(Bundle bundle) {
        StatusBarUtil.setTranslucentForImageView(getActivity(), 0, null);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_work;
    }



    @SuppressLint("NonConstantResourceId")
    @OnClick({
           // R.id.ll_gd_check,
            R.id.ll_tz,
            R.id.ll_sgbh,
            R.id.ll_job_xhgl,
            R.id.ll_job_fxgl,
            R.id.ll_job_ghg,
            R.id.ll_job_ffgl,
            R.id.ll_job_gcgl,

    })
    public void onClick(View view){
        switch (view.getId()) {
//            case R.id.ll_gd_check:
//                Intent intent=new Intent(context,GdCheckFormActivity.class);
//                context.startActivity(intent);
//                break;
            case R.id.ll_tz:
               openActivity(StandingBookActivity.class);
                break;

            case R.id.ll_sgbh:
               openActivity(SgbhActivity.class);
                break;
//            case R.id.ll_sdwb:
//               openActivity(SdwbActivity.class);
//                break;
            case R.id.ll_job_xhgl:
               openActivity(XhglActivity.class);
                break;
            case R.id.ll_job_fxgl:
               openActivity(FxglActivity.class);
                break;
            case R.id.ll_job_ghg:
               openActivity(GhgActivity.class);
                break;
            case R.id.ll_job_ffgl:
               openActivity(FfglActivity.class);
                break;
            case R.id.ll_job_gcgl:
               openActivity(GcglActivity.class);
                break;
        }
    }



}
