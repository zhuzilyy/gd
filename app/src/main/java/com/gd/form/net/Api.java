package com.gd.form.net;

import com.gd.form.model.BuildingDetailModel;
import com.gd.form.model.BuildingModel;
import com.gd.form.model.Department;
import com.gd.form.model.DeviceDetailModel;
import com.gd.form.model.ElectricityDetailModel;
import com.gd.form.model.FormModel;
import com.gd.form.model.HiddenDetailModel;
import com.gd.form.model.HighZoneDetailModel;
import com.gd.form.model.HighZoneModel;
import com.gd.form.model.HikingDetailModel;
import com.gd.form.model.InsulationDetailModel;
import com.gd.form.model.Jobs;
import com.gd.form.model.LoginModel;
import com.gd.form.model.MeasureModel;
import com.gd.form.model.NoApproveModel;
import com.gd.form.model.Pipelineinfo;
import com.gd.form.model.Pipemploys;
import com.gd.form.model.ResultMsg;
import com.gd.form.model.SearchArea;
import com.gd.form.model.SearchForm;
import com.gd.form.model.SearchPerson;
import com.gd.form.model.SearchStationModel;
import com.gd.form.model.ServerModel;
import com.gd.form.model.StationNoModel;
import com.gd.form.model.TunnelDetailModel;
import com.gd.form.model.TunnelModel;
import com.gd.form.model.VideoDetailModel;
import com.gd.form.model.WaitingApproveModel;
import com.gd.form.model.WaterDetailModel;
import com.gd.form.model.WaterProtectionModel;
import com.gd.form.model.WeightCarDetailModel;
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
     * 修改管道标识
     *
     * @return
     */
    @POST("pipestakeinfoUpdateBaseinfo.html")
    Call<ServerModel> updatePipeStakeInfo(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 添加测量记录
     *
     * @return
     */
    @POST("measurerecordsAdd.html")
    Call<ServerModel> addMeasureRecords(@Header("TokenValue") String token, @Body JsonObject jsonObject);


    /**
     * 获取测量记录详情
     *
     * @return
     */
    @POST("measurerecordsselectByStakeDate.html")
    Call<List<MeasureModel>> getMeasureRecordDetail(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 根据桩号名称和pipeid搜索数据
     *
     * @return
     */
    @POST("pipestakeinfogetForPrimaryKey.html")
    Call<List<SearchStationModel>> searchStation(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 添加责任人
     *
     * @return
     */
    @POST("StakeupdateForpipeowners.html")
    Call<ServerModel> addPrincipal(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 添加风向标
     *
     * @return
     */
    @POST("StakeUpdateForwindvanes.html")
    Call<ServerModel> addWindVane(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 添加站场或是阀室
     *
     * @return
     */
    @POST("StakeUpdateForstations.html")
    Call<ServerModel> addStation(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 添加站场宣传栏
     *
     * @return
     */
    @POST("StakeUpdateForpedurail.html")
    Call<ServerModel> addAdvocacyBoard(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 添加视频监控
     *
     * @return
     */
    @POST("StakeUpdateForviewmonitor.html")
    Call<ServerModel> addVideo(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 添加水工保护
     *
     * @return
     */
    @POST("StakeUpdateForwaterprotect.html")
    Call<ServerModel> addWaterProtect(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 添加人井(盘缆点)桩
     *
     * @return
     */
    @POST("StakeUpdateFormanpile.html")
    Call<ServerModel> addWell(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 添加其他
     *
     * @return
     */
    @POST("StakeUpdateForothers.html")
    Call<ServerModel> addOther(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 添加高后果区
     *
     * @return
     */
    @POST("highareasinfoAdd.html")
    Call<ServerModel> addHighZone(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 获取高后果区数据
     *
     * @return
     */
    @POST("highareasinfoGetByKey.html")
    Call<List<HighZoneModel>> getHighZoneData(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 搜索高后果区数据
     *
     * @return
     */
    @POST("highareasinfoGetByKey.html")
    Call<List<HighZoneModel>> searchHighZoneData(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 新增测量记录
     *
     * @return
     */
    @POST("measurerecordsAdd.html")
    Call<ServerModel> addMeasureRecord(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 获取测量记录
     *
     * @return
     */
    @POST("measurerecordsselectListBystakeid.html")
    Call<List<MeasureModel>> getMeasureRecords(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 查看违章违建
     *
     * @return
     */
    @POST("lllegalconstructionsinfoGetByKey.html")
    Call<List<BuildingModel>> getBuildingData(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 添加违章违建
     *
     * @return
     */
    @POST("lllegalconstructionsinfoAdd.html")
    Call<ServerModel> addBuilding(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 查询违章建筑
     *
     * @return
     */
    @POST("lllegalconstructionsinfoGetMultiKey.html")
    Call<List<BuildingModel>> searchBuilding(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 查询高后果区
     *
     * @return
     */
    @POST("highareasinfoGetMultiKey.html")
    Call<List<HighZoneModel>> searchHighZone(@Header("TokenValue") String token, @Body JsonObject jsonObject);


    /**
     * 添加隧道
     *
     * @return
     */
    @POST("pipeaccountAdd.html")
    Call<ServerModel> addTunnel(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 搜索隧道
     *
     * @return
     */
    @POST("pipeaccountGetMultiKey.html")
    Call<List<TunnelModel>> searchTunnel(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 查看隧道
     *
     * @return
     */
    @POST("pipeaccountGetByKey.html")
    Call<List<TunnelModel>> getTunnelData(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 查询界面查看作业区
     *
     * @return
     */
    @POST("PipedepartmentinfoGetByemployid.html")
    Call<List<SearchArea>> getSearchArea(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 查询界面人员名称
     *
     * @return
     */
    @POST("pipemploysGetListBydeptid.html")
    Call<List<SearchPerson>> getSearchPerson(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 获取工单类型
     *
     * @return
     */
    @POST("dataformbaseinfogetAll.html")
    Call<List<SearchForm>> getSearchForm(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 获取所有类型的工作单
     *
     * @return
     */
    @POST("DataFormMultiGetList.html")
    Call<List<FormModel>> getAllForms(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 水工保护巡检表
     *
     * @return
     */
    @POST("W001GetdataByKey.html")
    Call<WaterProtectionModel> getWaterProtectionDetail(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 隧道外部巡检表
     *
     * @return
     */
    @POST("W002GetdataByKey.html")
    Call<TunnelDetailModel> getTunnelDetail(@Header("TokenValue") String token, @Body JsonObject jsonObject);


    /**
     * 重车碾压
     *
     * @return
     */
    @POST("W004GetdataByKey.html")
    Call<WeightCarDetailModel> getWeightCarDetail(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 违章违建
     *
     * @return
     */
    @POST("W005GetdataByKey.html")
    Call<BuildingDetailModel> getBuildingDetail(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 徒步巡检
     *
     * @return
     */
    @POST("W006GetdataByKey.html")
    Call<HikingDetailModel> getHikingDetail(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 水工施工日志
     *
     * @return
     */
    @POST("W015GetdataByKey.html")
    Call<WaterDetailModel> getWaterDetail(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 隐蔽工程检查
     *
     * @return
     */
    @POST("W016GetdataByKey.html")
    Call<HiddenDetailModel> getHiddenDetail(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 高后果区
     *
     * @return
     */
    @POST("W009GetdataByKey.html")
    Call<HighZoneDetailModel> getHighZoneDetail(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 视频监控查看记录
     *
     * @return
     */
    @POST("W010GetdataByKey.html")
    Call<VideoDetailModel> getVideoDetail(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 区域阴保电位测试
     *
     * @return
     */
    @POST("W012GetdataByKey.html")
    Call<ElectricityDetailModel> getElectricityDetail(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 阀室绝缘件性能测试
     *
     * @return
     */
    @POST("W013GetdataByKey.html")
    Call<InsulationDetailModel> getInsulationDetail(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 去耦合器测试
     *
     * @return
     */
    @POST("W014GetdataByKey.html")
    Call<DeviceDetailModel> getDeviceDetail(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 待审批接口
     *
     * @return
     */
    @POST("DataFormMultiGetApprovalList.html")
    Call<List<WaitingApproveModel>> waitingApproveList(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 超期未批
     *
     * @return
     */
    @POST("DataFormMultiGetApprovalList.html")
    Call<List<NoApproveModel>> noApproveList(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 审批
     *
     * @return
     */
    @POST("dataformapprovalUpdate.html")
    Call<ServerModel> approve(@Header("TokenValue") String token, @Body JsonObject jsonObject);


}


