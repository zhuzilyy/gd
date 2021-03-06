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
import com.gd.form.R;
import com.gd.form.adapter.PhotoAdapter;
import com.gd.form.base.BaseActivity;
import com.gd.form.constants.Constant;
import com.gd.form.model.Department;
import com.gd.form.model.DepartmentPerson;
import com.gd.form.model.GlideImageLoader;
import com.gd.form.model.ServerModel;
import com.gd.form.model.WaterInsuranceDetailModel;
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
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class WaterInsuranceActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.tv_weather)
    TextView tvWeather;
    @BindView(R.id.tv_stationNo)
    TextView tvStationNo;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.tv_spr)
    TextView tvSpr;
    @BindView(R.id.tv_fileName)
    TextView tvFileName;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.rvResultPhoto)
    RecyclerView rvResultPhoto;
    @BindView(R.id.et_company)
    EditText etCompany;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_area)
    EditText etArea;
    @BindView(R.id.et_working_plane)
    EditText etWorkingPlane;
    @BindView(R.id.et_problem)
    EditText etProblem;
    @BindView(R.id.et_advice)
    EditText etAdvice;
    @BindView(R.id.et_manager)
    EditText etManager;
    @BindView(R.id.et_finishCount)
    EditText etFinishCount;
    @BindView(R.id.et_other)
    EditText etOther;
    @BindView(R.id.rg_handle)
    RadioGroup rgHandle;
    @BindView(R.id.ll_location)
    LinearLayout llLocation;
    @BindView(R.id.et_distance)
    EditText etDistance;
    @BindView(R.id.et_BuildingCompany)
    EditText etBuildingCompany;
    @BindView(R.id.et_location)
    EditText etLocation;
    @BindView(R.id.et_weather)
    EditText etWeather;
    @BindView(R.id.et_process)
    EditText etProcess;
    @BindView(R.id.rg_isHidden)
    RadioGroup rg_isHidden;
    private List<Department> departmentList;
    private ListDialog dialog;
    private int FILE_REQUEST_CODE = 100;
    private int SELECT_STATION = 101;
    private int SELECT_APPROVER = 102;
    private int SELECT_ADDRESS = 103;
    private int SELECT_AREA = 104;
    private int PERMISSIONS_REQUEST_READ_CONTACTS = 8;
    private String approverName;
    private String approverId;
    private Dialog mWeiboDialog;
    private OSSCredentialProvider ossCredentialProvider;
    private OSS oss;
    private String token, userId;
    private String ossFilePath;
    private String selectFileName;
    private String selectFilePath;
    private IHandlerCallBack iHandlerCallBack;
    private List<String> path;
    private GalleryConfig galleryConfig;
    private PhotoAdapter photoAdapter;
    private List<String> nameList;
    private int departmentId;
    private String stationId, stakeId;
    private String handlemode = "???????????????????????????";
    private String isHidden = "???";

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));

    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_water_insurance;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("????????????????????????");
        llLocation.setVisibility(View.GONE);
        dialog = new ListDialog(this);
        path = new ArrayList<>();
        nameList = new ArrayList<>();
        token = (String) SPUtil.get(this, "token", "");
        userId = (String) SPUtil.get(this, "userId", "");
        ossCredentialProvider = new OSSPlainTextAKSKCredentialProvider(Constant.ACCESSKEYID, Constant.ACCESSKEYSECRET);
        oss = new OSSClient(mContext.getApplicationContext(), Constant.ENDPOINT, ossCredentialProvider);
        //??????????????????
        pipeDepartmentInfoGetList();
        initGallery();
        initConfig();
        initListener();
    }

    private void initListener() {
        rgHandle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_mouth:
                        handlemode = "???????????????????????????";
                        break;
                    case R.id.rb_mouthUpload:
                        handlemode = "????????????????????????";
                        break;
                    case R.id.rb_writeUpload:
                        handlemode = "???????????????????????????";
                        break;
                }
            }
        });
        rg_isHidden.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_no:
                        isHidden = "???";
                        break;
                    case R.id.rb_yes:
                        isHidden = "???";
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
                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(WaterInsuranceActivity.this, "?????????...");
                mWeiboDialog.getWindow().setDimAmount(0f);
                for (int i = 0; i < path.size(); i++) {
                    String suffix = path.get(i).substring(path.get(i).length() - 4);
                    uploadFiles("W015/" + userId + "_" + TimeUtil.getFileNameTime() + "_" + i + suffix, path.get(i));
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


    private void pipeDepartmentInfoGetList() {
        Net.create(Api.class).pipedepartmentinfoGetList(token)
                .enqueue(new NetCallback<List<Department>>(this, false) {
                    @Override
                    public void onResponse(List<Department> list) {
                        departmentList = list;
                    }
                });
    }

    @OnClick({R.id.iv_back,
            R.id.ll_area,
            R.id.ll_weather,
            R.id.ll_zh,
            R.id.ll_selectImage,
            R.id.ll_location,
            R.id.ll_spr,
            R.id.ll_scfj,
            R.id.ll_address,
            R.id.btn_commit,
            R.id.ll_stationNo,
    })
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_stationNo:
                getWaters();
                break;
            case R.id.ll_address:
                Intent intentArea = new Intent(this, MapActivity.class);
                startActivityForResult(intentArea, SELECT_AREA);
                break;
            case R.id.btn_commit:
                if (paramsComplete()) {
                    commit();
                }
                break;
            case R.id.ll_scfj:
                Intent intentAddress = new Intent(this, SelectFileActivity.class);
                startActivityForResult(intentAddress, FILE_REQUEST_CODE);
                break;
            case R.id.ll_spr:
                getDefaultManager();
                break;
            case R.id.ll_location:
                Intent intent = new Intent(this, MapActivity.class);
                startActivityForResult(intent, SELECT_ADDRESS);
                break;
            case R.id.ll_selectImage:
                initPermissions();
                break;
//            case R.id.ll_zh:
//                Intent intentStartStation = new Intent(this, StationActivity.class);
//                startActivityForResult(intentStartStation, SELECT_STATION);
//                break;
            case R.id.ll_weather:
                List<String> weatherList = new ArrayList<>();
                weatherList.add("???");
                weatherList.add("???");
                weatherList.add("??????");
                weatherList.add("??????");
                weatherList.add("???");
                dialog.setData(weatherList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvWeather.setText(weatherList.get(positionM));
                    dialog.dismiss();
                });
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
        }
    }

    private void getWaters() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("empid", userId);
        Net.create(Api.class).getWatersByDepartment(token, jsonObject)
                .enqueue(new NetCallback<List<WaterModel>>(this, true) {
                    @Override
                    public void onResponse(List<WaterModel> list) {
                        List<WaterModel> waterModelList = new ArrayList<>();
                        if (list != null && list.size() > 0) {
                            for (int i = 0; i < list.size(); i++) {
                                WaterModel waterModel = list.get(i);
                                waterModel.setType("select");
                                waterModelList.add(waterModel);
                            }
                            Intent intent = new Intent(WaterInsuranceActivity.this, WaterProtectionListActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("waters", (Serializable) waterModelList);
                            intent.putExtras(bundle);
                            startActivityForResult(intent, SELECT_STATION);
                        }
                    }
                });
    }

    private boolean paramsComplete() {
        if (TextUtils.isEmpty(tvStationNo.getText().toString())) {
            ToastUtil.show("???????????????");
            return false;
        }
        if (TextUtils.isEmpty(etDistance.getText().toString())) {
            ToastUtil.show("???????????????");
            return false;
        }
        if (TextUtils.isEmpty(etName.getText().toString())) {
            ToastUtil.show("?????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(etLocation.getText().toString())) {
            ToastUtil.show("?????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(etBuildingCompany.getText().toString())) {
            ToastUtil.show("?????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(etWeather.getText().toString())) {
            ToastUtil.show("???????????????");
            return false;
        }
        if (TextUtils.isEmpty(etProblem.getText().toString())) {
            ToastUtil.show("??????????????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(etAdvice.getText().toString())) {
            ToastUtil.show("?????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(etManager.getText().toString())) {
            ToastUtil.show("????????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(etProcess.getText().toString())) {
            ToastUtil.show("?????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(tvSpr.getText().toString())) {
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
        jsonObject.addProperty("departmentid", departmentId);
        jsonObject.addProperty("stakeid", Integer.parseInt(stakeId));
        jsonObject.addProperty("stakefrom", etDistance.getText().toString());
        jsonObject.addProperty("protectname", etName.getText().toString());
        jsonObject.addProperty("protectunit", etBuildingCompany.getText().toString());
        jsonObject.addProperty("locations", etLocation.getText().toString());
        jsonObject.addProperty("weathers", etWeather.getText().toString());
        jsonObject.addProperty("col1", etProblem.getText().toString());
        jsonObject.addProperty("col1handle", etAdvice.getText().toString());
        jsonObject.addProperty("handlemode", handlemode);
        jsonObject.addProperty("constructionhandler", etManager.getText().toString());
        jsonObject.addProperty("col2", etProcess.getText().toString());
        jsonObject.addProperty("col3", isHidden);
        jsonObject.addProperty("locate", tvAddress.getText().toString());
        jsonObject.addProperty("creator", userId);
        jsonObject.addProperty("creatime", TimeUtil.getCurrentTime());
        jsonObject.addProperty("approvalid", approverId);
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
        Net.create(Api.class).commitWaterInsurance(token, jsonObject)
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

    //????????????
    private void initPermissions() {
        if (ContextCompat.checkSelfPermission(WaterInsuranceActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(WaterInsuranceActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(mContext, "?????? ??????-???????????? ????????????????????????????????????", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(WaterInsuranceActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {
            GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(WaterInsuranceActivity.this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(WaterInsuranceActivity.this);
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
            uploadOffice("W015/" + userId + "_" + TimeUtil.getFileNameTime() + "_" + selectFileName, selectFilePath);
            //????????????
        } else if (requestCode == SELECT_STATION) {
            stationId = data.getStringExtra("waterId");
            String stationName = data.getStringExtra("name");
            stakeId = data.getStringExtra("stakeId");
            tvStationNo.setText(stationName);
            getWaterDetail(stationId);
        } else if (requestCode == SELECT_ADDRESS) {
            String latitude = data.getStringExtra("latitude");
            String longitude = data.getStringExtra("longitude");
            if (!TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude)) {
                tvLocation.setText("??????:" + longitude + "   ??????:" + latitude);
            }
        } else if (requestCode == SELECT_APPROVER) {
            approverName = data.getStringExtra("name");
            approverId = data.getStringExtra("id");
            if (!TextUtils.isEmpty(approverName)) {
                tvSpr.setText(approverName);
            }
        } else if (requestCode == SELECT_AREA) {
            String area = data.getStringExtra("area");
            tvAddress.setText(area);
        }

    }

    private void getWaterDetail(String stationId) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", Integer.parseInt(stationId));
        Net.create(Api.class).getWatersByKey(token, jsonObject)
                .enqueue(new NetCallback<WaterInsuranceDetailModel>(this, true) {
                    @Override
                    public void onResponse(WaterInsuranceDetailModel detailModel) {
                        etName.setText(detailModel.getProtectname());
                        etDistance.setText(detailModel.getStakefrom());
                        etLocation.setText(detailModel.getLocations());
                        etBuildingCompany.setText(detailModel.getProtectunit());
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
                                tvSpr.setText(nameList.get(positionM));
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
