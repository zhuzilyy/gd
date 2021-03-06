package com.gd.form.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.gd.form.R;
import com.gd.form.adapter.PhotoAdapter;
import com.gd.form.base.BaseActivity;
import com.gd.form.constants.Constant;
import com.gd.form.model.GlideImageLoader;
import com.gd.form.model.Pipelineinfo;
import com.gd.form.model.ServerModel;
import com.gd.form.model.TunnelModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.NumberUtil;
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

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PipeTunnelActivity extends BaseActivity implements AMapLocationListener {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.tv_pipeName)
    TextView tvPipeName;
    @BindView(R.id.tv_startStationNo)
    TextView tvStartStationNo;
    @BindView(R.id.tv_endStationNo)
    TextView tvEndStationNo;
    @BindView(R.id.et_pipeName)
    EditText etPipeName;
    @BindView(R.id.et_pipeDepth)
    EditText etPipeDepth;
    @BindView(R.id.et_method)
    EditText etMethod;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.ll_pipeName)
    LinearLayout llPipeName;
    @BindView(R.id.ll_status)
    LinearLayout llStatus;
    @BindView(R.id.rvResultPhoto)
    RecyclerView rvResultPhoto;
    @BindView(R.id.tv_fileName)
    TextView tvFileName;
    @BindView(R.id.rl_selectImage)
    RelativeLayout rlSelectImage;
    @BindView(R.id.ll_selectFile)
    LinearLayout llSelectFile;
    @BindView(R.id.et_location)
    EditText etLocation;
    @BindView(R.id.ll_startStationNo)
    LinearLayout llStartStationNo;
    @BindView(R.id.ll_endStationNo)
    LinearLayout llEndStationNo;
    private String token, userId, stationId, pipeName, stationName, tunnelId;
    private final int SEARCH_TUNNEL = 100;
    private final int INPUT_STATUS = 101;
    private int FILE_REQUEST_CODE = 102;
    private int SELECT_STATION = 103;
    private int PERMISSIONS_REQUEST_READ_CONTACTS = 8;
    private List<Pipelineinfo> pipelineInfoList;
    private ListDialog dialog;
    private OSSCredentialProvider ossCredentialProvider;
    private OSS oss;
    private String ossFilePath;
    private String selectFileName;
    private String selectFilePath;
    private IHandlerCallBack iHandlerCallBack;
    private List<String> path;
    private GalleryConfig galleryConfig;
    private PhotoAdapter photoAdapter;
    private List<String> nameList;
    private Dialog mWeiboDialog;
    public AMapLocationClient mlocationClient;
    public AMapLocationClientOption mLocationOption = null;
    private int  pipeId;
    private String startStationId, endStationId,startPipeId,endPipeId,tag,fileName,uploadFilePath;
    /**
     * ?????????????????????????????????
     */
    protected String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };
    //???????????????target > 28????????????????????????????????????????????????"????????????"???????????????
    private static String BACKGROUND_LOCATION_PERMISSION = "android.permission.ACCESS_BACKGROUND_LOCATION";
    /**
     * ????????????????????????????????????????????????
     */
    private boolean isNeedCheck = true;
    private static final int PERMISSON_REQUESTCODE = 0;
    //????????????????????????????????????????????????true???????????????????????????????????????????????????????????????
    private boolean needCheckBackLocation = false;
    private boolean isLoactionSuccess;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_tunnel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("??????");
        dialog = new ListDialog(this);
        token = (String) SPUtil.get(PipeTunnelActivity.this, "token", "");
        userId = (String) SPUtil.get(PipeTunnelActivity.this, "userId", "");
        ossCredentialProvider = new OSSPlainTextAKSKCredentialProvider(Constant.ACCESSKEYID, Constant.ACCESSKEYSECRET);
        oss = new OSSClient(mContext.getApplicationContext(), Constant.ENDPOINT, ossCredentialProvider);
        path = new ArrayList<>();
        nameList = new ArrayList<>();
        getPipelineInfoListRequest();
        initGallery();
        initConfig();
        getLocation();
        if (getIntent() != null) {
            tag = getIntent().getExtras().getString("tag");
            tunnelId = getIntent().getExtras().getString("tunnelId");
//            stationId = getIntent().getExtras().getString("stationId");
//            pipeId = getIntent().getExtras().getString("pipeId");
//            String pipeName = getIntent().getExtras().getString("pipeName");
            if (!TextUtils.isEmpty(tunnelId)) {
                getTunnelData(tunnelId);
            }
            if ("update".equals(tag)) {
                tvPipeName.setEnabled(true);
                etLocation.setEnabled(true);
                tvStartStationNo.setEnabled(true);
                tvEndStationNo.setEnabled(true);
                etPipeName.setEnabled(true);
                etPipeDepth.setEnabled(true);
                etMethod.setEnabled(true);
                llPipeName.setEnabled(true);
                llStatus.setEnabled(true);
                rlSelectImage.setEnabled(true);
                llSelectFile.setEnabled(true);
                llStartStationNo.setEnabled(true);
                llEndStationNo.setEnabled(true);
                btnCommit.setVisibility(View.VISIBLE);
                llSearch.setVisibility(View.GONE);
            } else if ("check".equals(tag)) {
                tvPipeName.setEnabled(false);
                tvStartStationNo.setEnabled(false);
                etPipeName.setEnabled(false);
                etPipeDepth.setEnabled(false);
                etMethod.setEnabled(false);
                tvStatus.setEnabled(false);
                etLocation.setEnabled(false);
                llPipeName.setEnabled(false);
                tvEndStationNo.setEnabled(false);
                llStatus.setEnabled(false);
                rlSelectImage.setEnabled(false);
                llSelectFile.setEnabled(true);
                llEndStationNo.setEnabled(false);
                llStartStationNo.setEnabled(false);
                btnCommit.setVisibility(View.GONE);
                llSearch.setVisibility(View.GONE);
            }
        }
    }

    private void getLocation() {
        mlocationClient = new AMapLocationClient(getApplicationContext());
        //?????????????????????
        mLocationOption = new AMapLocationClientOption();
        //??????????????????
        mlocationClient.setLocationListener(this);
        //???????????????????????????????????????Battery_Saving?????????????????????Device_Sensors??????????????????
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //??????????????????,????????????,?????????2000ms
        mLocationOption.setInterval(2000);
        //??????????????????
        mlocationClient.setLocationOption(mLocationOption);
        // ????????????????????????????????????????????????????????????????????????????????????????????????????????????
        // ??????????????????????????????????????????????????????????????????1000ms?????????????????????????????????stopLocation()???????????????????????????
        // ???????????????????????????????????????????????????onDestroy()??????
        // ?????????????????????????????????????????????????????????????????????stopLocation()???????????????????????????sdk???????????????
        //????????????
        mlocationClient.startLocation();
    }

    private void getPipelineInfoListRequest() {
        Net.create(Api.class).pipelineinfosget(token)
                .enqueue(new NetCallback<List<Pipelineinfo>>(this, false) {
                    @Override
                    public void onResponse(List<Pipelineinfo> list) {
                        pipelineInfoList = list;
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
                nameList.clear();
                for (String s : photoList) {
                    path.add(s);
                }
                photoAdapter.notifyDataSetChanged();
                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(PipeTunnelActivity.this, "?????????...");
                mWeiboDialog.getWindow().setDimAmount(0f);
                for (int i = 0; i < path.size(); i++) {
                    String suffix = path.get(i).substring(path.get(i).length() - 4);
                    uploadFiles("pipeaccount/" + userId + "_" + TimeUtil.getFileNameTime() + "_" + i + suffix, path.get(i));
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

    private void getTunnelData(String tunnelId) {
        JsonObject params = new JsonObject();
        params.addProperty("id", Integer.parseInt(tunnelId));
        Log.i("tag", "params==" + params);
        Net.create(Api.class).getTunnelData(token, params)
                .enqueue(new NetCallback<List<TunnelModel>>(this, true) {
                    @Override
                    public void onResponse(List<TunnelModel> result) {
                        if (result != null && result.size() > 0) {
                            TunnelModel tunnelModel = result.get(0);
                            tvStartStationNo.setText(tunnelModel.getStakename());
                            tvEndStationNo.setText(tunnelModel.getEndstakename());
                            etLocation.setText(tunnelModel.getLocation());
                            etPipeName.setText(tunnelModel.getPipename());
                            etPipeDepth.setText(tunnelModel.getPipelength() + "");
                            tvStatus.setText(tunnelModel.getPipesituation());
                            etMethod.setText(tunnelModel.getSetupmode());
                            tvPipeName.setText(tunnelModel.getPipename());
                            startStationId = tunnelModel.getStakeid()+"";
                            endStationId = tunnelModel.getEndstakeid()+"";
                            pipeId = tunnelModel.getPipeid();
                            fileName = tunnelModel.getFilename();
                            uploadFilePath = tunnelModel.getUploadfile();
                            if(!TextUtils.isEmpty(tunnelModel.getFilename())){
                                if(tunnelModel.getFilename().contains("/")){
                                    String subFileName = tunnelModel.getFilename().split("/")[1];
                                    tvFileName.setText(subFileName);
                                }else{
                                    tvFileName.setText(tunnelModel.getFilename());
                                }

                            }
                            if (!TextUtils.isEmpty(tunnelModel.getUploadpicture())) {
                                if (tunnelModel.getUploadpicture().contains(";")) {
                                    String[] pathArr = tunnelModel.getUploadpicture().split(";");
                                    for (int i = 0; i < pathArr.length; i++) {
                                        path.add(pathArr[i]);
                                        photoAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    path.add(tunnelModel.getUploadpicture());
                                    photoAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                });
    }

    @OnClick({
            R.id.iv_back,
            R.id.btn_commit,
            R.id.ll_search,
            R.id.ll_pipeName,
            R.id.ll_status,
            R.id.rl_selectImage,
            R.id.ll_selectFile,
            R.id.ll_startStationNo,
            R.id.ll_endStationNo,
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_startStationNo:
                Intent intentStartStation = new Intent(this, StationByIdActivity.class);
                intentStartStation.putExtra("tag", "start");
                intentStartStation.putExtra("pipeId", pipeId+"");
                startActivityForResult(intentStartStation, SELECT_STATION);
                break;
            case R.id.ll_endStationNo:
                Intent intentEndStation = new Intent(this, StationByIdActivity.class);
                intentEndStation.putExtra("tag", "end");
                intentEndStation.putExtra("pipeId", pipeId+"");
                startActivityForResult(intentEndStation, SELECT_STATION);
                break;
            case R.id.ll_selectFile:
                if(tag.equals("update")){
                    Intent intentFile = new Intent(PipeTunnelActivity.this, SelectFileActivity.class);
                    startActivityForResult(intentFile, FILE_REQUEST_CODE);
                }else{
                    if (!uploadFilePath.equals("00")) {
                        Uri uri = Uri.parse(uploadFilePath);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                }
                break;
            case R.id.rl_selectImage:
                initPermissions();
                break;
            case R.id.ll_status:
                Intent intentStatus = new Intent(PipeTunnelActivity.this, TunnelStatusActivity.class);
                intentStatus.putExtra("content", tvStatus.getText().toString());
                startActivityForResult(intentStatus, INPUT_STATUS);
                break;
            case R.id.ll_pipeName:
                List<String> pipeList = new ArrayList<>();
                List<Integer> pipeIdList = new ArrayList<>();
                if (pipelineInfoList != null && pipelineInfoList.size() > 0) {
                    for (int i = 0; i < pipelineInfoList.size(); i++) {
                        pipeList.add(pipelineInfoList.get(i).getName());
                        pipeIdList.add(pipelineInfoList.get(i).getId());
                    }
                }
                dialog.setData(pipeList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvPipeName.setText(pipeList.get(positionM));
                    pipeId = pipeIdList.get(positionM);
                    dialog.dismiss();
                });
                break;
            case R.id.btn_commit:
                if (paramsComplete()) {
                    updateTunnel();
                }
                break;
            case R.id.ll_search:
                Intent intent = new Intent(PipeTunnelActivity.this, SearchTunnelActivity.class);
                startActivityForResult(intent, SEARCH_TUNNEL);
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
        } else if (requestCode == PERMISSON_REQUESTCODE) {
            if (!verifyPermissions(grantResults)) {
                showMissingPermissionDialog();
                isNeedCheck = false;
            }
        }
    }

    private void updateTunnel() {
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
        params.addProperty("id", Integer.parseInt(tunnelId));
        params.addProperty("stakeid", Integer.parseInt(startStationId));
        params.addProperty("endstakeid", Integer.parseInt(endStationId));
        params.addProperty("pipeid", pipeId);
        params.addProperty("location", etLocation.getText().toString());
        params.addProperty("pipename", etPipeName.getText().toString());
        params.addProperty("pipelength", Double.parseDouble(etPipeDepth.getText().toString()));
        params.addProperty("setupmode", etMethod.getText().toString());
        params.addProperty("pipesituation", tvStatus.getText().toString());
        if (!TextUtils.isEmpty(photoSb.toString())) {
            params.addProperty("uploadpicture", photoSb.toString());
        } else {
            params.addProperty("uploadpicture", "00");
        }
        if (!TextUtils.isEmpty(ossFilePath)) {
            params.addProperty("uploadfile", ossFilePath);
        } else {
            params.addProperty("uploadfile", "00");
        }
        Log.i("tag","params==="+params);
        Net.create(Api.class).updateTunnel(token, params)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel result) {
                        if (result.getCode() == Constant.SUCCESS_CODE) {
                            ToastUtil.show("????????????");
                            finish();
                        } else {
                            ToastUtil.show("????????????");
                        }
                    }
                });
    }

    private boolean paramsComplete() {
        if (TextUtils.isEmpty(tvStartStationNo.getText().toString())) {
            ToastUtil.show("???????????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(tvEndStationNo.getText().toString())) {
            ToastUtil.show("???????????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(etPipeName.getText().toString())) {
            ToastUtil.show("?????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(etPipeDepth.getText().toString())) {
            ToastUtil.show("?????????????????????");
            return false;
        }
        if (!NumberUtil.isNumber(etPipeDepth.getText().toString())) {
            ToastUtil.show("?????????????????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(etMethod.getText().toString())) {
            ToastUtil.show("??????????????????????????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(tvStatus.getText().toString())) {
            ToastUtil.show("?????????????????????????????????");
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == INPUT_STATUS) {
            String content = data.getStringExtra("content");
            tvStatus.setText(content);
        } else if (requestCode == FILE_REQUEST_CODE) {
            selectFileName = data.getStringExtra("fileName");
            selectFilePath = data.getStringExtra("selectFilePath");
            tvFileName.setText(selectFileName);
            mWeiboDialog = WeiboDialogUtils.createLoadingDialog(this, "?????????...");
            mWeiboDialog.getWindow().setDimAmount(0f);
            uploadOffice("pipeaccount/" + selectFileName, selectFilePath);
        }else if (requestCode == SELECT_STATION) {
            String selectTag = data.getStringExtra("selectTag");
            String stationName = data.getStringExtra("stationName");

            if (!TextUtils.isEmpty(selectTag)) {
                if (selectTag.equals("start")) {
                    tvStartStationNo.setText(stationName);
                    startStationId = data.getStringExtra("stationId");
                    startPipeId = data.getStringExtra("pipeId");
                } else {
                    tvEndStationNo.setText(stationName);
                    endStationId = data.getStringExtra("stationId");
                    endPipeId = data.getStringExtra("pipeId");

                }
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

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (Build.VERSION.SDK_INT >= 23
                && getApplicationInfo().targetSdkVersion >= 23) {
            if (isNeedCheck) {
                checkPermissions(needPermissions);
            }
        }

        if (Build.VERSION.SDK_INT > 28
                && getApplicationContext().getApplicationInfo().targetSdkVersion > 28) {
            needPermissions = new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE,
                    BACKGROUND_LOCATION_PERMISSION
            };
        }
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                if (!isLoactionSuccess) {
                    isLoactionSuccess = true;
                    //?????????????????????????????????????????????
                    amapLocation.getLocationType();//??????????????????????????????????????????????????????????????????????????????
                    amapLocation.getLatitude();//????????????
                    amapLocation.getLongitude();//????????????
                    amapLocation.getAccuracy();//??????????????????
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(amapLocation.getTime());
                    df.format(date);//????????????
//                    etLocation.setText(amapLocation.getAddress());
                }

            } else {
                //??????????????????ErrCode???????????????errInfo???????????????????????????????????????
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }
    }

    /**
     * @param permissions
     * @since 2.5.0
     */
    private void checkPermissions(String... permissions) {
        try {
            if (Build.VERSION.SDK_INT >= 23
                    && getApplicationInfo().targetSdkVersion >= 23) {
                List<String> needRequestPermissonList = findDeniedPermissions(permissions);
                if (null != needRequestPermissonList
                        && needRequestPermissonList.size() > 0) {
                    String[] array = needRequestPermissonList.toArray(new String[needRequestPermissonList.size()]);
                    Method method = getClass().getMethod("requestPermissions", new Class[]{String[].class,
                            int.class});

                    method.invoke(this, array, PERMISSON_REQUESTCODE);
                }
            }
        } catch (Throwable e) {
        }
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param permissions
     * @return
     * @since 2.5.0
     */
    private List<String> findDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<String>();
        if (Build.VERSION.SDK_INT >= 23
                && getApplicationInfo().targetSdkVersion >= 23) {
            try {
                for (String perm : permissions) {
                    Method checkSelfMethod = getClass().getMethod("checkSelfPermission", String.class);
                    Method shouldShowRequestPermissionRationaleMethod = getClass().getMethod("shouldShowRequestPermissionRationale",
                            String.class);
                    if ((Integer) checkSelfMethod.invoke(this, perm) != PackageManager.PERMISSION_GRANTED
                            || (Boolean) shouldShowRequestPermissionRationaleMethod.invoke(this, perm)) {
                        if (!needCheckBackLocation
                                && BACKGROUND_LOCATION_PERMISSION.equals(perm)) {
                            continue;
                        }
                        needRequestPermissonList.add(perm);
                    }
                }
            } catch (Throwable e) {

            }
        }
        return needRequestPermissonList;
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param grantResults
     * @return
     * @since 2.5.0
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * ??????????????????
     *
     * @since 2.5.0
     */
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("??????");
        builder.setMessage("??????????????????????????????????????????\"??????\"-\"??????\"-?????????????????????");

        // ??????, ????????????
        builder.setNegativeButton("??????",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        builder.setPositiveButton("??????",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                });

        builder.setCancelable(false);

        builder.show();
    }

    /**
     * ?????????????????????
     *
     * @since 2.5.0
     */
    private void startAppSettings() {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mlocationClient != null) {
            mlocationClient.onDestroy();
        }
    }
}
