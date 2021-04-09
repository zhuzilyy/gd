package com.gd.form.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class HikingCheckActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_pipeName)
    TextView tvPipeName;
    @BindView(R.id.tv_startStationNo)
    TextView tvStartStationNo;
    @BindView(R.id.tv_endStationNo)
    TextView tvEndStationNo;
    @BindView(R.id.tv_spr)
    TextView tvSpr;
    @BindView(R.id.tv_fileName)
    TextView tvFileName;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.et_allowPeople)
    TextView etAllowPeople;
    @BindView(R.id.rg_isPipeExpose)
    RadioGroup rg_isPipeExpose;
    @BindView(R.id.rg_isBuild)
    RadioGroup rg_isBuild;
    private List<Pipelineinfo> pipeLineinfoList;
    private List<Department> departmentList;
    private ListDialog dialog;
    private int FILE_REQUEST_CODE = 100;
    private int SELECT_STATION = 101;
    private int SELECT_APPROVER = 102;
    private int SELECT_ADDRESS = 103;
    private String approverName;
    private String approverId;

    @BindView(R.id.rg_isCar)
    RadioGroup rg_isCar;
    @BindView(R.id.rg_isNew)
    RadioGroup rg_isNew;
    @BindView(R.id.rg_isFull)
    RadioGroup rg_isFull;
    @BindView(R.id.rg_isCorrect)
    RadioGroup rg_isCorrect;
    @BindView(R.id.rg_isUseful)
    RadioGroup rg_isUseful;
    @BindView(R.id.rg_isProtect)
    RadioGroup rg_isProtect;
    @BindView(R.id.rg_isRelative)
    RadioGroup rg_isRelative;
    @BindView(R.id.rg_isIllegal)
    RadioGroup rg_isIllegal;
    @BindView(R.id.rg_isWeightCar)
    RadioGroup rg_isWeightCar;
    @BindView(R.id.rg_isTimely)
    RadioGroup rg_isTimely;
    @BindView(R.id.rg_isWearing)
    RadioGroup rg_isWearing;
    @BindView(R.id.rg_isWriting)
    RadioGroup rg_isWriting;
    @BindView(R.id.rg_isSafe)
    RadioGroup rg_isSafe;
    @BindView(R.id.et_advice)
    EditText etAdvice;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_hiking_check;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("徒步巡检表");
        dialog = new ListDialog(this);
        getPipelineInfoListRequest();
        pipeDepartmentInfoGetList();
        initListener();
    }

    private void initListener() {
        //管道是否发生露管
        rg_isPipeExpose.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesExpose:
                        break;
                    case R.id.rb_noExpose:
                        break;
                }
            }
        });
        //100米范围内，是否有机械施工行为
        rg_isBuild.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesBuild:
                        break;
                    case R.id.rb_noBuild:
                        break;
                }
            }
        });
        //管道沿线是否有可疑人员或车辆出现
        rg_isCar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesCar:
                        break;
                    case R.id.rb_noCar:
                        break;
                }
            }
        });//两侧是否有新近翻挖动土迹象
        rg_isNew.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesNew:
                        break;
                    case R.id.rb_noNew:
                        break;
                }
            }
        });
        //地面标识是否完好
        rg_isFull.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesFull:
                        break;
                    case R.id.rb_noFull:
                        break;
                }
            }
        });
        //地面标识位置是否准确
        rg_isCorrect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesCorrect:
                        break;
                    case R.id.rb_noCorrect:
                        break;
                }
            }
        });
        //附属设施是否完好可用
        rg_isUseful.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesUseful:
                        break;
                    case R.id.rb_noUseful:
                        break;
                }
            }
        });
        //水工保护工程是否完好
        rg_isProtect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesProtect:
                        break;
                    case R.id.rb_noProtect:
                        break;
                }
            }
        });
        //管道两侧是否发生新的相关工程
        rg_isRelative.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesRelative:
                        break;
                    case R.id.rb_noRelative:
                        break;
                }
            }
        });
        //管道上方是否有新增违建
        rg_isIllegal.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesIllegal:
                        break;
                    case R.id.rb_noIllegal:
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
                }
            }
        });
        //管道上方是否有重车碾压
        rg_isTimely.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesTimely:
                        break;
                    case R.id.rb_noTimely:
                        break;
                }
            }
        });
        //巡线工是否穿戴工服及巡检工具
        rg_isWearing.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesWearing:
                        break;
                    case R.id.rb_noWearing:
                        break;
                }
            }
        });
        //巡线工是否按规定填写巡线记录
        rg_isWriting.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesWriting:
                        break;
                    case R.id.rb_noWriting:
                        break;
                }
            }
        });
        //是否有危及管道安全其它情况
        rg_isSafe.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesSafe:
                        break;
                    case R.id.rb_noSafe:
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
                        pipeLineinfoList = list;
                    }
                });
    }

    @OnClick({R.id.iv_back,
            R.id.ll_pipeName,
            R.id.tv_startStationNo,
            R.id.tv_endStationNo,
            R.id.ll_location,
            R.id.ll_scfj,
            R.id.ll_spr,
            R.id.ll_area,
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
            case R.id.ll_location:
                Intent intent = new Intent(this, MapActivity.class);
                startActivityForResult(intent, SELECT_ADDRESS);
                break;
            case R.id.tv_startStationNo:
                Intent intentStartStation = new Intent(this, StationActivity.class);
                intentStartStation.putExtra("tag", "start");
                startActivityForResult(intentStartStation, SELECT_STATION);
                break;
            case R.id.tv_endStationNo:
                Intent intentEndStation = new Intent(this, StationActivity.class);
                intentEndStation.putExtra("tag", "end");
                startActivityForResult(intentEndStation, SELECT_STATION);
                break;
            case R.id.ll_spr:
                Intent intentApprover = new Intent(this, ApproverActivity.class);
                startActivityForResult(intentApprover, SELECT_APPROVER);
                break;
            case R.id.ll_pipeName:
                List<String> pipeNameList = new ArrayList<>();
                if (pipeLineinfoList != null && pipeLineinfoList.size() > 0) {
                    for (int i = 0; i < pipeLineinfoList.size(); i++) {
                        pipeNameList.add(pipeLineinfoList.get(i).getName());
                    }
                }
                dialog.setData(pipeNameList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvPipeName.setText(pipeNameList.get(positionM));
                    dialog.dismiss();
                });
                break;
            case R.id.ll_scfj:
                Intent intentAddress = new Intent(this, SelectFileActivity.class);
                startActivityForResult(intentAddress, FILE_REQUEST_CODE);
                break;


        }
    }

    private boolean paramsComplete() {
        if (TextUtils.isEmpty(tvPipeName.getText().toString())) {
            ToastUtil.show("请选择管道名称");
            return false;
        }
        if (TextUtils.isEmpty(tvStartStationNo.getText().toString())) {
            ToastUtil.show("请选择开始桩号");
            return false;
        }
        if (TextUtils.isEmpty(tvEndStationNo.getText().toString())) {
            ToastUtil.show("请选择结束桩号");
            return false;
        }
        if (TextUtils.isEmpty(etAllowPeople.getText().toString())) {
            ToastUtil.show("请输入陪巡人");
            return false;
        }
        if (TextUtils.isEmpty(tvArea.getText().toString())) {
            ToastUtil.show("请选择所辖作业区");
            return false;
        }
        if (TextUtils.isEmpty(etAdvice.getText().toString())) {
            ToastUtil.show("请输入改进意见");
            return false;
        }
        if (TextUtils.isEmpty(tvLocation.getText().toString())) {
            ToastUtil.show("请选择坐标");
            return false;
        }
        if (TextUtils.isEmpty(tvLocation.getText().toString())) {
            ToastUtil.show("请选择审批人");
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
        if (requestCode == FILE_REQUEST_CODE) {
            String name = data.getStringExtra("fileName");
            tvFileName.setText(name);
            //选择桩号
        } else if (requestCode == SELECT_STATION) {
            String selectTag = data.getStringExtra("selectTag");
            String stationName = data.getStringExtra("stationName");
            if (!TextUtils.isEmpty(selectTag)) {
                if (selectTag.equals("start")) {
                    tvStartStationNo.setText(stationName);
                } else {
                    tvEndStationNo.setText(stationName);
                }
            }

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
