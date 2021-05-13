package com.gd.form.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.bumptech.glide.Glide;
import com.gd.form.R;
import com.gd.form.adapter.PhotoAdapter;
import com.gd.form.base.BaseActivity;
import com.gd.form.constants.Constant;
import com.gd.form.model.GlideImageLoader;
import com.gd.form.model.ServerModel;
import com.gd.form.model.TunnelDataDetail;
import com.gd.form.model.TunnelDetailModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.SPUtil;
import com.gd.form.utils.TimeUtil;
import com.gd.form.utils.ToastUtil;
import com.gd.form.utils.Util;
import com.gd.form.utils.WeiboDialogUtils;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;
import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;
import com.yancy.gallerypick.inter.IHandlerCallBack;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ApproveTunnelActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_pipeName)
    TextView tvPipeName;
    @BindView(R.id.et_other_problem)
    EditText etOtherPro;
    @BindView(R.id.tv_fileName)
    TextView tvFileName;
    @BindView(R.id.tv_spr)
    TextView tvSpr;
    @BindView(R.id.tv_approveStatus)
    TextView tvApproveStatus;
    @BindView(R.id.tv_photo)
    TextView tvPhoto;
    @BindView(R.id.iv_approveStatus)
    ImageView ivApproveStatus;
    @BindView(R.id.btn_approve)
    Button btnApprove;
    @BindView(R.id.map)
    MapView mapView;
    @BindView(R.id.rvResultPhoto)
    RecyclerView rvResultPhoto;
    @BindView(R.id.tv_approveAdvice)
    TextView tvApproveAdvice;
    @BindView(R.id.ll_approveAdvice)
    LinearLayout llApproveAdvice;
    @BindView(R.id.ll_file)
    LinearLayout llFile;
    @BindView(R.id.view_file)
    View viewFile;

    @BindView(R.id.rg_illegal)
    RadioGroup rgIllegal;
    @BindView(R.id.rb_yes)
    RadioButton rbYes;
    @BindView(R.id.rb_no)
    RadioButton rbNo;
    @BindView(R.id.et_buildingPro)
    EditText etBuildingPro;


    @BindView(R.id.rg_third)
    RadioGroup rgThird;
    @BindView(R.id.rb_thirdNo)
    RadioButton rbThirdNo;
    @BindView(R.id.rb_thirdYes)
    RadioButton rbThirdYes;
    @BindView(R.id.et_thirdPro)
    EditText etThirdPro;

    @BindView(R.id.rg_suspicious)
    RadioGroup rgSuspicious;
    @BindView(R.id.rb_suspectNo)
    RadioButton rbSuspectNo;
    @BindView(R.id.rb_suspectYes)
    RadioButton rbSuspectYes;
    @BindView(R.id.et_suspectPro)
    EditText etSuspectPro;

    @BindView(R.id.rg_block)
    RadioGroup rgBlock;
    @BindView(R.id.rb_blockYes)
    RadioButton rbBlockYes;
    @BindView(R.id.rb_blockMiddle)
    RadioButton rbBlockMiddle;
    @BindView(R.id.rb_blockNo)
    RadioButton rbBlockNo;
    @BindView(R.id.et_blockPro)
    EditText etBlockPro;

    @BindView(R.id.rg_cave)
    RadioGroup rgCave;
    @BindView(R.id.rb_caveNo)
    RadioButton rbCaveNo;
    @BindView(R.id.rb_caveMiddle)
    RadioButton rbCaveMiddle;
    @BindView(R.id.rb_caveYes)
    RadioButton rbCaveYes;
    @BindView(R.id.et_cavePro)
    EditText etCavePro;

    @BindView(R.id.rg_water)
    RadioGroup rgWater;
    @BindView(R.id.rb_waterYes)
    RadioButton rbWaterYes;
    @BindView(R.id.rb_waterMiddle)
    RadioButton rbWaterMiddle;
    @BindView(R.id.rb_waterNo)
    RadioButton rbWaterNo;
    @BindView(R.id.et_waterPro)
    EditText etWaterPro;

    @BindView(R.id.rg_transit)
    RadioGroup rgTransit;
    @BindView(R.id.rb_transitNo)
    RadioButton rbTransitNo;
    @BindView(R.id.rb_transitMiddle)
    RadioButton rbTransitMiddle;
    @BindView(R.id.rb_transitYes)
    RadioButton rbTransitYes;
    @BindView(R.id.et_transitPro)
    EditText etTransitPro;

    @BindView(R.id.rg_smell)
    RadioGroup rgSmell;
    @BindView(R.id.rb_badNo)
    RadioButton rbBadNo;
    @BindView(R.id.rb_badYes)
    RadioButton rbBadYes;
    @BindView(R.id.et_smellPro)
    EditText etSmellPro;

    @BindView(R.id.rg_gas)
    RadioGroup rgGas;
    @BindView(R.id.rb_gasYes)
    RadioButton rbGasYes;
    @BindView(R.id.rb_gasNo)
    RadioButton rbGasNo;
    @BindView(R.id.et_gasPro)
    EditText etGasPro;

    @BindView(R.id.rg_alarm)
    RadioGroup rgAlarm;
    @BindView(R.id.rb_alarmYes)
    RadioButton rbAlarmYes;
    @BindView(R.id.rb_alarmNo)
    RadioButton rbAlarmNo;
    @BindView(R.id.et_alarmPro)
    EditText etAlarmPro;
    @BindView(R.id.ll_chooseImages)
    LinearLayout llChooseImages;
    @BindView(R.id.ll_approveStatus)
    LinearLayout llApproveStatus;
    private String formId;
    private String token, userId;
    private MarkerOptions markerOption;
    private AMap aMap;
    private PhotoAdapter photoAdapter;
    private List<String> path;
    private String filePath;
    private String col1, col1Desc, col2, col2Desc, col3, col3Desc, col4, col4Desc, col5, col5Desc, col6, col6Desc, col7, col7Desc, col8, col8Desc, col9,
            col9Desc, col10, col10Desc;
    private String tag, approveId;
    private IHandlerCallBack iHandlerCallBack;
    private GalleryConfig galleryConfig;
    private List<String> nameList;
    private Dialog mWeiboDialog;
    private OSSCredentialProvider ossCredentialProvider;
    private OSS oss;
    private int PERMISSIONS_REQUEST_READ_CONTACTS = 8;
    private int pipeId;
    private String location;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_approval_tunnel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.activityList.add(this);
        tvTitle.setText("隧道外部检查表");
        // 此方法必须重写
        mapView.onCreate(savedInstanceState);
        initMap();
        token = (String) SPUtil.get(ApproveTunnelActivity.this, "token", "");
        userId = (String) SPUtil.get(ApproveTunnelActivity.this, "userId", "");
        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            tag = bundle.getString("tag");
            if (tag.equals("detail")) {
                btnApprove.setVisibility(View.GONE);
                llChooseImages.setEnabled(false);
            } else if (tag.equals("update")) {
                btnApprove.setText("提交");
                llChooseImages.setEnabled(true);
                ivApproveStatus.setVisibility(View.GONE);
//                llApproveAdvice.setVisibility(View.GONE);
                llApproveStatus.setVisibility(View.GONE);
            } else if (tag.equals("approve")) {
                llApproveStatus.setVisibility(View.GONE);
                llApproveAdvice.setVisibility(View.GONE);
            }
            if (tag.equals("detail") || tag.equals("approve")) {
                rbAlarmNo.setEnabled(false);
                rbAlarmYes.setEnabled(false);
                rbBadNo.setEnabled(false);
                rbBadYes.setEnabled(false);
                rbBlockMiddle.setEnabled(false);
                rbBlockNo.setEnabled(false);
                rbBlockYes.setEnabled(false);
                rbCaveMiddle.setEnabled(false);
                rbCaveNo.setEnabled(false);
                rbCaveYes.setEnabled(false);
                rbGasNo.setEnabled(false);
                rbGasYes.setEnabled(false);
                rbNo.setEnabled(false);
                rbYes.setEnabled(false);
                rbSuspectNo.setEnabled(false);
                rbSuspectYes.setEnabled(false);
                rbThirdNo.setEnabled(false);
                rbThirdYes.setEnabled(false);
                rbTransitMiddle.setEnabled(false);
                rbTransitNo.setEnabled(false);
                rbTransitYes.setEnabled(false);
                rbWaterMiddle.setEnabled(false);
                rbWaterNo.setEnabled(false);
                rbWaterYes.setEnabled(false);
                etOtherPro.setEnabled(false);
                etGasPro.setEnabled(false);
                etBuildingPro.setEnabled(false);
                etThirdPro.setEnabled(false);
                etSuspectPro.setEnabled(false);
                etBlockPro.setEnabled(false);
                etCavePro.setEnabled(false);
                etWaterPro.setEnabled(false);
                etTransitPro.setEnabled(false);
                etSmellPro.setEnabled(false);
                etAlarmPro.setEnabled(false);
                llChooseImages.setEnabled(false);
            } else {
                rbAlarmNo.setEnabled(true);
                rbAlarmYes.setEnabled(true);
                rbBadNo.setEnabled(true);
                rbBadYes.setEnabled(true);
                rbBlockMiddle.setEnabled(true);
                rbBlockNo.setEnabled(true);
                rbBlockYes.setEnabled(true);
                rbCaveMiddle.setEnabled(true);
                rbCaveNo.setEnabled(true);
                rbCaveYes.setEnabled(true);
                rbGasNo.setEnabled(true);
                rbGasYes.setEnabled(true);
                rbNo.setEnabled(true);
                rbYes.setEnabled(true);
                rbSuspectNo.setEnabled(true);
                rbSuspectYes.setEnabled(true);
                rbThirdNo.setEnabled(true);
                rbThirdYes.setEnabled(true);
                rbTransitMiddle.setEnabled(true);
                rbTransitNo.setEnabled(true);
                rbTransitYes.setEnabled(true);
                rbWaterMiddle.setEnabled(true);
                rbWaterNo.setEnabled(true);
                rbWaterYes.setEnabled(true);
                etOtherPro.setEnabled(true);
                etGasPro.setEnabled(true);
                etBuildingPro.setEnabled(true);
                etThirdPro.setEnabled(true);
                etSuspectPro.setEnabled(true);
                etBlockPro.setEnabled(true);
                etCavePro.setEnabled(true);
                etWaterPro.setEnabled(true);
                etTransitPro.setEnabled(true);
                etSmellPro.setEnabled(true);
                etAlarmPro.setEnabled(true);
            }

            formId = bundle.getString("formId");
        }
        llFile.setVisibility(View.GONE);
        viewFile.setVisibility(View.GONE);
        path = new ArrayList<>();
        nameList = new ArrayList<>();
        ossCredentialProvider = new OSSPlainTextAKSKCredentialProvider(Constant.ACCESSKEYID, Constant.ACCESSKEYSECRET);
        oss = new OSSClient(mContext.getApplicationContext(), Constant.ENDPOINT, ossCredentialProvider);
        getDetail(formId);
        initListener();
        initGallery();
        initConfig();
    }

    private void initListener() {
        //违规行为
        rgIllegal.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yes:
                        col1 = "无违章采石、采矿、爆破行为";
                        break;
                    case R.id.rb_no:
                        col1 = "有违章采石、采矿、爆破行为";
                        break;
                }
            }
        });
        //第三方施工
        rgThird.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_thirdYes:
                        col2 = "无第三方施工行为";
                        break;
                    case R.id.rb_thirdNo:
                        col2 = "有第三方施工行为";
                        break;
                }
            }
        });
        //可疑现象
        rgSuspicious.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_suspectNo:
                        col3 = "无可疑人员、车辆逗留现象";
                        break;
                    case R.id.rb_suspectYes:
                        col3 = "有可疑人员、车辆逗留现象";
                        break;
                }
            }
        });
        //砖砌封堵
        rgBlock.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_blockYes:
                        col4 = "完好";
                        break;
                    case R.id.rb_blockMiddle:
                        col4 = "发生局部破损";
                        break;
                    case R.id.rb_blockNo:
                        col4 = "发生大面积破损";
                        break;
                }
            }
        });
        //洞口
        rgCave.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_caveNo:
                        col5 = "无裂缝、沉降，四周有无滑坡、水土流失";
                        break;
                    case R.id.rb_caveMiddle:
                        col5 = "有裂缝、沉降情况";
                        break;
                    case R.id.rb_caveYes:
                        col5 = ".有滑坡、水土流失情况";
                        break;
                }
            }
        });
        //水工设施
        rgWater.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_waterYes:
                        col6 = "完好，无破损";
                        break;
                    case R.id.rb_waterMiddle:
                        col6 = "局部损坏";
                        break;
                    case R.id.rb_waterNo:
                        col6 = "损坏严重，作用基本失效";
                        break;
                }
            }
        });
        //管道入地过渡段
        rgTransit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_transitNo:
                        col7 = "无管道本体翘起、下沉，无管沟沉降";
                        break;
                    case R.id.rb_transitMiddle:
                        col7 = "有管道本体偏离原有位置";
                        break;
                    case R.id.rb_transitYes:
                        col7 = "有管沟沉降情况";
                        break;
                }
            }
        });
        //异响异味
        rgSmell.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_badNo:
                        col8 = "无异响、异味现象";
                        break;
                    case R.id.rb_badYes:
                        col8 = "有异响、异味现象";
                        break;

                }
            }
        });
        //可燃气体检测
        rgGas.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_gasYes:
                        col9 = "检测合格";
                        break;
                    case R.id.rb_gasNo:
                        col9 = "检测不合格";
                        break;

                }
            }
        });
        //报警系统
        rgAlarm.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_alarmYes:
                        col10 = "外观完好";
                        break;
                    case R.id.rb_alarmNo:
                        col10 = "外观损坏";
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
                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(ApproveTunnelActivity.this, "加载中...");
                mWeiboDialog.getWindow().setDimAmount(0f);
                for (int i = 0; i < path.size(); i++) {
                    String suffix = path.get(i).substring(path.get(i).length() - 4);
                    uploadFiles("W002/" + userId + "_" + TimeUtil.getFileNameTime() + "_" + i + suffix, path.get(i));
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
                .imageLoader(new GlideImageLoader())    // ImageLoader 加载框架（必填）
                .iHandlerCallBack(iHandlerCallBack)     // 监听接口（必填）
                .provider("com.gd.form.fileprovider")   // provider(必填)
                .pathList(path)                         // 记录已选的图片
                .multiSelect(true)                      // 是否多选   默认：false
                .multiSelect(true, 9)                   // 配置是否多选的同时 配置多选数量   默认：false ， 9
                .maxSize(9)                             // 配置多选时 的多选数量。    默认：9
                .crop(false)                             // 快捷开启裁剪功能，仅当单选 或直接开启相机时有效
                .crop(false, 1, 1, 500, 500)             // 配置裁剪功能的参数，   默认裁剪比例 1:1
                .isShowCamera(true)                     // 是否现实相机按钮  默认：false
                .filePath("/Gallery/Pictures")// 图片存放路径
                .build();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvResultPhoto.setLayoutManager(gridLayoutManager);
        photoAdapter = new PhotoAdapter(this, path);
        rvResultPhoto.setAdapter(photoAdapter);
    }

    /**
     * 初始化AMap对象
     */
    private void initMap() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
    }

    private void getDetail(String formId) {
        JsonObject params = new JsonObject();
        params.addProperty("formid", formId);
        Log.i("tag", "params===" + params);
        Net.create(Api.class).getTunnelDetail(token, params)
                .enqueue(new NetCallback<TunnelDetailModel>(this, true) {
                    @Override
                    public void onResponse(TunnelDetailModel model) {
                        if (model != null) {
                            TunnelDataDetail dataDetail = model.getDatadetail();
                            tvPipeName.setText(model.getPipeString());
                            pipeId = model.getDatadetail().getPipeid();
                            if (dataDetail.getCol1().equals("无违章采石、采矿、爆破行为")) {
                                rbNo.setChecked(true);
                            } else if (dataDetail.getCol1().equals("有违章采石、采矿、爆破行为")) {
                                rbYes.setChecked(true);
                            }
                            col1 = dataDetail.getCol1();
                            col1Desc = dataDetail.getCol1desc();
                            etBuildingPro.setText(col1Desc);

                            if (dataDetail.getCol2().equals("无第三方施工行为")) {
                                rbThirdNo.setChecked(true);
                            } else if (dataDetail.getCol2().equals("有第三方施工行为")) {
                                rbThirdYes.setChecked(true);
                            }
                            col2 = dataDetail.getCol2();
                            col2Desc = dataDetail.getCol2desc();
                            etThirdPro.setText(col2Desc);

                            if (dataDetail.getCol3().equals("无可疑人员、车辆逗留现象")) {
                                rbSuspectNo.setChecked(true);
                            } else if (dataDetail.getCol3().equals("有可疑人员、车辆逗留现象")) {
                                rbThirdYes.setChecked(true);
                            }
                            col3 = dataDetail.getCol3();
                            col3Desc = dataDetail.getCol3desc();
                            etSuspectPro.setText(col3Desc);


                            if (dataDetail.getCol4().equals("完好")) {
                                rbBlockYes.setChecked(true);
                            } else if (dataDetail.getCol4().equals("发生局部破损")) {
                                rbBlockMiddle.setChecked(true);
                            } else if (dataDetail.getCol4().equals("发生大面积破损")) {
                                rbBlockNo.setChecked(true);
                            }
                            col4 = dataDetail.getCol4();
                            col4Desc = dataDetail.getCol4desc();
                            etBlockPro.setText(col4Desc);

                            if (dataDetail.getCol5().equals("无裂缝、沉降，四周有无滑坡、水土流失")) {
                                rbCaveNo.setChecked(true);
                            } else if (dataDetail.getCol5().equals("有裂缝、沉降情况")) {
                                rbCaveMiddle.setChecked(true);
                            } else if (dataDetail.getCol5().equals("有滑坡、水土流失情况")) {
                                rbCaveYes.setChecked(true);
                            }
                            col5 = dataDetail.getCol5();
                            col5Desc = dataDetail.getCol5desc();
                            etCavePro.setText(col5Desc);


                            if (dataDetail.getCol6().equals("完好，无破损")) {
                                rbWaterYes.setChecked(true);
                            } else if (dataDetail.getCol5().equals("局部损坏")) {
                                rbWaterMiddle.setChecked(true);
                            } else if (dataDetail.getCol5().equals("损坏严重，作用基本失效")) {
                                rbWaterNo.setChecked(true);
                            }
                            col6 = dataDetail.getCol6();
                            col6Desc = dataDetail.getCol6desc();
                            etWaterPro.setText(col6Desc);


                            if (dataDetail.getCol7().equals("无管道本体翘起、下沉，无管沟沉降")) {
                                rbTransitNo.setChecked(true);
                            } else if (dataDetail.getCol7().equals("有管道本体偏离原有位置")) {
                                rbTransitMiddle.setChecked(true);
                            } else if (dataDetail.getCol7().equals("有管沟沉降情况")) {
                                rbTransitYes.setChecked(true);
                            }
                            col7 = dataDetail.getCol7();
                            col7Desc = dataDetail.getCol7desc();
                            etTransitPro.setText(col7Desc);

                            if (dataDetail.getCol8().equals("无异响、异味现象")) {
                                rbBadNo.setChecked(true);
                            } else if (dataDetail.getCol8().equals("有异响、异味现象")) {
                                rbBadYes.setChecked(true);
                            }
                            col8 = dataDetail.getCol8();
                            col8Desc = dataDetail.getCol8desc();
                            etSmellPro.setText(col8Desc);

                            if (dataDetail.getCol9().equals("检测合格")) {
                                rbGasYes.setChecked(true);
                            } else if (dataDetail.getCol8().equals("检测不合格")) {
                                rbGasNo.setChecked(true);
                            }
                            col9 = dataDetail.getCol9();
                            col9Desc = dataDetail.getCol9desc();
                            etGasPro.setText(col9Desc);

                            if (dataDetail.getCol10().equals("外观完好")) {
                                rbAlarmYes.setChecked(true);
                            } else if (dataDetail.getCol8().equals("外观损坏")) {
                                rbAlarmNo.setChecked(true);
                            }
                            col10 = dataDetail.getCol10();
                            col10Desc = dataDetail.getCol10desc();
                            etAlarmPro.setText(col10Desc);
                            etOtherPro.setText(dataDetail.getCol11());

                            location = model.getDatadetail().getLocate();
                            if (!TextUtils.isEmpty(location) && location.contains(",")) {
                                String[] locationArr = location.split(",");
                                LatLng latLng = new LatLng(Double.parseDouble(locationArr[1]), Double.parseDouble(locationArr[0]));
                                markerOption = new MarkerOptions().icon(BitmapDescriptorFactory
                                        .defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                                        .position(latLng)
                                        .draggable(true);
                                aMap.addMarker(markerOption);
                                aMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                                        new CameraPosition(latLng, 11f, 0, 0)));
                            }
                            //上传的文件
                            if (model.getDataupload() != null) {
                                if ("00".equals(model.getDataupload().getFilepath())) {
                                    tvFileName.setText("无");
                                } else {
                                    tvFileName.setText(model.getDataupload().getFilename());
                                    filePath = model.getDataupload().getFilepath();
                                }
                            } else {
                                tvFileName.setText("无");
                            }

                            //上传的图片
                            if (model.getDataupload() != null) {
                                if ("00".equals(model.getDataupload().getPicturepath())) {
                                    tvPhoto.setText("无");
                                } else {
                                    String[] photoArr = model.getDataupload().getPicturepath().split(";");
                                    for (int i = 0; i < photoArr.length; i++) {
                                        path.add(photoArr[i]);
                                        photoAdapter.notifyDataSetChanged();
                                    }
                                }
                            } else {
                                tvPhoto.setText("无");
                            }

                            //审批人
                            if (model.getDatapproval() != null) {
                                String approval = model.getDatapproval().getEmployid();
                                if (!TextUtils.isEmpty(approval) && approval.contains(":")) {
                                    tvSpr.setText(approval.split(":")[1]);
                                    approveId = approval.split(":")[0];
                                }
                                //审批状态，0-表示批复不同意，1-表示批复同意，3-表示未批复
                                tvApproveStatus.setText(Util.getApprovalStatus(model.getDatapproval().getApprovalresult()));
                                if (!TextUtils.isEmpty(model.getDatapproval().getApprovalcomment())
                                        && (tag.equals("detail") || tag.equals("update"))
                                        && model.getDatapproval().getApprovalresult()!=3) {
                                    llApproveAdvice.setVisibility(View.VISIBLE);
                                    tvApproveAdvice.setText(model.getDatapproval().getApprovalcomment());
                                }
                                //显示审批图片
                                if (!TextUtils.isEmpty(model.getDatapproval().getSignfilepath())) {
                                    Glide.with(ApproveTunnelActivity.this).
                                            load(model.getDatapproval().getSignfilepath()).
                                            into(ivApproveStatus);
                                }
                            }
                        }
                    }
                });
    }

    @OnClick({
            R.id.iv_back,
            R.id.ll_file,
            R.id.ll_chooseImages,
            R.id.btn_approve})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_chooseImages:
                initPermissions();
                break;
            case R.id.btn_approve:
                if (tag.equals("update")) {
                    if (paramsComplete()) {
                        commit();
                    }
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("formid", formId);
                    openActivity(ApproveFormActivity.class, bundle);
                }
                break;
            case R.id.ll_file:
                if (!TextUtils.isEmpty(filePath)) {
                    Uri uri = Uri.parse(filePath);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
                break;

        }
    }

    private boolean paramsComplete() {
        if (TextUtils.isEmpty(etBuildingPro.getText().toString())) {
            ToastUtil.show("请输入违规行为问题描述");
            return false;
        }
        if (TextUtils.isEmpty(etThirdPro.getText().toString())) {
            ToastUtil.show("请输入第三方施工行为问题描述");
            return false;
        }
        if (TextUtils.isEmpty(etSuspectPro.getText().toString())) {
            ToastUtil.show("请输入可疑行为问题描述");
            return false;
        }
        if (TextUtils.isEmpty(etBlockPro.getText().toString())) {
            ToastUtil.show("请输入砖砌封堵问题描述");
            return false;
        }
        if (TextUtils.isEmpty(etCavePro.getText().toString())) {
            ToastUtil.show("请输入洞口问题描述");
            return false;
        }
        if (TextUtils.isEmpty(etWaterPro.getText().toString())) {
            ToastUtil.show("请输入水工设施问题描述");
            return false;
        }
        if (TextUtils.isEmpty(etTransitPro.getText().toString())) {
            ToastUtil.show("请输入管道入地过渡段问题描述");
            return false;
        }
        if (TextUtils.isEmpty(etSmellPro.getText().toString())) {
            ToastUtil.show("请输入异响异味问题描述");
            return false;
        }
        if (TextUtils.isEmpty(etGasPro.getText().toString())) {
            ToastUtil.show("请输入可燃气体检测问题描述");
            return false;
        }
        if (TextUtils.isEmpty(etAlarmPro.getText().toString())) {
            ToastUtil.show("请输入报警系统问题描述");
            return false;
        }
        if (TextUtils.isEmpty(etOtherPro.getText().toString())) {
            ToastUtil.show("请输入其他情况");
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
        jsonObject.addProperty("id", formId);
        jsonObject.addProperty("pipeid", pipeId);
        jsonObject.addProperty("stakeid", 0);
        jsonObject.addProperty("pipelength", 0);
        jsonObject.addProperty("col1", col1);
        jsonObject.addProperty("col1desc", etBuildingPro.getText().toString());
        jsonObject.addProperty("col2", col2);
        jsonObject.addProperty("col2desc", etThirdPro.getText().toString());
        jsonObject.addProperty("col3", col3);
        jsonObject.addProperty("col3desc", etSuspectPro.getText().toString());
        jsonObject.addProperty("col4", col4);
        jsonObject.addProperty("col4desc", etBlockPro.getText().toString());
        jsonObject.addProperty("col5", col5);
        jsonObject.addProperty("col5desc", etCavePro.getText().toString());
        jsonObject.addProperty("col6", col6);
        jsonObject.addProperty("col6desc", etWaterPro.getText().toString());
        jsonObject.addProperty("col7", col7);
        jsonObject.addProperty("col7desc", etTransitPro.getText().toString());
        jsonObject.addProperty("col8", col8);
        jsonObject.addProperty("col8desc", etSmellPro.getText().toString());
        jsonObject.addProperty("col9", col9);
        jsonObject.addProperty("col9desc", etGasPro.getText().toString());
        jsonObject.addProperty("col10", col10);
        jsonObject.addProperty("col10desc", etAlarmPro.getText().toString());
        jsonObject.addProperty("col11", etOtherPro.getText().toString());
        jsonObject.addProperty("locate", location);
        jsonObject.addProperty("creator", userId);
        jsonObject.addProperty("creatime", TimeUtil.getCurrentTime());
        jsonObject.addProperty("approvalid", approveId);
        if (!TextUtils.isEmpty(photoSb.toString())) {
            jsonObject.addProperty("picturepath", photoSb.toString());
        } else {
            jsonObject.addProperty("picturepath", "00");
        }
        jsonObject.addProperty("filepath", "00");
        Log.i("tag", "jsonObject===" + jsonObject);
        Net.create(Api.class).updateTunnelRecord(token, jsonObject)
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

    // 授权管理
    private void initPermissions() {
        if (ContextCompat.checkSelfPermission(ApproveTunnelActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ApproveTunnelActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(mContext, "请在 设置-应用管理 中开启此应用的储存授权。", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(ApproveTunnelActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {
            GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(ApproveTunnelActivity.this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(ApproveTunnelActivity.this);
            } else {
                Log.i("tag", "拒绝授权");
            }
        }
    }

    //上传阿里云文件
    public void uploadFiles(String fileName, String filePath) {
        PutObjectRequest put = new PutObjectRequest(Constant.BUCKETSTRING, fileName, filePath);
        // 此处调用异步上传方法
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

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Util.activityList.remove(this);
    }
}
