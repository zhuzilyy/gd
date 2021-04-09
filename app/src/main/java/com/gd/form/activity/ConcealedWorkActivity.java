package com.gd.form.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.gd.form.model.GlideImageLoader;
import com.gd.form.utils.ToastUtil;
import com.jaeger.library.StatusBarUtil;
import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;
import com.yancy.gallerypick.inter.IHandlerCallBack;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ConcealedWorkActivity extends BaseActivity {
    private int FILE_REQUEST_CODE = 100;
    private int SELECT_STATION = 101;
    private int SELECT_APPROVER = 102;
    private int SELECT_ADDRESS = 103;
    private int SELECT_AREA = 104;
    private int PERMISSIONS_REQUEST_READ_CONTACTS = 8;
    private String approverName;
    private String approverId;
    private IHandlerCallBack iHandlerCallBack;
    private List<String> path;
    private GalleryConfig galleryConfig;
    private PhotoAdapter photoAdapter;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_stationNo)
    TextView tvStationNo;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.tv_spr)
    TextView tvSpr;
    @BindView(R.id.tv_fileName)
    TextView tvFileName;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.rvResultPhoto)
    RecyclerView rvResultPhoto;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_companyName)
    EditText etCompanyName;
    @BindView(R.id.et_hideContent)
    EditText etHideContent;
    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.et_advice)
    EditText etAdvice;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_concealed_work;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("隐蔽工程检查");
        path = new ArrayList<>();
        initGallery();
        initConfig();
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

    @OnClick({R.id.iv_back,
            R.id.ll_location,
            R.id.ll_scfj,
            R.id.ll_spr,
            R.id.ll_area,
            R.id.ll_stationNo,
            R.id.ll_selectPic,
            R.id.btn_commit,
    })
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_commit:
                paramsComplete();
                break;
            case R.id.ll_selectPic:
                initPermissions();
                break;
            case R.id.ll_area:
                Intent intentArea = new Intent(this, MapActivity.class);
                startActivityForResult(intentArea, SELECT_AREA);
                break;
            case R.id.ll_location:
                Intent intentLocation = new Intent(this, MapActivity.class);
                startActivityForResult(intentLocation, SELECT_ADDRESS);
                break;
            case R.id.ll_stationNo:
                Intent intent = new Intent(this, StationActivity.class);
                startActivityForResult(intent, SELECT_STATION);
                break;
            case R.id.ll_scfj:
                Intent intentAddress = new Intent(this, SelectFileActivity.class);
                startActivityForResult(intentAddress, FILE_REQUEST_CODE);
                break;
            case R.id.ll_spr:
                Intent intentApprover = new Intent(this, ApproverActivity.class);
                startActivityForResult(intentApprover, SELECT_APPROVER);
                break;
        }
    }

    private boolean paramsComplete() {
        if (TextUtils.isEmpty(etName.getText().toString())) {
            ToastUtil.show("请输入工程名称");
            return false;
        }
        if (TextUtils.isEmpty(etCompanyName.getText().toString())) {
            ToastUtil.show("请输入施工单位");
            return false;
        }
        if (TextUtils.isEmpty(tvStationNo.getText().toString())) {
            ToastUtil.show("请选择桩号");
            return false;
        }
        if (TextUtils.isEmpty(tvArea.getText().toString())) {
            ToastUtil.show("请选择施工地点");
            return false;
        }
        if (TextUtils.isEmpty(etHideContent.getText().toString())) {
            ToastUtil.show("请输入隐蔽内容");
            return false;
        }
        if (TextUtils.isEmpty(etRemark.getText().toString())) {
            ToastUtil.show("请输入备注");
            return false;
        }
        if (TextUtils.isEmpty(etAdvice.getText().toString())) {
            ToastUtil.show("请输入检查意见");
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
        if (ContextCompat.checkSelfPermission(ConcealedWorkActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ConcealedWorkActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(mContext, "请在 设置-应用管理 中开启此应用的储存授权。", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(ConcealedWorkActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {
            GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(ConcealedWorkActivity.this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(ConcealedWorkActivity.this);
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
        } else if (requestCode == SELECT_AREA) {
            String area = data.getStringExtra("area");
            tvArea.setText(area);
        }

    }
}
