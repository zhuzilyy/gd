package com.gd.form.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gd.form.R;
import com.gd.form.adapter.FileDetailAdapter;
import com.gd.form.adapter.OnItemClickListener;
import com.gd.form.base.BaseActivity;
import com.gd.form.constants.Constant;
import com.gd.form.model.FileDetailModel;
import com.gd.form.model.ServerModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.SPUtil;
import com.gd.form.utils.ToastUtil;
import com.gd.form.view.DeleteDialog;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class StandardFileListActivity extends BaseActivity {
    @BindView(R.id.rv_file)
    RecyclerView rvFile;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    private String token, userId;
    private List<FileDetailModel> standardFileModels;
    private FileDetailAdapter fileDetailAdapter;
    private DeleteDialog deleteDialog;
    private int deleteIndex;
    private String type;
    private MyReceiver myReceiver;
    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_standard_file_list;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("文件列表");
        tvRight.setText("新增");
        deleteDialog = new DeleteDialog(this);
        Intent intent = getIntent();
        if (intent != null) {
            type = intent.getExtras().getString("name");
            getData(type);
        }
        standardFileModels = new ArrayList<>();
        token = (String) SPUtil.get(StandardFileListActivity.this, "token", "");
        userId = (String) SPUtil.get(StandardFileListActivity.this, "userId", "");
        String departmentId = (String) SPUtil.get(StandardFileListActivity.this, "departmentId", "");
        if(!TextUtils.isEmpty(departmentId)){
            if(Integer.parseInt(departmentId) == Constant.PIPE_DEPARTMENT){
                tvRight.setVisibility(View.VISIBLE);
            }
        }
        initViews();
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.action.addFile.success");
        registerReceiver(myReceiver,intentFilter);
    }

    private void getData(String type) {
        JsonObject params = new JsonObject();
        params.addProperty("filetype", type);
        Net.create(Api.class).getStandardFilesList(token, params)
                .enqueue(new NetCallback<List<FileDetailModel>>(this, true) {
                    @Override
                    public void onResponse(List<FileDetailModel> result) {
                        if (result != null && result.size() > 0) {
                            standardFileModels.addAll(result);
                            fileDetailAdapter.notifyDataSetChanged();
                            rvFile.setVisibility(View.VISIBLE);
                            llNoData.setVisibility(View.GONE);
                        } else {
                            rvFile.setVisibility(View.GONE);
                            llNoData.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    private void initViews() {
        rvFile.setLayoutManager(new LinearLayoutManager(mContext));
        fileDetailAdapter = new FileDetailAdapter(mContext, standardFileModels, R.layout.adapter_item_file_list);
        rvFile.setAdapter(fileDetailAdapter);
        fileDetailAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                if (v.getId() == R.id.btn_check) {
                    String filePath = standardFileModels.get(position).getUploadfile();
                    if (!TextUtils.isEmpty(filePath)) {
                        Uri uri = Uri.parse(filePath);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    } else {
                        ToastUtil.show("查看失败");
                    }
                } else if (v.getId() == R.id.btn_delete) {
                    deleteIndex = position;
                    deleteDialog.show();
                }
            }
        });
        deleteDialog.setOnClickBottomListener(new DeleteDialog.OnClickBottomListener() {
            @Override
            public void onPositiveClick() {
                deleteDialog.dismiss();
                deleteFile();
            }

            @Override
            public void onNegativeClick() {
                deleteDialog.dismiss();
            }
        });
    }

    private void deleteFile() {
        JsonObject params = new JsonObject();
        params.addProperty("filetype", type);
        params.addProperty("id", standardFileModels.get(deleteIndex).getId() + "");
        params.addProperty("filename", standardFileModels.get(deleteIndex).getFilename());
        Net.create(Api.class).deleteFile(token, params)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel result) {
                        if (result.getCode() == Constant.SUCCESS_CODE) {
                            standardFileModels.remove(deleteIndex);
                            fileDetailAdapter.notifyDataSetChanged();
                            if (standardFileModels != null && standardFileModels.size() > 0) {
                                llNoData.setVisibility(View.GONE);
                                rvFile.setVisibility(View.VISIBLE);
                            } else {
                                llNoData.setVisibility(View.VISIBLE);
                                rvFile.setVisibility(View.GONE);
                            }
                        }
                    }
                });
    }
    class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("com.action.addFile.success")){
                standardFileModels.clear();
                getData(type);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(myReceiver!=null){
            unregisterReceiver(myReceiver);
        }
    }

    @OnClick({R.id.iv_back, R.id.tv_right})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                Bundle bundle = new Bundle();
                bundle.putString("type",type);
                openActivity(UploadStandardFileActivity.class,bundle);
                break;
        }
    }
}
