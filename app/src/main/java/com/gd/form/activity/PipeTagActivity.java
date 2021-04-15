package com.gd.form.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.gd.form.R;
import com.gd.form.base.BaseActivity;
import com.gd.form.constants.Constant;
import com.gd.form.model.Department;
import com.gd.form.model.Pipelineinfo;
import com.gd.form.model.SearchStationModel;
import com.gd.form.model.ServerModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.NumberUtil;
import com.gd.form.utils.SPUtil;
import com.gd.form.utils.ToastUtil;
import com.gd.form.view.ListDialog;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PipeTagActivity extends BaseActivity {
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
    @BindView(R.id.ll_stationNo)
    LinearLayout llStationNo;
    @BindView(R.id.tv_stationNo)
    TextView tvStationNo;
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
    @BindView(R.id.et_location)
    EditText etLocation;

    private int departmentId, pipeId;
    private ListDialog dialog;
    private String token, userId;
    private String stationId, stationName, pipeName, departmentName;
    private SearchStationModel searchStationModel;
    private String tag;
    private List<Department> departmentList;
    private List<Pipelineinfo> pipelineInfoList;
    private int SELECT_STATION = 101;
    private String newPipeTagId;

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
        tvTitle.setText("管道标识");
        token = (String) SPUtil.get(this, "token", "");
        userId = (String) SPUtil.get(this, "userId", "");
        dialog = new ListDialog(this);
        if (getIntent() != null) {
            tag = getIntent().getExtras().getString("tag");
            newPipeTagId = getIntent().getExtras().getString("newPipeTagId");
            if (!tag.equals("new")) {
                stationId = getIntent().getExtras().getString("stationId");
                departmentName = getIntent().getExtras().getString("departmentName");
                stationName = getIntent().getExtras().getString("stationName");
                pipeName = getIntent().getExtras().getString("pipeName");
                departmentId = Integer.valueOf(getIntent().getExtras().getString("departmentId"));
                pipeId = Integer.valueOf(getIntent().getExtras().getString("pipeId"));
                searchStationModel = (SearchStationModel) getIntent().getExtras().getSerializable("searchStationModel");
                if (!TextUtils.isEmpty(departmentName) && !departmentName.equals("null")) {
                    tvArea.setText(departmentName);
                } else {
                    tvArea.setText("暂无");
                }
                tvPipeName.setText(pipeName);
                tvStationNo.setText(stationName);
            }
            if (searchStationModel != null) {
                tvPipeName.setText(pipeName);
                tvStationNo.setText(stationName);
                tvGroundTagType.setText(searchStationModel.getStaketype());
                etKgInfo.setText(searchStationModel.getMileageinfo());
                etCorner.setText(searchStationModel.getCornerinfo());
                etLongitude.setText(searchStationModel.getEastlongitude());
                etLatitude.setText(searchStationModel.getNorthlatitude());
                tvLandForm.setText(searchStationModel.getTopagraphy());
                etLocation.setText(searchStationModel.getLocationdesc());
                etDepth.setText(searchStationModel.getStandardeep());
                etName.setText(searchStationModel.getLandinfo());
                etPhone.setText(searchStationModel.getLandtel());
                etRemark.setText(searchStationModel.getRemarks());
            }
            //查看
            if (tag.equals("check")) {
                llArea.setEnabled(false);
                llPipeName.setEnabled(false);
                llGroundTagType.setEnabled(false);
                llLandForm.setEnabled(false);
                llLocation.setEnabled(false);
                tvStationNo.setEnabled(false);
                etKgInfo.setEnabled(false);
                etCorner.setEnabled(false);
                etLongitude.setEnabled(false);
                etLatitude.setEnabled(false);
                etDepth.setEnabled(false);
                etName.setEnabled(false);
                etPhone.setEnabled(false);
                etRemark.setEnabled(false);
                etLocation.setEnabled(false);
                llStationNo.setEnabled(false);
            } else if (tag.equals("add")) {
                tvRight.setVisibility(View.VISIBLE);
                tvRight.setText("测量");
                llArea.setEnabled(true);
                llPipeName.setEnabled(true);
                llGroundTagType.setEnabled(true);
                llLandForm.setEnabled(true);
                llLocation.setEnabled(true);
                tvStationNo.setEnabled(true);
                etKgInfo.setEnabled(true);
                etCorner.setEnabled(true);
                etLongitude.setEnabled(true);
                etLatitude.setEnabled(true);
                etDepth.setEnabled(true);
                etName.setEnabled(true);
                etPhone.setEnabled(true);
                etRemark.setEnabled(true);
                etLocation.setEnabled(true);
                llStationNo.setEnabled(true);
            }
        }
        pipeDepartmentInfoGetList();
        getPipelineInfoListRequest();
    }

    private void pipeDepartmentInfoGetList() {
        Net.create(Api.class).pipedepartmentinfoGetList()
                .enqueue(new NetCallback<List<Department>>(this, false) {
                    @Override
                    public void onResponse(List<Department> list) {
                        departmentList = list;
                    }
                });
    }

    private void getPipelineInfoListRequest() {

        Net.create(Api.class).pipelineinfosget()
                .enqueue(new NetCallback<List<Pipelineinfo>>(this, false) {
                    @Override
                    public void onResponse(List<Pipelineinfo> list) {
                        pipelineInfoList = list;
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
            R.id.ll_stationNo,
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_stationNo:
                Intent intentStation = new Intent(this, StationActivity.class);
                startActivityForResult(intentStation, SELECT_STATION);
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
                landFormList.add("水泥");
                landFormList.add("钢");
                landFormList.add("玻璃钢");
                dialog.setData(landFormList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvLandForm.setText(landFormList.get(positionM));
                    dialog.dismiss();
                });
                break;
            case R.id.ll_groundTagType:
                List<String> typeList = new ArrayList<>();
                typeList.add("标志桩（交叉穿越、站场、阀室、里程）");
                typeList.add("转角桩");
                typeList.add("电位测试桩");
                typeList.add("交流排流桩（去耦合器）");
                typeList.add("新增交叉桩");
                typeList.add("警示牌");
                typeList.add("高杆警示牌");
                typeList.add("加密桩");
                typeList.add("人井(盘缆点)桩");
                typeList.add("吹缆点标识桩");
                typeList.add("光缆测试桩");
                typeList.add("其他");
                dialog.setData(typeList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvGroundTagType.setText(typeList.get(positionM));
                    dialog.dismiss();
                });
                break;
            case R.id.btn_commit:
                if (paramsComplete()) {
                    commit();
                }
                break;
            case R.id.tv_right:
                Bundle bundle = new Bundle();
                bundle.putString("stationId", stationId);
                openActivity(PipeMeasureActivity.class, bundle);
                break;

        }
    }

    private void commit() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("departmentid", departmentId);
        jsonObject.addProperty("pipeid", pipeId);
        jsonObject.addProperty("name", tvStationNo.getText().toString());
        jsonObject.addProperty("staketype", tvGroundTagType.getText().toString());
        jsonObject.addProperty("mileageinfo", etKgInfo.getText().toString());
        jsonObject.addProperty("cornerinfo", etCorner.getText().toString());
        jsonObject.addProperty("eastlongitude", etLongitude.getText().toString());
        jsonObject.addProperty("northlatitude", etLatitude.getText().toString());
        jsonObject.addProperty("topagraphy", tvLandForm.getText().toString());
        jsonObject.addProperty("locationdesc", etLocation.getText().toString());
        jsonObject.addProperty("standardeep", etDepth.getText().toString());
        jsonObject.addProperty("landinfo", etName.getText().toString());
        jsonObject.addProperty("landtel", etPhone.getText().toString());
        jsonObject.addProperty("remarks", etRemark.getText().toString());
        if ("add".equals(tag)) {
            if (!TextUtils.isEmpty(newPipeTagId)) {
                jsonObject.addProperty("id", newPipeTagId);
            }
        }
        Log.i("tag", "jsonObject==" + jsonObject);
        //新增
        if ("new".equals(tag)) {
            Net.create(Api.class).addPipeStakeInfo(token, jsonObject)
                    .enqueue(new NetCallback<ServerModel>(this, true) {
                        @Override
                        public void onResponse(ServerModel result) {
                            if (result.getCode() == Constant.SUCCESS_CODE) {
                                Intent intent = new Intent();
                                intent.putExtra("id", result.getMsg());
                                intent.setAction("com.action.add");
                                sendBroadcast(intent);
                                finish();
                            } else {
                                ToastUtil.show("保存失败");
                            }
                        }
                    });
            //更新
        } else if ("add".equals(tag)) {
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
                                ToastUtil.show("保存失败");
                            }
                        }
                    });
        }

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
        if (TextUtils.isEmpty(tvStationNo.getText().toString())) {
            ToastUtil.show("请选择桩号");
            return false;
        }
        if (TextUtils.isEmpty(tvGroundTagType.getText().toString())) {
            ToastUtil.show("请选择地面标志类型");
            return false;
        }
        if (TextUtils.isEmpty(etKgInfo.getText().toString())) {
            ToastUtil.show("请输入里程信息");
            return false;
        }
        if (TextUtils.isEmpty(etCorner.getText().toString())) {
            ToastUtil.show("请输入转角信息");
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
        if (TextUtils.isEmpty(etDepth.getText().toString())) {
            ToastUtil.show("请输入标准埋深");
            return false;
        }
        if (!NumberUtil.isNumber(etDepth.getText().toString())) {
            ToastUtil.show("标准埋深格式不正确");
            return false;
        }
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
            stationId = data.getStringExtra("stationId");
            String stationName = data.getStringExtra("stationName");
            tvStationNo.setText(stationName);
        }
    }
}
