package com.gd.form.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.gd.form.R;
import com.gd.form.adapter.UsersExpandableListAdapter;
import com.gd.form.base.BaseActivity;
import com.gd.form.model.Jobs;
import com.gd.form.model.Pipemploys;
import com.gd.form.model.ResultMsg;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.ToastUtil;
import com.gd.form.view.ListDialog;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class UsersActivity extends BaseActivity {
    @BindView(R.id.lv_users)
    ExpandableListView lv_users;
    List<Jobs> list=new ArrayList<>();
    private ListDialog dialog;

    private  String[] departmentNames;
    private  String[][] userNames;
    private  String[][] userNameIds;
    private  List<String> departList=new ArrayList<>();
    private  List<String> departNameList=new ArrayList<>();
    private  List<String> nameList=new ArrayList<>();
    private  List<String> nameIdList=new ArrayList<>();
    private UsersExpandableListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //adapter = new UsersExpandableListAdapter(departmentNames, userNames);
      //  lv_users.setAdapter(adapter);

        // 清除默认的 Indicator
      //  lv_users.setGroupIndicator(null);

        int groupCount = lv_users.getCount();
//                for (int i=0; i<groupCount; i++)
//                {
//                    listView.expandGroup(i);
//                }
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


                List<String> listM = new ArrayList<>();

                listM.add("修改");
                listM.add("删除");
                if (dialog == null) {
                    dialog = new ListDialog(mContext);
                }
                dialog.setData(listM);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    Log.i("-------------",positionM+"");
                    if (positionM==0){
                        Bundle bundle=new Bundle();
                        bundle.putString("id", userNameIds[groupPosition][childPosition]);
                        openActivity(UserActivity.class,bundle,false);
                    }  else if (positionM==1){
                        delRequest(userNameIds[groupPosition][childPosition]);
                    }
                    dialog.dismiss();
                });
                return true;
            }
        });
    }

    private void delRequest(String id){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id",id);

        Net.create(Api.class).pipemploysDelete(jsonObject)
                .enqueue(new NetCallback<ResultMsg>(this,true) {
                    @Override
                    public void onResponse(ResultMsg resultMsg) {
                        Log.i("--------",resultMsg.getMsg()+"");
                        Log.i("--------",resultMsg.getCode()+"");
                        ToastUtil.show(resultMsg.getMsg()+"");
                        if (resultMsg.getCode()==200){
                            getListRequest();
                        }

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
                .enqueue(new NetCallback<List<Pipemploys>>(this,true) {
                    @Override
                    public void onResponse(List<Pipemploys> list) {
                        Log.i("--------",list.size()+"");


                       for (int i=0;i<list.size();i++){
                           if (!departNameList.contains(list.get(i).getDeptname()+"")){
                               departNameList.add(list.get(i).getDeptname()+"");
                           }
                       }

                        departmentNames=new String[departNameList.size()];
                        userNames=new String[departNameList.size()][];
                        userNameIds=new String[departNameList.size()][];
                       for (int j=0;j<departNameList.size();j++){
                           departmentNames[j]=departNameList.get(j);
                       }

                       for (int m=0;m<departNameList.size();m++){
                           nameList.clear();
                           nameIdList.clear();
                           for (int n=0;n<list.size();n++){
                               if (departNameList.get(m).equals(list.get(n).getDeptname()+"")){
                                   nameList.add(list.get(n).getName());
                                   nameIdList.add(list.get(n).getId());
                               }
                           }
                           userNames[m]=new String[nameList.size()];
                           userNameIds[m]=new String[nameList.size()];
                           for (int k=0;k<nameList.size();k++)  {
                               userNames[m][k]=nameList.get(k);
                               userNameIds[m][k]=nameIdList.get(k);
                           }
                       }

                        Log.i("--------",departmentNames.length+"");
                        Log.i("--------",userNames.length+"");
                       adapter = new UsersExpandableListAdapter(departmentNames, userNames);
                        lv_users.setAdapter(adapter);
                       // adapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext,R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_users;
    }

    @OnClick({
            R.id.iv_back,
            R.id.iv_add,
    })
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.iv_back:
               finish();
                break;
            case R.id.iv_add:
                openActivity(UserActivity.class);
                break;

        }
    }


}