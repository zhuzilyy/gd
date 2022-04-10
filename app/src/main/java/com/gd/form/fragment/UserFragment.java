package com.gd.form.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.gd.form.R;
import com.gd.form.activity.JobsActivity;
import com.gd.form.activity.SettingActivity;
import com.gd.form.activity.UsersActivity;
import com.gd.form.base.BaseFragment;
import com.gd.form.utils.SPUtil;

import butterknife.BindView;
import butterknife.OnClick;


public class UserFragment extends BaseFragment {
    @BindView(R.id.ll_gwwh)
    LinearLayout llWorking;
    @BindView(R.id.ll_rywh)
    LinearLayout llPerson;

    @Override
    protected void initView(Bundle bundle) {
        int roleId = (int) SPUtil.get(getActivity(), "roleId", 0);
        if(roleId == 200){
            llWorking.setVisibility(View.GONE);
            llPerson.setVisibility(View.GONE);
        }else if(roleId == 100){
            llWorking.setVisibility(View.VISIBLE);
            llPerson.setVisibility(View.VISIBLE);
        }


    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user;
    }

    @OnClick({
            R.id.ll_gwwh,
            R.id.ll_rywh,
            R.id.ll_setting,
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_gwwh:
                openActivity(JobsActivity.class);
                break;
            case R.id.ll_rywh:
                openActivity(UsersActivity.class);
                break;
            case R.id.ll_setting:
                openActivity(SettingActivity.class);
//                ArrayList<String> filters = new ArrayList<String>();
//                filters.add(FileSelectorActivity.FILE_TYPE_IMAGE);//图片
//                filters.add(FileSelectorActivity.FILE_TYPE_VIDEO);//视频
//                filters.add(FileSelectorActivity.FILE_TYPE_DOC);//文档
//                filters.add(FileSelectorActivity.FILE_TYPE_AUDIO);//音频
//
//                FileSelector.Builder builder = new FileSelector.Builder(getActivity());
//                Intent intent = builder.setFileRoot("")//初始路径  init file root
//                        .setIsMultiple(true)//是否多选模式 whether is multiple select
//                        .setMaxCount(3)//限定文件选择数 max file count
//                        .setFilters(filters)//筛选文件类型  file filter
//                        .getIntent();
//                startActivityForResult(intent, 100);

                break;

        }
    }
}
