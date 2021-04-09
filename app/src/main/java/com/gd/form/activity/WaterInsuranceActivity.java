package com.gd.form.activity;

import android.Manifest;
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

import com.gd.form.R;
import com.gd.form.adapter.PhotoAdapter;
import com.gd.form.base.BaseActivity;
import com.gd.form.model.Department;
import com.gd.form.model.GlideImageLoader;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.ToastUtil;
import com.gd.form.view.ListDialog;
import com.jaeger.library.StatusBarUtil;
import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;
import com.yancy.gallerypick.inter.IHandlerCallBack;

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
    @BindView(R.id.tv_zh)
    TextView tvStationNo;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.tv_spr)
    TextView tvSpr;
    @BindView(R.id.tv_fileName)
    TextView tvFileName;
    @BindView(R.id.rvResultPhoto)
    RecyclerView rvResultPhoto;
    private List<Department> departmentList;
    private ListDialog dialog;
    private int FILE_REQUEST_CODE = 100;
    private int SELECT_STATION = 101;
    private int SELECT_APPROVER = 102;
    private int SELECT_ADDRESS = 103;
    private int PERMISSIONS_REQUEST_READ_CONTACTS = 8;
    private String approverName;
    private String approverId;
    private IHandlerCallBack iHandlerCallBack;
    private List<String> path;
    private GalleryConfig galleryConfig;
    private PhotoAdapter photoAdapter;
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
        tvTitle.setText("水工施工检查日志");
        dialog = new ListDialog(this);
        path = new ArrayList<>();
        //获取管道单位
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
                        break;
                    case R.id.rb_mouthUpload:
                        break;
                    case R.id.rb_writeUpload:
                        break;
                }
            }
        });
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
        Net.create(Api.class).pipedepartmentinfoGetList()
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
            R.id.ll_selectPic,
    })
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_commit:
                paramsComplete();
                break;
            case R.id.ll_scfj:
                Intent intentAddress = new Intent(this, SelectFileActivity.class);
                startActivityForResult(intentAddress, FILE_REQUEST_CODE);
                break;
            case R.id.ll_spr:
                Intent intentApprover = new Intent(this, ApproverActivity.class);
                startActivityForResult(intentApprover, SELECT_APPROVER);
                break;
            case R.id.ll_location:
                Intent intent = new Intent(this, MapActivity.class);
                startActivityForResult(intent, SELECT_ADDRESS);
                break;
            case R.id.ll_selectImage:
            case R.id.ll_selectPic:
                initPermissions();
                break;
            case R.id.ll_zh:
                Intent intentStartStation = new Intent(this, StationActivity.class);
                startActivityForResult(intentStartStation, SELECT_STATION);
                break;
            case R.id.ll_weather:
                List<String> weatherList = new ArrayList<>();
                weatherList.add("晴");
                weatherList.add("阴");
                weatherList.add("小雪");
                weatherList.add("大雪");
                weatherList.add("雨");
                dialog.setData(weatherList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvWeather.setText(weatherList.get(positionM));
                    dialog.dismiss();
                });
                break;
            case R.id.ll_area:
                List<String> areaList = new ArrayList<>();
                if (departmentList != null && departmentList.size() > 0) {
                    for (int i = 0; i < departmentList.size(); i++) {
                        areaList.add(departmentList.get(i).getName());
                    }
                }
                dialog.setData(areaList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvArea.setText(areaList.get(positionM));
                    dialog.dismiss();
                });
                break;
        }
    }

    private boolean paramsComplete() {
        if (TextUtils.isEmpty(tvArea.getText().toString())) {
            ToastUtil.show("请选择作业区");
            return false;
        }
        if (TextUtils.isEmpty(etCompany.getText().toString())) {
            ToastUtil.show("请输入施工单位");
            return false;
        }
        if (TextUtils.isEmpty(etName.getText().toString())) {
            ToastUtil.show("请输入工程名称");
            return false;
        }
        if (TextUtils.isEmpty(tvWeather.getText().toString())) {
            ToastUtil.show("请选择天气");
            return false;
        }
        if (TextUtils.isEmpty(etArea.getText().toString())) {
            ToastUtil.show("请选择施工地点");
            return false;
        }
        if (TextUtils.isEmpty(etWorkingPlane.getText().toString())) {
            ToastUtil.show("请输入施工作业面");
            return false;
        }
        if (TextUtils.isEmpty(tvStationNo.getText().toString())) {
            ToastUtil.show("请选择施桩号");
            return false;
        }
        if (TextUtils.isEmpty(etProblem.getText().toString())) {
            ToastUtil.show("请输入检查存在的问题");
            return false;
        }
        if (TextUtils.isEmpty(etAdvice.getText().toString())) {
            ToastUtil.show("请输入处理意见");
            return false;
        }
        if (TextUtils.isEmpty(etManager.getText().toString())) {
            ToastUtil.show("请输入施工负责人");
            return false;
        }
        if (TextUtils.isEmpty(etFinishCount.getText().toString())) {
            ToastUtil.show("请输入完成工程量");
            return false;
        }
        if (TextUtils.isEmpty(etOther.getText().toString())) {
            ToastUtil.show("请输入施工动态、其它人员巡视情况");
            return false;
        }
        if (TextUtils.isEmpty(tvLocation.getText().toString())) {
            ToastUtil.show("请选择坐标");
            return false;
        }
        if (TextUtils.isEmpty(tvSpr.getText().toString())) {
            ToastUtil.show("请选择审批人");
            return false;
        }
        return true;
    }

    //授权管理
    private void initPermissions() {
        if (ContextCompat.checkSelfPermission(WaterInsuranceActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(WaterInsuranceActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(mContext, "请在 设置-应用管理 中开启此应用的储存授权。", Toast.LENGTH_SHORT).show();
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
                Log.i("tag", "拒绝授权");
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
            String name = data.getStringExtra("fileName");
            tvFileName.setText(name);
            //选择桩号
        } else if (requestCode == SELECT_STATION) {
            String stationName = data.getStringExtra("stationName");
            tvStationNo.setText(stationName);
        } else if (requestCode == SELECT_ADDRESS) {
            String latitude = data.getStringExtra("latitude");
            String longitude = data.getStringExtra("longitude");
            if (!TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude)) {
                tvLocation.setText("经度:" + longitude + "   纬度:" + latitude);
            }
        } else if (requestCode == SELECT_APPROVER) {
            approverName = data.getStringExtra("name");
            approverId = data.getStringExtra("id");
            if (!TextUtils.isEmpty(approverName)) {
                tvSpr.setText(approverName);
            }
        }

    }
}
