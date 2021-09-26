package com.gd.form.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gd.form.R;
import com.gd.form.adapter.PhotoAdapter;
import com.gd.form.base.BaseActivity;
import com.gd.form.model.GlideImageLoader;
import com.gd.form.model.OtherDetailModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.SPUtil;
import com.gd.form.utils.TimeUtil;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;
import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.inter.IHandlerCallBack;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SomeOthersActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_stationNo)
    TextView tvStationNo;
    @BindView(R.id.et_distance)
    EditText etDistance;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.ll_name)
    LinearLayout llName;
    @BindView(R.id.view_name)
    View viewName;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.rvResultPhoto)
    RecyclerView rvResultPhoto;
    @BindView(R.id.rb_yes)
    RadioButton rbYes;
    @BindView(R.id.rb_no)
    RadioButton rbNo;
    private String token, userId;
    private String activityName;
    private List<String> path;
    private GalleryConfig galleryConfig;
    private PhotoAdapter photoAdapter;
    private IHandlerCallBack iHandlerCallBack;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_some_other;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        path = new ArrayList<>();
        token = (String) SPUtil.get(SomeOthersActivity.this, "token", "");
        userId = (String) SPUtil.get(SomeOthersActivity.this, "userId", "");
        if (getIntent() != null) {
            activityName = getIntent().getExtras().getString("name");
            String id = getIntent().getExtras().getString("id");
            if (activityName.equals("windVane")) {
                tvTitle.setText("风向标详情");
            } else if (activityName.equals("videoMonitoring")) {
                tvTitle.setText("视频监控详情");
            } else if (activityName.equals("advocacyBoard")) {
                tvTitle.setText("宣教栏详情");
            } else if (activityName.equals("other")) {
                tvTitle.setText("地震监测等设备设施详情");
            }
            initGallery();
            initConfig();
            getDetail(id);
        }
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

    @OnClick({
            R.id.iv_back,
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private void getDetail(String id) {
        JsonObject params = new JsonObject();
        params.addProperty("id", id);
        Log.i("tag", "params====" + params);
        Net.create(Api.class).otherDetail(token, params)
                .enqueue(new NetCallback<OtherDetailModel>(this, true) {
                    @Override
                    public void onResponse(OtherDetailModel result) {
                        tvStationNo.setText(result.getStakename());
                        etDistance.setText(result.getStakefrom());
                        if (result.getCreatime() != null) {
                            tvTime.setText(TimeUtil.longToFormatTime(result.getCreatime().getTime()));
                        }
                        if (result.getPerfectcondition().equals("正常")) {
                            rbYes.setChecked(true);
                        } else {
                            rbNo.setChecked(true);
                        }
                        etRemark.setText(result.getRemark());
                        if (!result.getUploadpicture().equals("00")) {
                            String[] photoArr = result.getUploadpicture().split(";");
                            for (int i = 0; i < photoArr.length; i++) {
                                path.add(photoArr[i]);
                            }
                            photoAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}
