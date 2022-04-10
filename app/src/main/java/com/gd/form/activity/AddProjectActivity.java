package com.gd.form.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.gd.form.R;
import com.gd.form.base.BaseActivity;
import com.gd.form.constants.Constant;
import com.gd.form.model.ProjectDetailModel;
import com.gd.form.model.ServerModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.ContentUriUtil;
import com.gd.form.utils.NumberUtil;
import com.gd.form.utils.SPUtil;
import com.gd.form.utils.TimeUtil;
import com.gd.form.utils.ToastUtil;
import com.gd.form.utils.WeiboDialogUtils;
import com.gd.form.view.ListDialog;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AddProjectActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_fileName)
    TextView tvFileName;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_stationNo)
    TextView tvStationNo;
    @BindView(R.id.et_distance)
    EditText etDistance;
    @BindView(R.id.et_projectName)
    EditText etProjectName;
    @BindView(R.id.et_baseInfo)
    EditText etBaseInfo;
    @BindView(R.id.et_constructName)
    EditText etConstructName;
    @BindView(R.id.et_progress)
    EditText etProgress;
    @BindView(R.id.et_progressDetail)
    EditText etProgressDetail;
    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.tv_projectType)
    TextView tvProjectType;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    private int FILE_REQUEST_CODE = 100;
    private int SELECT_STATION = 101;
    private String selectFileName;
    private String selectFilePath;
    private Dialog mWeiboDialog;
    private OSSCredentialProvider ossCredentialProvider;
    private OSS oss;
    private String token, userId;
    private String ossFilePath;
    private String pipeId, stationId;
    private TimePickerView pvTime;
    private String tag, projectId, location;
    private ProjectDetailModel projectDetailModel;
    private ListDialog projectTypeDialog;
    private List<String> typeList;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_add_project;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        token = (String) SPUtil.get(this, "token", "");
        userId = (String) SPUtil.get(this, "userId", "");
        ossCredentialProvider = new OSSPlainTextAKSKCredentialProvider(Constant.ACCESSKEYID, Constant.ACCESSKEYSECRET);
        oss = new OSSClient(mContext.getApplicationContext(), Constant.ENDPOINT, ossCredentialProvider);
        tag = getIntent().getExtras().getString("tag");
        if ("detail".equals(tag)) {
            tvRight.setVisibility(View.VISIBLE);
            tvRight.setText("跟踪管理");
            tvTitle.setText("项目工程详情");
            projectId = getIntent().getExtras().getString("projectId");
            getDetail(projectId);
        } else {
            tvRight.setVisibility(View.GONE);
            tvTitle.setText("项目工程新增");
        }
        typeList = new ArrayList<>();
        typeList.add("相关工程");
        typeList.add("计划项目");
        projectTypeDialog = new ListDialog(this);
        projectTypeDialog.setData(typeList);
        initTimePicker();
    }

    private void getDetail(String projectId) {
        JsonObject params = new JsonObject();
        params.addProperty("id", projectId);
        Net.create(Api.class).getProjectDetail(token, params)
                .enqueue(new NetCallback<ProjectDetailModel>(this, true) {
                    @Override
                    public void onResponse(ProjectDetailModel result) {
                        if (result != null) {
                            projectDetailModel = result;
                            tvProjectType.setText(result.getProcessdesc());
                            tvStationNo.setText(result.getStakename());
                            etDistance.setText(result.getStakefrom());
                            etProjectName.setText(result.getConstructionname());
                            etConstructName.setText(result.getConstructionunit());
                            if (result.getConstructiondate() != null) {
                                tvTime.setText(TimeUtil.longToFormatTime(result.getConstructiondate().getTime()));
                            }
                            etProgress.setText(result.getConstructionprocess());
                            etProgressDetail.setText(result.getConstructiondesc());
                            etBaseInfo.setText(result.getConstructiondesc());
                            etRemark.setText(result.getRemark());
                            stationId = result.getStakeid() + "";
                            tvAddress.setText(result.getConstructionlocation());
                            pipeId = "0";
                            if (result.getUploadfile() != null) {
                                if (!result.getUploadfile().equals("00")) {
                                    if (result.getFilename().contains("_")) {
                                        tvFileName.setText(result.getFilename().split("_")[2]);
                                    }

                                }
                            }
                        }
                    }
                });
    }

    @OnClick({R.id.iv_back,
            R.id.tv_right,
            R.id.ll_upload,
            R.id.ll_stationNo,
            R.id.ll_time,
            R.id.btn_commit,
            R.id.ll_projectType,
    })
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_projectType:
                projectTypeDialog.show();
                projectTypeDialog.setListItemClick(positionM -> {
                    tvProjectType.setText(typeList.get(positionM));
                    projectTypeDialog.dismiss();
                });
                break;
            case R.id.btn_commit:
                if (!infoIsComplete()) {
                    if ("detail".equals(tag)) {
                        update();
                    } else {
                        commit();
                    }
                }
                break;
            case R.id.ll_time:
                pvTime.show(view);
                break;
            case R.id.ll_stationNo:
                Intent intentEndStation = new Intent(this, StationByIdActivity.class);
                intentEndStation.putExtra("tag", "addProject");
                intentEndStation.putExtra("pipeId", "0");
                startActivityForResult(intentEndStation, SELECT_STATION);
                break;
            case R.id.ll_upload:
                if ("detail".equals(tag) && projectDetailModel != null &&
                        !projectDetailModel.getUploadfile().equals("00")) {
                    Uri uri = Uri.parse(projectDetailModel.getUploadfile());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } else {
//                    Intent intentAddress = new Intent(AddProjectActivity.this, SelectFileActivity.class);
//                    startActivityForResult(intentAddress, FILE_REQUEST_CODE);
//                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                    intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
//                    intent.addCategory(Intent.CATEGORY_OPENABLE);
//                    startActivityForResult(intent,FILE_REQUEST_CODE);
                    getPermission();
                }
                break;
            case R.id.tv_right:
                Bundle bundle = new Bundle();
                bundle.putString("projectId", projectId);
                openActivity(AddProjectRecordActivity.class, bundle);
                break;
        }
    }

    private void getPermission() {
        AndPermission
                .with(this)
                .permission(Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE)
                .onDenied(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        //拒绝权限
                        ToastUtil.show("请赋予必要权限");
                    }
                }).onGranted(new Action() {
            @Override
            public void onAction(List<String> permissions) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, FILE_REQUEST_CODE);
            }
        }).start();
    }

    private void commit() {
        JsonObject params = new JsonObject();
        params.addProperty("stakeid", Integer.valueOf(stationId));
        params.addProperty("pipeid", Integer.valueOf(pipeId));
        params.addProperty("stakefrom", etDistance.getText().toString());
        params.addProperty("constructionname", etProjectName.getText().toString());
        params.addProperty("constructionunit", etConstructName.getText().toString());
        params.addProperty("constructiondesc", etBaseInfo.getText().toString());
        params.addProperty("construcitondate", TimeUtil.getCurrentTimeYYmmdd());
        params.addProperty("constructionprocess", "0");
        params.addProperty("processdesc", tvProjectType.getText().toString());
        params.addProperty("remark", etRemark.getText().toString());
        params.addProperty("creator", userId);
        params.addProperty("creatime", TimeUtil.getCurrentTime());
        params.addProperty("uploadfile", "00");
        params.addProperty("constructionlocation", location);
        Log.i("tag", "params====" + params);
        Net.create(Api.class).addProject(token, params)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel result) {
                        if (result.getCode() == Constant.SUCCESS_CODE) {
                            Intent intent = new Intent();
                            intent.setAction("com.action.status.change");
                            sendBroadcast(intent);
                            ToastUtil.show("增加成功");
                            finish();
                        } else {
                            ToastUtil.show(result.getMsg());
                        }
                    }
                });
    }

    private void update() {
        JsonObject params = new JsonObject();
        params.addProperty("id", projectId);
        params.addProperty("stakeid", Integer.valueOf(stationId));
        params.addProperty("pipeid", Integer.valueOf(pipeId));
        params.addProperty("stakefrom", etDistance.getText().toString());
        params.addProperty("constructionname", etProjectName.getText().toString());
        params.addProperty("constructionunit", etConstructName.getText().toString());
        params.addProperty("constructiondesc", etBaseInfo.getText().toString());
        params.addProperty("construcitondate", TimeUtil.getCurrentTimeYYmmdd());
        params.addProperty("constructionprocess", "0");
        params.addProperty("processdesc", tvProjectType.getText().toString());
        params.addProperty("remark", etRemark.getText().toString());
        params.addProperty("creator", userId);
        params.addProperty("creatime", TimeUtil.getCurrentTime());
        params.addProperty("uploadfile", "00");
        params.addProperty("constructionlocation", tvAddress.getText().toString());
        Net.create(Api.class).updateProject(token, params)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel result) {
                        if (result.getCode() == Constant.SUCCESS_CODE) {
                            Intent intent = new Intent();
                            intent.setAction("com.action.status.change");
                            sendBroadcast(intent);
                            ToastUtil.show("更新成功");
                            finish();
                        } else {
                            ToastUtil.show(result.getMsg());
                        }
                    }
                });
    }

    private boolean infoIsComplete() {
        if (TextUtils.isEmpty(tvProjectType.getText().toString())) {
            ToastUtil.show("请选择工程类型");
            return true;
        }
        if (TextUtils.isEmpty(tvStationNo.getText().toString())) {
            ToastUtil.show("请选择桩号");
            return true;
        }
        if (TextUtils.isEmpty(etDistance.getText().toString())) {
            ToastUtil.show("请输入距离");
            return true;
        }
        if (!NumberUtil.isNumber(etDistance.getText().toString())) {
            ToastUtil.show("距离格式输入不正确");
            return true;
        }
        if (TextUtils.isEmpty(etProjectName.getText().toString())) {
            ToastUtil.show("请输入工程名称");
            return true;
        }
        if (TextUtils.isEmpty(etBaseInfo.getText().toString())) {
            ToastUtil.show("请输入工程基本信息");
            return true;
        }
        if (TextUtils.isEmpty(etConstructName.getText().toString())) {
            ToastUtil.show("请输入施工单位");
            return true;
        }
        if (TextUtils.isEmpty(etRemark.getText().toString())) {
            ToastUtil.show("请输入备注");
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if (null != uri) {
                selectFilePath = ContentUriUtil.getPath(this, uri);
                String[] splitPath = selectFilePath.split("/");
                selectFileName = splitPath[splitPath.length - 1];
                tvFileName.setText(selectFileName);
                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(this, "加载中...");
                mWeiboDialog.getWindow().setDimAmount(0f);
                uploadOffice("projectfile/" + userId + "_" + TimeUtil.getFileNameTime() + "_" + selectFileName, selectFilePath);
            }
        } else if (requestCode == SELECT_STATION) {
            String stationName = data.getStringExtra("stationName");
            pipeId = data.getStringExtra("pipeId");
            stationId = data.getStringExtra("stationId");
            location = data.getStringExtra("location");
            tvStationNo.setText(stationName);
            tvAddress.setText(location);
        }
    }

    public void uploadOffice(String fileName, String filePath) {
        PutObjectRequest put = new PutObjectRequest(Constant.BUCKETSTRING, fileName, filePath);
        // 此处调用异步上传方法
        OSSAsyncTask ossAsyncTask = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                ossFilePath = fileName;
                WeiboDialogUtils.closeDialog(mWeiboDialog);
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientException, ServiceException serviceException) {
                ToastUtil.show("上传失败请重试");
                // 请求异常。
                if (clientException != null) {
                    // 本地异常，如网络异常等。
                }
                if (serviceException != null) {


                }
            }
        });
    }

    private void initTimePicker() {//Dialog 模式下，在底部弹出
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                tvTime.setText(getTime(date));
            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {
                        Log.i("pvTime", "onTimeSelectChanged");
                    }
                })
                .setType(new boolean[]{true, true, true, false, false, false})
                .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .addOnCancelClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("pvTime", "onCancelClickListener");
                    }
                })
                .setItemVisibleCount(5) //若设置偶数，实际值会加1（比如设置6，则最大可见条目为7）
                .setLineSpacingMultiplier(2.0f)
                .isAlphaGradient(true)
                .build();

        Dialog mDialog = pvTime.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            pvTime.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
                dialogWindow.setDimAmount(0.3f);
            }
        }
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

}
