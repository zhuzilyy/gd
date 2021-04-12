package com.gd.form.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.gd.form.R;
import com.gd.form.adapter.UsersExpandableListAdapter;
import com.gd.form.base.BaseActivity;
import com.gd.form.model.Jobs;
import com.gd.form.model.Pipemploys;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ApproverActivity extends BaseActivity {
    @BindView(R.id.lv_users)
    ExpandableListView lv_users;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    List<Jobs> list = new ArrayList<>();
    private String[] departmentNames;
    private String[][] userNames;
    private String[][] userNameIds;
    private List<String> departNameList = new ArrayList<>();
    private List<String> departIdList = new ArrayList<>();
    private List<String> nameList = new ArrayList<>();
    private List<String> nameIdList = new ArrayList<>();
    private UsersExpandableListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("审批人");
        //  设置分组项的点击监听事件
        lv_users.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // Log.d(TAG, "onGroupClick: groupPosition:" + groupPosition + ", id:" + id);
                boolean groupExpanded = parent.isGroupExpanded(groupPosition);
                adapter.setIndicatorState(groupPosition, groupExpanded);
                // 请务必返回 false，否则分组不会展开
                return false;
            }
        });

        //  设置子选项点击监听事件
        lv_users.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                // Toast.makeText(UsersActivity.this, userNames[groupPosition][childPosition], Toast.LENGTH_SHORT).show();
                //  Toast.makeText(UsersActivity.this, userNameIds[groupPosition][childPosition], Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("name",userNames[groupPosition][childPosition]);
                intent.putExtra("id",userNameIds[groupPosition][childPosition]);
                intent.putExtra("departmentId", departIdList.get(groupPosition));
                intent.putExtra("departmentName", departNameList.get(groupPosition));
                setResult(RESULT_OK,intent);
                finish();
                return true;
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        getListRequest();
    }


    private void getListRequest() {

        Net.create(Api.class).pipemploysGetList()
                .enqueue(new NetCallback<List<Pipemploys>>(this, true) {
                    @Override
                    public void onResponse(List<Pipemploys> list) {

                        for (int i = 0; i < list.size(); i++) {
                            if (!departNameList.contains(list.get(i).getDeptname() + "")) {
                                departNameList.add(list.get(i).getDeptname() + "");
                                departIdList.add(list.get(i).getDepartmentid()+"");
                            }
                        }

                        departmentNames = new String[departNameList.size()];
                        userNames = new String[departNameList.size()][];
                        userNameIds = new String[departNameList.size()][];
                        for (int j = 0; j < departNameList.size(); j++) {
                            departmentNames[j] = departNameList.get(j);
                        }

                        for (int m = 0; m < departNameList.size(); m++) {
                            nameList.clear();
                            nameIdList.clear();
                            for (int n = 0; n < list.size(); n++) {
                                if (departNameList.get(m).equals(list.get(n).getDeptname() + "")) {
                                    nameList.add(list.get(n).getName());
                                    nameIdList.add(list.get(n).getId());
                                }
                            }
                            userNames[m] = new String[nameList.size()];
                            userNameIds[m] = new String[nameList.size()];
                            for (int k = 0; k < nameList.size(); k++) {
                                userNames[m][k] = nameList.get(k);
                                userNameIds[m][k] = nameIdList.get(k);
                            }
                        }

                        adapter = new UsersExpandableListAdapter(departmentNames, userNames);
                        lv_users.setAdapter(adapter);
                        // adapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_approver;
    }

    @OnClick({
            R.id.iv_back,
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_add:

                break;

        }
    }


}