package com.gd.form.activity;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.gd.form.R;
import com.gd.form.base.BaseActivity;
import com.gd.form.demo.IndicatorExpandableListAdapter;
import com.gd.form.model.Pipelineinfo;
import com.gd.form.model.Pipestakeinfo;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.MessageEvent;
import com.gd.form.utils.Util;
import com.gd.form.view.ListDialog;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SgbhActivity extends BaseActivity {
    @BindView(R.id.tv_location)
    TextView tv_location;
    @BindView(R.id.tv_sgxs)
    TextView tv_sgxs;
    @BindView(R.id.tv_cz)
    TextView tv_cz;
    @BindView(R.id.tv_jcsj)
    TextView tv_jcsj;
    @BindView(R.id.tv_tbrq)
    TextView tv_tbrq;
    @BindView(R.id.tv_zh)
    TextView tv_zh;
    @BindView(R.id.rg_isgood)
    RadioGroup rg_isgood;

    private TimePickerView pvTime;
    private ListDialog dialog;

    List<Pipelineinfo> listPipelineinfo=new ArrayList<>();
    List<Pipestakeinfo> listPipestakeinfo=new ArrayList<>();

    private  String[][] pipeStakes;
    private  String[] pipLines;
    private Dialog confirmDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initTimePicker();

        confirmDialog = new Dialog(mContext, R.style.BottomDialog);

        rg_isgood.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                String sel = radioButton.getText().toString();
                Log.i("-----------",sel);
            }
        });

        getPipelineInfoListRequest();
      //  getPipestakeinfoRequest(0);


    }



    private void getPipelineInfoListRequest() {

        Net.create(Api.class).pipelineinfosget()
                .enqueue(new NetCallback<List<Pipelineinfo>>(this,false) {
                    @Override
                    public void onResponse(List<Pipelineinfo> list1) {
                        Log.i("--------",list1.size()+"");
                        listPipelineinfo=list1;
                        pipLines=new String[list1.size()];
                        pipeStakes=new String[list1.size()][];
                        for (int j=0;j<list1.size();j++){
                            pipLines[j]=list1.get(j).getName();
                            Log.i("--------pipLines",pipLines[j]+"");
                            getPipeStakeInfoRequest(list1.get(j).getId(),j);
                        }


                        Log.i("--------pipLines",pipLines.toString()+"");
                    }
                });
    }

    private void getPipeStakeInfoRequest(int id,int i) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", id);
        Net.create(Api.class).pipestakeinfoget(jsonObject)
                .enqueue(new NetCallback<List<Pipestakeinfo>>(this,true) {
                    @Override
                    public void onResponse(List<Pipestakeinfo> list2) {
                        Log.i("--------1",list2.size()+"");
                        Log.i("--------1id",id+"");
                        pipeStakes[i]=new String[list2.size()];
                        for (int j=0;j<list2.size();j++){
                            pipeStakes[i][j]=list2.get(j).getName();
                            Log.i("--------pipeStakes",pipeStakes[i][j]+"");
                        }
                    }
                });
    }



    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext,R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_sgbh;
    }

    @OnClick({
            R.id.ll_location,
            R.id.ll_sgxs,
            R.id.ll_cz,
            R.id.ll_jcsj,
            R.id.ll_zh,
            R.id.ll_scfj,
            R.id.ll_tbrq,
            R.id.iv_back,
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_location:
                Bundle bundle=new Bundle();
                openActivity(MapActivity.class, bundle, false);
                break;
            case R.id.ll_sgxs:
                List<String> listM = new ArrayList<>();

                listM.add("护岸、过水面");
                listM.add("挡土墙");
                listM.add("护坡");
                if (dialog == null) {
                    dialog = new ListDialog(mContext);
                }
                dialog.setData(listM);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    Log.i("-------------",positionM+"");
                    tv_sgxs.setText(listM.get(positionM));
                    dialog.dismiss();
                });
                break;
            case R.id.ll_cz:
                List<String> listCz = new ArrayList<>();

                listCz.add("浆砌石");
                listCz.add("草袋");
                if (dialog == null) {
                    dialog = new ListDialog(mContext);
                }
                dialog.setData(listCz);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    Log.i("-------------",positionM+"");
                    tv_cz.setText(listCz.get(positionM));
                    dialog.dismiss();
                });
                break;
            case R.id.ll_zh:

                LinearLayout root = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.activity_expand, null);
               // TextView tv_tips = root.findViewById(R.id.tv_tips);
                final ExpandableListView listView = (ExpandableListView) root.findViewById(R.id.expandable_list);
            //    final IndicatorExpandableListAdapter adapter = new IndicatorExpandableListAdapter(Constant.BOOKS, Constant.FIGURES);
                final IndicatorExpandableListAdapter adapter = new IndicatorExpandableListAdapter(pipLines, pipeStakes);
                listView.setAdapter(adapter);

                // 清除默认的 Indicator
                listView.setGroupIndicator(null);

                int groupCount = listView.getCount();
