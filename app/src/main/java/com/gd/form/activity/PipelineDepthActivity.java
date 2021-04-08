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
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.gd.form.R;
import com.gd.form.adapter.PhotoAdapter;
import com.gd.form.base.BaseActivity;
import com.gd.form.model.GlideImageLoader;
import com.gd.form.view.ListDialog;
import com.jaeger.library.StatusBarUtil;
import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;
import com.yancy.gallerypick.inter.IHandlerCallBack;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PipelineDepthActivity extends BaseActivity {
    @BindView(R.id.rvResultPhoto)
    RecyclerView rvResultPhoto;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_stationNo)
    TextView tvStationNo;
    @BindView(R.id.tv_pipeName)
    TextView tvPipeName;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_material)
    TextView tvMaterial;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.tv_fileName)
    TextView tvFileName;
    @BindView(R.id.tv_date)
    TextView tvDate;
    private IHandlerCallBack iHandlerCallBack;
    private List<String> path;
    private GalleryConfig galleryConfig;
    private PhotoAdapter photoAdapter;
    private int PERMISSIONS_REQUEST_READ_CONTACTS = 8;
    private int SELECT_STATION = 101;
    private int SELECT_ADDRESS = 102;
    private int FILE_REQUEST_CODE = 100;
    private ListDialog dialog;
    private TimePickerView pvTime;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_pipeline_depth;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("管道埋深及地面标识信息记录表");
        path = new ArrayList<>();
        initGallery();
        initConfig();
        initTimePicker();
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

    private void initTimePicker() {//Dialog 模式下，在底部弹出
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                tvDate.setText(getTime(date));

            }
        }).setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {
                        Log.i("pvTime", "onTimeSelectChanged");
                    }
                })
                .setType(new boolean[]{true, true, true, false, false, false})
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

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    @OnClick({R.id.ll_selectPic,
            R.id.iv_back,
            R.id.ll_pipeName,
            R.id.ll_stationNo,
            R.id.ll_type,
            R.id.ll_material,
            R.id.ll_location,
            R.id.ll_scfj,
            R.id.ll_tbrq,
    })
    public void click(View view) {
        switch (view.getId()) {
            case R.id.ll_selectPic:
                initPermissions();
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_scfj:
                Intent intentAddress = new Intent(PipelineDepthActivity.this, SelectFileActivity.class);
                startActivityForResult(intentAddress, FILE_REQUEST_CODE);
                break;
            case R.id.ll_tbrq:
                pvTime.show(view);
                break;
            case R.id.ll_location:
                Intent intent = new Intent(PipelineDepthActivity.this, MapActivity.class);
                startActivityForResult(intent, SELECT_ADDRESS);
                break;
            case R.id.ll_stationNo:
                Intent intentStation = new Intent(this, StationActivity.class);
                startActivityForResult(intentStation, SELECT_STATION);
                break;
            case R.id.ll_type:
                List<String> typeList = new ArrayList<>();
                typeList.add("类型1");
                typeList.add("类型2");
                typeList.add("类型3");
                if (dialog == null) {
                    dialog = new ListDialog(mContext);
                }
                dialog.setData(typeList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvType.setText(typeList.get(positionM));
                    dialog.dismiss();
                });
                break;
            case R.id.ll_material:
                List<String> materialList = new ArrayList<>();
                materialList.add("材质1");
                materialList.add("材质2");
                materialList.add("材质3");
                if (dialog == null) {
                    dialog = new ListDialog(mContext);
                }
                dialog.setData(materialList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvMaterial.setText(materialList.get(positionM));
                    dialog.dismiss();
                });
                break;
            case R.id.ll_pipeName:
                List<String> pipeList = new ArrayList<>();
                pipeList.add("管道1");
                pipeList.add("管道2");
                pipeList.add("管道3");
                if (dialog == null) {
                    dialog = new ListDialog(mContext);
                }
                dialog.setData(pipeList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvPipeName.setText(pipeList.get(positionM));
                    dialog.dismiss();
                });
                break;
        }
    }

    // 授权管理
    private void initPermissions() {
        if (ContextCompat.checkSelfPermission(PipelineDepthActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(PipelineDepthActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(mContext, "请在 设置-应用管理 中开启此应用的储存授权。", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(PipelineDepthActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {
            GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(PipelineDepthActivity.this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(PipelineDepthActivity.this);
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
        if (requestCode == SELECT_STATION) {
            String stationName = data.getStringExtra("stationName");
            tvStationNo.setText(stationName);
        } else if (requestCode == SELECT_ADDRESS) {
            String latitude = data.getStringExtra("latitude");
            String longitude = data.getStringExtra("longitude");
            if (!TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude)) {
                tvLocation.setText("经度:" + longitude + "   纬度:" + latitude);
            }
        } else if (requestCode == FILE_REQUEST_CODE) {
            String name = data.getStringExtra("fileName");
            tvFileName.setText(name);
        }
    }
}
