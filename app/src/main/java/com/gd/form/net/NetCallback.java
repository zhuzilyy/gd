package com.gd.form.net;


import android.content.Context;
import android.util.Log;

import com.gd.form.R;
import com.gd.form.utils.ToastUtil;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URLDecoder;

import retrofit2.Call;
import retrofit2.Response;

/**
 * <p>类描述：最外层数据解析CallBack封装
 * <p>创建人：wh
 * <p>创建时间：2019/6/20
 */
public abstract class NetCallback<T> extends BaseCallback<T> {

    public NetCallback(Context activity){
        super(activity);
    }

    public NetCallback(Context activity, boolean loading){
        super(activity, loading);
    }

    public NetCallback(Context activity, boolean loading, String loadText) {
        super(activity, loading, loadText);
    }

    public abstract void onResponse(T t);
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        super.onResponse(call, response);
        if(response.code()==200){
            T body = response.body();

            if (body != null) {
                onResponse(body);
            } else {
                onError(response);
            }
        }else{
            onError(response);
        }
    }
//    @Override
//    public void onResponse(Call<T> call, Response<T> response) {
//        super.onResponse(call, response);
//        if(response.code()==200){
//            T body = response.body();
//
//            if (body != null) {
//                onResponse(body);
//            } else {
//                onError(response);
//            }
//        }else{
//            onError(response);
//        }
//    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        super.onFailure(call, t);
        if(t instanceof SocketTimeoutException){
            ToastUtil.show(R.string.timeout);
        }else if(t instanceof ConnectException){
            ToastUtil.show(R.string.connect);
        }else{
            ToastUtil.show(t.getMessage());
            Log.i("-------------->onFailure",t.getMessage()+"");
        }
    }



    /**
     * 错误处理
     * {"code":4,"message":"其它异常","data":"Exception","ok":false}
     */
    private void onError(Response<T> response) {
       // ToastUtil.show(response.code());
        Log.i("------------onError1",response.message());
        Log.i("------------onError2",response.errorBody().toString());

//        if(response.code()==200){
//            ToastUtil.show(response.message());
//            return;
//        }
//        try {
//            Result error = new Gson().fromJson(response.errorBody().string(), Result.class);
//
////            String info = error.code > 200 ? error.error : error.msg;
//            String info=error.msg;
//            //code为401时，默认为Cookie过期，需要重新登录
//            if(!error.success){
//                ToastUtil.show(info);
////                Intent intent=new Intent(context, LoginActivity.class);
////                context.startActivity(intent);
////                ((AppCompatActivity)context).finish();
//            }else {
//                //404一般为接口找不到，检查路径无误后，询问后台相关接口问题
//                info ="error:" + info;
//                ToastUtil.show(info);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }


}
