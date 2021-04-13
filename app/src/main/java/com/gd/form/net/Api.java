package com.gd.form.net;

import com.gd.form.model.Department;
import com.gd.form.model.Jobs;
import com.gd.form.model.LoginModel;
import com.gd.form.model.MeasureRecordModel;
import com.gd.form.model.Pipelineinfo;
import com.gd.form.model.Pipemploys;
import com.gd.form.model.ResultMsg;
import com.gd.form.model.SearchStationModel;
import com.gd.form.model.ServerModel;
import com.gd.form.model.StationNoModel;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * <p>类描述：请求接口
 * <p>创建人：wh
 * <p>创建时间：2019/6/20
 */
public interface Api {

    /**
     * 登录请求
     *
     * @param jsonObject 登录参数
     */
    @POST("pipemploysVerification.html")
    Call<LoginModel> login(@Body JsonObject jsonObject);

    @POST("androidTroubleformController.do?getAllDeparts")
    Call<String> getAllDeparts();

    /**
     * 获取所有岗位职称
     *
     * @return
     */
    @GET("professionalinfoget.html")
    Call<List<Jobs>> professionalinfoget();


    /**
     * 岗位职称增加.修改
     *
     * @return
     */
    @POST("professionalinfoAdd.html")
    Call<ResultMsg> professionalinfoAdd(@Body JsonObject jsonObject);

    /**
     * 岗位职称删除
     *
     * @return
     */
    @POST("professionalinfodelete.html")
    Call<ResultMsg> professionalinfodelete(@Body JsonObject jsonObject);


    /**
     * 获取所有管道信息
     *
     * @return
     */
    @GET("pipelineinfosget.html")
    Call<List<Pipelineinfo>> pipelineinfosget();

    /**
     * 根据管道线路id获取该管道桩信息接口
     *
     * @return
     */
    @POST("pipestakeinfogetMulti.html")
    Call<List<StationNoModel>> pipestakeinfoget(@Body JsonObject jsonObject);


    /**
     * 获取所有用户信息
     *
     * @return
     */
    // @GET("pipemploysGetList.html")
    @GET("pipemploysGetListUI.html")
    Call<List<Pipemploys>> pipemploysGetList();

    /**
     * 获取所有部门名称
     *
     * @return
     */
    @GET("pipedepartmentinfoGetList.html")
    Call<List<Department>> pipedepartmentinfoGetList();

    /**
     * 新增和修改用户
     *
     * @return
     */
    @POST("pipemploysAdd.html")
    Call<ResultMsg> pipemploysAdd(@Body JsonObject jsonObject);

    /**
     * 删除用户
     *
     * @return
     */
    @POST("pipemploysDelete.html")
    Call<ResultMsg> pipemploysDelete(@Body JsonObject jsonObject);


    /**
     * 根据用户id获取用户信息：
     *
     * @return
     */
    @POST("pipemploysGetListUIByPrimaryKey.html")
    Call<List<Pipemploys>> pipemploysGetListByPrimaryKey(@Body JsonObject jsonObject);

    /**
     * 水工保护表单提交
     *
     * @return
     */
    @POST("w001_dataformdataAdd.html")
    Call<ServerModel> commitWaterProtection(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 隧道外部表单提交
     *
     * @return
     */
    @POST("w002_dataformdataAdd.html")
    Call<ServerModel> commitTunnel(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 重车表单提交
     *
     * @return
     */
    @POST("w004_dataformdataAdd.html")
    Call<ServerModel> commitWeightCar(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 违规违建表单提交
     *
     * @return
     */
    @POST("w005_dataformdataAdd.html")
    Call<ServerModel> commitIllegalBuilding(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 违规违建表单提交
     *
     * @return
     */
    @POST("w006_dataformdataAdd.html")
    Call<ServerModel> commitHiking(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 隐蔽工程
     *
     * @return
     */
    @POST("w016_dataformdataAdd.html")
    Call<ServerModel> commitConcealedWork(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 高后果区徒步巡检表
     *
     * @return
     */
    @POST("w009_dataformdataAdd.html")
    Call<ServerModel> commitHighZone(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 高后果区视频监控
     *
     * @return
     */
    @POST("w010_dataformdataAdd.html")
    Call<ServerModel> commitVideo(@Header("TokenValue") String token, @Body JsonObject jsonObject);


    /**
     * 区域阴保电位测试记录表
     *
     * @return
     */
    @POST("w012_dataformdataAdd.html")
    Call<ServerModel> commitZoneElectricity(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 阀室绝缘件性能测试记录表
     *
     * @return
     */
    @POST("w013_dataformdataAdd.html")
    Call<ServerModel> commitProperty(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 去耦合测试器
     *
     * @return
     */
    @POST("w014_dataformdataAdd.html")
    Call<ServerModel> commitDevice(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 水保工程施工监理日志
     *
     * @return
     */
    @POST("w015_dataformdataAdd.html")
    Call<ServerModel> commitWaterInsurance(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 添加管道标识
     *
     * @return
     */
    @POST("pipestakeinfoAdd.html")
    Call<ServerModel> addPipeStakeInfo(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 添加测量记录
     *
     * @return
     */
    @POST("measurerecordsAdd.html")
    Call<ServerModel> addMeasureRecords(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 查询测量记录
     *
     * @return
     */
    @POST("measurerecordsselectListBystakeid.html")
    Call<List<MeasureRecordModel>> getMeasureRecordList(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 获取测量记录详情
     *
     * @return
     */
    @POST("measurerecordsselectByStakeDate.html")
    Call<MeasureRecordModel> getMeasureRecordDetail(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 根据桩号名称和pipeid搜索数据
     *
     * @return
     */
    @POST("pipestakeinfoBookgetMulti.html")
    Call<SearchStationModel> searchStation(@Header("TokenValue") String token, @Body JsonObject jsonObject);
}


