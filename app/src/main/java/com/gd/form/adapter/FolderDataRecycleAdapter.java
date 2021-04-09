package com.gd.form.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gd.form.R;
import com.gd.form.activity.SelectFileActivity;
import com.gd.form.model.FileInfo;
import com.gd.form.utils.FileUtil;

import java.io.File;
import java.util.List;


/**
 * 使用遍历文件夹的方式
 * Created by yis on 2018/4/17.
 */

public class FolderDataRecycleAdapter extends RecyclerView.Adapter<FolderDataRecycleAdapter.ViewHolder> {

    private Context mContext;
    private List<FileInfo> data;
    private Dialog tipDialog;

    public FolderDataRecycleAdapter(Context mContext, List<FileInfo> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_folder_data_rv_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.tv_content.setText(data.get(position).getFileName());
        holder.tv_size.setText(FileUtil.FormetFileSize(data.get(position).getFileSize()));
        holder.tv_time.setText(data.get(position).getTime());
        holder.rlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File(data.get(position).getFilePath());
                showDialog(file, position);
            }
        });
        //封面图
        Glide.with(mContext).load(FileUtil.getFileTypeImageId(mContext, data.get(position).getFilePath())).fitCenter().into(holder.iv_cover);
    }

    private void showDialog(File file, int position) {
        if (tipDialog == null) {
            tipDialog = new Dialog(mContext, R.style.BottomDialog);
        }
        LinearLayout root = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.dialog_upload_file, null);
        TextView detail = root.findViewById(R.id.detail);
        TextView upload = root.findViewById(R.id.upload);
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = FileProvider.getUriForFile(mContext,"com.gd.form.fileprovider", file);
                intent.setData(content_url);
                mContext.startActivity(intent);
                tipDialog.dismiss();
//                Intent intent = new Intent(mContext, WebViewActivity.class);
//                intent.putExtra("filePath", file.getAbsolutePath());
//                mContext.startActivity(intent);
//                tipDialog.dismiss();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipDialog.dismiss();
                Intent intent = new Intent();
                intent.putExtra("fileName", file.getName());
                intent.putExtra("selectFilePath", file.getAbsolutePath());
                ((SelectFileActivity) mContext).setResult(Activity.RESULT_OK, intent);
                ((SelectFileActivity) mContext).finish();
            }
        });
        //初始化视图
        tipDialog.setContentView(root);
        //不允许触摸和系统返回键取消
        tipDialog.setCancelable(true);
        tipDialog.setCanceledOnTouchOutside(true);
        Window window = tipDialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.CENTER;
            params.width = (int) ((mContext.getResources().getDisplayMetrics().widthPixels) * 0.7); // 宽度// 宽度
            window.setAttributes(params);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        tipDialog.show();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rlMain;
        TextView tv_content;
        TextView tv_size;
        TextView tv_time;
        ImageView iv_cover;

        public ViewHolder(View itemView) {
            super(itemView);
            rlMain = itemView.findViewById(R.id.rl_main);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_size = itemView.findViewById(R.id.tv_size);
            tv_time = itemView.findViewById(R.id.tv_time);
            iv_cover = itemView.findViewById(R.id.iv_cover);
        }
    }

    private String getMIMEType(File file) {

        String type = "*/*";
        String fName = file.getName();
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0)
            return type;
        /* 获取文件的后缀名 */
        String fileType = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (fileType == null || "".equals(fileType))
            return type;
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) {
            if (fileType.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }

    private static final String[][] MIME_MapTable = {
            //{后缀名，    MIME类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".prop", "text/plain"},
            {".rar", "application/x-rar-compressed"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            //{".xml",    "text/xml"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/zip"},
            {"", "*/*"}
    };
}
