package com.gd.form.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.gd.form.R;
import com.gd.form.activity.AddWaterInsuranceActivity;
import com.gd.form.activity.FfglActivity;
import com.gd.form.activity.GhgActivity;
import com.gd.form.activity.JobsActivity;
import com.gd.form.activity.KpiAddActivity;
import com.gd.form.activity.KpiDisplayActivity;
import com.gd.form.activity.PipeTagActivity;
import com.gd.form.activity.ProjectListActivity;
import com.gd.form.activity.SearchDataActivity;
import com.gd.form.activity.SearchStationActivity;
import com.gd.form.activity.SearchTaskActivity;
import com.gd.form.activity.StandardFileActivity;
import com.gd.form.activity.TaskDispatchActivity;
import com.gd.form.activity.UploadRecordActivity;
import com.gd.form.activity.XhglActivity;
import com.gd.form.base.BaseFragment;
import com.gd.form.utils.SPUtil;
import com.jaeger.library.StatusBarUtil;

import butterknife.BindView;
import butterknife.OnClick;

import static com.gd.form.constants.Constant.PIPE_DEPARTMENT;


public class WorkFragment extends BaseFragment {
    @BindView(R.id.ll_task_dispatch)
    LinearLayout llTaskDispatch;
    @BindView(R.id.ll_task)
    LinearLayout llTask;
    @BindView(R.id.ll_kpi_add)
    LinearLayout llKpiAdd;
    @Override
    protected void initView(Bundle bundle) {
        StatusBarUtil.setTranslucentForImageView(getActivity(), 0, null);
        String departmentId = (String) SPUtil.get(getContext(), "departmentId", "");
        if (!TextUtils.isEmpty(departmentId)) {
            if (PIPE_DEPARTMENT == Integer.parseInt(departmentId)) {
                llTaskDispatch.setVisibility(View.VISIBLE);
                llTask.setVisibility(View.VISIBLE);
            } else {
                llTaskDispatch.setVisibility(View.GONE);
                llTask.setVisibility(View.GONE);
                llKpiAdd.setVisibility(View.GONE);
            }
        }
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
            R.id.ll_upload_record,
            R.id.ll_standard_file,
            R.id.ll_kpi_add,
            R.id.ll_kpi_display
    })
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.ll_jobs:
                openActivity(JobsActivity.class);
                break;
            case R.id.ll_kpi_display:
                openActivity(KpiDisplayActivity.class);
                break;
            case R.id.ll_kpi_add:
                openActivity(KpiAddActivity.class);
                break;
            case R.id.ll_standard_file:
                openActivity(StandardFileActivity.class);
                break;
            //数据查询
            case R.id.ll_search_data:
                bundle.putString("name", "张三");
                setArguments(bundle);
                openActivity(SearchDataActivity.class);
                break;
            case R.id.ll_job_xhgl:
                openActivity(XhglActivity.class);
                break;
            case R.id.ll_job_fxgl:
                openActivity(ProjectListActivity.class);
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
            case R.id.ll_upload_record:
                openActivity(UploadRecordActivity.class, bundle);
                break;
        }
    }

}
