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
import com.gd.form.model.DepartmentPerson;
import com.gd.form.model.GlideImageLoader;
import com.gd.form.model.ServerModel;
import com.gd.form.model.StationWaterDetailModel;
import com.gd.form.model.WaterModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.SPUtil;
import com.gd.form.utils.TimeUtil;
import com.gd.form.utils.ToastUtil;
import com.gd.form.utils.WeiboDialogUtils;
import com.gd.form.view.ListDialog;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;
import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;
import com.yancy.gallerypick.inter.IHandlerCallBack;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SgbhActivity extends BaseActivity {
    @BindView(R.id.tv_location)
    TextView tv_location;
    @BindView(R.id.tv_sgxs)
    TextView tv_sgxs;
    @BindView(R.id.tv_cz)
    TextView tv_cz;
    @BindView(R.id.tv_jcsj)
    TextView tv_jcsj;
    @BindView(R.id.tv_tbrq)
    TextView tv_tbrq;
    @BindView(R.id.tv_zh)
    TextView tv_zh;
    @BindView(R.id.tv_fileName)
    TextView tv_fileName;
    @BindView(R.id.rg_isgood)
    RadioGroup rg_isgood;
    @BindView(R.id.rvResultPhoto)
    RecyclerView rvResultPhoto;
    @BindView(R.id.tv_spr)
    TextView tv_spr;
    @BindView(R.id.et_clqk)
    EditText et_clqk;
    @BindView(R.id.et_distance)
    EditText etDistance;
    @BindView(R.id.tv_manager)
    TextView tvManager;

    private TimePickerView pvTime;
    private ListDialog dialog;
    private int FILE_REQUEST_CODE = 100;
    private int SELECT_STATION = 101;
    private int SELECT_ADDRESS = 102;
    private int SELECT_APPROVER = 103;
    private int SELECT_WATER = 104;
    private int PERMISSIONS_REQUEST_READ_CONTACTS = 8;
    private IHandlerCallBack iHandlerCallBack;
    private List<String> path;
    private GalleryConfig galleryConfig;
    private PhotoAdapter photoAdapter;
    private String approverName;
    private String approverId;
    private List<String> nameList;
    private String location;
    private int stationId,pipeId;
    private String isFull = "???";
    private String token, userId, ownerId, waterId;
    private String ossFilePath;
    private OSSCredentialProvider ossCredentialProvider;
    private OSS oss;
    private String selectFileName;
    private String selectFilePath;
    private Dialog mWeiboDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTimePicker();
        path = new ArrayList<>();
        nameList = new ArrayList<>();
        token = (String) SPUtil.get(SgbhActivity.this, "token", "");
        userId = (String) SPUtil.get(SgbhActivity.this, "userId", "");
        initGallery();
        initConfig();
        initListener();
        ossCredentialProvider = new OSSPlainTextAKSKCredentialProvider(Constant.ACCESSKEYID, Constant.ACCESSKEYSECRET);
        oss = new OSSClient(mContext.getApplicationContext(), Constant.ENDPOINT, ossCredentialProvider);

    }

    //????????????
    private void initListener() {
        rg_isgood.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yes:
                        isFull = "???";
                        break;
                    case R.id.rb_no:
                        isFull = "???";
                        break;
                }
            }
        });
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
                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(SgbhActivity.this, "?????????...");
                mWeiboDialog.getWindow().setDimAmount(0f);
                for (int i = 0; i < path.size(); i++) {
                    String suffix = path.get(i).substring(path.get(i).length() - 4);
                    uploadFiles("W001/" + userId + "_" + TimeUtil.getFileNameTime() + "_" + i + suffix, path.get(i));
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

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_sgbh;
    }

    @OnClick({
            R.id.ll_location,
            R.id.ll_cz,
            R.id.ll_jcsj,
            R.id.ll_spr,
            R.id.ll_zh,
            R.id.ll_scfj,
            R.id.ll_tbrq,
            R.id.iv_back,
            R.id.rl_selectImage,
            R.id.btn_commit,
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            //????????????
            case R.id.btn_commit:
                if (paramsIsComplete()) {
                    commit();
                }
                break;
            case R.id.ll_location:
                Intent intent = new Intent(SgbhActivity.this, MapActivity.class);
                startActivityForResult(intent, SELECT_ADDRESS);
                break;
            case R.id.ll_sgxs:
                List<String> listM = new ArrayList<>();
                listM.add("??????????????????");
                listM.add("?????????");
                listM.add("??????");
                if (dialog == null) {
                    dialog = new ListDialog(mContext);
                }
                dialog.setData(listM);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tv_sgxs.setText(listM.get(positionM));
                    dialog.dismiss();
                });
                break;
            case R.id.ll_cz:
                List<String> listCz = new ArrayList<>();

                listCz.add("?????????");
                listCz.add("??????");
                if (dialog == null) {
                    dialog = new ListDialog(mContext);
                }
                dialog.setData(listCz);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tv_cz.setText(listCz.get(positionM));
                    dialog.dismiss();
                });
                break;
            case R.id.ll_zh:
                getWaterStations();
