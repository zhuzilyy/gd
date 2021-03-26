package com.gd.form.net;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;


import com.gd.form.R;
import com.gd.form.view.LoadView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * <p>类描述：对RetrofitCallback进行简单封装
 * <p>创建人：wh
 * <p>创建时间：2019/7/29
 */
public class BaseCallback<T> implements Callback<T>, LifecycleObserver {

    private Call<T> call;
    private boolean isStop;
    private LoadView loading;
    protected Context context;
    BaseCallback(Context activity) {
        this(activity, true);
        this.context = activity;
    }

    BaseCallback(Context activity, boolean loading) {
        this(activity, loading, activity.getResources().getString(R.string.loading));
    }

    BaseCallback(Context activity, boolean loading, String loadText) {
        ((AppCompatActivity) activity).getLifecycle().addObserver(this);
        if (loading) {
            showLoading(activity, loadText);
        }
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        this.call = call;
        if (isStop) {
            cancelCall();
            return;
        }
        dismissLoading();
    }


    @Override
    public void onFailure(Call<T> call, Throwable t) {
        this.call = call;
        t.printStackTrace();
        dismissLoading();
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private void onPause(LifecycleOwner owner) {
        dismissLoading();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private void onStop(LifecycleOwner owner) {
        isStop = true;
        cancelCall();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void onDestroy(LifecycleOwner owner) {
        cancelCall();
        call = null;
        owner.getLifecycle().removeObserver(this);
    }

    private void cancelCall() {
        dismissLoading();
        if (call != null && !call.isCanceled()) {
            call.cancel();
        }
    }


    private void showLoading(Context activity, String loadText) {
        if (loading == null) {
            loading = new LoadView(activity);
            loading.setCancelable(false);
            loading.setCanceledOnTouchOutside(false);
        }
        loading.loading(loadText);
        loading.getWindow().setDimAmount(0f);
        loading.show();
    }

    private void dismissLoading() {
        if (loading != null && loading.isShowing()) {
            loading.dismiss();
        }
    }

}
