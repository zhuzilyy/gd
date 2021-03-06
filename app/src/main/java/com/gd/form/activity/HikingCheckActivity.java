package com.gd.form.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.gd.form.R;
import com.gd.form.adapter.PhotoAdapter;
import com.gd.form.base.BaseActivity;
import com.gd.form.constants.Constant;
import com.gd.form.model.Department;
import com.gd.form.model.GlideImageLoader;
import com.gd.form.model.Pipelineinfo;
import com.gd.form.model.ServerModel;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class HikingCheckActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_pipeName)
    TextView tvPipeName;
    @BindView(R.id.tv_startStationNo)
    TextView tvStartStationNo;
    @BindView(R.id.tv_endStationNo)
    TextView tvEndStationNo;
    @BindView(R.id.tv_spr)
    TextView tvSpr;
    @BindView(R.id.tv_fileName)
    TextView tvFileName;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.et_allowPeople)
    TextView etAllowPeople;
    @BindView(R.id.rg_isPipeExpose)
    RadioGroup rg_isPipeExpose;
    @BindView(R.id.rg_isBuild)
    RadioGroup rg_isBuild;
    @BindView(R.id.rg_isCar)
    RadioGroup rg_isCar;
    @BindView(R.id.rg_isNew)
    RadioGroup rg_isNew;
    @BindView(R.id.rg_isFull)
    RadioGroup rg_isFull;
    @BindView(R.id.rg_isCorrect)
    RadioGroup rg_isCorrect;
    @BindView(R.id.rg_isUseful)
    RadioGroup rg_isUseful;
    @BindView(R.id.rg_isProtect)
    RadioGroup rg_isProtect;
    @BindView(R.id.rg_isRelative)
    RadioGroup rg_isRelative;
    @BindView(R.id.rg_isIllegal)
    RadioGroup rg_isIllegal;
    @BindView(R.id.rg_isWeightCar)
    RadioGroup rg_isWeightCar;
    @BindView(R.id.rg_isTimely)
    RadioGroup rg_isTimely;
    @BindView(R.id.rg_isWearing)
    RadioGroup rg_isWearing;
    @BindView(R.id.rg_isWriting)
    RadioGroup rg_isWriting;
    @BindView(R.id.rg_isSafe)
    RadioGroup rg_isSafe;
    @BindView(R.id.et_advice)
    EditText etAdvice;
    @BindView(R.id.rvResultPhoto)
    RecyclerView rvResultPhoto;
    private List<Pipelineinfo> pipeLineinfoList;
    private List<Department> departmentList;
    private ListDialog dialog;
    private int FILE_REQUEST_CODE = 100;
    private int SELECT_STATION = 101;
    private int SELECT_APPROVER = 102;
    private int SELECT_ADDRESS = 103;
    private int PERMISSIONS_REQUEST_READ_CONTACTS = 8;
    private String approverName;
    private String approverId;
    private int pipeId,departmentId;
    private Dialog mWeiboDialog;
    private OSSCredentialProvider ossCredentialProvider;
    private OSS oss;
    private String token, userId;
    private String ossFilePath;
    private String selectFileName;
    private String selectFilePath;
    private String startStationId, endStationId,location;
    private IHandlerCallBack iHandlerCallBack;
    private List<String> path;
    private GalleryConfig galleryConfig;
    private PhotoAdapter photoAdapter;
    private List<String> nameList;
    private String col1 = "???";
    private String col2 = "???";
    private String col3 = "???";
    private String col4 = "???";
    private String col5 = "???";
    private String col6 = "???";
    private String col7 = "???";
    private String col8 = "???";
    private String col9 = "???";
    private String col10 = "???";
    private String col11 = "???";
    private String col12 = "???";
    private String col13 = "???";
    private String col14 = "???";
    private String col15 = "???";
    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_hiking_check;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("???????????????");
        path = new ArrayList<>();
        nameList = new ArrayList<>();
        dialog = new ListDialog(this);
        token = (String) SPUtil.get(this, "token", "");
        userId = (String) SPUtil.get(this, "userId", "");
        ossCredentialProvider = new OSSPlainTextAKSKCredentialProvider(Constant.ACCESSKEYID, Constant.ACCESSKEYSECRET);
        oss = new OSSClient(mContext.getApplicationContext(), Constant.ENDPOINT, ossCredentialProvider);
        getPipelineInfoListRequest();
        pipeDepartmentInfoGetList();
        initListener();
        initGallery();
        initConfig();
    }

    private void initListener() {
        //????????????????????????
        rg_isPipeExpose.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesExpose:
                        col1 = "???";
                        break;
                    case R.id.rb_noExpose:
                        col1 = "???";
                        break;
                }
            }
        });
        //100??????????????????????????????????????????
        rg_isBuild.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesBuild:
                        col2 = "???";
                        break;
                    case R.id.rb_noBuild:
                        col2 = "???";
                        break;
                }
            }
        });
        //????????????????????????????????????????????????
        rg_isCar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesCar:
                        col3 = "???";
                        break;
                    case R.id.rb_noCar:
                        col3 = "???";
                        break;
                }
            }
        });//???????????????????????????????????????
        rg_isNew.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesNew:
                        col4 = "???";
                        break;
                    case R.id.rb_noNew:
                        col4 = "???";
                        break;
                }
            }
        });
        //????????????????????????
        rg_isFull.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesFull:
                        col5 = "???";
                        break;
                    case R.id.rb_noFull:
                        col5 = "???";
                        break;
                }
            }
        });
        //??????????????????????????????
        rg_isCorrect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesCorrect:
                        col6 = "???";
                        break;
                    case R.id.rb_noCorrect:
                        col6 = "???";
                        break;
                }
            }
        });
        //??????????????????????????????
        rg_isUseful.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesUseful:
                        col7 = "???";
                        break;
                    case R.id.rb_noUseful:
                        col7 = "???";
                        break;
                }
            }
        });
        //??????????????????????????????
        rg_isProtect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesProtect:
                        col8 = "???";
                        break;
                    case R.id.rb_noProtect:
                        col8 = "???";
                        break;
                }
            }
        });
        //??????????????????????????????????????????
        rg_isRelative.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesRelative:
                        col9 = "???";
                        break;
                    case R.id.rb_noRelative:
                        col9 = "???";
                        break;
                }
            }
        });
        //?????????????????????????????????
        rg_isIllegal.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesIllegal:
                        col10 = "???";
                        break;
                    case R.id.rb_noIllegal:
                        col10 = "???";
                        break;
                }
            }
        });
        //?????????????????????????????????
        rg_isWeightCar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesWeightCar:
                        col11 = "???";
                        break;
                    case R.id.rb_noWeightCar:
                        col11 = "???";
                        break;
                }
            }
        });
        //???????????????????????????????????????
        rg_isTimely.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesTimely:
                        col12 = "???";
                        break;
                    case R.id.rb_noTimely:
                        col12 = "???";
                        break;
                }
            }
        });
        //??????????????????????????????????????????
        rg_isWearing.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesWearing:
                        col13 = "???";
                        break;
                    case R.id.rb_noWearing:
                        col13 = "???";
                        break;
                }
            }
        });
        //??????????????????????????????????????????
        rg_isWriting.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesWriting:
                        col14 = "???";
                        break;
                    case R.id.rb_noWriting:
                        col14 = "???";
                        break;
                }
            }
        });
        //???????????????????????????????????????
        rg_isSafe.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesSafe:
                        col15 = "???";
                        break;
                    case R.id.rb_noSafe:
                        col15 = "???";
                        break;
                }
            }
        });
    }

    private void pipeDepartmentInfoGetList() {
        Net.create(Api.class).pipedepartmentinfoGetList(token)
                .enqueue(new NetCallback<List<Department>>(this, false) {
                    @Override
                    public void onResponse(List<Department> list) {
                        departmentList = list;
                    }
                });
    }

    private void getPipelineInfoListRequest() {
        Net.create(Api.class).pipelineinfosget(token)
                .enqueue(new NetCallback<List<Pipelineinfo>>(this, false) {
                    @Override
                    public void onResponse(List<Pipelineinfo> list) {
                        pipeLineinfoList = list;
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
                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(HikingCheckActivity.this, "?????????...");
                mWeiboDialog.getWindow().setDimAmount(0f);
                for (int i = 0; i < path.size(); i++) {
                    String suffix = path.get(i).substring(path.get(i).length() - 4);
                    uploadFiles(userId + "_" + TimeUtil.getFileNameTime() + "_" + i + suffix, path.get(i));
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
    @OnClick({R.id.iv_back,
            R.id.ll_pipeName,
            R.id.tv_startStationNo,
            R.id.tv_endStationNo,
            R.id.ll_location,
            R.id.ll_scfj,
            R.id.ll_spr,
            R.id.ll_area,
            R.id.btn_commit,
            R.id.ll_selectPic,
    })
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_selectPic:
                initPermissions();
                break;
            case R.id.btn_commit:
                if (paramsComplete()) {
                    commit();
                }
                break;
            case R.id.ll_area:
                List<String> areaList = new ArrayList<>();
                List<Integer> idList = new ArrayList<>();
                if (departmentList != null && departmentList.size() > 0) {
                    for (int i = 0; i < departmentList.size(); i++) {
                        areaList.add(departmentList.get(i).getName());
                        idList.add(departmentList.get(i).getId());
                    }
                }
                dialog.setData(areaList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvArea.setText(areaList.get(positionM));
                    departmentId = idList.get(positionM);
                    dialog.dismiss();
                });
                break;
            case R.id.ll_location:
                Intent intent = new Intent(this, MapActivity.class);
                startActivityForResult(intent, SELECT_ADDRESS);
                break;
            case R.id.tv_startStationNo:
                Intent intentStartStation = new Intent(this, StationActivity.class);
                intentStartStation.putExtra("tag", "start");
                startActivityForResult(intentStartStation, SELECT_STATION);
                break;
            case R.id.tv_endStationNo:
                Intent intentEndStation = new Intent(this, StationActivity.class);
                intentEndStation.putExtra("tag", "end");
                startActivityForResult(intentEndStation, SELECT_STATION);
                break;
            case R.id.ll_spr:
                Intent intentApprover = new Intent(this, ApproverActivity.class);
                startActivityForResult(intentApprover, SELECT_APPROVER);
                break;
            case R.id.ll_pipeName:
                List<String> pipeNameList = new ArrayList<>();
                List<Integer> pipeIdList = new ArrayList<>();
                if (pipeLineinfoList != null && pipeLineinfoList.size() > 0) {
                    for (int i = 0; i < pipeLineinfoList.size(); i++) {
                        pipeNameList.add(pipeLineinfoList.get(i).getName());
                        pipeIdList.add(pipeLineinfoList.get(i).getId());
                    }
                }
                dialog.setData(pipeNameList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvPipeName.setText(pipeNameList.get(positionM));
                    pipeId = pipeIdList.get(positionM);
                    dialog.dismiss();
                });
                break;
            case R.id.ll_scfj:
                Intent intentAddress = new Intent(this, SelectFileActivity.class);
                startActivityForResult(intentAddress, FILE_REQUEST_CODE);
                break;


        }
    }

    private boolean paramsComplete() {
        if (TextUtils.isEmpty(tvPipeName.getText().toString())) {
            ToastUtil.show("?????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(tvStartStationNo.getText().toString())) {
            ToastUtil.show("?????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(tvEndStationNo.getText().toString())) {
            ToastUtil.show("?????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(etAllowPeople.getText().toString())) {
            ToastUtil.show("??????????????????");
            return false;
        }
        if (TextUtils.isEmpty(tvArea.getText().toString())) {
            ToastUtil.show("????????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(etAdvice.getText().toString())) {
            ToastUtil.show("?????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(tvLocation.getText().toString())) {
            ToastUtil.show("???????????????");
            return false;
        }
        if (TextUtils.isEmpty(tvLocation.getText().toString())) {
            ToastUtil.show("??????????????????");
            return false;
        }
        return true;
    }

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
        jsonObject.addProperty("pipeid", pipeId);
        jsonObject.addProperty("fromstakeid", Integer.valueOf(startStationId));
        jsonObject.addProperty("tostakeid", Integer.valueOf(endStationId));
        jsonObject.addProperty("departmentid", departmentId);
        jsonObject.addProperty("routeinspect", etAllowPeople.getText().toString());
        jsonObject.addProperty("locate", location);
        jsonObject.addProperty("creator", userId);
        jsonObject.addProperty("creatime", TimeUtil.getCurrentTime());
        jsonObject.addProperty("approvalid", approverId);
        jsonObject.addProperty("col1", col1);
        jsonObject.addProperty("col2", col2);
        jsonObject.addProperty("col3", col3);
        jsonObject.addProperty("col4", col4);
        jsonObject.addProperty("col5", col5);
        jsonObject.addProperty("col6", col6);
        jsonObject.addProperty("col7", col7);
        jsonObject.addProperty("col8", col8);
        jsonObject.addProperty("col9", col9);
        jsonObject.addProperty("col10", col10);
        jsonObject.addProperty("col11", col11);
        jsonObject.addProperty("col12", col12);
        jsonObject.addProperty("col13", col13);
        jsonObject.addProperty("col14", col14);
        jsonObject.addProperty("col15", col15);
        jsonObject.addProperty("col16", etAdvice.getText().toString());
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
        Net.create(Api.class).commitHiking(token, jsonObject)
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == FILE_REQUEST_CODE) {
            selectFileName = data.getStringExtra("fileName");
            selectFilePath = data.getStringExtra("selectFilePath");
            tvFileName.setText(selectFileName);
            mWeiboDialog = WeiboDialogUtils.createLoadingDialog(this, "?????????...");
            mWeiboDialog.getWindow().setDimAmount(0f);
            uploadOffice(userId + "_" + TimeUtil.getFileNameTime() + "_" + selectFileName, selectFilePath);
            //????????????
        } else if (requestCode == SELECT_STATION) {
            String selectTag = data.getStringExtra("selectTag");
            String stationName = data.getStringExtra("stationName");

            if (!TextUtils.isEmpty(selectTag)) {
                if (selectTag.equals("start")) {
                    tvStartStationNo.setText(stationName);
                    startStationId = data.getStringExtra("stationId");
                } else {
                    tvEndStationNo.setText(stationName);
                    endStationId = data.getStringExtra("stationId");
                }
            }

        } else if (requestCode == SELECT_ADDRESS) {
            String latitude = data.getStringExtra("latitude");
            String longitude = data.getStringExtra("longitude");
            location = longitude + "," + latitude;
            if (!TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude)) {
                tvLocation.setText("??????:" + longitude + "   ??????:" + latitude);
            }
        } else if (requestCode == SELECT_APPROVER) {
            approverName = data.getStringExtra("name");
            approverId = data.getStringExtra("id");
            if (!TextUtils.isEmpty(approverName)) {
                tvSpr.setText(approverName);
            }
        }

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
