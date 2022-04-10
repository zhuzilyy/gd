package com.gd.form.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.gd.form.R;
import com.gd.form.base.BaseActivity;
import com.gd.form.utils.ToastUtil;
import com.gd.form.view.ListDialog;
import com.jaeger.library.StatusBarUtil;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class EngineeringFieldActivity extends BaseActivity {
    private ListDialog dialog;
    private TimePickerView pvTime;
    private int FILE_REQUEST_CODE = 100;
    private int SELECT_APPROVER = 102;
    private int SELECT_ADDRESS = 103;
    private int SELECT_AREA = 104;
    private String approverName;
    private String approverId;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_weather)
    TextView tvWeather;
    @BindView(R.id.tv_startTime)
    TextView tvStartTime;
    @BindView(R.id.tv_endTime)
    TextView tvEndTime;
    @BindView(R.id.tv_spr)
    TextView tvSpr;
    @BindView(R.id.tv_fileName)
    TextView tvFileName;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.rg_isGood)
    RadioGroup rg_isGood;
    @BindView(R.id.rg_isTagGood)
    RadioGroup rg_isTagGood;
    @BindView(R.id.rg_isVideoGood)
    RadioGroup rg_isVideoGood;
    @BindView(R.id.rg_isWeightCar)
    RadioGroup rg_isWeightCar;
    @BindView(R.id.rg_isMachine)
    RadioGroup rg_isMachine;
    @BindView(R.id.rg_isBad)
    RadioGroup rg_isBad;
    @BindView(R.id.et_process)
    EditText etProcess;
    @BindView(R.id.et_other)
    EditText etOther;


    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_engineering_field;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("交叉工程现场检查");
        dialog = new ListDialog(this);
        initTimePicker();
        initListener();
    }

    private void initListener() {
        //现场隔离措施是否完好
        rg_isGood.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesGood:
                        break;
                    case R.id.rb_noGood:
                        break;
                }
            }
        });
        //现场管道标识是否完好
        rg_isTagGood.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesTagGood:
                        break;
                    case R.id.rb_noTagGood:
                        break;
                    case R.id.rb_tagNoRelative:
                        break;
                }
            }
        });
        //现场视频监控是否完好
        rg_isVideoGood.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesVideoGood:
                        break;
                    case R.id.rb_noVideoGood:
                        break;
                    case R.id.rb_videoNoRelative:
                        break;
                }
            }
        });
        //管道上方是否有重车碾压
        rg_isWeightCar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesWeightCar:
                        break;
                    case R.id.rb_noWeightCar:
                        break;
                    case R.id.rb_weightCarNoRelative:
                        break;
                }
            }
        });
        //管道两侧5米范围内是否有机械施工
        rg_isMachine.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesMachine:
                        break;
                    case R.id.rb_noMachine:
                        break;
                    case R.id.rb_machineNoRelative:
                        break;
                }
            }
        });
        //管道（光缆）有无移位、漂浮、裸露、防腐层破损等现象
        rg_isBad.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesBad:
                        break;
                    case R.id.rb_noBad:
                        break;
                    case R.id.rb_badNoRelative:
                        break;
                }
            }
        });
    }


    @OnClick({R.id.iv_back,
            R.id.ll_weather,
            R.id.ll_startTime,
            R.id.ll_endTime,
            R.id.ll_location,
            R.id.ll_scfj,
            R.id.ll_spr,
            R.id.ll_address,
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
            case R.id.ll_startTime:
            case R.id.ll_endTime:
                pvTime.show(view);
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
            case R.id.ll_location:
                Intent intent = new Intent(this, MapActivity.class);
                startActivityForResult(intent, SELECT_ADDRESS);
                break;
            case R.id.ll_spr:
                Intent intentApprover = new Intent(this, ApproverActivity.class);
                startActivityForResult(intentApprover, SELECT_APPROVER);
                break;
            case R.id.ll_scfj:
//                Intent intentAddress = new Intent(this, SelectFileActivity.class);
//                startActivityForResult(intentAddress, FILE_REQUEST_CODE);

//                Intent intentAddress = new Intent(Intent.ACTION_GET_CONTENT);
//                intentAddress.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
//                intentAddress.addCategory(Intent.CATEGORY_OPENABLE);
//                startActivityForResult(intentAddress,FILE_REQUEST_CODE);
                getPermission();

                break;
            case R.id.ll_address:
                Intent intentArea = new Intent(this, MapActivity.class);
                startActivityForResult(intentArea, SELECT_AREA);
                break;
        }
    }
    private void getPermission(){
        AndPermission
                .with(this)
                .permission(Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE)
                .onDenied(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        //拒绝权限
                        ToastUtil.show("请赋予必要权限");
                    }
                }).onGranted(new Action() {
            @Override
            public void onAction(List<String> permissions) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent,FILE_REQUEST_CODE);
            }
        }).start();
    }
    private boolean paramsComplete() {
        if (TextUtils.isEmpty(tvWeather.getText().toString())) {
            ToastUtil.show("请选择天气");
            return false;
        }
        if (TextUtils.isEmpty(tvStartTime.getText().toString())) {
            ToastUtil.show("请选择开始时间");
            return false;
        }
        if (TextUtils.isEmpty(tvEndTime.getText().toString())) {
            ToastUtil.show("请选择结束时间");
            return false;
        }
        if (TextUtils.isEmpty(tvAddress.getText().toString())) {
            ToastUtil.show("请输入当前位置");
            return false;
        }
        if (TextUtils.isEmpty(etProcess.getText().toString())) {
            ToastUtil.show("请输入工程进度");
            return false;
        }
        if (TextUtils.isEmpty(etOther.getText().toString())) {
            ToastUtil.show("请输入请他情况");
            return false;
        }
        if (TextUtils.isEmpty(tvLocation.getText().toString())) {
            ToastUtil.show("请输入坐标");
            return false;
        }
        if (TextUtils.isEmpty(tvSpr.getText().toString())) {
            ToastUtil.show("请选择审批人");
            return false;
        }
        return true;
    }

    private void initTimePicker() {//Dialog 模式下，在底部弹出
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (v.getId() == R.id.ll_startTime) {
                    tvStartTime.setText(getTime(date));
                } else if (v.getId() == R.id.ll_endTime) {
                    tvEndTime.setText(getTime(date));
                }
                Log.i("pvTime", "onTimeSelect");

            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
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
        Log.d("getTime()", "choice date millis: " + date.getTime());
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
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
            tvAddress.setText(area);
        }

    }
}
