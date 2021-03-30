package com.gd.form.model;

import android.app.Activity;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gd.form.R;
import com.lzy.imagepicker.loader.ImageLoader;

import java.io.File;

public class PicassoImageLoader implements ImageLoader {
    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        Glide.with(activity)//
                .load(Uri.fromFile(new File(path)))//
                .placeholder(R.mipmap.ic_launcher)//
                .error(R.mipmap.ic_launcher)//
                .into(imageView);
    }

    @Override
    public void displayImagePreview(Activity activity, String path, ImageView imageView, int width, int height) {

    }

    @Override
    public void clearMemoryCache() {
        //这里是清除缓存的方法,根据需要自己实现
    }
}