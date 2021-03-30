package com.gd.form.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gd.form.R;
import com.gd.form.adapter.FolderDataRecycleAdapter;
import com.gd.form.base.BaseActivity;
import com.gd.form.model.FileInfo;
import com.gd.form.utils.FileUtil;
import com.gd.form.view.LoadView;
import com.jaeger.library.StatusBarUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SelectFileActivity extends BaseActivity {
    private ArrayList<FileInfo> imageData = new ArrayList<>();
    private ArrayList<FileInfo> wordData = new ArrayList<>();
    private ArrayList<FileInfo> xlsData = new ArrayList<>();
    private ArrayList<FileInfo> pptData = new ArrayList<>();
    private ArrayList<FileInfo> pdfData = new ArrayList<>();
    private List<FileInfo> allFiles = new ArrayList<>();
    @BindView(R.id.recyclerViewFiles)
    RecyclerView recyclerViewFiles;
    @BindView(R.id.tv_noData)
    TextView tv_noData;
    private LoadView loading;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                initData();
            }
        }
    };
    private Thread myThread;
    private MyRunnable myRunnable;
    private FolderDataRecycleAdapter pptListAdapter;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageView(this, 0, null);
        tv_noData.setVisibility(View.GONE);
        if (loading == null) {
            loading = new LoadView(this);
        }
        loading.loading("加载中");
        loading.getWindow().setDimAmount(0f);
        loading.show();
        myRunnable = new MyRunnable();
        myThread = new Thread(myRunnable);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(SelectFileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //没有权限则申请权限
                ActivityCompat.requestPermissions(SelectFileActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
                //有权限直接执行,docode()不用做处理
                myThread.start();
            }
        } else {
            //小于6.0，不用申请权限，直接执行
            myThread.start();
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SelectFileActivity.this);
        //设置RecyclerView 布局
        recyclerViewFiles.setLayoutManager(linearLayoutManager);
        pptListAdapter = new FolderDataRecycleAdapter(SelectFileActivity.this, allFiles);
        recyclerViewFiles.setAdapter(pptListAdapter);
    }

    /**
     * 遍历文件夹中资源
     */
    public void getFolderData() {
        scanDirNoRecursion(Environment.getExternalStorageDirectory().toString());
        handler.sendEmptyMessage(1);

    }

    private void initData() {
        loading.dismiss();
        allFiles.addAll(wordData);
        allFiles.addAll(xlsData);
        allFiles.addAll(pdfData);
        if(recyclerViewFiles!=null && tv_noData!=null){
            if(allFiles.size()>0){
                recyclerViewFiles.setVisibility(View.VISIBLE);
                tv_noData.setVisibility(View.GONE);
            }else{
                recyclerViewFiles.setVisibility(View.GONE);
                tv_noData.setVisibility(View.VISIBLE);
            }
        }
        pptListAdapter.notifyDataSetChanged();
    }

    /**
     * 非递归
     *
     * @param path
     */
    public void scanDirNoRecursion(String path) {
        LinkedList list = new LinkedList();
        File dir = new File(path);
        File file[] = dir.listFiles();
        if (file == null) return;
        for (int i = 0; i < file.length; i++) {
            if (file[i].isDirectory())
                list.add(file[i]);
            else {
                System.out.println(file[i].getAbsolutePath());
            }
        }
        File tmp;
        while (!list.isEmpty()) {
            tmp = (File) list.removeFirst();//首个目录
            if (tmp.isDirectory()) {
                file = tmp.listFiles();
                if (file == null)
                    continue;
                for (int i = 0; i < file.length; i++) {
                    if (file[i].isDirectory())
                        list.add(file[i]);//目录则加入目录列表，关键
                    else {
                        if (file[i].getName().endsWith(".png") || file[i].getName().endsWith(".jpg") || file[i].getName().endsWith(".gif")) {
                            //往图片集合中 添加图片的路径
                            FileInfo document = FileUtil.getFileInfoFromFile(new File(file[i].getAbsolutePath()));
                            imageData.add(document);
                        } else if (file[i].getName().endsWith(".doc") || file[i].getName().endsWith(".docx")) {
//                            System.out.println(file[i]);
                            FileInfo document = FileUtil.getFileInfoFromFile(new File(file[i].getAbsolutePath()));
                            wordData.add(document);
                        } else if (file[i].getName().endsWith(".xls") || file[i].getName().endsWith(".xlsx")) {
                            //往图片集合中 添加图片的路径
                            FileInfo document = FileUtil.getFileInfoFromFile(new File(file[i].getAbsolutePath()));
                            xlsData.add(document);
                        } else if (file[i].getName().endsWith(".ppt") || file[i].getName().endsWith(".pptx")) {
                            //往图片集合中 添加图片的路径
                            FileInfo document = FileUtil.getFileInfoFromFile(new File(file[i].getAbsolutePath()));
                            pptData.add(document);
                        } else if (file[i].getName().endsWith(".pdf")) {
                            //往图片集合中 添加图片的路径
                            FileInfo document = FileUtil.getFileInfoFromFile(new File(file[i].getAbsolutePath()));
                            pdfData.add(document);
                        }
                    }
                }
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //执行代码,这里是已经申请权限成功了,可以不用做处理
                    //小于6.0，不用申请权限，直接执行
                    myThread.start();

                } else {
                    Toast.makeText(SelectFileActivity.this, "权限申请失败", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @OnClick({R.id.deviceOrderEditBackImageView})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.deviceOrderEditBackImageView:
                finish();
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loading != null && loading.isShowing()) {
            loading.dismiss();
        }
        handler.removeMessages(1);
        handler.removeCallbacks(myRunnable);
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_select_file;
    }

    public class MyRunnable implements Runnable {
        @Override
        public void run() {
            getFolderData();
        }
    }


}
