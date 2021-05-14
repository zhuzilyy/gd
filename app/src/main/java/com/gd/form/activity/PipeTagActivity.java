package com.gd.form.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.gd.form.R;
import com.gd.form.base.BaseActivity;
import com.gd.form.constants.Constant;
import com.gd.form.model.NextStationModel;
import com.gd.form.model.Pipelineinfo;
import com.gd.form.model.Pipemploys;
import com.gd.form.model.SearchArea;
import com.gd.form.model.ServerModel;
import com.gd.form.model.StationDetailInfo;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.NumberUtil;
import com.gd.form.utils.SPUtil;
import com.gd.form.utils.ToastUtil;
import com.gd.form.view.ListDialog;
import com.gd.form.view.ListLandTagDialog;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PipeTagActivity extends BaseActivity implements AMapLocationListener {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.ll_area)
    LinearLayout llArea;
    @BindView(R.id.ll_pipeName)
    LinearLayout llPipeName;
    @BindView(R.id.ll_groundTagType)
    LinearLayout llGroundTagType;
    @BindView(R.id.ll_landForm)
    LinearLayout llLandForm;
    @BindView(R.id.ll_location)
    LinearLayout llLocation;
    @BindView(R.id.et_kgInfo)
    EditText etKgInfo;
    @BindView(R.id.et_corner)
    EditText etCorner;
    @BindView(R.id.et_longitude)
    EditText etLongitude;
    @BindView(R.id.et_latitude)
    EditText etLatitude;
    @BindView(R.id.et_depth)
    EditText etDepth;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.tv_pipeName)
    TextView tvPipeName;
    @BindView(R.id.tv_groundTagType)
    TextView tvGroundTagType;
    @BindView(R.id.tv_landForm)
    TextView tvLandForm;
    @BindView(R.id.tv_upStationNo)
    TextView tvUpStationNo;
    @BindView(R.id.tv_downStationNo)
    TextView tvDownStationNo;
    @BindView(R.id.tv_stationNoPrefix)
    TextView tvStationNoPrefix;
    @BindView(R.id.et_location)
    EditText etLocation;
    @BindView(R.id.et_stationNameNo)
    EditText etStationNameNo;
    @BindView(R.id.tv_pipeManager)
    TextView tvPipeManager;
    @BindView(R.id.ll_upStationNo)
    LinearLayout llUpStationNo;
    @BindView(R.id.ll_downStationNo)
    LinearLayout llDownStationNo;
    @BindView(R.id.ll_pipeManager)
    LinearLayout llPipeManager;
    @BindView(R.id.tv_upStationKm)
    TextView tvUpStationKm;
    @BindView(R.id.tv_downStationKm)
    TextView tvDownStationKm;
    private int departmentId, pipeId;
    private ListLandTagDialog landTypeDialog;
    private ListDialog dialog;
    private String token, userId;
    private String stationId, highZoneId, tunnelId, prefixName;
    private String tag;
    private List<SearchArea> departmentList;
    private List<Pipelineinfo> pipelineInfoList;
    private int SELECT_STATION = 101;
    private int SELECT_APPROVER = 102;
    private String upStationId, approverName, approverId, id, lineId, highZoneName = "", pipeName = "";
    public AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    private AMapLocation currentAmapLocation;
    private StationDetailInfo stationDetailInfo;
    /**
     * 需要进行检测的权限数组
     */
    protected String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };
    //如果设置了target > 28，需要增加这个权限，否则不会弹出"始终允许"这个选择框
    private static String BACKGROUND_LOCATION_PERMISSION = "android.permission.ACCESS_BACKGROUND_LOCATION";
    /**
     * 判断是否需要检测，防止不停的弹框
     */
    private boolean isNeedCheck = true;
    private static final int PERMISSON_REQUESTCODE = 0;
    //是否需要检测后台定位权限，设置为true时，如果用户没有给予后台定位权限会弹窗提示
    private boolean needCheckBackLocation = false;
    private boolean isLoactionSuccess;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_pipe_tag;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("新增桩体");
        tvRight.setVisibility(View.VISIBLE);
        token = (String) SPUtil.get(this, "token", "");
        userId = (String) SPUtil.get(this, "userId", "");
        landTypeDialog = new ListLandTagDialog(this);
        departmentList = new ArrayList<>();
        pipelineInfoList = new ArrayList<>();
        dialog = new ListDialog(this);
        if (getIntent() != null) {
            tag = getIntent().getStringExtra("tag");
            //查看
            if (tag.equals("update")) {
                id = getIntent().getStringExtra("id");
                lineId = getIntent().getStringExtra("lineId");
                getDetailInfo(id, lineId);
                tvRight.setVisibility(View.VISIBLE);
                tvTitle.setText("维护桩体");
//                llArea.setEnabled(false);
//                llPipeName.setEnabled(false);
//                llGroundTagType.setEnabled(false);
//                llUpStationNo.setEnabled(false);
//                llDownStationNo.setEnabled(false);
//                etStationNameNo.setEnabled(false);
//                etKgInfo.setEnabled(false);
//                llPipeManager.setEnabled(false);
//                etCorner.setEnabled(false);
//                etLongitude.setEnabled(false);
//                etLatitude.setEnabled(false);
//                llLandForm.setEnabled(false);
//                etLocation.setEnabled(false);
//                etDepth.setEnabled(false);
//                etName.setEnabled(false);
//                etPhone.setEnabled(false);
//                etRemark.setEnabled(false);
//                llLocation.setEnabled(false);
            } else if (tag.equals("add")) {
                tvRight.setVisibility(View.GONE);
                tvRight.setText("测量");
//                llArea.setEnabled(true);
//                llPipeName.setEnabled(true);
//                llGroundTagType.setEnabled(true);
//                llUpStationNo.setEnabled(true);
//                llDownStationNo.setEnabled(true);
//                etStationNameNo.setEnabled(true);
//                etKgInfo.setEnabled(true);
//                llPipeManager.setEnabled(true);
//                etCorner.setEnabled(true);
//                etLongitude.setEnabled(true);
//                etLatitude.setEnabled(true);
//                llLandForm.setEnabled(true);
//                etLocation.setEnabled(true);
//                etDepth.setEnabled(true);
//                etName.setEnabled(true);
//                etPhone.setEnabled(true);
//                etRemark.setEnabled(true);
//                llLocation.setEnabled(true);
            }
        }
        getLocation();
        pipeDepartmentInfoGetList();
    }

    //获取详情
    private void getDetailInfo(String id, String lineId) {
        JsonObject params = new JsonObject();
        params.addProperty("id", Integer.parseInt(id));
        params.addProperty("pipeid", Integer.parseInt(lineId));
        Log.i("tag", "params==1==" + params);
        Net.create(Api.class).getStationDetailInfo(token, params)
                .enqueue(new NetCallback<List<StationDetailInfo>>(this, true) {
                    @Override
                    public void onResponse(List<StationDetailInfo> list) {
                        if (list != null && list.size() > 0) {
                            stationDetailInfo = list.get(0);
                            if (!TextUtils.isEmpty(stationDetailInfo.getDesc())) {
                                if (stationDetailInfo.getDesc().contains(":")) {
                                    tvArea.setText(stationDetailInfo.getDesc().split(":")[0]);
                                    tvPipeName.setText(stationDetailInfo.getDesc().split(":")[1]);
                                }
                            }
                            tvGroundTagType.setText(stationDetailInfo.getStaketype());
                            tvUpStationNo.setText(stationDetailInfo.getName());
                            etCorner.setText(stationDetailInfo.getCornerinfo());
                            etLongitude.setText(stationDetailInfo.getEastlongitude());
                            etLatitude.setText(stationDetailInfo.getNorthlatitude());
                            tvLandForm.setText(stationDetailInfo.getTopagraphy());
                            etKgInfo.setText(stationDetailInfo.getMileageinfo());
                            etLocation.setText(stationDetailInfo.getLocationdesc());
                            etName.setText(stationDetailInfo.getLandinfo());
                            etPhone.setText(stationDetailInfo.getLandtel());
                            etRemark.setText(stationDetailInfo.getRemarks());
                            upStationId = stationDetailInfo.getId() + "";
                            etStationNameNo.setText(stationDetailInfo.getName());
                            highZoneName = stationDetailInfo.getHighareasname();
                            pipeName = stationDetailInfo.getPipeaccountname();
                            departmentId = stationDetailInfo.getDepartmentid();
                            pipeId = stationDetailInfo.getPipeid();
                            approverId = stationDetailInfo.getPipeowners();
                            tvPipeManager.setText(stationDetailInfo.getOwnername());
                            if (currentAmapLocation != null) {
                                if (TextUtils.isEmpty(stationDetailInfo.getEastlongitude())) {
                                    etLongitude.setText(currentAmapLocation.getLongitude() + "");

                                }
                                if (TextUtils.isEmpty(stationDetailInfo.getNorthlatitude())) {
                                    etLatitude.setText(currentAmapLocation.getLatitude() + "");
                                }
                                if (TextUtils.isEmpty(stationDetailInfo.getLocationdesc())) {
                                    etLocation.setText(currentAmapLocation.getAddress());
                                }
                            }
                            getStationInfo();

                        }
                    }
                });
    }

    private void getLocation() {
        mlocationClient = new AMapLocationClient(getApplicationContext());
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mlocationClient.startLocation();
    }

    private void pipeDepartmentInfoGetList() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("employid", userId);
        Net.create(Api.class).getSearchArea(token, jsonObject)
                .enqueue(new NetCallback<List<SearchArea>>(this, false) {
                    @Override
                    public void onResponse(List<SearchArea> list) {
                        departmentList = list;
                    }
                });
    }

    private void getPipelineInfoListRequest() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("dptid", departmentId);
        Net.create(Api.class).pipelineinfosgetById(token, jsonObject)
                .enqueue(new NetCallback<List<Pipelineinfo>>(this, false) {
                    @Override
                    public void onResponse(List<Pipelineinfo> list) {
                        pipelineInfoList = list;
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
                    }
                });
    }

    @OnClick({
            R.id.iv_back,
            R.id.tv_right,
            R.id.btn_commit,
            R.id.ll_groundTagType,
            R.id.ll_landForm,
            R.id.ll_area,
            R.id.ll_pipeName,
            R.id.ll_upStationNo,
            R.id.ll_pipeManager,
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_pipeManager:
//                if(TextUtils.isEmpty(tvArea.getText().toString())){
//                    ToastUtil.show("请先选择作业区");
//                    return;
//                }
//                getPipeManager();
//                Intent intentApprover = new Intent(PipeTagActivity.this, ApproverActivity.class);
//                startActivityForResult(intentApprover, SELECT_APPROVER);
                break;
            case R.id.ll_upStationNo:
                if (TextUtils.isEmpty(tvGroundTagType.getText().toString())) {
                    ToastUtil.show("请先选择地面标志类型");
                    return;
                }
                if (TextUtils.isEmpty(tvPipeName.getText().toString())) {
                    ToastUtil.show("请先选择线路");
                    return;
                }
                Intent intentStartStation = new Intent(this, StationByFullParamsActivity.class);
                intentStartStation.putExtra("tag", "start");
                intentStartStation.putExtra("departmentId", departmentId + "");
                intentStartStation.putExtra("pipeId", pipeId + "");
                startActivityForResult(intentStartStation, SELECT_STATION);
                break;
            case R.id.ll_pipeName:
                if (TextUtils.isEmpty(tvArea.getText().toString())) {
                    ToastUtil.show("请先选择作业区");
                    return;
                }
                getPipelineInfoListRequest();
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
            case R.id.ll_landForm:
                List<String> landFormList = new ArrayList<>();
                landFormList.add("平原林地");
                landFormList.add("平原耕地");
                landFormList.add("平原荒地");
                landFormList.add("山区耕地");
                landFormList.add("山区荒地");
                landFormList.add("河道");
                landFormList.add("大棚区");
                landFormList.add("违建占压区");
                landFormList.add("城乡建设区");
                landFormList.add("公路绿化带");
                landFormList.add("路边（边沟）");
                landFormList.add("其他");
                dialog.setData(landFormList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvLandForm.setText(landFormList.get(positionM));
                    dialog.dismiss();
                });
                break;
            case R.id.ll_groundTagType:
                List<String> typeList = new ArrayList<>();
                typeList.add("标志桩H");
                typeList.add("标志桩G");
                typeList.add("标志桩A");
                typeList.add("标志桩D");
                typeList.add("标志桩T");
                typeList.add("标志桩J");
                typeList.add("标志桩Z");
                typeList.add("转角桩");
                typeList.add("电位测试桩P");
                typeList.add("电位测试桩L");
                typeList.add("电位测试桩Y");
                typeList.add("排流桩");
                typeList.add("警示牌");
                typeList.add("加密桩");
                typeList.add("光缆桩C");
                typeList.add("光缆桩R");
                typeList.add("光缆桩P");
                landTypeDialog.setData(typeList);
                landTypeDialog.show();
                landTypeDialog.setListItemClick(positionM -> {
                    tvGroundTagType.setText(typeList.get(positionM));
                    if (!TextUtils.isEmpty(tvUpStationNo.getText().toString())) {
                        getStationInfo();
                    }
                    landTypeDialog.dismiss();
                });
                break;
            case R.id.btn_commit:
                if (paramsComplete()) {
                    if ("add".equals(tag)) {
                        addPipeTag();
                    } else if ("update".equals(tag)) {
                        updateTag();
                    }
                }
                break;
            case R.id.tv_right:
                Bundle bundle = new Bundle();
                bundle.putString("stationId", id + "");
                openActivity(PipeMeasureActivity.class, bundle);
                break;

        }
    }

    private void getPipeManager() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("dptid", departmentId);
        Net.create(Api.class).getPipeManager(token, jsonObject)
                .enqueue(new NetCallback<List<Pipemploys>>(this, true) {
                    @Override
                    public void onResponse(List<Pipemploys> list) {
                        List<String> nameList = new ArrayList<>();
                        List<String> idList = new ArrayList<>();
                        if(list!=null && list.size()>0){
                            for (int i = 0; i <list.size() ; i++) {
                                nameList.add(list.get(i).getName());
                                idList.add(list.get(i).getId());
                            }
                        }
                        dialog.setData(nameList);
                        dialog.show();
                        dialog.setListItemClick(positionM -> {
                            tvPipeManager.setText(nameList.get(positionM));
                            approverId = idList.get(positionM);
                            dialog.dismiss();
                        });
                    }
                });
    }

    private void updateTag() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("departmentid", departmentId);
        jsonObject.addProperty("pipeid", pipeId);
        jsonObject.addProperty("id", Integer.parseInt(id));
        jsonObject.addProperty("name", tvUpStationNo.getText().toString());
        jsonObject.addProperty("desc", tvArea.getText().toString() + ":" + tvPipeName.getText().toString());
        jsonObject.addProperty("staketype", tvGroundTagType.getText().toString());
        jsonObject.addProperty("mileageinfo", etKgInfo.getText().toString());
        jsonObject.addProperty("cornerinfo", etCorner.getText().toString());
        jsonObject.addProperty("eastlongitude", etLongitude.getText().toString());
        jsonObject.addProperty("northlatitude", etLatitude.getText().toString());
        jsonObject.addProperty("topagraphy", tvLandForm.getText().toString());
        jsonObject.addProperty("locationdesc", etLocation.getText().toString());
        jsonObject.addProperty("pipeowners", approverId);
        jsonObject.addProperty("landinfo", etName.getText().toString());
        jsonObject.addProperty("landtel", etPhone.getText().toString());
        jsonObject.addProperty("remarks", etRemark.getText().toString());
        jsonObject.addProperty("highareasid", highZoneId);
        jsonObject.addProperty("pipeaccountid", tunnelId);
        jsonObject.addProperty("highareasname", highZoneName);
        jsonObject.addProperty("pipeaccountname", pipeName);
        Log.i("tag", "jsonObject====" + jsonObject);
        Net.create(Api.class).updateStation(token, jsonObject)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel result) {
                        if (result.getCode() == Constant.SUCCESS_CODE) {
                            ToastUtil.show("保存成功");
                            Intent intent = new Intent();
                            intent.setAction("com.action.update");
                            sendBroadcast(intent);
                            finish();
                        } else {
                            ToastUtil.show(result.getMsg());
                        }
                    }
                });
    }

    private void addPipeTag() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("departmentid", departmentId);
        jsonObject.addProperty("pipeid", pipeId);
        jsonObject.addProperty("prixname", prefixName);
        jsonObject.addProperty("tailname", etStationNameNo.getText().toString());
        jsonObject.addProperty("staketype", tvGroundTagType.getText().toString());
        jsonObject.addProperty("mileageinfo", etKgInfo.getText().toString());
        jsonObject.addProperty("cornerinfo", etCorner.getText().toString());
        jsonObject.addProperty("eastlongitude", etLongitude.getText().toString());
        jsonObject.addProperty("northlatitude", etLatitude.getText().toString());
        jsonObject.addProperty("topagraphy", tvLandForm.getText().toString());
        jsonObject.addProperty("locationdesc", etLocation.getText().toString());
        jsonObject.addProperty("pipeowners", approverId);
        jsonObject.addProperty("landinfo", etName.getText().toString());
        jsonObject.addProperty("landtel", etPhone.getText().toString());
        jsonObject.addProperty("remarks", etRemark.getText().toString());
        jsonObject.addProperty("highareasid", highZoneId);
        jsonObject.addProperty("pipeaccountid", tunnelId);
        Log.i("tag", "jsonObject===" + jsonObject);
        Net.create(Api.class).addPipeStakeInfo(token, jsonObject)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel result) {
                        if (result.getCode() == Constant.SUCCESS_CODE) {
                            ToastUtil.show("保存成功");
                            Intent intent = new Intent();
                            intent.setAction("com.action.update");
                            sendBroadcast(intent);
                            finish();
                        } else {
                            ToastUtil.show(result.getMsg());
                        }
                    }
                });
    }

    public boolean paramsComplete() {
        if (TextUtils.isEmpty(tvArea.getText().toString())) {
            ToastUtil.show("请选择作业区");
            return false;
        }
        if (TextUtils.isEmpty(tvPipeName.getText().toString())) {
            ToastUtil.show("请选择管道");
            return false;
        }
        if (TextUtils.isEmpty(tvGroundTagType.getText().toString())) {
            ToastUtil.show("请选择地面标志类型");
            return false;
        }
        if (TextUtils.isEmpty(tvUpStationNo.getText().toString())) {
            ToastUtil.show("请选择上游桩号");
            return false;
        }
        if (TextUtils.isEmpty(tvDownStationNo.getText().toString())) {
            ToastUtil.show("请选择下游桩号");
            return false;
        }
        if (TextUtils.isEmpty(tvStationNoPrefix.getText().toString())) {
            ToastUtil.show("桩名前缀不能为空");
            return false;
        }
        if (TextUtils.isEmpty(etStationNameNo.getText().toString())) {
            ToastUtil.show("桩名序号不能为空");
            return false;
        }

        if (TextUtils.isEmpty(etKgInfo.getText().toString())) {
            ToastUtil.show("请输入里程信息");
            return false;
        }
        if (!NumberUtil.isNumber(etKgInfo.getText().toString())) {
            ToastUtil.show("里程信息输入不正确");
            return false;
        }
        double kgInfo = Double.parseDouble(etKgInfo.getText().toString());
        double upStationKm = Double.parseDouble(tvUpStationKm.getText().toString());
        double downStationKm = Double.parseDouble(tvDownStationKm.getText().toString());
        if (kgInfo < upStationKm || kgInfo > downStationKm) {
            ToastUtil.show("里程信息要介于上游桩和下游桩之间");
            return false;
        }
        if (TextUtils.isEmpty(etCorner.getText().toString())) {
            ToastUtil.show("非转角桩为0");
            return false;
        }
        if (TextUtils.isEmpty(etLongitude.getText().toString())) {
            ToastUtil.show("请输入位置坐标信息-E(东经)");
            return false;
        }
        if (TextUtils.isEmpty(etLatitude.getText().toString())) {
            ToastUtil.show("位置坐标信息-N(北纬)");
            return false;
        }
        if (TextUtils.isEmpty(tvLandForm.getText().toString())) {
            ToastUtil.show("请选择地形地貌");
            return false;
        }
        if (TextUtils.isEmpty(etLocation.getText().toString())) {
            ToastUtil.show("请输入行政位置");
            return false;
        }
//        if (TextUtils.isEmpty(etDepth.getText().toString())) {
//            ToastUtil.show("请输入标准埋深");
//            return false;
//        }
//        if (!NumberUtil.isNumber(etDepth.getText().toString())) {
//            ToastUtil.show("标准埋深格式不正确");
//            return false;
//        }
        if (TextUtils.isEmpty(etName.getText().toString())) {
            ToastUtil.show("请输入地主信息-姓名");
            return false;
        }
        if (TextUtils.isEmpty(etPhone.getText().toString())) {
            ToastUtil.show("请输入地主信息-联系电话");
            return false;
        }
        if (TextUtils.isEmpty(etRemark.getText().toString())) {
            ToastUtil.show("请输入备注");
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
        if (requestCode == SELECT_STATION) {
            String selectTag = data.getStringExtra("selectTag");
            String stationName = data.getStringExtra("stationName");
            if (!TextUtils.isEmpty(selectTag)) {
                if (selectTag.equals("start")) {
                    tvUpStationNo.setText(stationName);
                    upStationId = data.getStringExtra("stationId");
                    //获取下游桩等信息
                    getStationInfo();
                }
            }
        } else if (requestCode == SELECT_APPROVER) {
            approverName = data.getStringExtra("name");
            approverId = data.getStringExtra("id");
            if (!TextUtils.isEmpty(approverName)) {
                tvPipeManager.setText(approverName);
            }
        }
    }

    private void getStationInfo() {
        JsonObject params = new JsonObject();
        params.addProperty("stakeid", upStationId);
        params.addProperty("ptype", tvGroundTagType.getText().toString());
        Log.i("tag", "params==" + params);
        Net.create(Api.class).getStationInfo(token, params)
                .enqueue(new NetCallback<NextStationModel>(this, true) {
                    @Override
                    public void onResponse(NextStationModel model) {
                        if (model != null) {
                            highZoneId = model.getHighareasid();
                            tunnelId = model.getPipeaccountid();
                            approverId = model.getOwnerid();
                            prefixName = model.getPrixname();
                            tvUpStationKm.setText(model.getStakemile() + "");
                            tvDownStationNo.setText(model.getNextname());
                            tvDownStationKm.setText(model.getNextmile() + "");
                            tvStationNoPrefix.setText(model.getPrixname());
                            tvPipeManager.setText(model.getOwnername());
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
                currentAmapLocation = amapLocation;
                if (!isLoactionSuccess) {
                    isLoactionSuccess = true;
                    //定位成功回调信息，设置相关消息
                    amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    amapLocation.getLatitude();//获取纬度
                    amapLocation.getLongitude();//获取经度
                    amapLocation.getAccuracy();//获取精度信息
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(amapLocation.getTime());
                    df.format(date);//定位时间
                    if ("add".equals(tag)) {
                        etLocation.setText(amapLocation.getAddress());
                        etLongitude.setText(amapLocation.getLongitude() + "");
                        etLatitude.setText(amapLocation.getLatitude() + "");
                    } else if ("update".equals(tag)) {
                        if (TextUtils.isEmpty(stationDetailInfo.getEastlongitude())) {
                            etLongitude.setText(currentAmapLocation.getLongitude() + "");

                        }
                        if (TextUtils.isEmpty(stationDetailInfo.getNorthlatitude())) {
                            etLatitude.setText(currentAmapLocation.getLatitude() + "");
                        }
                        if (TextUtils.isEmpty(stationDetailInfo.getLocationdesc())) {
                            etLocation.setText(currentAmapLocation.getAddress());
                        }
                    }
                }

            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
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
     * 获取权限集中需要申请权限的列表
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

    @TargetApi(23)
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] paramArrayOfInt) {
        if (requestCode == PERMISSON_REQUESTCODE) {
            if (!verifyPermissions(paramArrayOfInt)) {
                showMissingPermissionDialog();
                isNeedCheck = false;
            }
        }
    }

    /**
     * 检测是否所有的权限都已经授权
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
     * 显示提示信息
     *
     * @since 2.5.0
     */
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("当前应用缺少必要权限。请点击\"设置\"-\"权限\"-打开所需权限。");

        // 拒绝, 退出应用
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        builder.setPositiveButton("设置",
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
     * 启动应用的设置
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
