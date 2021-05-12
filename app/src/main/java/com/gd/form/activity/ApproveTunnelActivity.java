package com.gd.form.activity;

import android.content.Intent;
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

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.gd.form.model.TunnelDataDetail;
import com.gd.form.model.TunnelDetailModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.SPUtil;
import com.gd.form.utils.Util;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;

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
    EditText et_alarmPro;

    private String formId;
    private String token, userId;
    private MarkerOptions markerOption;
    private AMap aMap;
    private PhotoAdapter photoAdapter;
    private List<String> path;
    private String filePath;

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
            String tag = bundle.getString("tag");
            if (tag.equals("detail")) {
                btnApprove.setVisibility(View.GONE);
            }
            formId = bundle.getString("formId");
        }
        llFile.setVisibility(View.GONE);
        viewFile.setVisibility(View.GONE);
        path = new ArrayList<>();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvResultPhoto.setLayoutManager(gridLayoutManager);
        photoAdapter = new PhotoAdapter(this, path);
        rvResultPhoto.setAdapter(photoAdapter);
        getDetail(formId);

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
                            tvPipeName.setText(dataDetail.getPipeString());
                            if (dataDetail.getCol1().equals("无违章采石、采矿、爆破行为")) {
                                rbNo.setChecked(true);
                            } else if (dataDetail.getCol1().equals("有违章采石、采矿、爆破行为")) {
                                rbYes.setChecked(true);
                            }
                            etBuildingPro.setText(dataDetail.getCol1desc());

                            if (dataDetail.getCol2().equals("无第三方施工行为")) {
                                rbThirdNo.setChecked(true);
                            } else if (dataDetail.getCol2().equals("有第三方施工行为")) {
                                rbThirdYes.setChecked(true);
                            }
                            etThirdPro.setText(dataDetail.getCol2desc());

                            if (dataDetail.getCol3().equals("无可疑人员、车辆逗留现象")) {
                                rbSuspectNo.setChecked(true);
                            } else if (dataDetail.getCol3().equals("有可疑人员、车辆逗留现象")) {
                                rbThirdYes.setChecked(true);
                            }
                            etSuspectPro.setText(dataDetail.getCol3desc());


                            if (dataDetail.getCol4().equals("完好")) {
                                rbBlockYes.setChecked(true);
                            } else if (dataDetail.getCol4().equals("发生局部破损")) {
                                rbBlockMiddle.setChecked(true);
                            } else if (dataDetail.getCol4().equals("发生大面积破损")) {
                                rbBadNo.setChecked(true);
                            }
                            etBlockPro.setText(dataDetail.getCol4());

                            if (dataDetail.getCol5().equals("无裂缝、沉降，四周有无滑坡、水土流失")) {
                                rbCaveNo.setChecked(true);
                            } else if (dataDetail.getCol5().equals("有裂缝、沉降情况")) {
                                rbCaveMiddle.setChecked(true);
                            } else if (dataDetail.getCol5().equals("有滑坡、水土流失情况")) {
                                rbCaveYes.setChecked(true);
                            }
                            etCavePro.setText(dataDetail.getCol5desc());


                            if (dataDetail.getCol6().equals("完好，无破损")) {
                                rbWaterYes.setChecked(true);
                            } else if (dataDetail.getCol5().equals("局部损坏")) {
                                rbWaterMiddle.setChecked(true);
                            } else if (dataDetail.getCol5().equals("损坏严重，作用基本失效")) {
                                rbWaterNo.setChecked(true);
                            }
                            etWaterPro.setText(dataDetail.getCol6desc());


                            if (dataDetail.getCol7().equals("无管道本体翘起、下沉，无管沟沉降")) {
                                rbTransitNo.setChecked(true);
                            } else if (dataDetail.getCol7().equals("有管道本体偏离原有位置")) {
                                rbTransitMiddle.setChecked(true);
                            } else if (dataDetail.getCol7().equals("有管沟沉降情况")) {
                                rbThirdYes.setChecked(true);
                            }
                            etTransitPro.setText(dataDetail.getCol7desc());

                            if (dataDetail.getCol8().equals("无异响、异味现象")) {
                                rbBadNo.setChecked(true);
                            } else if (dataDetail.getCol8().equals("有异响、异味现象")) {
                                rbBadYes.setChecked(true);
                            }
                            etTransitPro.setText(dataDetail.getCol8desc());

                            if (dataDetail.getCol9().equals("检测合格")) {
                                rbGasYes.setChecked(true);
                            } else if (dataDetail.getCol8().equals("检测不合格")) {
                                rbGasNo.setChecked(true);
                            }
                            etGasPro.setText(dataDetail.getCol9desc());

                            if (dataDetail.getCol10().equals("外观完好")) {
                                rbAlarmYes.setChecked(true);
                            } else if (dataDetail.getCol8().equals("外观损坏")) {
                                rbAlarmNo.setChecked(true);
                            }
                            et_alarmPro.setText(dataDetail.getCol9desc());
                            etOtherPro.setText(dataDetail.getCol11());

                            String location = model.getDatadetail().getLocate();
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
                                }
                                //审批状态，0-表示批复不同意，1-表示批复同意，3-表示未批复
                                tvApproveStatus.setText(Util.getApprovalStatus(model.getDatapproval().getApprovalresult()));
                                if (!TextUtils.isEmpty(model.getDatapproval().getApprovalcomment())) {
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

    @OnClick({R.id.iv_back,
            R.id.ll_file,
            R.id.btn_approve})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_approve:
                Bundle bundle = new Bundle();
                bundle.putString("formid", formId);
                openActivity(ApproveFormActivity.class, bundle);
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
