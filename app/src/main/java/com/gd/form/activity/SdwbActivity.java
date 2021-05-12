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
import android.widget.EditText;
import android.widget.FrameLayout;
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

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.gd.form.R;
import com.gd.form.adapter.PhotoAdapter;
import com.gd.form.base.BaseActivity;
import com.gd.form.constants.Constant;
import com.gd.form.model.Department;
import com.gd.form.model.GlideImageLoader;
import com.gd.form.model.Pipelineinfo;
import com.gd.form.model.Pipemploys;
import com.gd.form.model.SearchPipeInfoModel;
import com.gd.form.model.ServerModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.SPUtil;
import com.gd.form.utils.TimeUtil;
import com.gd.form.utils.ToastUtil;
import com.gd.form.utils.WeiboDialogUtils;
import com.gd.form.view.ListDialog;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;
import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;
import com.yancy.gallerypick.inter.IHandlerCallBack;

import java.io.Serializable;
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
    @BindView(R.id.et_pipeLength)
    EditText et_pipeLength;
    @BindView(R.id.rvResultPhoto)
    RecyclerView rvResultPhoto;
    private int SELECT_ADDRESS = 102;
    private int FILE_REQUEST_CODE = 100;
    private int SELECT_APPROVER = 101;
    private int SELECT_STATION = 103;
    private int SELECT_STATION_NAME = 104;
    private int PERMISSIONS_REQUEST_READ_CONTACTS = 8;
    private TimePickerView pvTime;
    private ListDialog dialog;
    private String approverName;
    private String approverId;
    private List<Department> departmentList;
    private List<Pipelineinfo> pipelineinfoList;
    private IHandlerCallBack iHandlerCallBack;
    private List<String> path;
    private GalleryConfig galleryConfig;
    private PhotoAdapter photoAdapter;
    private List<String> nameList;
    private Dialog mWeiboDialog;
    private OSSCredentialProvider ossCredentialProvider;
    private OSS oss;
    private String token, userId;
    private String ossFilePath;
    private String selectFileName;
    private String selectFilePath;
    private String stationId, pipeId, location;
    private int selectPipeId, departmentId;
    private String col1 = "无违章采石、采矿、爆破行为";
    private String col2 = "无第三方施工行为";
    private String col3 = "无可疑人员、车辆逗留现象";
    private String col4 = "完好";
    private String col5 = "无裂缝、沉降，四周有无滑坡、水土流失";
    private String col6 = "完好，无破损";
    private String col7 = "无管道本体翘起、下沉，无管沟沉降";
    private String col8 = "无异响、异味现象";
    private String col9 = "检测合格";
    private String col10 = "外观完好";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        token = (String) SPUtil.get(SdwbActivity.this, "token", "");
        userId = (String) SPUtil.get(SdwbActivity.this, "userId", "");
        ossCredentialProvider = new OSSPlainTextAKSKCredentialProvider(Constant.ACCESSKEYID, Constant.ACCESSKEYSECRET);
        oss = new OSSClient(mContext.getApplicationContext(), Constant.ENDPOINT, ossCredentialProvider);
        dialog = new ListDialog(mContext);
        path = new ArrayList<>();
        nameList = new ArrayList<>();
        initTimePicker();
        //获取管道单位
        pipeDepartmentInfoGetList();
        //获取隧道名称
        getPipelineInfoListRequest();
        //监听事件
        initListener();
        initGallery();
        initConfig();
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
                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(SdwbActivity.this, "加载中...");
                mWeiboDialog.getWindow().setDimAmount(0f);
                for (int i = 0; i < path.size(); i++) {
                    String suffix = path.get(i).substring(path.get(i).length() - 4);
                    uploadFiles("W002/"+userId + "_" + TimeUtil.getFileNameTime() + "_" + i + suffix, path.get(i));
                }

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

    private void initListener() {
        //违规行为
        rg_wgxw.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yes:
                        col1 = "无违章采石、采矿、爆破行为";
                        break;
                    case R.id.rb_no:
                        col1 = "有违章采石、采矿、爆破行为";
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
                        col2 = "无第三方施工行为";
                        break;
                    case R.id.rb_thirdNo:
                        col2 = "有第三方施工行为";
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
                        col3 = "无可疑人员、车辆逗留现象";
                        break;
                    case R.id.rb_suspectYes:
                        col3 = "有可疑人员、车辆逗留现象";
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
                        col4 = "完好";
                        break;
                    case R.id.rb_blockMiddle:
                        col4 = "发生局部破损";
                        break;
                    case R.id.rb_blockNo:
                        col4 = "发生大面积破损";
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
                        col5 = "无裂缝、沉降，四周有无滑坡、水土流失";
                        break;
                    case R.id.rb_caveMiddle:
                        col5 = "有裂缝、沉降情况";
                        break;
                    case R.id.rb_caveYes:
                        col5 = ".有滑坡、水土流失情况";
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
                        col6 = "完好，无破损";
                        break;
                    case R.id.rb_waterMiddle:
                        col6 = "局部损坏";
                        break;
                    case R.id.rb_waterNo:
                        col6 = "损坏严重，作用基本失效";
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
                        col7 = "无管道本体翘起、下沉，无管沟沉降";
                        break;
                    case R.id.rb_transitMiddle:
                        col7 = "有管道本体偏离原有位置";
                        break;
                    case R.id.rb_transitYes:
                        col7 = "有管沟沉降情况";
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
                        col8 = "无异响、异味现象";
                        break;
                    case R.id.rb_badYes:
                        col8 = "有异响、异味现象";
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
                        col9 = "检测合格";
                        break;
                    case R.id.rb_gasNo:
                        col9 = "检测不合格";
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
                        col10 = "外观完好";
                        break;
                    case R.id.rb_alarmNo:
                        col10 = "外观损坏";
                        break;

                }
            }
        });

    }

    private void pipeDepartmentInfoGetList() {
        Net.create(Api.class).pipedepartmentinfoGetList(token)
                .enqueue(new NetCallback<List<Department>>(this, false) {
                    @Override
                    public void onResponse(List<Department> list) {
                        departmentList = list;
                    }
                });
    }

    private void getPipelineInfoListRequest() {

        Net.create(Api.class).pipelineinfosget(token)
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
            R.id.btn_commit,
            R.id.rl_selectImage,
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_selectImage:
                initPermissions();
                break;
            case R.id.btn_commit:
                if (paramsComplete()) {
                    commit();
                }
                break;
            case R.id.ll_location:
                Intent intent = new Intent(SdwbActivity.this, MapActivity.class);
                startActivityForResult(intent, SELECT_ADDRESS);
                break;
            case R.id.ll_sdmc:
                getTunnelList();
//                List<String> pipeList = new ArrayList<>();
//                List<Integer> idList = new ArrayList<>();
//                if (pipelineinfoList != null && pipelineinfoList.size() > 0) {
//                    for (int i = 0; i < pipelineinfoList.size(); i++) {
//                        pipeList.add(pipelineinfoList.get(i).getName());
//                        idList.add(pipelineinfoList.get(i).getId());
//                    }
//                }
//                dialog.setData(pipeList);
//                dialog.show();
//                dialog.setListItemClick(positionM -> {
//                    tv_sdmc.setText(pipeList.get(positionM));
//                    selectPipeId = idList.get(positionM);
//                    dialog.dismiss();
//                });
                break;
            case R.id.ll_sdwz:
                Intent intentStartStation = new Intent(this, StationActivity.class);
                startActivityForResult(intentStartStation, SELECT_STATION);
                break;
            case R.id.ll_gddz:
                List<String> areaList = new ArrayList<>();
                List<Integer> departmentIdList = new ArrayList<>();
                if (departmentList != null && departmentList.size() > 0) {
                    for (int i = 0; i < departmentList.size(); i++) {
                        areaList.add(departmentList.get(i).getName());
                        departmentIdList.add(departmentList.get(i).getId());
                    }
                }
                dialog.setData(areaList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tv_gddw.setText(areaList.get(positionM));
                    departmentId = departmentIdList.get(positionM);
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

    private void getTunnelList() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("empid", userId);
        Net.create(Api.class).getAllTunnelList(token, jsonObject)
                .enqueue(new NetCallback<List<SearchPipeInfoModel>>(this, true) {
                    @Override
                    public void onResponse(List<SearchPipeInfoModel> list) {
                        List<SearchPipeInfoModel> pipeInfoModelList = new ArrayList<>();
                         if(list!=null && list.size()>0){
                             for (int i = 0; i <list.size() ; i++) {
                                 SearchPipeInfoModel searchPipeInfoModel = list.get(i);
                                 searchPipeInfoModel.setType("select");
                                 pipeInfoModelList.add(searchPipeInfoModel);
                             }
                             Bundle bundle = new Bundle();
                             bundle.putSerializable("tunnels", (Serializable) pipeInfoModelList);
                             Intent intent = new Intent(SdwbActivity.this,TunnelListActivity.class);
                             intent.putExtras(bundle);
                             startActivityForResult(intent,SELECT_STATION_NAME);
                         }

                    }
                });
    }

    //提交数据
    private void commit() {
        StringBuilder photoSb = new StringBuilder();
        if (nameList.size() > 0) {
            for (int i = 0; i < nameList.size(); i++) {
                if (i != nameList.size() - 1) {
                    photoSb.append(nameList.get(i) + ";");
                } else {
                    photoSb.append(nameList.get(i));
                }
            }
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("pipeid", stationId);
        jsonObject.addProperty("departmentid", departmentId);
        jsonObject.addProperty("stakeid", Integer.valueOf(stationId));
        jsonObject.addProperty("pipelength", et_pipeLength.getText().toString());
        jsonObject.addProperty("col1", col1);
        jsonObject.addProperty("col1desc", et_wgxw_problem.getText().toString());
        jsonObject.addProperty("col2", col2);
        jsonObject.addProperty("col2desc", et_dsfsg_problem.getText().toString());
        jsonObject.addProperty("col3", col3);
        jsonObject.addProperty("col3desc", et_kyxx_problem.getText().toString());
        jsonObject.addProperty("col4", col4);
        jsonObject.addProperty("col4desc", et_zqfd_problem.getText().toString());
        jsonObject.addProperty("col5", col5);
        jsonObject.addProperty("col5desc", et_dk_problem.getText().toString());
        jsonObject.addProperty("col6", col6);
        jsonObject.addProperty("col6desc", et_sgss_problem.getText().toString());
        jsonObject.addProperty("col7", col7);
        jsonObject.addProperty("col7desc", et_gdrdgdd_problem.getText().toString());
        jsonObject.addProperty("col8", col8);
        jsonObject.addProperty("col8desc", et_yxyw_problem.getText().toString());
        jsonObject.addProperty("col9", col9);
        jsonObject.addProperty("col9desc", et_krq_problem.getText().toString());
        jsonObject.addProperty("col10", col10);
        jsonObject.addProperty("col10desc", et_bjxt_problem.getText().toString());
        jsonObject.addProperty("col11", et_other_problem.getText().toString());
        jsonObject.addProperty("locate", location);
        jsonObject.addProperty("creator", userId);
        jsonObject.addProperty("creatime", TimeUtil.getCurrentTime());
        jsonObject.addProperty("approvalid", approverId);
        if (!TextUtils.isEmpty(photoSb.toString())) {
            jsonObject.addProperty("picturepath", photoSb.toString());
        } else {
            jsonObject.addProperty("picturepath", "00");
        }
        if (!TextUtils.isEmpty(ossFilePath)) {
            jsonObject.addProperty("filepath", ossFilePath);
        } else {
            jsonObject.addProperty("filepath", "00");
        }
        Log.i("tag","jsonObject=="+jsonObject);
        Net.create(Api.class).commitTunnel(token, jsonObject)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel result) {
                        ToastUtil.show(result.getMsg());
                        if (result.getCode() == Constant.SUCCESS_CODE) {
                            Intent intent = new Intent();
                            intent.setAction("com.action.update.waitingTask");
                            sendBroadcast(intent);
                            finish();
                        }

                    }
                });
    }

    private boolean paramsComplete() {
//        if (TextUtils.isEmpty(tv_gddw.getText().toString())) {
//            ToastUtil.show("请选择管道单位");
//            return false;
//        }
        if (TextUtils.isEmpty(tv_sdmc.getText().toString())) {
            ToastUtil.show("请选择隧道名称");
            return false;
        }
//        if (TextUtils.isEmpty(tv_sdwz.getText().toString())) {
//            ToastUtil.show("请选择隧道位置");
//            return false;
//        }
//        if (TextUtils.isEmpty(et_pipeLength.getText().toString())) {
//            ToastUtil.show("请输入管道长度");
//            return false;
//        }
//        if (!NumberUtil.isNumber(et_pipeLength.getText().toString())) {
//            ToastUtil.show("管道长度输入格式不正确");
//            return false;
//        }
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
        if (TextUtils.isEmpty(et_other_problem.getText().toString())) {
            ToastUtil.show("请输入其他情况");
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
            selectFileName = data.getStringExtra("fileName");
            selectFilePath = data.getStringExtra("selectFilePath");
            tv_fileName.setText(selectFileName);
            mWeiboDialog = WeiboDialogUtils.createLoadingDialog(this, "加载中...");
            mWeiboDialog.getWindow().setDimAmount(0f);
            uploadOffice("W002/"+userId + "_" + TimeUtil.getFileNameTime() + "_" + selectFileName, selectFilePath);
            //选择桩号
        } else if (requestCode == SELECT_ADDRESS) {
            String latitude = data.getStringExtra("latitude");
            String longitude = data.getStringExtra("longitude");
            location = longitude + "," + latitude;
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
            stationId = data.getStringExtra("stationId");
            pipeId = data.getStringExtra("pipeId");
            String stationName = data.getStringExtra("stationName");
            tv_sdwz.setText(stationName);
        }else if(requestCode == SELECT_STATION_NAME){
//            intent.putExtra("stationName",searchPipeInfoModel.getStakename());
//            intent.putExtra("stationId",searchPipeInfoModel.getId()+"");
            String stationName = data.getStringExtra("stationName");
            stationId = data.getStringExtra("stationId");
            tv_sdmc.setText(stationName);
            getDefaultManager();
        }

    }
    private void getDefaultManager() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("empid",userId);
        Net.create(Api.class).getTunnelDefaultManager(token, jsonObject)
                .enqueue(new NetCallback<Pipemploys>(this, true) {
                    @Override
                    public void onResponse(Pipemploys pipemploys) {
                        approverId = pipemploys.getId();
                        tv_spr.setText(pipemploys.getName());
                    }
                });
    }

    // 授权管理
    private void initPermissions() {
        if (ContextCompat.checkSelfPermission(SdwbActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(SdwbActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(mContext, "请在 设置-应用管理 中开启此应用的储存授权。", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(SdwbActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {
            GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(SdwbActivity.this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(SdwbActivity.this);
            } else {
                Log.i("tag", "拒绝授权");
            }
        }
    }

    //上传阿里云文件
    public void uploadFiles(String fileName, String filePath) {
        PutObjectRequest put = new PutObjectRequest(Constant.BUCKETSTRING, fileName, filePath);
        // 此处调用异步上传方法
        OSSAsyncTask ossAsyncTask = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                nameList.add(fileName);
                if (nameList.size() == path.size()) {
                    WeiboDialogUtils.closeDialog(mWeiboDialog);
                }
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientException, ServiceException serviceException) {
                ToastUtil.show("上传失败请重试");
                // 请求异常。
                if (clientException != null) {
                    // 本地异常，如网络异常等。
                }
                if (serviceException != null) {


                }
            }
        });

    }

    public void uploadOffice(String fileName, String filePath) {
        PutObjectRequest put = new PutObjectRequest(Constant.BUCKETSTRING, fileName, filePath);
        // 此处调用异步上传方法
        OSSAsyncTask ossAsyncTask = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                ossFilePath = fileName;
                WeiboDialogUtils.closeDialog(mWeiboDialog);
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientException, ServiceException serviceException) {
                ToastUtil.show("上传失败请重试");
                // 请求异常。
                if (clientException != null) {
                    // 本地异常，如网络异常等。
                }
                if (serviceException != null) {


                }
            }
        });
    }

}
