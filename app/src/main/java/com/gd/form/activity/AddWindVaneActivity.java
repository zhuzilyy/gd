package com.gd.form.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.gd.form.adapter.PhotoAdapter;
import com.gd.form.base.BaseActivity;
import com.gd.form.constants.Constant;
import com.gd.form.model.GlideImageLoader;
import com.gd.form.model.ServerModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.NumberUtil;
import com.gd.form.utils.SPUtil;
import com.gd.form.utils.TimeUtil;
import com.gd.form.utils.ToastUtil;
import com.gd.form.utils.WeiboDialogUtils;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;
import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;
import com.yancy.gallerypick.inter.IHandlerCallBack;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AddWindVaneActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_stationNo)
    TextView tvStationNo;
    @BindView(R.id.et_distance)
    EditText etDistance;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.ll_name)
    LinearLayout llName;
    @BindView(R.id.view_name)
    View viewName;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.rg_status)
    RadioGroup rgStatus;
    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.rvResultPhoto)
    RecyclerView rvResultPhoto;
    private String stationId, pipeId, windVanes, departmentId;
    private String token, userId;
    private int SELECT_STATION = 101;
    private String activityName;
    private TimePickerView pvTime;
    private String status = "??????";
    private IHandlerCallBack iHandlerCallBack;
    private List<String> path;
    private GalleryConfig galleryConfig;
    private PhotoAdapter photoAdapter;
    private Dialog mWeiboDialog;
    private OSSCredentialProvider ossCredentialProvider;
    private OSS oss;
    private List<String> nameList;
    private int PERMISSIONS_REQUEST_READ_CONTACTS = 8;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_add_wind_vane;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        token = (String) SPUtil.get(AddWindVaneActivity.this, "token", "");
        userId = (String) SPUtil.get(AddWindVaneActivity.this, "userId", "");
        if (getIntent() != null) {
            activityName = getIntent().getExtras().getString("name");
            pipeId = getIntent().getExtras().getString("pipeId");
            departmentId = getIntent().getExtras().getString("departmentId");
            if (activityName.equals("windVane")) {
                tvTitle.setText("???????????????");
            } else if (activityName.equals("videoMonitoring")) {
                tvTitle.setText("??????????????????");
            } else if (activityName.equals("advocacyBoard")) {
                tvTitle.setText("???????????????");
            } else if (activityName.equals("other")) {
                tvTitle.setText("?????????????????????????????????");
//                llName.setVisibility(View.VISIBLE);
//                viewName.setVisibility(View.VISIBLE);
            }
        }
        ossCredentialProvider = new OSSPlainTextAKSKCredentialProvider(Constant.ACCESSKEYID, Constant.ACCESSKEYSECRET);
        oss = new OSSClient(mContext.getApplicationContext(), Constant.ENDPOINT, ossCredentialProvider);
        path = new ArrayList<>();
        nameList = new ArrayList<>();
        initTimePicker();
        initListener();
        initGallery();
        initConfig();
    }

    private void initListener() {
        rgStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yes:
                        status = "??????";
                        break;
                    case R.id.rb_no:
                        status = "??????";
                        break;
                }
            }
        });
    }

    private void initGallery() {
        iHandlerCallBack = new IHandlerCallBack() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(List<String> photoList) {
                path.clear();
                for (String s : photoList) {
                    path.add(s);
                }
                photoAdapter.notifyDataSetChanged();
                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(AddWindVaneActivity.this, "?????????...");
                mWeiboDialog.getWindow().setDimAmount(0f);
                for (int i = 0; i < path.size(); i++) {
                    String suffix = path.get(i).substring(path.get(i).length() - 4);
                    uploadFiles("stakeothers/" + userId + "_" + TimeUtil.getFileNameTime() + "_" + i + suffix, path.get(i));
                }

            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onError() {

            }
        };

    }

    private void initConfig() {
        galleryConfig = new GalleryConfig.Builder()
                .imageLoader(new GlideImageLoader())    // ImageLoader ????????????????????????
                .iHandlerCallBack(iHandlerCallBack)     // ????????????????????????
                .provider("com.gd.form.fileprovider")   // provider(??????)
                .pathList(path)                         // ?????????????????????
                .multiSelect(true)                      // ????????????   ?????????false
                .multiSelect(true, 9)                   // ??????????????????????????? ??????????????????   ?????????false ??? 9
                .maxSize(9)                             // ??????????????? ??????????????????    ?????????9
                .crop(false)                             // ??????????????????????????????????????? ??????????????????????????????
                .crop(false, 1, 1, 500, 500)             // ??????????????????????????????   ?????????????????? 1:1
                .isShowCamera(true)                     // ????????????????????????  ?????????false
                .filePath("/Gallery/Pictures")// ??????????????????
                .build();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvResultPhoto.setLayoutManager(gridLayoutManager);
        photoAdapter = new PhotoAdapter(this, path);
        rvResultPhoto.setAdapter(photoAdapter);
    }

    @OnClick({
            R.id.iv_back,
            R.id.btn_commit,
            R.id.ll_stationNo,
            R.id.ll_time,
            R.id.rl_selectImage,
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_selectImage:
                initPermissions();
                break;
            case R.id.ll_time:
                pvTime.show(view);
                break;
            case R.id.ll_stationNo:
                Intent intentStation = new Intent(this, StationByFullParamsActivity.class);
                intentStation.putExtra("departmentId", departmentId + "");
                intentStation.putExtra("pipeId", pipeId + "");
                startActivityForResult(intentStation, SELECT_STATION);
                break;
            case R.id.btn_commit:
                if (TextUtils.isEmpty(etDistance.getText().toString())) {
                    ToastUtil.show("???????????????");
                    return;
                }
                if (TextUtils.isEmpty(tvStationNo.getText().toString().trim())) {
                    ToastUtil.show("???????????????");
                    return;
                }
                if (TextUtils.isEmpty(tvTime.getText().toString().trim())) {
                    ToastUtil.show("?????????????????????");
                    return;
                }
                if (TextUtils.isEmpty(etRemark.getText().toString().trim())) {
                    ToastUtil.show("???????????????");
                    return;
                }
                if (!NumberUtil.isNumber(etDistance.getText().toString())) {
                    ToastUtil.show("???????????????????????????");
                    return;
                }
                if (activityName.equals("windVane")) {
                    addSomeOthers("1");
                } else if (activityName.equals("advocacyBoard")) {
                    addSomeOthers("2");
//                    addBoard(etDistance.getText().toString());
                } else if (activityName.equals("videoMonitoring")) {
                    addSomeOthers("3");
//                    addVideo(etDistance.getText().toString());
                } else if (activityName.equals("other")) {
                    addSomeOthers("4");
//                    addOther(etDistance.getText().toString(), etName.getText().toString());
                }
                break;
        }
    }

    // ????????????
    private void initPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(mContext, "?????? ??????-???????????? ????????????????????????????????????", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {
            GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(this);
            } else {
                Log.i("tag", "????????????");
            }
        }
    }

    private void initTimePicker() {
        //Dialog ???????????????????????????
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                tvTime.setText(getTime(date));
            }
        }).setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
            @Override
            public void onTimeSelectChanged(Date date) {
                Log.i("pvTime", "onTimeSelectChanged");
            }
        }).setType(new boolean[]{true, true, true, false, false, false}).isDialog(true) //????????????false ??????????????????DecorView ????????????????????????
                .addOnCancelClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("pvTime", "onCancelClickListener");
                    }
                })
                .setItemVisibleCount(5) //?????????????????????????????????1???????????????6???????????????????????????7???
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
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//??????????????????
                dialogWindow.setGravity(Gravity.BOTTOM);//??????Bottom,????????????
                dialogWindow.setDimAmount(0.3f);
            }
        }
    }

    private String getTime(Date date) {//???????????????????????????????????????
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }
    private void addSomeOthers(String type) {
        StringBuilder photoSb = new StringBuilder();
        if (nameList.size() > 0) {
            for (int i = 0; i < nameList.size(); i++) {
                if (i != nameList.size() - 1) {
                    photoSb.append(nameList.get(i) + ";");
                } else {
                    photoSb.append(nameList.get(i));
                }
            }
        }
        JsonObject params = new JsonObject();
        params.addProperty("stakeid", Integer.valueOf(stationId));
        params.addProperty("pipeid", Integer.valueOf(pipeId));
        params.addProperty("stakefrom", etDistance.getText().toString());
        params.addProperty("perfectcondition", status);
        params.addProperty("remark", etRemark.getText().toString());
        params.addProperty("creator", userId);
        params.addProperty("othertype", type);
        params.addProperty("creatime", tvTime.getText().toString());
        if (!TextUtils.isEmpty(photoSb.toString())) {
            params.addProperty("uploadpicture", photoSb.toString());
        } else {
            params.addProperty("uploadpicture", "00");
        }
        Net.create(Api.class).addSomeOthers(token, params)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel result) {
                        ToastUtil.show(result.getMsg());
                        if (result.getCode() == Constant.SUCCESS_CODE) {
                            Intent intent = new Intent();
                            intent.putExtra("name", tvStationNo.getText().toString());
                            intent.putExtra("distance", etDistance.getText().toString());
                            intent.putExtra("id", stationId);
                            intent.setAction("com.action.update");
                            sendBroadcast(intent);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                });
    }

    private void addWindVanes(String distance) {
        JsonObject params = new JsonObject();
        params.addProperty("stakeid", Integer.valueOf(stationId));
        params.addProperty("windvanes", distance);
        Net.create(Api.class).addWindVane(token, params)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel result) {
                        ToastUtil.show(result.getMsg());
                        if (result.getCode() == Constant.SUCCESS_CODE) {
                            Intent intent = new Intent();
                            intent.putExtra("name", tvStationNo.getText().toString());
                            intent.putExtra("distance", etDistance.getText().toString());
                            intent.putExtra("id", stationId);
                            intent.setAction("com.action.update");
                            sendBroadcast(intent);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                });
    }

    private void addBoard(String distance) {
        JsonObject params = new JsonObject();
        params.addProperty("stakeid", Integer.valueOf(stationId));
        params.addProperty("pedurail", distance);
        Net.create(Api.class).addBoard(token, params)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel result) {
                        ToastUtil.show(result.getMsg());
                        if (result.getCode() == Constant.SUCCESS_CODE) {
                            Intent intent = new Intent();
                            intent.putExtra("name", tvStationNo.getText().toString());
                            intent.putExtra("distance", etDistance.getText().toString());
                            intent.putExtra("id", stationId);
                            intent.setAction("com.action.update");
                            sendBroadcast(intent);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                });
    }

    private void addVideo(String distance) {
        JsonObject params = new JsonObject();
        params.addProperty("stakeid", Integer.valueOf(stationId));
        params.addProperty("viewmonitor", distance);
        Net.create(Api.class).addVideoMonitoring(token, params)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel result) {
                        ToastUtil.show(result.getMsg());
                        if (result.getCode() == Constant.SUCCESS_CODE) {
                            Intent intent = new Intent();
                            intent.putExtra("name", tvStationNo.getText().toString());
                            intent.putExtra("distance", etDistance.getText().toString());
                            intent.putExtra("id", stationId);
                            intent.setAction("com.action.update");
                            sendBroadcast(intent);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                });
    }

    private void addOther(String distance, String name) {
        JsonObject params = new JsonObject();
        params.addProperty("stakeid", Integer.valueOf(stationId));
        params.addProperty("othername", name);
        params.addProperty("others", distance);
        Net.create(Api.class).addOthers(token, params)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel result) {
                        ToastUtil.show(result.getMsg());
                        if (result.getCode() == Constant.SUCCESS_CODE) {
                            Intent intent = new Intent();
                            intent.putExtra("name", tvStationNo.getText().toString());
                            intent.putExtra("distance", etDistance.getText().toString());
                            intent.putExtra("otherName", name);
                            intent.putExtra("id", stationId);
                            intent.setAction("com.action.update");
                            sendBroadcast(intent);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                });
    }

    public void uploadFiles(String fileName, String filePath) {
        PutObjectRequest put = new PutObjectRequest(Constant.BUCKETSTRING, fileName, filePath);
        // ??????????????????????????????
        OSSAsyncTask ossAsyncTask = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                nameList.add(fileName);
                if (nameList.size() == path.size()) {
                    WeiboDialogUtils.closeDialog(mWeiboDialog);
                }
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientException, ServiceException serviceException) {
                ToastUtil.show("?????????????????????");
                // ???????????????
                if (clientException != null) {
                    // ????????????????????????????????????
                }
                if (serviceException != null) {


                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == SELECT_STATION) {
            stationId = data.getStringExtra("stationId");
            pipeId = data.getStringExtra("pipeId");
            String stationName = data.getStringExtra("stationName");
            tvStationNo.setText(stationName);
        }

    }
}
