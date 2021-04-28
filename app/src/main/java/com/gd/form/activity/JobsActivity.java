package com.gd.form.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gd.form.R;
import com.gd.form.adapter.JobsListAdapter;
import com.gd.form.base.BaseActivity;
import com.gd.form.model.Jobs;
import com.gd.form.model.ResultMsg;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.SPUtil;
import com.gd.form.utils.ToastUtil;
import com.gd.form.view.ListDialog;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class JobsActivity extends BaseActivity {
    @BindView(R.id.rv_jobs)
    RecyclerView rv_jobs;
    List<Jobs> list=new ArrayList<>();
    JobsListAdapter adapter;
    private ListDialog dialog;
    private String token, userId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        token = (String) SPUtil.get(JobsActivity.this, "token", "");
        userId = (String) SPUtil.get(JobsActivity.this, "userId", "");
        rv_jobs.setLayoutManager(new LinearLayoutManager(this));
        adapter = new JobsListAdapter(this, list, R.layout.item_jobs);
        rv_jobs.setAdapter(adapter);
        adapter.setOnItemClickListener((v, position) -> {
            List<String> listM = new ArrayList<>();

            listM.add("修改");
            listM.add("删除");
            if (dialog == null) {
                dialog = new ListDialog(mContext);
            }
            dialog.setData(listM);
            dialog.show();
            dialog.setListItemClick(positionM -> {
                if (positionM==0){
                    Bundle bundle=new Bundle();
                    bundle.putString("name",list.get(position).getName());
                    bundle.putInt("code",list.get(position).getId());
                    openActivity(JobActivity.class,bundle,false);
                }  else if (positionM==1){
                    delRequest(list.get(position).getId(),list.get(position).getName());
                }
                dialog.dismiss();
            });

        });

    }

    private void delRequest(int id,String name) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", id);
        jsonObject.addProperty("name",name);
        Net.create(Api.class).professionalinfodelete(token,jsonObject)
                .enqueue(new NetCallback<ResultMsg>(this,true) {
                    @Override
                    public void onResponse(ResultMsg resultMsg) {
                        if (resultMsg.getCode()==200){
                            ToastUtil.show("删除成功");
                            getListRequest();
                        }else {
                            ToastUtil.show("删除失败");
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
        Net.create(Api.class).professionalinfoget(token)
                .enqueue(new NetCallback<List<Jobs>>(this,true) {
                    @Override
                    public void onResponse(List<Jobs> list1) {
                        list=list1;
                        adapter.updateList(list);
                    }
                });
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext,R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_jobs;
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
                openActivity(JobActivity.class);
                break;

        }
    }


}