//                for (int i=0; i<groupCount; i++)
//                {
//                    listView.expandGroup(i);
//                }
                //  设置分组项的点击监听事件
                listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                    @Override
                    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                       // Log.d(TAG, "onGroupClick: groupPosition:" + groupPosition + ", id:" + id);
                        boolean groupExpanded = parent.isGroupExpanded(groupPosition);
                        adapter.setIndicatorState(groupPosition, groupExpanded);
                        // 请务必返回 false，否则分组不会展开
                        return false;
                    }
                });

                //  设置子选项点击监听事件
                listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                        Toast.makeText(SgbhActivity.this, pipeStakes[groupPosition][childPosition], Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                //初始化视图
                confirmDialog.setContentView(root);

                Window window = confirmDialog.getWindow();
                if (window != null) {
                    WindowManager.LayoutParams params = window.getAttributes();
                    params.gravity = Gravity.CENTER;
                    params.width = mContext.getResources().getDisplayMetrics().widthPixels - Util.dpToPx(mContext, (int) mContext.getResources().getDimension(R.dimen.d_30)); // 宽度
                    params.height = mContext.getResources().getDisplayMetrics().heightPixels - Util.dpToPx(mContext, (int) mContext.getResources().getDimension(R.dimen.d_120)); // 高度
                    window.setAttributes(params);
                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }

                if (confirmDialog != null && !confirmDialog.isShowing()) {
                    confirmDialog.show();
                }




//                List<String> listZh = new ArrayList<>();
//
//                listZh.add("Lt0081+300");
//                listZh.add("LT0081+1334");
//                listZh.add("LTL0082");
//                if (dialog == null) {
//                    dialog = new ListDialog(mContext);
//                }
//                dialog.setData(listZh);
//                dialog.show();
//                dialog.setListItemClick(positionM -> {
//                    Log.i("-------------",positionM+"");
//                    tv_zh.setText(listZh.get(positionM));
//                    dialog.dismiss();
//                });
                break;

            case R.id.ll_jcsj:
                pvTime.show(view);
                break;
            case R.id.ll_tbrq:
                pvTime.show(view);
                break;
            case R.id.ll_scfj:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/*");//设置类型
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
                break;
        }
    }


    private void initTimePicker() {//Dialog 模式下，在底部弹出
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
               // Toast.makeText(SgbhActivity.this, getTime(date), Toast.LENGTH_SHORT).show();

                if (v.getId()==R.id.iv_jcsj){
                    tv_jcsj.setText(getTime(date));
                } else if (v.getId()==R.id.iv_tbrq){
                    tv_tbrq.setText(getTime(date));
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
        if (resultCode == RESULT_OK && data != null) {
            Log.i("------", "选择的文件Uri = " + data.toString());
            //通过Uri获取真实路径
            final String excelPath = getRealFilePath(this, data.getData());
            Log.i("------", "excelPath = " + excelPath);//    /storage/emulated/0/test.xls

        }

    }

    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent) {
        tv_location.setText(messageEvent.getMessage());
    }
}
