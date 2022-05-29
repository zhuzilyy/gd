package com.gd.form.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.gd.form.R;
import com.gd.form.adapter.PhotoAdapter;
import com.gd.form.base.BaseActivity;
import com.gd.form.constants.Constant;
import com.gd.form.model.BuildingModel;
import com.gd.form.model.GlideImageLoader;
import com.gd.form.model.Pipelineinfo;
import com.gd.form.model.ServerModel;
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
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

public class PipeBuildingActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_stationNo)
    TextView tvStationNo;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.et_startStationNo)
    EditText etStartStationNo;
    @BindView(R.id.et_location)
    EditText etLocation;
    @BindView(R.id.tv_pipeProperty)
    TextView tvPipeProperty;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.rg_isHighZone)
    RadioGroup rgIsHighZone;
    @BindView(R.id.rb_yes)
    RadioButton rbYes;
    @BindView(R.id.rb_no)
    RadioButton rbNo;
    @BindView(R.id.et_missLength)
    EditText etMissLength;
    @BindView(R.id.et_minDistance)
    EditText etMinDistance;
    @BindView(R.id.et_missArea)
    EditText etMissArea;
    @BindView(R.id.et_personActivity)
    EditText etPersonActivity;
    @BindView(R.id.et_des)
    EditText etDes;
    @BindView(R.id.tv_riskEvaluate)
    TextView tvRiskEvaluate;
    @BindView(R.id.tv_riskType)
    TextView tvRiskType;
    @BindView(R.id.et_beforeChangeMethod)
    EditText etBeforeChangeMethod;
    @BindView(R.id.et_changeMethod)
    EditText etChangeMethod;
    @BindView(R.id.tv_pipeName)
    TextView tvPipeName;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.ll_pipeName)
    LinearLayout llPipeName;
    @BindView(R.id.ll_time)
    LinearLayout llTime;
    @BindView(R.id.ll_pipeProperty)
    LinearLayout llPipeProperty;
    @BindView(R.id.ll_type)
    LinearLayout llType;
    @BindView(R.id.ll_riskEvaluate)
    LinearLayout llRiskEvaluate;
    @BindView(R.id.tv_riskResult)
    TextView tvRiskResult;
    @BindView(R.id.ll_riskResult)
    LinearLayout llRiskResult;
    @BindView(R.id.ll_riskType)
    LinearLayout llRiskType;
    @BindView(R.id.rvResultPhoto)
    RecyclerView rvResultPhoto;
    @BindView(R.id.rl_selectImage)
    RelativeLayout rlSelectImage;
    @BindView(R.id.ll_location)
    LinearLayout llLocation;
    @BindView(R.id.tv_totalLevel)
    TextView tvTotalLevel;
    @BindView(R.id.tv_highZone)
    TextView tvHighZone;
    @BindView(R.id.flow_layout)
    TagFlowLayout mFlowLayout;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rg_property)
    RadioGroup rgProperty;
    @BindView(R.id.rb_direct)
    RadioButton rbDirect;
    @BindView(R.id.rb_distance)
    RadioButton rbDistance;
    private String isHighZone = "是";
    private String property = "直接占压";
    private TimePickerView pvTime;
    private String token, userId, buildingId;
    private final int SEARCH_BUILDING = 100;
    private final int SELECT_STATION = 101;
    private int SELECT_AREA = 104;
    private int PERMISSIONS_REQUEST_READ_CONTACTS = 8;
    private List<Pipelineinfo> pipelineInfoList;
    private ListDialog dialog;
    private List<String> pipePropertyList, typeList, riskEvaluateList, riskResultList, riskTypeList;
    private List<String> path;
    private GalleryConfig galleryConfig;
    private PhotoAdapter photoAdapter;
    private List<String> nameList;
    private IHandlerCallBack iHandlerCallBack;
    private Dialog mWeiboDialog;
    private OSSCredentialProvider ossCredentialProvider;
    private OSS oss;
    private int stationId, pipeId;
    private double rateLevel, resultLevel;
    private TagAdapter<String> mAdapter;
    private String[] dataSource = {"房屋(有人居住)", "房屋(无人居住)", "简易结构(简易厕所、杂货房、车棚等)", "厂房", "大棚", "围墙", "动物棚圈", "重物占压"};
    private Set<Integer> selectedIndex;
    private String selectPressureName;
    private String departmentId;
    private String stakeId;
    private List<String> highZoneList;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_building;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        token = (String) SPUtil.get(PipeBuildingActivity.this, "token", "");
        userId = (String) SPUtil.get(PipeBuildingActivity.this, "userId", "");
        ossCredentialProvider = new OSSPlainTextAKSKCredentialProvider(Constant.ACCESSKEYID, Constant.ACCESSKEYSECRET);
        oss = new OSSClient(mContext.getApplicationContext(), Constant.ENDPOINT, ossCredentialProvider);
        tvTitle.setText("违规违建");
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("巡检记录");
        dialog = new ListDialog(this);
        pipePropertyList = new ArrayList<>();
        typeList = new ArrayList<>();
        riskEvaluateList = new ArrayList<>();
        riskResultList = new ArrayList<>();
        riskTypeList = new ArrayList<>();
        highZoneList = new ArrayList<>();
        path = new ArrayList<>();
        nameList = new ArrayList<>();
        if (getIntent() != null) {
            String stakeStatus = getIntent().getExtras().getString("stakeStatus");
            String tag = getIntent().getExtras().getString("tag");
            buildingId = getIntent().getExtras().getString("buildingId");
            departmentId = getIntent().getExtras().getString("departmentId");
            stakeId = getIntent().getExtras().getString("stakeId");
            if (!TextUtils.isEmpty(buildingId)) {
                if (tag.equals("detail")) {
                    tvRight.setVisibility(View.GONE);
                    getBuildingDetail(stakeStatus, buildingId);
                } else {
                    Log.i("tag","1111");
                    getBuildingData(buildingId);
                    tvRight.setVisibility(View.GONE);
                }
            } else {
                tvRight.setVisibility(View.GONE);
                tvTitle.setText("增加违规违建");
            }
            if ("add".equals(tag)) {
                btnCommit.setVisibility(View.VISIBLE);
                llSearch.setVisibility(View.GONE);
                llPipeName.setEnabled(true);
                etStartStationNo.setEnabled(true);
                etLocation.setEnabled(true);
                rgProperty.setEnabled(true);
                rbDirect.setEnabled(true);
                rbDistance.setEnabled(true);
                tvType.setEnabled(true);
                etName.setEnabled(true);
                tvTime.setEnabled(true);
                rgIsHighZone.setEnabled(true);
                etMissLength.setEnabled(true);
                etMinDistance.setEnabled(true);
                etMissArea.setEnabled(true);
                etPersonActivity.setEnabled(true);
                etDes.setEnabled(true);
                tvRiskEvaluate.setEnabled(true);
                tvRiskType.setEnabled(true);
                etBeforeChangeMethod.setEnabled(true);
                etChangeMethod.setEnabled(true);
                llTime.setEnabled(true);
                llPipeName.setEnabled(true);
                rbNo.setEnabled(true);
                rbYes.setEnabled(true);
                llPipeProperty.setEnabled(true);
                llType.setEnabled(true);
                llRiskEvaluate.setEnabled(true);
                llRiskResult.setEnabled(true);
                llRiskType.setEnabled(true);
                rlSelectImage.setEnabled(true);
            } else if (("check".equals(tag)) ||
                    "detail".equals(tag)) {
                btnCommit.setVisibility(View.GONE);
                llSearch.setVisibility(View.GONE);
                llPipeName.setEnabled(false);
                etStartStationNo.setEnabled(false);
                etLocation.setEnabled(false);
                tvPipeProperty.setEnabled(false);
                rgProperty.setEnabled(false);
                rbDirect.setEnabled(false);
                rbDistance.setEnabled(false);
                tvType.setEnabled(false);
                etName.setEnabled(false);
                tvTime.setEnabled(false);
                rgIsHighZone.setEnabled(false);
                etMissLength.setEnabled(false);
                etMinDistance.setEnabled(false);
                etMissArea.setEnabled(false);
                etPersonActivity.setEnabled(false);
                etDes.setEnabled(false);
                tvRiskEvaluate.setEnabled(false);
                tvRiskType.setEnabled(false);
                etBeforeChangeMethod.setEnabled(false);
                etChangeMethod.setEnabled(false);
                llPipeName.setEnabled(false);
                rbNo.setEnabled(false);
                rbYes.setEnabled(false);
                llTime.setEnabled(false);
                llPipeProperty.setEnabled(false);
                llType.setEnabled(false);
                llRiskEvaluate.setEnabled(false);
                llRiskResult.setEnabled(false);
                llRiskType.setEnabled(false);
                rlSelectImage.setEnabled(false);
                llLocation.setEnabled(false);
            }
        }
        initListener();
        initTimePicker();
        getPipelineInfoListRequest();
        initDialog();
        initGallery();
        initConfig();
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
                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(PipeBuildingActivity.this, "加载中...");
                mWeiboDialog.getWindow().setDimAmount(0f);
                for (int i = 0; i < path.size(); i++) {
                    String suffix = path.get(i).substring(path.get(i).length() - 4);
//                    uploadFiles(userId + "_" + TimeUtil.getFileNameTime() + "_" + i + suffix, path.get(i));
                    uploadFiles("llegalaccount/" + buildingId + "_" + TimeUtil.getFileNameTime() + "_" + i + suffix, path.get(i));
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

    private void initDialog() {
        pipePropertyList.add("直接占压");
        pipePropertyList.add("距离不足");
        typeList.add("房屋（有人居住）");
        typeList.add("房屋（无人居住）");
        typeList.add("简易结构（简易厕所、杂货房、车棚等）");
        typeList.add("厂房");
        typeList.add("大棚");
        typeList.add("围墙");
        typeList.add("动物棚圈");
        typeList.add("重物占压");
        riskEvaluateList.add("1");
        riskEvaluateList.add("2");
        riskEvaluateList.add("3");
        riskEvaluateList.add("4");
        riskEvaluateList.add("5");
        riskResultList.add("I");
        riskResultList.add("II");
        riskResultList.add("III");
        riskResultList.add("IV");
        riskResultList.add("V");
        riskTypeList.add("一般");
        riskTypeList.add("较大");
        riskTypeList.add("重大");
        highZoneList.add("是");
        highZoneList.add("否");
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

    private void initTimePicker() {
        //Dialog 模式下，在底部弹出
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                tvTime.setText(format.format(date));
            }
        }).setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
            @Override
            public void onTimeSelectChanged(Date date) {
                Log.i("pvTime", "onTimeSelectChanged");
            }
        }).setType(new boolean[]{true, true, true, false, false, false})
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

    private void initListener() {
        rgIsHighZone.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yes:
                        isHighZone = "是";
                        break;
                    case R.id.rb_no:
                        isHighZone = "否";
                        break;
                }
            }
        });
        rgProperty.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_direct:
                        property = "直接占压";
                        break;
                    case R.id.rb_distance:
                        property = "距离不足";
                        break;
                }
            }
        });

        final LayoutInflater mInflater = LayoutInflater.from(this);
        mFlowLayout.setAdapter(mAdapter = new com.zhy.view.flowlayout.TagAdapter<String>(dataSource) {

            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.background_tag,
                        mFlowLayout, false);
                tv.setText(s);
                return tv;
            }
        });

        mFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                selectedIndex = selectPosSet;
                StringBuilder selectedTagBuilder = new StringBuilder();
                if (selectedIndex != null && selectedIndex.size() > 0) {
                    for (int i : selectedIndex) {
                        selectedTagBuilder.append(dataSource[i]);
                        selectedTagBuilder.append(":");
                    }
                }
                selectPressureName = selectedTagBuilder.toString();
            }
        });
    }

    private void getBuildingData(String buildingId) {
        JsonObject params = new JsonObject();
        params.addProperty("id", buildingId);
        Net.create(Api.class).getBuildingData(token, params)
                .enqueue(new NetCallback<List<BuildingModel>>(this, true) {
                    @Override
                    public void onResponse(List<BuildingModel> result) {
                        if (result != null && result.size() > 0) {
                            BuildingModel buildingModel = result.get(0);
                            setDefaultValue(buildingModel);
                        }
                    }
                });
    }


    private void getBuildingDetail(String status, String buildingId) {
        JsonObject params = new JsonObject();
        params.addProperty("appempid", userId);
        params.addProperty("id", buildingId);
        params.addProperty("stakestatus", Integer.parseInt(status));
        params.addProperty("devicetype", 2);
        Net.create(Api.class).getArrpoveBuildingData(token, params)
                .enqueue(new NetCallback<BuildingModel>(this, true) {
                    @Override
                    public void onResponse(BuildingModel result) {
                        if (result != null) {
                            setDefaultValue(result);
                        }
                        if (rateLevel != 0 && resultLevel != 0) {
                            tvTotalLevel.setText(rateLevel * resultLevel + "");
                        }

                    }
                });
    }


    private void setDefaultValue(BuildingModel buildingModel) {
        String pipeDesc = buildingModel.getPipedesc();
        if (!TextUtils.isEmpty(pipeDesc)) {
            String[] descArr = pipeDesc.split(":");
            tvPipeName.setText(descArr[0]);
        }
        tvStationNo.setText(buildingModel.getStakename());
        etLocation.setText(buildingModel.getLocationdesc());
        if(buildingModel.getOverpropety().equals("距离不足")){
            property = "距离不足";
            rbDistance.setChecked(true);
        }else if(buildingModel.getOverpropety().equals("直接占压")){
            property = "直接占压";
            rbDirect.setChecked(true);
        }
        Set<Integer> set = new HashSet<>();
        StringBuilder selectedTagBuilder = new StringBuilder();
        if (!TextUtils.isEmpty(buildingModel.getOvertype())) {
            String[] arrayName = buildingModel.getOvertype().split(":");
            for (int i = 0; i < dataSource.length; i++) {
                for (int j = 0; j < arrayName.length; j++) {
                    if (dataSource[i].equals(arrayName[j])) {
                        set.add(i);
                        selectedTagBuilder.append(dataSource[i]);
                        selectedTagBuilder.append(":");
                    }
                }
            }
            selectPressureName = selectedTagBuilder.toString();
            mAdapter.setSelectedList(set);
        }
        etName.setText(buildingModel.getOvername());
        tvTime.setText(buildingModel.getGenernaldate());
        if (buildingModel.getHighareasflag().equals("是")) {
            rbYes.setChecked(true);
        } else {
            rbNo.setChecked(true);
        }
        etMissLength.setText(buildingModel.getShortareas() + "");
        etMinDistance.setText(buildingModel.getMinspacing() + "");
        etMissArea.setText(buildingModel.getShortareas() + "");
        etPersonActivity.setText(buildingModel.getPeractives());
        etDes.setText(buildingModel.getDangerdesc());
        tvRiskEvaluate.setText(buildingModel.getRiskevaluation());
        tvRiskType.setText(buildingModel.getDangertype());
        etBeforeChangeMethod.setText(buildingModel.getPresolution().trim());
        etChangeMethod.setText(buildingModel.getAftsolution());
        stationId = buildingModel.getStakeid();
        pipeId = buildingModel.getPipeid();
        tvRiskEvaluate.setText(buildingModel.getRiskevaluation1());
        tvRiskResult.setText(buildingModel.getRiskevaluation2());
        tvTotalLevel.setText(buildingModel.getRiskevaluation3());
        if (!TextUtils.isEmpty(buildingModel.getUploadpicture()) &&
                !buildingModel.getUploadpicture().equals("00")) {
            if (buildingModel.getUploadpicture().contains(";")) {
                String[] pathArr = buildingModel.getUploadpicture().split(";");
                for (int i = 0; i < pathArr.length; i++) {
                    path.add(pathArr[i]);
                    photoAdapter.notifyDataSetChanged();
                }
            } else {
                path.add(buildingModel.getUploadpicture());
                photoAdapter.notifyDataSetChanged();
            }
        }
        if (!TextUtils.isEmpty((buildingModel.getRiskevaluation1())) &&
                NumberUtil.isNumber(buildingModel.getRiskevaluation1())) {
            rateLevel = Double.parseDouble(buildingModel.getRiskevaluation1());
        }
        switch (buildingModel.getRiskevaluation2()) {
            case "I":
                resultLevel = 1;
                break;
            case "II":
                resultLevel = 2;
                break;
            case "III":
                resultLevel = 3;
                break;
            case "IV":
                resultLevel = 4;
                break;
            case "V":
                resultLevel = 5;
                break;
        }
        if (rateLevel != 0 && resultLevel != 0) {
            tvTotalLevel.setText(rateLevel * resultLevel + "");
        }

    }

    @OnClick({
            R.id.iv_back,
            R.id.btn_commit,
            R.id.ll_pipeName,
            R.id.ll_time,
            R.id.ll_search,
            R.id.ll_location,
            R.id.ll_pipeProperty,
            R.id.ll_type,
            R.id.ll_riskEvaluate,
            R.id.ll_riskResult,
            R.id.ll_riskType,
            R.id.rl_selectImage,
            R.id.ll_highZone,
            R.id.ll_stationNo,
            R.id.tv_right,
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                Bundle bundle = new Bundle();
                bundle.putString("departmentId", departmentId+"");
                bundle.putInt("stakeId", Integer.parseInt(stakeId));
                bundle.putString("tag", "illegal");
                openActivity(FormActivity.class, bundle);
                break;
            case R.id.ll_stationNo:
                Intent intentStartStation = new Intent(this, StationWaterActivity.class);
                startActivityForResult(intentStartStation, SELECT_STATION);
                break;
            case R.id.ll_highZone:
                dialog.setData(highZoneList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvHighZone.setText(highZoneList.get(positionM));
                    dialog.dismiss();
                });
                break;
            case R.id.rl_selectImage:
                initPermissions();
                break;
            case R.id.ll_riskType:
                dialog.setData(riskTypeList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvRiskType.setText(riskTypeList.get(positionM));
                    dialog.dismiss();
                });
                break;
            case R.id.ll_riskResult:
                dialog.setData(riskResultList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvRiskResult.setText(riskResultList.get(positionM));
                    resultLevel = positionM + 1;
                    if (resultLevel != 0 && rateLevel != 0) {
                        tvTotalLevel.setText(resultLevel * rateLevel + "");
                    }
                    dialog.dismiss();
                });
                break;
            case R.id.ll_riskEvaluate:
                dialog.setData(riskEvaluateList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvRiskEvaluate.setText(riskEvaluateList.get(positionM));
                    rateLevel = positionM + 1;
                    if (resultLevel != 0 && rateLevel != 0) {
                        tvTotalLevel.setText(resultLevel * rateLevel + "");
                    }
                    dialog.dismiss();
                });
                break;
            case R.id.ll_type:
                dialog.setData(typeList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvType.setText(typeList.get(positionM));
                    dialog.dismiss();
                });
                break;
