package com.gd.form.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gd.form.R;
import com.gd.form.adapter.PhotoAdapter;
import com.gd.form.base.BaseActivity;
import com.gd.form.model.EventHistoryDetailModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.SPUtil;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class EventHistoryDetailActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_pic)
    TextView tvPic;
    @BindView(R.id.tv_fileName)
    TextView tvFileName;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.rvResultPhoto)
    RecyclerView rvResultPhoto;
    private PhotoAdapter photoAdapter;
    private List<String> path;
    private String token, userId, eventId, createTime;
    private EventHistoryDetailModel eventHistoryDetailModel;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));

    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_event_history_detail;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("记录详情");
        token = (String) SPUtil.get(EventHistoryDetailActivity.this, "token", "");
        userId = (String) SPUtil.get(EventHistoryDetailActivity.this, "userId", "");
        path = new ArrayList<>();
        if (getIntent() != null) {
            eventId = getIntent().getExtras().getString("eventId");
            createTime = getIntent().getExtras().getString("createTime");
            getDetail(eventId, createTime);

        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvResultPhoto.setLayoutManager(gridLayoutManager);
        photoAdapter = new PhotoAdapter(this, path);
        rvResultPhoto.setAdapter(photoAdapter);

    }

    private void getDetail(String eventId, String createTime) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("eventid", eventId);
        jsonObject.addProperty("creatime", createTime);
        Log.i("tag", "jsonObject==" + jsonObject);
        Net.create(Api.class).getEventHistoryDetail(token, jsonObject)
                .enqueue(new NetCallback<EventHistoryDetailModel>(this, true) {
                    @Override
                    public void onResponse(EventHistoryDetailModel result) {
                        eventHistoryDetailModel = result;
                        if (result != null) {
                            etContent.setText(result.getHdcontent());
                            if (!TextUtils.isEmpty(result.getPicturepath())) {
                                if (!result.getPicturepath().equals("00")) {
                                    String[] photoArr = result.getPicturepath().split(";");
                                    for (int i = 0; i < photoArr.length; i++) {
                                        path.add(photoArr[i]);
                                        photoAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    tvPic.setText("无");
                                }

                            }
                            if (!TextUtils.isEmpty(result.getFilename())) {
                                tvFileName.setText(result.getFilename());
                            } else {
                                tvFileName.setText("无");
                            }
                        }
                    }
                });
    }

    @OnClick({
            R.id.iv_back,
            R.id.ll_selectFile,

    })
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_selectFile:
                if (!TextUtils.isEmpty(eventHistoryDetailModel.getFilepath())) {
                    if (!eventHistoryDetailModel.getFilepath().equals("00")) {
                        Uri uri = Uri.parse(eventHistoryDetailModel.getFilepath());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                }
                break;

        }
    }

}
