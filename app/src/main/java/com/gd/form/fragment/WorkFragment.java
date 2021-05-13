package com.gd.form.fragment;

import android.os.Bundle;
import android.view.View;

import com.gd.form.R;
import com.gd.form.activity.AddWaterInsuranceActivity;
import com.gd.form.activity.FfglActivity;
import com.gd.form.activity.FxglActivity;
import com.gd.form.activity.GhgActivity;
import com.gd.form.activity.JobsActivity;
import com.gd.form.activity.PipeTagActivity;
import com.gd.form.activity.SearchDataActivity;
import com.gd.form.activity.SearchStationActivity;
import com.gd.form.activity.SearchTaskActivity;
import com.gd.form.activity.TaskDispatchActivity;
import com.gd.form.activity.XhglActivity;
import com.gd.form.base.BaseFragment;
import com.jaeger.library.StatusBarUtil;

import butterknife.OnClick;


public class WorkFragment extends BaseFragment {
    @Override
    protected void initView(Bundle bundle) {
        StatusBarUtil.setTranslucentForImageView(getActivity(), 0, null);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_work;
    }


    @OnClick({
            R.id.ll_jobs,
            R.id.ll_job_xhgl,
            R.id.ll_job_ghg,
            R.id.ll_job_ffgl,
            R.id.ll_task_dispatch,
            R.id.ll_job_fxgl,
            R.id.ll_tzsj,
            R.id.ll_search_data,
            R.id.ll_addOrReduce,
            R.id.ll_waterInsurance,
            R.id.ll_search_task,
    })
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.ll_jobs:
                openActivity(JobsActivity.class);
                break;
            //数据查询
            case R.id.ll_search_data:
                openActivity(SearchDataActivity.class);
                break;
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
            case R.id.ll_task_dispatch:
                openActivity(TaskDispatchActivity.class);
                break;
            case R.id.ll_tzsj:
                openActivity(SearchStationActivity.class);
                break;
            case R.id.ll_addOrReduce:
                bundle.putString("tag", "add");
                openActivity(PipeTagActivity.class, bundle);
                break;
            case R.id.ll_waterInsurance:
                openActivity(AddWaterInsuranceActivity.class, bundle);
                break;
            case R.id.ll_search_task:
                openActivity(SearchTaskActivity.class, bundle);
                break;
        }
    }


}
