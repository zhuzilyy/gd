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
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
import com.gd.form.model.DepartmentPerson;
import com.gd.form.model.GlideImageLoader;
import com.gd.form.model.Pipelineinfo;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class VideoMonitoringActivity extends BaseActivity {
    private TimePickerView pvTime;
    private List<Pipelineinfo> pipeLineinfoList;
    private ListDialog dialog;
    private int FILE_REQUEST_CODE = 100;
    private int SELECT_APPROVER = 102;
    private int SELECT_ADDRESS = 103;
    private int PERMISSIONS_REQUEST_READ_CONTACTS = 8;
    private String approverName;
    private String approverId;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_pipeName)
    TextView tvPipeName;
    @BindView(R.id.tv_spr)
    TextView tvSpr;
    @BindView(R.id.tv_fileName)
    TextView tvFileName;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.et_no)
    EditText etNo;
    @BindView(R.id.et_des)
    EditText etDes;
    @BindView(R.id.et_process)
    EditText etProcess;
    @BindView(R.id.et_person)
    EditText etPerson;
    @BindView(R.id.et_result)
    EditText etResult;
    @BindView(R.id.rvResultPhoto)
    RecyclerView rvResultPhoto;
    @BindView(R.id.ll_location)
    LinearLayout llLocation;
    @BindView(R.id.ll_scfj)
    LinearLayout llSelectFile;

    @BindView(R.id.rg_isThird)
    RadioGroup rgIsThird;
    @BindView(R.id.rb_yes)
    RadioButton rbYes;
    @BindView(R.id.rb_no)
    RadioButton rbNo;

    @BindView(R.id.rg_isMachine)
    RadioGroup rgIsMachine;
    @BindView(R.id.rb_yesMachine)
    RadioButton rbYesMachine;
    @BindView(R.id.rb_noMachine)
    RadioButton rbNoMachine;

    @BindView(R.id.rg_isComplete)
    RadioGroup rgIsComplete;
    @BindView(R.id.rb_yesComplete)
    RadioButton rbYesComplete;
    @BindView(R.id.rb_noComplete)
    RadioButton rbNoComplete;

    @BindView(R.id.rg_isCover)
    RadioGroup rgIsCover;
    @BindView(R.id.rb_yesCover)
    RadioButton rbYesCover;
    @BindView(R.id.rb_noCover)
    RadioButton rbNoCover;

    @BindView(R.id.rg_isBuilding)
    RadioGroup rgIsBuilding;
    @BindView(R.id.rb_yesBuild)
    RadioButton rbYesBuild;
    @BindView(R.id.rb_noBuild)
    RadioButton rbNoBuild;

    @BindView(R.id.rg_isClear)
    RadioGroup rgIsClear;
    @BindView(R.id.rb_yesClear)
    RadioButton rbYesClear;
    @BindView(R.id.rb_noClear)
    RadioButton rbNoClear;

    @BindView(R.id.rg_isNormal)
    RadioGroup rgIsNormal;
    @BindView(R.id.rb_yesNormal)
    RadioButton rbYesNormal;
    @BindView(R.id.rb_noNormal)
    RadioButton rbNoNormal;

    @BindView(R.id.rg_isOther)
    RadioGroup rgIsOther;
    @BindView(R.id.rb_yesOther)
    RadioButton rbYesOther;
    @BindView(R.id.rb_noOther)
    RadioButton rbNoOther;


    private Dialog mWeiboDialog;
    private OSSCredentialProvider ossCredentialProvider;
    private OSS oss;
    private String token, userId;
    private String ossFilePath;
    private String selectFileName;
    private String selectFilePath;
    private String location;
    private IHandlerCallBack iHandlerCallBack;
    private List<String> path;
    private GalleryConfig galleryConfig;
    private PhotoAdapter photoAdapter;
    private List<String> nameList;
    private int pipeId;
    private String col1 = "???", col2 = "???", col3 = "???", col4 = "???", col5 = "???", col6 = "???", col7 = "???", col8 = "???";

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_video_monitoring;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("????????????????????????");
        dialog = new ListDialog(this);
        llLocation.setVisibility(View.GONE);
        llSelectFile.setVisibility(View.GONE);
        path = new ArrayList<>();
        nameList = new ArrayList<>();
        dialog = new ListDialog(this);
        token = (String) SPUtil.get(this, "token", "");
        userId = (String) SPUtil.get(this, "userId", "");
        ossCredentialProvider = new OSSPlainTextAKSKCredentialProvider(Constant.ACCESSKEYID, Constant.ACCESSKEYSECRET);
        oss = new OSSClient(mContext.getApplicationContext(), Constant.ENDPOINT, ossCredentialProvider);
        initGallery();
        initConfig();
        initTimePicker();
        getPipelineInfoListRequest();
        iniListener();
    }

    private void getDefaultManager() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("empid", userId);
        Net.create(Api.class).getTunnelDefaultManager(token, jsonObject)
                .enqueue(new NetCallback<List<DepartmentPerson>>(this, true) {
                    @Override
                    public void onResponse(List<DepartmentPerson> list) {
                        List<String> nameList = new ArrayList<>();
                        List<String> idList = new ArrayList<>();
                        if(list!=null && list.size()>0){
                            for (int i = 0; i <list.size() ; i++) {
                                DepartmentPerson departmentPerson = list.get(i);
                                nameList.add(departmentPerson.getName());
                                idList.add(departmentPerson.getId());
                            }
                            if (dialog == null) {
                                dialog = new ListDialog(mContext);
                            }
                            dialog.setData(nameList);
                            dialog.show();
                            dialog.setListItemClick(positionM -> {
                                tvSpr.setText(nameList.get(positionM));
                                approverId = idList.get(positionM);
                                dialog.dismiss();
                            });
                        }
                    }
                });
    }

    private void iniListener() {
        rgIsThird.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yes:
                        col1 = "???";
                        break;
                    case R.id.rb_no:
                        col1 = "???";
                        break;
                }
            }
        });


        rgIsMachine.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesMachine:
                        col2 = "???";
                        break;
                    case R.id.rb_noMachine:
                        col2 = "???";
                        break;
                }
            }
        });

        rgIsComplete.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesComplete:
                        col3 = "???";
                        break;
                    case R.id.rb_noComplete:
                        col3 = "???";
                        break;
                }
            }
        });

        rgIsCover.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesCover:
                        col4 = "???";
                        break;
                    case R.id.rb_noCover:
                        col4 = "???";
                        break;
                }
            }
        });

        rgIsBuilding.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesBuild:
                        col5 = "???";
                        break;
                    case R.id.rb_noBuild:
                        col5 = "???";
                        break;
                }
            }
        });

        rgIsClear.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesClear:
                        col6 = "???";
                        break;
                    case R.id.rb_noClear:
                        col6 = "???";
                        break;
                }
            }
        });

        rgIsNormal.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesNormal:
                        col7 = "???";
                        break;
                    case R.id.rb_noNormal:
                        col7 = "???";
                        break;
                }
            }
        });

        rgIsOther.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesOther:
                        col8 = "???";
                        break;
                    case R.id.rb_noOther:
                        col8 = "???";
                        break;
                }
            }
        });
    }

    private void initConfig() {
        galleryConfig = new GalleryConfig.Builder()
                .imageLoader(new GlideImageLoader())    // ImageLoader ????????????????????????
                .iHandlerCallBack(iHandlerCallBack)     // ????????????????????????
                .provider("com.gd.form.fileprovider")   // provider(??????)
                .pathList(path)                         // ?????????????????????
                .multiSelect(true)                      // ????????????   ?????????false
                .multiSelect(true, 9)                   // ??????????????????????????? ??????????????????   ?????????false ??? 9
                .maxSize(9)                             // ??????????????? ??????????????????    ?????????9
                .crop(false)                             // ??????????????????????????????????????? ??????????????????????????????
                .crop(false, 1, 1, 500, 500)             // ??????????????????????????????   ?????????????????? 1:1
                .isShowCamera(true)                     // ????????????????????????  ?????????false
                .filePath("/Gallery/Pictures")// ??????????????????
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
                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(VideoMonitoringActivity.this, "?????????...");
                mWeiboDialog.getWindow().setDimAmount(0f);
                for (int i = 0; i < path.size(); i++) {
                    String suffix = path.get(i).substring(path.get(i).length() - 4);
                    uploadFiles("W010/" + userId + "_" + TimeUtil.getFileNameTime() + "_" + i + suffix, path.get(i));
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

    private void getPipelineInfoListRequest() {
        Net.create(Api.class).pipelineinfosget(token)
                .enqueue(new NetCallback<List<Pipelineinfo>>(this, false) {
                    @Override
                    public void onResponse(List<Pipelineinfo> list) {
                        pipeLineinfoList = list;
                    }
                });
    }

    private void initTimePicker() {//Dialog ???????????????????????????
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                tvTime.setText(getTime(date));

            }
        }).setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
            @Override
            public void onTimeSelectChanged(Date date) {
                Log.i("pvTime", "onTimeSelectChanged");
            }
        })
                .setType(new boolean[]{false, false, false, true, true, true})
                .isDialog(true) //????????????false ??????????????????DecorView ????????????????????????
                .addOnCancelClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("pvTime", "onCancelClickListener");
                    }
                })
                .setItemVisibleCount(5) //?????????????????????????????????1???????????????6???????????????????????????7???
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
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//??????????????????
                dialogWindow.setGravity(Gravity.BOTTOM);//??????Bottom,????????????
                dialogWindow.setDimAmount(0.3f);
            }
        }
    }

    private String getTime(Date date) {//???????????????????????????????????????
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    @OnClick({R.id.iv_back,
            R.id.ll_time,
            R.id.ll_pipeName,
            R.id.ll_location,
            R.id.ll_scfj,
            R.id.ll_spr,
            R.id.btn_commit,
            R.id.ll_selectPic,
    })
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_selectPic:
                initPermissions();
                break;
            case R.id.btn_commit:
                if (paramsComplete()) {
                    commit();
                }
                break;
            case R.id.ll_scfj:
                Intent intentAddress = new Intent(this, SelectFileActivity.class);
                startActivityForResult(intentAddress, FILE_REQUEST_CODE);
                break;
            case R.id.ll_location:
                Intent intent = new Intent(this, MapActivity.class);
                startActivityForResult(intent, SELECT_ADDRESS);
                break;
            case R.id.ll_spr:
                getDefaultManager();
                break;
            case R.id.ll_pipeName:
                List<String> pipeNameList = new ArrayList<>();
                List<Integer> idList = new ArrayList<>();
                if (pipeLineinfoList != null && pipeLineinfoList.size() > 0) {
                    for (int i = 0; i < pipeLineinfoList.size(); i++) {
                        pipeNameList.add(pipeLineinfoList.get(i).getName());
                        idList.add(pipeLineinfoList.get(i).getId());
                    }
                }
                dialog.setData(pipeNameList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvPipeName.setText(pipeNameList.get(positionM));
                    pipeId = idList.get(positionM);
                    dialog.dismiss();
                });
                break;
            case R.id.ll_time:
                pvTime.show();
                break;
        }
    }

    private boolean paramsComplete() {
        if (TextUtils.isEmpty(etDes.getText().toString())) {
            ToastUtil.show("?????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(etProcess.getText().toString())) {
            ToastUtil.show("?????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(tvSpr.getText().toString())) {
            ToastUtil.show("??????????????????");
            return false;
        }
        return true;
    }

    // ????????????
    private void initPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(mContext, "?????? ??????-???????????? ????????????????????????????????????", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {
            GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(this);
            } else {
                Log.i("tag", "????????????");
            }
        }
    }

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
        jsonObject.addProperty("exceptiondesc", etDes.getText().toString());
        jsonObject.addProperty("dealprocess", etProcess.getText().toString());
        jsonObject.addProperty("col1", col1);
        jsonObject.addProperty("col2", col2);
        jsonObject.addProperty("col3", col3);
        jsonObject.addProperty("col4", col4);
        jsonObject.addProperty("col5", col5);
        jsonObject.addProperty("col6", col6);
        jsonObject.addProperty("col7", col7);
        jsonObject.addProperty("col8", col8);
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
        Net.create(Api.class).commitVideo(token, jsonObject)
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == FILE_REQUEST_CODE) {
            selectFileName = data.getStringExtra("fileName");
            selectFilePath = data.getStringExtra("selectFilePath");
            tvFileName.setText(selectFileName);
            mWeiboDialog = WeiboDialogUtils.createLoadingDialog(this, "?????????...");
            mWeiboDialog.getWindow().setDimAmount(0f);
            uploadOffice("W010/" + userId + "_" + TimeUtil.getFileNameTime() + "_" + selectFileName, selectFilePath);
            //????????????
        } else if (requestCode == SELECT_ADDRESS) {
            String latitude = data.getStringExtra("latitude");
            String longitude = data.getStringExtra("longitude");
            location = longitude + "," + latitude;
            if (!TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude)) {
                tvLocation.setText("??????:" + longitude + "   ??????:" + latitude);
            }
        } else if (requestCode == SELECT_APPROVER) {
            approverName = data.getStringExtra("name");
            approverId = data.getStringExtra("id");
            if (!TextUtils.isEmpty(approverName)) {
                tvSpr.setText(approverName);
            }
        }

    }

    //?????????????????????
    public void uploadFiles(String fileName, String filePath) {
        PutObjectRequest put = new PutObjectRequest(Constant.BUCKETSTRING, fileName, filePath);
        // ??????????????????????????????
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
                ToastUtil.show("?????????????????????");
                // ???????????????
                if (clientException != null) {
                    // ????????????????????????????????????
                }
                if (serviceException != null) {


                }
            }
        });

    }

    public void uploadOffice(String fileName, String filePath) {
        PutObjectRequest put = new PutObjectRequest(Constant.BUCKETSTRING, fileName, filePath);
        // ??????????????????????????????
        OSSAsyncTask ossAsyncTask = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                ossFilePath = fileName;
                WeiboDialogUtils.closeDialog(mWeiboDialog);
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientException, ServiceException serviceException) {
                ToastUtil.show("?????????????????????");
                // ???????????????
                if (clientException != null) {
                    // ????????????????????????????????????
                }
                if (serviceException != null) {


                }
            }
        });
    }
}
