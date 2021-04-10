package com.gd.form.utils;

import android.content.Context;

import com.gd.form.view.LoadView;

public class LoadingUtil {
    private LoadView loading;
    private Context context;

    public LoadingUtil(LoadView loading, Context context) {
        this.loading = loading;
        this.context = context;
    }

    public void showLoading(String loadText) {
        if (loading == null) {
            loading = new LoadView(context);
            loading.setCancelable(false);
            loading.setCanceledOnTouchOutside(false);
        }
        loading.loading(loadText);
        loading.getWindow().setDimAmount(0f);
        loading.show();
    }

    public void dismissLoading() {
        if (loading != null && loading.isShowing()) {
            loading.dismiss();
        }
    }

}