//                Intent intentStartStation = new Intent(this, WaterStationActivity.class);
//                startActivityForResult(intentStartStation, SELECT_STATION);
                break;
            case R.id.ll_jcsj:
            case R.id.ll_tbrq:
                pvTime.show(view);
                break;
            case R.id.ll_scfj:
                Intent intentAddress = new Intent(SgbhActivity.this, SelectFileActivity.class);
                startActivityForResult(intentAddress, FILE_REQUEST_CODE);
                break;
            case R.id.rl_selectImage:
                initPermissions();
                break;
            case R.id.ll_spr:
                getDefaultManager();
                break;
        }
    }

    private void getWaterStations() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("empid", userId);
        Net.create(Api.class).getWaterStations(token, jsonObject)
                .enqueue(new NetCallback<List<WaterModel>>(this, true) {
                    @Override
                    public void onResponse(List<WaterModel> list) {
                        Bundle bundle = new Bundle();
                        List<WaterModel> waterModels = new ArrayList<>();
                        if (list != null && list.size() > 0) {
                            for (int i = 0; i < list.size(); i++) {
                                WaterModel waterModel = list.get(i);
                                waterModel.setType("select");
                                waterModels.add(waterModel);
                            }
                            bundle.putSerializable("waters", (Serializable) waterModels);
                        }
                        Intent intent = new Intent(SgbhActivity.this, WaterProtectionListActivity.class);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, SELECT_WATER);

                    }
                });
    }

    //????????????
    private void commit() {
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
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("pipeid", Integer.valueOf(pipeId));
        jsonObject.addProperty("stakeid", Integer.valueOf(stationId));
        jsonObject.addProperty("hydraulicform", tv_sgxs.getText().toString());
        jsonObject.addProperty("material", etDistance.getText().toString());
        jsonObject.addProperty("condition", ownerId);
        jsonObject.addProperty("solution", et_clqk.getText().toString());
        jsonObject.addProperty("locate", location);
        jsonObject.addProperty("creator", userId);
        jsonObject.addProperty("creatime", TimeUtil.getCurrentTime());
        jsonObject.addProperty("approvalid", approverId);
        jsonObject.addProperty("waterid", waterId);
        if (!TextUtils.isEmpty(photoSb.toString())) {
            jsonObject.addProperty("picturepath", photoSb.toString());
        } else {
            jsonObject.addProperty("picturepath", "00");
        }
        if (!TextUtils.isEmpty(ossFilePath)) {
            jsonObject.addProperty("filepath", ossFilePath);
        } else {
            jsonObject.addProperty("filepath", "00");
        }
        Log.i("tag","jsonObject==="+jsonObject);
        Net.create(Api.class).commitWaterProtection(token, jsonObject)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel result) {
                        ToastUtil.show(result.getMsg());
                        if (result.getCode() == Constant.SUCCESS_CODE) {
                            Intent intent = new Intent();
                            intent.setAction("com.action.update.waitingTask");
                            sendBroadcast(intent);
                            finish();
                        }

                    }
                });

    }

    //???????????????????????????
    private boolean paramsIsComplete() {
        if (TextUtils.isEmpty(tv_zh.getText().toString())) {
            ToastUtil.show("???????????????");
            return false;
        }
        if (TextUtils.isEmpty(etDistance.getText().toString())) {
            ToastUtil.show("??????????????????");
            return false;
        }
        if (TextUtils.isEmpty(tv_sgxs.getText().toString())) {
            ToastUtil.show("?????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(tvManager.getText().toString())) {
            ToastUtil.show("???????????????????????????");
            return false;
        }
//        if (TextUtils.isEmpty(tv_cz.getText().toString())) {
//            ToastUtil.show("???????????????");
//            return false;
//        }
        if (TextUtils.isEmpty(et_clqk.getText().toString())) {
            ToastUtil.show("?????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(tv_location.getText().toString())) {
            ToastUtil.show("????????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(tv_spr.getText().toString())) {
            ToastUtil.show("??????????????????");
            return false;
        }
        return true;
    }

    // ????????????
    private void initPermissions() {
        if (ContextCompat.checkSelfPermission(SgbhActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(SgbhActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(mContext, "?????? ??????-???????????? ????????????????????????????????????", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(SgbhActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {
            GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(SgbhActivity.this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(SgbhActivity.this);
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
                if (v.getId() == R.id.ll_jcsj) {
                    tv_jcsj.setText(getTime(date));
                } else if (v.getId() == R.id.ll_tbrq) {
                    tv_tbrq.setText(getTime(date));
                }
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
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == FILE_REQUEST_CODE) {
            selectFileName = data.getStringExtra("fileName");
            selectFilePath = data.getStringExtra("selectFilePath");
            tv_fileName.setText(selectFileName);
            mWeiboDialog = WeiboDialogUtils.createLoadingDialog(SgbhActivity.this, "?????????...");
            mWeiboDialog.getWindow().setDimAmount(0f);
            uploadOffice(userId + "_" + TimeUtil.getFileNameTime() + "_" + selectFileName, selectFilePath);
            //????????????
        } else if (requestCode == SELECT_STATION) {
            stationId = Integer.parseInt(data.getStringExtra("stationId"));
            pipeId = Integer.parseInt(data.getStringExtra("pipeId"));
            String stationName = data.getStringExtra("stationName");
            tv_zh.setText(stationName);
//            stationId = data.getStringExtra("stationId");
//            pipeId = data.getStringExtra("pipeId");
//            String stationName = data.getStringExtra("stationName");
//            tv_zh.setText(stationName);
        } else if (requestCode == SELECT_ADDRESS) {
            String latitude = data.getStringExtra("latitude");
            String longitude = data.getStringExtra("longitude");
            location = longitude + "," + latitude;
            if (!TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude)) {
                tv_location.setText("??????:" + longitude + "   ??????:" + latitude);
            }
        } else if (requestCode == SELECT_APPROVER) {
            approverName = data.getStringExtra("name");
            approverId = data.getStringExtra("id");
            if (!TextUtils.isEmpty(approverName)) {
                tv_spr.setText(approverName);
            }
        } else if (requestCode == SELECT_WATER) {
            waterId = data.getStringExtra("waterId");
            String name = data.getStringExtra("name");
            tv_zh.setText(name);
            getWaterDetail(waterId);
        }

    }

    private void getWaterDetail(String waterId) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", Integer.parseInt(waterId));
        jsonObject.addProperty("empid", userId);
        Log.i("tag","jsonObject=="+jsonObject);
        Net.create(Api.class).getWaterStationDetail(token, jsonObject)
                .enqueue(new NetCallback<StationWaterDetailModel>(this, true) {
                    @Override
                    public void onResponse(StationWaterDetailModel result) {
                        ownerId = result.getOwnerid();
                        pipeId = result.getPipeid();
                        approverId = result.getApprovalid();
                        stationId = result.getStakeid();
                        etDistance.setText(result.getStakefrom());
                        tv_sgxs.setText(result.getHydraulicform());
                        tvManager.setText(result.getOwnername());
                        tv_spr.setText(result.getApprovalname());
                    }
                });
    }
    private void getDefaultManager() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("empid", userId);
        Net.create(Api.class).getTunnelDefaultManager(token, jsonObject)
                .enqueue(new NetCallback<List<DepartmentPerson>>(this, true) {
                    @Override
                    public void onResponse(List<DepartmentPerson> list) {
                        List<String> nameList = new ArrayList<>();
                        List<String> idList = new ArrayList<>();
                        if(list!=null && list.size()>0){
                            for (int i = 0; i <list.size() ; i++) {
                                DepartmentPerson departmentPerson = list.get(i);
                                nameList.add(departmentPerson.getName());
                                idList.add(departmentPerson.getId());
                            }
                            if (dialog == null) {
                                dialog = new ListDialog(mContext);
                            }
                            dialog.setData(nameList);
                            dialog.show();
                            dialog.setListItemClick(positionM -> {
                                tv_spr.setText(nameList.get(positionM));
                                approverId = idList.get(positionM);
                                dialog.dismiss();
                            });
                        }
                    }
                });
    }
    //?????????????????????
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

    public void uploadOffice(String fileName, String filePath) {
        PutObjectRequest put = new PutObjectRequest(Constant.BUCKETSTRING, fileName, filePath);
        // ??????????????????????????????
        OSSAsyncTask ossAsyncTask = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                ossFilePath = fileName;
                WeiboDialogUtils.closeDialog(mWeiboDialog);
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
}
