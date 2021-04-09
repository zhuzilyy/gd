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
import com.gd.form.model.Department;
import com.gd.form.model.Pipelineinfo;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.ToastUtil;
import com.gd.form.view.ListDialog;
import com.jaeger.library.StatusBarUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SdwbActivity extends BaseActivity {
    @BindView(R.id.tv_location)
    TextView tv_location;
    @BindView(R.id.tv_sdmc)
    TextView tv_sdmc;
    @BindView(R.id.tv_sdwz)
    TextView tv_sdwz;
    @BindView(R.id.tv_tbrq)
    TextView tv_tbrq;
    @BindView(R.id.tv_gddw)
    TextView tv_gddw;
    @BindView(R.id.tv_fileName)
    TextView tv_fileName;
    @BindView(R.id.tv_spr)
    TextView tv_spr;
    @BindView(R.id.rg_wgxw)
    RadioGroup rg_wgxw;
    @BindView(R.id.rg_sgss)
    RadioGroup rg_sgss;
    @BindView(R.id.rg_dsfsg)
    RadioGroup rg_dsfsg;
    @BindView(R.id.rg_kyxx)
    RadioGroup rg_kyxx;
    @BindView(R.id.rg_zqfd)
    RadioGroup rg_zqfd;
    @BindView(R.id.rg_dk)
    RadioGroup rg_dk;
    @BindView(R.id.rg_gdrdgdd)
    RadioGroup rg_gdrdgdd;
    @BindView(R.id.rg_yxyw)
    RadioGroup rg_yxyw;
    @BindView(R.id.rg_krq)
    RadioGroup rg_krq;
    @BindView(R.id.rg_bjxt)
    RadioGroup rg_bjxt;
    @BindView(R.id.et_wgxw_problem)
    EditText et_wgxw_problem;
    @BindView(R.id.et_dsfsg_problem)
    EditText et_dsfsg_problem;
    @BindView(R.id.et_kyxx_problem)
    EditText et_kyxx_problem;
    @BindView(R.id.et_zqfd_problem)
    EditText et_zqfd_problem;
    @BindView(R.id.et_dk_problem)
    EditText et_dk_problem;
    @BindView(R.id.et_sgss_problem)
    EditText et_sgss_problem;
    @BindView(R.id.et_gdrdgdd_problem)
    EditText et_gdrdgdd_problem;
    @BindView(R.id.et_yxyw_problem)
    EditText et_yxyw_problem;
    @BindView(R.id.et_krq_problem)
    EditText et_krq_problem;
    @BindView(R.id.et_bjxt_problem)
    EditText et_bjxt_problem;
    @BindView(R.id.et_other_problem)
    EditText et_other_problem;
    private int SELECT_ADDRESS = 102;
    private int FILE_REQUEST_CODE = 100;
    private int SELECT_APPROVER = 101;
    private int SELECT_STATION = 103;
    private TimePickerView pvTime;
    private ListDialog dialog;
    private String approverName;
    private String approverId;
    private List<Department> departmentList;
    private List<Pipelineinfo> pipelineinfoList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new ListDialog(mContext);
        initTimePicker();
        //获取管道单位
        pipeDepartmentInfoGetList();
        //获取隧道名称
        getPipelineInfoListRequest();
        //监听事件
        initListener();
    }

    private void initListener() {
        //违规行为
        rg_wgxw.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yes:
                        break;
                    case R.id.rb_no:
                        break;
                }
            }
        });
        //第三方施工
        rg_dsfsg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_thirdYes:
                        break;
                    case R.id.rb_thirdNo:
                        break;
                }
            }
        });
        //可疑现象
        rg_kyxx.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_suspectNo:
                        break;
                    case R.id.rb_suspectYes:
                        break;
                }
            }
        });
        //砖砌封堵
        rg_zqfd.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_blockYes:
                        break;
                    case R.id.rb_blockMiddle:
                        break;
                    case R.id.rb_blockNo:
                        break;
                }
            }
        });
        //洞口
        rg_dk.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_caveNo:
                        break;
                    case R.id.rb_caveMiddle:
                        break;
                    case R.id.rb_caveYes:
                        break;
                }
            }
        });
        //水工设施
        rg_sgss.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_waterYes:
                        break;
                    case R.id.rb_waterMiddle:
                        break;
                    case R.id.rb_waterNo:
                        break;
                }
            }
        });
        //管道入地过渡段
        rg_gdrdgdd.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_transitNo:
                        break;
                    case R.id.rb_transitMiddle:
                        break;
                    case R.id.rb_transitYes:
                        break;
                }
            }
        });
        //异响异味
        rg_yxyw.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_badNo:
                        break;
                    case R.id.rb_badYes:
                        break;

                }
            }
        });
        //可燃气体检测
        rg_krq.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_gasYes:
                        break;
                    case R.id.rb_gasNo:
                        break;

                }
            }
        });
        //报警系统
        rg_bjxt.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_alarmYes:
                        break;
                    case R.id.rb_alarmNo:
                        break;

                }
            }
        });

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
                        pipelineinfoList = list;
                    }
                });
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_sdwb;
    }

    @OnClick({
            R.id.ll_location,
            R.id.ll_sdmc,
            R.id.ll_sdwz,
            R.id.ll_gddz,
            R.id.ll_scfj,
            R.id.ll_tbrq,
            R.id.iv_back,
            R.id.ll_spr,
            R.id.btn_commit,
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_commit:
                paramsComplete();
                break;
            case R.id.ll_location:
                Intent intent = new Intent(SdwbActivity.this, MapActivity.class);
                startActivityForResult(intent, SELECT_ADDRESS);
                break;
            case R.id.ll_sdmc:
                List<String> pipeList = new ArrayList<>();
                if (pipelineinfoList != null && pipelineinfoList.size() > 0) {
                    for (int i = 0; i < pipelineinfoList.size(); i++) {
                        pipeList.add(pipelineinfoList.get(i).getName());
                    }
                }
                dialog.setData(pipeList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tv_sdmc.setText(pipeList.get(positionM));
                    dialog.dismiss();
                });
                break;
            case R.id.ll_sdwz:
                Intent intentStartStation = new Intent(this, StationActivity.class);
                startActivityForResult(intentStartStation, SELECT_STATION);
                break;
            case R.id.ll_gddz:
                List<String> areaList = new ArrayList<>();
                if (departmentList != null && departmentList.size() > 0) {
                    for (int i = 0; i < departmentList.size(); i++) {
                        areaList.add(departmentList.get(i).getName());
                    }
                }
                dialog.setData(areaList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tv_gddw.setText(areaList.get(positionM));
                    dialog.dismiss();
                });
                break;
            case R.id.ll_tbrq:
                pvTime.show(view);
                break;
            case R.id.ll_scfj:
                Intent intentAddress = new Intent(SdwbActivity.this, SelectFileActivity.class);
                startActivityForResult(intentAddress, FILE_REQUEST_CODE);
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("application/*");//设置类型
//                intent.addCategory(Intent.CATEGORY_OPENABLE);
//                startActivityForResult(intent, 1);
                break;
            case R.id.ll_spr:
                Intent intentApprover = new Intent(this, ApproverActivity.class);
                startActivityForResult(intentApprover, SELECT_APPROVER);
                break;
        }
    }

    private boolean paramsComplete() {
        if (TextUtils.isEmpty(tv_gddw.getText().toString())) {
            ToastUtil.show("请选择管道单位");
            return false;
        }
        if (TextUtils.isEmpty(tv_sdmc.getText().toString())) {
            ToastUtil.show("请选择隧道名称");
            return false;
        }
        if (TextUtils.isEmpty(tv_sdwz.getText().toString())) {
            ToastUtil.show("请选择隧道位置");
            return false;
        }
        if (TextUtils.isEmpty(et_wgxw_problem.getText().toString())) {
            ToastUtil.show("请输入违规行为问题描述");
            return false;
        }
        if (TextUtils.isEmpty(et_dsfsg_problem.getText().toString())) {
            ToastUtil.show("请输入第三方施工行为问题描述");
            return false;
        }
        if (TextUtils.isEmpty(et_kyxx_problem.getText().toString())) {
            ToastUtil.show("请输入可疑行为问题描述");
            return false;
        }
        if (TextUtils.isEmpty(et_zqfd_problem.getText().toString())) {
            ToastUtil.show("请输入砖砌封堵问题描述");
            return false;
        }
        if (TextUtils.isEmpty(et_dk_problem.getText().toString())) {
            ToastUtil.show("请输入洞口问题描述");
            return false;
        }
        if (TextUtils.isEmpty(et_sgss_problem.getText().toString())) {
            ToastUtil.show("请输入水工设施问题描述");
            return false;
        }
        if (TextUtils.isEmpty(et_gdrdgdd_problem.getText().toString())) {
            ToastUtil.show("请输入管道入地过渡段问题描述");
            return false;
        }
        if (TextUtils.isEmpty(et_yxyw_problem.getText().toString())) {
            ToastUtil.show("请输入异响异味问题描述");
            return false;
        }
        if (TextUtils.isEmpty(et_krq_problem.getText().toString())) {
            ToastUtil.show("请输入可燃气体检测问题描述");
            return false;
        }
        if (TextUtils.isEmpty(et_bjxt_problem.getText().toString())) {
            ToastUtil.show("请输入报警系统问题描述");
            return false;
        }
        if (TextUtils.isEmpty(et_bjxt_problem.getText().toString())) {
            ToastUtil.show("请输入处理情况");
            return false;
        }
        if (TextUtils.isEmpty(tv_location.getText().toString())) {
            ToastUtil.show("请选择坐标");
            return false;
        }
        if (TextUtils.isEmpty(tv_spr.getText().toString())) {
            ToastUtil.show("请选择审批人");
            return false;
        }
        return true;
    }


    private void initTimePicker() {//Dialog 模式下，在底部弹出
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (v.getId() == R.id.iv_jcsj) {
                    //  tv_jcsj.setText(getTime(date));
                } else if (v.getId() == R.id.ll_tbrq) {
                    tv_tbrq.setText(getTime(date));
                }

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
            tv_fileName.setText(name);
            //选择桩号
        } else if (requestCode == SELECT_ADDRESS) {
            String latitude = data.getStringExtra("latitude");
            String longitude = data.getStringExtra("longitude");
            if (!TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude)) {
                tv_location.setText("经度:" + longitude + "   纬度:" + latitude);
            }
        } else if (requestCode == SELECT_APPROVER) {
            approverName = data.getStringExtra("name");
            approverId = data.getStringExtra("id");
            if (!TextUtils.isEmpty(approverName)) {
                tv_spr.setText(approverName);
            }
        } else if (requestCode == SELECT_STATION) {
            String stationName = data.getStringExtra("stationName");
            tv_sdwz.setText(stationName);
        }

    }

}
