package com.gd.form.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.gd.form.R;
import com.gd.form.base.BaseActivity;
import com.gd.form.model.Department;
import com.gd.form.model.Jobs;
import com.gd.form.model.Pipemploys;
import com.gd.form.model.ResultMsg;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.ToastUtil;
import com.gd.form.utils.Util;
import com.gd.form.view.ListDialog;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class UserActivity extends BaseActivity {
    @BindView(R.id.et_user_id)
    EditText et_user_id;
    @BindView(R.id.et_user_name)
    EditText et_user_name;
    @BindView(R.id.et_user_pwd)
    EditText et_user_pwd;
    @BindView(R.id.et_user_mail)
    EditText et_user_mail;
    @BindView(R.id.et_user_tel)
    EditText et_user_tel;
    @BindView(R.id.tv_gwzc)
    TextView tv_gwzc;
    @BindView(R.id.tv_bmmc)
    TextView tv_bmmc;
    private String id;

    List<Jobs> listJobs=new ArrayList<>();
    List<String> listJobsName = new ArrayList<>();
    private Integer professionalid=-1;
    private Integer departmentid=-1;
    List<Department> listDepartments=new ArrayList<>();
    List<String> listDepartmentsName = new ArrayList<>();
    private ListDialog dialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getIntent()!=null&&getIntent().getExtras()!=null){
            Bundle extra=getIntent().getExtras();
            id=extra.getString("id");

        }
        if (!Util.isEmpty(id)){
            pipemploysGetListByPrimaryKey();

        }


        professionalinfoget();
        pipedepartmentinfoGetList();
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext,R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_user;
    }

    @OnClick({
            R.id.iv_back,
            R.id.bt_confirm,
            R.id.ll_gwzc,
            R.id.ll_bmmc,

    })
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.bt_confirm:

                if (Util.isEmpty(et_user_id.getText().toString())){
                    ToastUtil.show("用户ID不能为空");
                    break;
                }
                if (Util.isEmpty(et_user_name.getText().toString())){
                    ToastUtil.show("用户名不能为空");
                    break;
                }
                if (Util.isEmpty(tv_gwzc.getText().toString())){
                    ToastUtil.show("岗位职称不能为空");
                    break;
                }
                if (Util.isEmpty(tv_bmmc.getText().toString())){
                    ToastUtil.show("部门名称不能为空");
                    break;
                }
                if (Util.isEmpty(et_user_pwd.getText().toString())){
                    ToastUtil.show("密码不能为空");
                    break;
                }
                if (Util.isEmpty(et_user_mail.getText().toString())){
                    ToastUtil.show("邮箱不能为空");
                    break;
                }
                if (Util.isEmpty(et_user_tel.getText().toString())){
                    ToastUtil.show("电话不能为空");
                    break;
                }


                addRequest();
                break;
            case R.id.ll_gwzc:

                if (dialog == null) {
                    dialog = new ListDialog(mContext);
                }
                dialog.setData(listJobsName);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    Log.i("-------------",positionM+"");
                    tv_gwzc.setText(listJobsName.get(positionM));
                    professionalid=listJobs.get(positionM).getId();
                    dialog.dismiss();
                });

                break;
            case R.id.ll_bmmc:

                if (dialog == null) {
                    dialog = new ListDialog(mContext);
                }
                dialog.setData(listDepartmentsName);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    Log.i("-------------",positionM+"");
                    tv_bmmc.setText(listDepartmentsName.get(positionM));
                    departmentid=listDepartments.get(positionM).getId();
                    dialog.dismiss();
                });

                break;

        }
    }

    private void pipemploysGetListByPrimaryKey(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", id);

        Net.create(Api.class).pipemploysGetListByPrimaryKey(jsonObject)
                .enqueue(new NetCallback<List<Pipemploys>>(this,true) {
                    @Override
                    public void onResponse(List<Pipemploys> resultMsg) {
                        Log.i("--------",resultMsg.size()+"");
                        et_user_id.setText(resultMsg.get(0).getId());
                        et_user_name.setText(resultMsg.get(0).getName());
                        tv_gwzc.setText(resultMsg.get(0).getProname());
                        tv_bmmc.setText(resultMsg.get(0).getDeptname());
                        et_user_pwd.setText(resultMsg.get(0).getPassword());
                        et_user_mail.setText(resultMsg.get(0).getMail());
                        et_user_tel.setText(resultMsg.get(0).getTelenumber());
                    }
                });
    }


    private void addRequest(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", Integer.valueOf(et_user_id.getText().toString()));
        jsonObject.addProperty("name",et_user_name.getText().toString());
        jsonObject.addProperty("professionalid",professionalid);
        jsonObject.addProperty("departmentid",departmentid);
        jsonObject.addProperty("password",et_user_pwd.getText().toString());
        jsonObject.addProperty("mail",et_user_mail.getText().toString());
        jsonObject.addProperty("telenumber",et_user_tel.getText().toString());
        jsonObject.addProperty("roleid","");
        Net.create(Api.class).pipemploysAdd(jsonObject)
                .enqueue(new NetCallback<ResultMsg>(this,true) {
                    @Override
                    public void onResponse(ResultMsg resultMsg) {
                        Log.i("--------",resultMsg.getMsg()+"");
                        Log.i("--------",resultMsg.getCode()+"");
                        ToastUtil.show(resultMsg.getMsg()+"");
                        if (resultMsg.getCode()==200){
                            finish();
                        }
                    }
                });
    }


    private void professionalinfoget() {

        Net.create(Api.class).professionalinfoget()
                .enqueue(new NetCallback<List<Jobs>>(this,false) {
                    @Override
                    public void onResponse(List<Jobs> list1) {
                        Log.i("--------",list1.size()+"");
                        listJobs=list1;
                        for (int i=0;i<listJobs.size();i++){
                            listJobsName.add(listJobs.get(i).getName());
                        }
                    }
                });
    }
    private void pipedepartmentinfoGetList() {

        Net.create(Api.class).pipedepartmentinfoGetList()
                .enqueue(new NetCallback<List<Department>>(this,false) {
                    @Override
                    public void onResponse(List<Department> list1) {
                        Log.i("--------",list1.size()+"");
                        listDepartments=list1;
                        for (int i=0;i<listDepartments.size();i++){
                            listDepartmentsName.add(listDepartments.get(i).getName());
                        }
                    }
                });
    }
}