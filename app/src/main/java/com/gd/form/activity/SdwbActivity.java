package com.gd.form.activity;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RadioButton;
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
import com.gd.form.utils.MessageEvent;
import com.gd.form.view.ListDialog;
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
    @BindView(R.id.rg_wgxw)
    RadioGroup rg_wgxw;
    @BindView(R.id.rg_sgss)
    RadioGroup rg_sgss;

    private TimePickerView pvTime;
    private ListDialog dialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initTimePicker();


        rg_wgxw.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                String sel = radioButton.getText().toString();
                Log.i("-----------",sel);
            }
        });

        rg_sgss.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                String sel = radioButton.getText().toString();
                Log.i("-----------",sel);
            }
        });

    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext,R.color.colorFF52A7F9));
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
            case R.id.ll_sdmc:
                List<String> listM = new ArrayList<>();

                listM.add("隧道1");
                listM.add("隧道2");
                listM.add("隧道3");

                if (dialog == null) {
                    dialog = new ListDialog(mContext);
                }
                dialog.setData(listM);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    Log.i("-------------",positionM+"");
                    tv_sdmc.setText(listM.get(positionM));
                    dialog.dismiss();
                });
                break;
            case R.id.ll_sdwz:
                List<String> listCz = new ArrayList<>();

                listCz.add("位置1");
                listCz.add("位置2");
                if (dialog == null) {
                    dialog = new ListDialog(mContext);
                }
                dialog.setData(listCz);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    Log.i("-------------",positionM+"");
                    tv_sdwz.setText(listCz.get(positionM));
                    dialog.dismiss();
                });
                break;
            case R.id.ll_gddz:
                List<String> listZh = new ArrayList<>();

                listZh.add("作业区1");
                listZh.add("作业区2");
                listZh.add("作业区3");
                listZh.add("作业区4");
                listZh.add("作业区5");

                if (dialog == null) {
                    dialog = new ListDialog(mContext);
                }
                dialog.setData(listZh);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    Log.i("-------------",positionM+"");
                    tv_gddw.setText(listZh.get(positionM));
                    dialog.dismiss();
                });
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
                  //  tv_jcsj.setText(getTime(date));
                } else if (v.getId()==R.id.ll_tbrq){
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
