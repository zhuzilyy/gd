package com.gd.form.net;

import com.gd.form.model.Department;
import com.gd.form.model.Jobs;
import com.gd.form.model.Pipelineinfo;
import com.gd.form.model.Pipemploys;
import com.gd.form.model.Pipestakeinfo;
import com.gd.form.model.ResultMsg;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * <p>类描述：请求接口
 * <p>创建人：wh
 * <p>创建时间：2019/6/20
 */
public interface Api {

    /**
     * 登录请求
     * @param jsonObject 登录参数
     */
    @POST("loginController.do?loginApp")
    Call<String> login(@Body JsonObject jsonObject);
    @POST("androidTroubleformController.do?getAllDeparts")
    Call<String> getAllDeparts();


    /**
     *获取所有岗位职称
     * @return
     */
    @GET("professionalinfoget.html")
    Call<List<Jobs>> professionalinfoget();


    /**
     *岗位职称增加.修改
     * @return
     */
    @POST("professionalinfoAdd.html")
    Call<ResultMsg> professionalinfoAdd(@Body JsonObject jsonObject);

    /**
     *岗位职称删除
     * @return
     */
    @POST("professionalinfodelete.html")
    Call<ResultMsg> professionalinfodelete(@Body JsonObject jsonObject);


    /**
     *获取所有管道信息
     * @return
     */
    @GET("pipelineinfosget.html")
    Call<List<Pipelineinfo>> pipelineinfosget();

    /**
     * 根据管道线路id获取该管道桩信息接口
     * @return
     */
    @POST("pipestakeinfoget.html")
    Call<List<Pipestakeinfo>> pipestakeinfoget(@Body JsonObject jsonObject);


    /**
     *获取所有用户信息
     * @return
     */
    @GET("pipemploysGetList.html")
    Call<List<Pipemploys>> pipemploysGetList();

    /**
     *获取所有部门名称
     * @return
     */
    @GET("pipedepartmentinfoGetList.html")
    Call<List<Department>> pipedepartmentinfoGetList();

    /**
     *新增和修改用户
     * @return
     */
    @POST("pipemploysAdd.html")
    Call<ResultMsg> pipemploysAdd(@Body JsonObject jsonObject);

    /**
     *	删除用户
     * @return
     */
    @POST("pipemploysDelete.html")
    Call<ResultMsg> pipemploysDelete(@Body JsonObject jsonObject);


    /**
     *根据用户id获取用户信息：
     * @return
     */
    @POST("pipemploysGetListByPrimaryKey.html")
    Call<List<Pipemploys>> pipemploysGetListByPrimaryKey(@Body JsonObject jsonObject);
}


