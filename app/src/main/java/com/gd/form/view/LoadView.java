package com.gd.form.view;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.gd.form.R;


/**
 * <p>类描述：自定义加载进度框
 */
public class LoadView extends Dialog {

    private Context context;
    public LoadView(Context context) {
        super(context);
        this.context=context;
    }




    public void loading(String s){
        View view= LayoutInflater.from(context).inflate(R.layout.load_view,null,false);
//        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(150,110);
        this.setContentView(view);
    }
    /**
     * 得到屏幕宽度
     * @return 屏幕宽度
     */
    private int getScreenWidth() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }
    /**
     * 得到屏幕高度
     * @return 屏幕宽度
     */
    private int getScreenHeight(){
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

}