//            case R.id.ll_pipeProperty:
//                dialog.setData(pipePropertyList);
//                dialog.show();
//                dialog.setListItemClick(positionM -> {
//                    tvPipeProperty.setText(pipePropertyList.get(positionM));
//                    dialog.dismiss();
//                });
//                break;
            case R.id.ll_location:
//                Intent intentArea = new Intent(this, MapActivity.class);
//                startActivityForResult(intentArea, SELECT_AREA);
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
                    if (TextUtils.isEmpty(buildingId)) {
                        addBuilding();
                    } else {
                        updateBuilding();
                    }
                }
                break;
            case R.id.ll_time:
                pvTime.show(view);
                break;
            case R.id.ll_search:
                Intent intent = new Intent(PipeBuildingActivity.this, SearchBuildingActivity.class);
                startActivityForResult(intent, SEARCH_BUILDING);
                break;
        }
    }

    private void addBuilding() {
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
        params.addProperty("appempid", userId);
        params.addProperty("id", 0);
        params.addProperty("stakeid", stationId);
        params.addProperty("pipeid", pipeId);
        params.addProperty("locationdesc", etLocation.getText().toString());
        params.addProperty("overpropety", property);
        params.addProperty("overtype", selectPressureName);
        params.addProperty("overname", etName.getText().toString());
        params.addProperty("genernaldate", tvTime.getText().toString());
        params.addProperty("highareasflag", tvHighZone.getText().toString());
        params.addProperty("shortlength", Double.parseDouble(etMissLength.getText().toString()));
        params.addProperty("minspacing", Double.parseDouble(etMinDistance.getText().toString()));
        params.addProperty("shortareas", Double.parseDouble(etMissArea.getText().toString()));
        params.addProperty("peractives", etPersonActivity.getText().toString());
        params.addProperty("dangerdesc", etDes.getText().toString());
        params.addProperty("riskevaluation1", tvRiskEvaluate.getText().toString());
        params.addProperty("riskevaluation2", tvRiskResult.getText().toString());
        params.addProperty("riskevaluation3", tvTotalLevel.getText().toString());
        params.addProperty("dangertype", tvRiskType.getText().toString());
        params.addProperty("presolution", etBeforeChangeMethod.getText().toString());
        params.addProperty("aftsolution", etChangeMethod.getText().toString());
        if (!TextUtils.isEmpty(photoSb.toString())) {
            params.addProperty("uploadpicture", photoSb.toString());
        } else {
            params.addProperty("uploadpicture", "00");
        }
        Log.i("tag","params======="+params);
        Net.create(Api.class).addBuilding(token, params)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel result) {
                        if (result.getCode() == Constant.SUCCESS_CODE) {
                            ToastUtil.show("保存成功");
                            finish();
                        } else {
                            ToastUtil.show("保存失败");
                        }
                    }
                });
    }

    private void updateBuilding() {
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
        params.addProperty("id", Integer.parseInt(buildingId));
        params.addProperty("stakeid", stationId);
        params.addProperty("pipeid", pipeId);
        params.addProperty("appempid", userId);
        params.addProperty("locationdesc", etLocation.getText().toString());
        params.addProperty("overpropety", property);
        params.addProperty("overtype", selectPressureName);
        params.addProperty("overname", etName.getText().toString());
        params.addProperty("genernaldate", tvTime.getText().toString());
        params.addProperty("highareasflag", tvHighZone.getText().toString());
        params.addProperty("shortlength", Double.parseDouble(etMissLength.getText().toString()));
        params.addProperty("minspacing", Double.parseDouble(etMinDistance.getText().toString()));
        params.addProperty("shortareas", Double.parseDouble(etMissArea.getText().toString()));
        params.addProperty("peractives", etPersonActivity.getText().toString());
        params.addProperty("dangerdesc", etDes.getText().toString());
        params.addProperty("riskevaluation1", tvRiskEvaluate.getText().toString());
        params.addProperty("riskevaluation2", tvRiskResult.getText().toString());
        params.addProperty("riskevaluation3", tvTotalLevel.getText().toString());
        params.addProperty("dangertype", tvRiskType.getText().toString());
        params.addProperty("presolution", etBeforeChangeMethod.getText().toString());
        params.addProperty("aftsolution", etChangeMethod.getText().toString());

        if (!TextUtils.isEmpty(photoSb.toString())) {
            params.addProperty("uploadpicture", photoSb.toString());
        } else {
            params.addProperty("uploadpicture", "00");
        }
        Net.create(Api.class).updateBuilding(token, params)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel result) {
                        if (result.getCode() == Constant.SUCCESS_CODE) {
                            ToastUtil.show("保存成功");
                            finish();
                        } else {
                            ToastUtil.show("保存失败");
                        }
                    }
                });
    }

    private boolean paramsComplete() {
//        if (TextUtils.isEmpty(tvPipeName.getText().toString())) {
//            ToastUtil.show("请选择管道名称");
//            return false;
//        }
//        if (TextUtils.isEmpty(etStartStationNo.getText().toString())) {
//            ToastUtil.show("请输入起始桩号");
//            return false;
//        }
        if (TextUtils.isEmpty(etLocation.getText().toString())) {
            ToastUtil.show("请输入行政位置");
            return false;
        }
//        if (TextUtils.isEmpty(tvPipeProperty.getText().toString())) {
//            ToastUtil.show("请选择占据性质");
//            return false;
//        }
        if (TextUtils.isEmpty(selectPressureName)) {
            ToastUtil.show("请选择占压类型");
            return false;
        }
        if (TextUtils.isEmpty(etName.getText().toString())) {
            ToastUtil.show("请输入占压名称");
            return false;
        }
        if (TextUtils.isEmpty(tvTime.getText().toString())) {
            ToastUtil.show("请选择形成时间");
            return false;
        }
        if (TextUtils.isEmpty(etMissLength.getText().toString())) {
            ToastUtil.show("请输入占压或安全距离不足长度(m)");
            return false;
        }
        if (!NumberUtil.isNumber(etMissLength.getText().toString())) {
            ToastUtil.show("占压或安全距离格式不正确");
            return false;
        }
        if (TextUtils.isEmpty(etMinDistance.getText().toString())) {
            ToastUtil.show("请输入与管道最小间距(m)");
            return false;
        }
        if (!NumberUtil.isNumber(etMinDistance.getText().toString())) {
            ToastUtil.show("与管道最小间距格式不正确");
            return false;
        }
        if (TextUtils.isEmpty(etMissArea.getText().toString())) {
            ToastUtil.show("请输入占压或安全距离不足面积(㎡)");
            return false;
        }
        if (!NumberUtil.isNumber(etMissArea.getText().toString())) {
            ToastUtil.show("占压或安全距离不足面积格式不正确");
            return false;
        }
        if (TextUtils.isEmpty(etPersonActivity.getText().toString())) {
            ToastUtil.show("请输入人员活动情况");
            return false;
        }
        if (TextUtils.isEmpty(etDes.getText().toString())) {
            ToastUtil.show("请输入占压物描述");
            return false;
        }
//        if (TextUtils.isEmpty(tvRiskEvaluate.getText().toString())) {
//            ToastUtil.show("风险评价-事故概率");
//            return false;
//        }
//        if (TextUtils.isEmpty(tvRiskResult.getText().toString())) {
//            ToastUtil.show("风险评价-事故后果");
//            return false;
//        }
        if (TextUtils.isEmpty(tvRiskType.getText().toString())) {
            ToastUtil.show("请选择隐患类型");
            return false;
        }
        if (TextUtils.isEmpty(etBeforeChangeMethod.getText().toString())) {
            ToastUtil.show("请输入整改前控制措施");
            return false;
        }
        if (TextUtils.isEmpty(etChangeMethod.getText().toString())) {
            ToastUtil.show("请输入整改计划");
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
        if (requestCode == SEARCH_BUILDING) {
            BuildingModel buildingModel = (BuildingModel) data.getSerializableExtra("building");
            String pipeDesc = buildingModel.getPipedesc();
            if (!TextUtils.isEmpty(pipeDesc)) {
                String[] descArr = pipeDesc.split(":");
                tvPipeName.setText(descArr[0]);
                etStartStationNo.setText(descArr[1]);
            }
            etLocation.setText(buildingModel.getLocationdesc());
            tvPipeProperty.setText(buildingModel.getOverpropety());
            tvType.setText(buildingModel.getOvertype());
            etName.setText(buildingModel.getOvername());
            tvTime.setText(buildingModel.getGenernaldate());
            if (buildingModel.getHighareasflag().equals("是")) {
                rbYes.setChecked(true);
            } else {
                rbNo.setChecked(true);
            }
            etMissLength.setText(buildingModel.getShortareas() + "");
            etMinDistance.setText(buildingModel.getMinspacing() + "");
            etMissArea.setText(buildingModel.getShortareas() + "");
            etPersonActivity.setText(buildingModel.getPeractives());
            etDes.setText(buildingModel.getDangerdesc());
            tvRiskEvaluate.setText(buildingModel.getRiskevaluation());
            tvRiskType.setText(buildingModel.getDangertype());
            etBeforeChangeMethod.setText(buildingModel.getPresolution());
            etChangeMethod.setText(buildingModel.getAftsolution());
        } else if (requestCode == SELECT_AREA) {
            String area = data.getStringExtra("area");
            etLocation.setText(area);
        } else if (requestCode == SELECT_STATION) {
            stationId = Integer.parseInt(data.getStringExtra("stationId"));
            pipeId = Integer.parseInt(data.getStringExtra("pipeId"));
            String stationName = data.getStringExtra("stationName");
            String location = data.getStringExtra("location");
            tvStationNo.setText(stationName);
            etLocation.setText(location);
        }
    }

    // 授权管理
    private void initPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(mContext, "请在 设置-应用管理 中开启此应用的储存授权。", Toast.LENGTH_SHORT).show();
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
}
