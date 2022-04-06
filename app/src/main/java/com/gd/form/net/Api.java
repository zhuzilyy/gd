package com.gd.form.net;

import com.gd.form.model.BuildingApproveModel;
import com.gd.form.model.BuildingDetailModel;
import com.gd.form.model.BuildingModel;
import com.gd.form.model.Department;
import com.gd.form.model.DepartmentPerson;
import com.gd.form.model.DeviceDetailModel;
import com.gd.form.model.ElectricityDetailModel;
import com.gd.form.model.ElectricityRecordDetailModel;
import com.gd.form.model.EventDetailModel;
import com.gd.form.model.EventHistoryDetailModel;
import com.gd.form.model.EventHistoryModel;
import com.gd.form.model.FileDetailModel;
import com.gd.form.model.FormModel;
import com.gd.form.model.HiddenDetailModel;
import com.gd.form.model.HighZoneDetailModel;
import com.gd.form.model.HighZoneModel;
import com.gd.form.model.HikingDetailModel;
import com.gd.form.model.InsulationDetailModel;
import com.gd.form.model.Jobs;
import com.gd.form.model.KpiModel;
import com.gd.form.model.LoginModel;
import com.gd.form.model.MeasureModel;
import com.gd.form.model.NextStationModel;
import com.gd.form.model.NoApproveModel;
import com.gd.form.model.OssModel;
import com.gd.form.model.OtherDetailModel;
import com.gd.form.model.OverTimeModel;
import com.gd.form.model.Pipelineinfo;
import com.gd.form.model.Pipemploys;
import com.gd.form.model.ProgressModel;
import com.gd.form.model.ProjectDetailModel;
import com.gd.form.model.ProjectModel;
import com.gd.form.model.ResultMsg;
import com.gd.form.model.ScoreModel;
import com.gd.form.model.SearchArea;
import com.gd.form.model.SearchBuildingModel;
import com.gd.form.model.SearchForm;
import com.gd.form.model.SearchPerson;
import com.gd.form.model.SearchPipeInfoModel;
import com.gd.form.model.SearchPipeModel;
import com.gd.form.model.SearchStationModel;
import com.gd.form.model.ServerModel;
import com.gd.form.model.StakeModel;
import com.gd.form.model.StandardFileModel;
import com.gd.form.model.StationApproveModel;
import com.gd.form.model.StationDetailInfo;
import com.gd.form.model.StationNoApproveModel;
import com.gd.form.model.StationNoModel;
import com.gd.form.model.StationWaterDetailModel;
import com.gd.form.model.TaskCountModel;
import com.gd.form.model.TaskDetailModel;
import com.gd.form.model.TestStakeModel;
import com.gd.form.model.TunnelDetailModel;
import com.gd.form.model.TunnelModel;
import com.gd.form.model.UpdateModel;
import com.gd.form.model.UploadEventModel;
import com.gd.form.model.UploadEventStakeModel;
import com.gd.form.model.VideoDetailModel;
import com.gd.form.model.WaitingApproveModel;
import com.gd.form.model.WaitingHandleTaskModel;
import com.gd.form.model.WaitingTakModel;
import com.gd.form.model.WaterDetailModel;
import com.gd.form.model.WaterInsuranceDetailModel;
import com.gd.form.model.WaterModel;
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
    Call<List<Jobs>> professionalinfoget(@Header("TokenValue") String token);


    /**
     * 岗位职称增加.修改
     *
     * @return
     */
    @POST("professionalinfoAdd.html")
    Call<ResultMsg> professionalinfoAdd(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 岗位职称删除
     *
     * @return
     */
    @POST("professionalinfodelete.html")
    Call<ResultMsg> professionalinfodelete(@Header("TokenValue") String token, @Body JsonObject jsonObject);


    /**
     * 获取所有管道信息
     *
     * @return
     */
    @GET("pipelineinfosget.html")
    Call<List<Pipelineinfo>> pipelineinfosget(@Header("TokenValue") String token);

    /**
     * 获取所有管道信息
     *
     * @return
     */
    @POST("GetlinesByDptid.html")
    Call<List<Pipelineinfo>> pipelineinfosgetById(@Header("TokenValue") String token, @Body JsonObject jsonObject);


    /**
     * 通过用户id获取所有管道信息
     *
     * @return
     */
    @POST("GetlinesByemployid.html")
    Call<List<Pipelineinfo>> getLinesByUserId(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 根据管道线路id获取该管道桩信息接口
     *
     * @return
     */
    @POST("pipestakeinfogetMulti.html")
    Call<List<StationNoModel>> pipestakeinfoget(@Header("TokenValue") String token, @Body JsonObject jsonObject);


    /**
     * 新增水工保护的桩号搜索
     *
     * @return
     */
    @POST("GetPipestakeinfoForWaterProject.html")
    Call<List<StationNoModel>> waterStation(@Header("TokenValue") String token, @Body JsonObject jsonObject);


    /**
     * 获取所有用户信息
     *
     * @return
     */
    @GET("pipemploysGetListUI.html")
    Call<List<Pipemploys>> pipemploysGetList(@Header("TokenValue") String token);

    /**
     * 获取所有部门名称
     *
     * @return
     */
    @GET("pipedepartmentinfoGetList.html")
    Call<List<Department>> pipedepartmentinfoGetList(@Header("TokenValue") String token);

    /**
     * 新增和修改用户
     *
     * @return
     */
    @POST("pipemploysAdd.html")
    Call<ResultMsg> pipemploysAdd(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 删除用户
     *
     * @return
     */
    @POST("pipemploysDelete.html")
    Call<ResultMsg> pipemploysDelete(@Header("TokenValue") String token, @Body JsonObject jsonObject);


    /**
     * 根据用户id获取用户信息：
     *
     * @return
     */
    @POST("pipemploysGetListUIByPrimaryKey.html")
    Call<List<Pipemploys>> pipemploysGetListByPrimaryKey(@Header("TokenValue") String token, @Body JsonObject jsonObject);

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
     * 隧道外部表单提交
     *
     * @return
     */
    @POST("w002_dataformdataUpdate.html")
    Call<ServerModel> updateTunnelRecord(@Header("TokenValue") String token, @Body JsonObject jsonObject);

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
    @POST("highareasinfoupdate.html")
    Call<ServerModel> updateHighZone(@Header("TokenValue") String token, @Body JsonObject jsonObject);

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
     * 审批违章违建时查看详情
     *
     * @return
     */
    @POST("approvalAccountinfoForPrimaryKey.html")
    Call<BuildingModel> getArrpoveBuildingData(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 更新违章违建
     *
     * @return
     */
    @POST("lllegalconstructionsinfoUpdate.html")
    Call<ServerModel> updateBuilding(@Header("TokenValue") String token, @Body JsonObject jsonObject);

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
    @POST("pipeaccountupdate.html")
    Call<ServerModel> updateTunnel(@Header("TokenValue") String token, @Body JsonObject jsonObject);

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
     * 获取所有类型的工作单
     *
     * @return
     */
    @POST("DataFormMultiGetListByStakeId.html")
    Call<List<FormModel>> getIllegalForms(@Header("TokenValue") String token, @Body JsonObject jsonObject);

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
     * 违章违建待审批
     *
     * @return
     */
    @POST("accountapprovalWait.html")
    Call<List<BuildingApproveModel>> buildingWaitingApproveList(@Header("TokenValue") String token, @Body JsonObject jsonObject);


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

    /**
     * 审批
     *
     * @return
     */
    @POST("accountapprovalHandle.html")
    Call<ServerModel> approveBuilding(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 超期任务
     *
     * @return
     */
    @POST("DataFormMultiWaitStatusList.html")
    Call<List<OverTimeModel>> overTimeList(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 待办任务
     *
     * @return
     */
    @POST("DataFormGetExpectList.html")
    Call<List<OverTimeModel>> waitingHandleList(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 获取任务数量
     *
     * @return
     */
    @POST("GetTasksTotal.html")
    Call<TaskCountModel> getTaskTotal(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 修改密码
     *
     * @return
     */
    @POST("pipemploysUpdatePwd.html")
    Call<ServerModel> changePwd(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 获取作业区
     *
     * @return
     */
    @POST("PipedepartmentinfoGetByemployid.html")
    Call<List<Department>> getDepartmentById(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 工程管理部分获取作业区
     *
     * @return
     */
    @POST("departmentinfoGetByemployidForProject.html")
    Call<List<Department>> getProjectDepartmentById(@Header("TokenValue") String token, @Body JsonObject jsonObject);


    /**
     * 根据管道和用户获取桩号
     *
     * @return
     */
    @POST("GetpipestakeinfoByIDandPipeidandKey.html")
    Call<List<StationNoModel>> getStationByFullParams(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 获取管道信息
     *
     * @return
     */
    @POST("GetpipestakeinfoBydptid.html")
    Call<SearchPipeModel> getPipeInfo(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 获取管道信息
     *
     * @return
     */
    @POST("GetpipestakeinfoBySection.html")
    Call<SearchPipeModel> getPipeInfoByStationId(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 增加水保工程
     *
     * @return
     */
    @POST("waterProtectAdd.html")
    Call<ServerModel> addWaterProtection(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 增加水保工程
     *
     * @return
     */
    @POST("waterProtectupdate.html")
    Call<ServerModel> updateWaterProtection(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 获取水保工程详情
     *
     * @return
     */
    @POST("waterProtectSelectKey.html")
    Call<WaterInsuranceDetailModel> waterProtectionDetail(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 删除风向标
     *
     * @return
     */
    @POST("StakeDeleteForwindvanes.html")
    Call<ServerModel> deleteWindVane(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 新增宣教栏
     *
     * @return
     */
    @POST("StakeUpdateForpedurail.html")
    Call<ServerModel> addBoard(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 删除宣教栏
     *
     * @return
     */
    @POST("StakeDeleteForpedurail.html")
    Call<ServerModel> deleteBoard(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 新增视频监控
     *
     * @return
     */
    @POST("StakeUpdateForviewmonitor.html")
    Call<ServerModel> addVideoMonitoring(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 删除宣教栏
     *
     * @return
     */
    @POST("StakeDeleteForviewmonitor.html")
    Call<ServerModel> deleteVideo(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 新增其他
     *
     * @return
     */
    @POST("StakeUpdateForothers.html")
    Call<ServerModel> addOthers(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 删除宣教栏
     *
     * @return
     */
    @POST("StakeDeleteForothers.html")
    Call<ServerModel> deleteOther(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 获取桩的信息
     *
     * @return
     */
    @POST("pipestakeinfoGetNextByKey.html")
    Call<NextStationModel> getStationInfo(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 获取桩的信息
     *
     * @return
     */
    @POST("pipestakeinfogetForPrimaryKey.html")
    Call<List<StationDetailInfo>> getStationDetailInfo(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 获取桩的信息
     *
     * @return
     */
    @POST("approvalstakeinfoForPrimaryKey.html")
    Call<StationDetailInfo> getStationDetailInfoFromApprove(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 更新桩的信息
     *
     * @return
     */
    @POST("pipestakeinfoUpdateBaseinfo.html")
    Call<ServerModel> updateStation(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 获取桩号信息
     *
     * @return
     */
    @POST("GetpipestakeinfoByDptidAndPipeidandKey.html")
    Call<List<StationNoModel>> getStationByDptidAndPipeIdAndKey(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 查找管道负责人
     *
     * @return
     */
    @POST("pipemploysGetListUIByDpt.html")
    Call<List<Pipemploys>> getPipeManager(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 查找管道负责人
     *
     * @return
     */
    @POST("pipemploysGetListUIByEmpID.html")
    Call<List<Pipemploys>> getPipeManagerByUserId(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 获取水工桩号
     *
     * @return
     */
    @POST("waterprojectByEmpDpt.html")
    Call<List<WaterModel>> getWaterStations(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 获取水工详情
     *
     * @return
     */
    @POST("waterProtectCheckSelectKey.html")
    Call<StationWaterDetailModel> getWaterStationDetail(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 获取隧道
     *
     * @return
     */
    @POST("pipeaccountGetAll.html")
    Call<List<SearchPipeInfoModel>> getAllTunnelList(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 获取默认审批人
     *
     * @return
     */
    @POST("pipemploysGetMaster.html")
    Call<List<DepartmentPerson>> getTunnelDefaultManager(@Header("TokenValue") String token, @Body JsonObject jsonObject);


    /**
     * 获取违章违建的数据
     *
     * @return
     */
    @POST("lllegalconstructionsinfoGetByDpt.html")
    Call<List<SearchBuildingModel>> getBuildings(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 获取违章违建隐患内容描述
     *
     * @return
     */
    @POST("lllegalconstructionsinfoGetTitleByKey.html")
    Call<BuildingModel> getBuildingDees(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 获取违章违建隐患内容描述
     *
     * @return
     */
    @POST("StationsGetByDpt.html")
    Call<List<String>> getStations(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 获取场站名称
     *
     * @return
     */
    @POST("StationsGetByDptType.html")
    Call<List<StakeModel>> getStationsByDepartmentType(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 获取水保工程
     *
     * @return
     */
    @POST("waterprojectByEmpDpt.html")
    Call<List<WaterModel>> getWatersByDepartment(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 获取水保工程详情
     *
     * @return
     */
    @POST("waterProtectSelectTitleKey.html")
    Call<WaterInsuranceDetailModel> getWatersByKey(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 获取阿里云oss数据
     *
     * @return
     */
    @GET("ossapiconnectionget.html")
    Call<OssModel> getOssData(@Header("TokenValue") String token);

    /**
     * 获取消息数量
     *
     * @return
     */
    @POST("DataFormGetAllMsg.html")
    Call<TaskCountModel> getTaskCount(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 审批未通过接口
     *
     * @return
     */
    @POST("DataFormMultiWaitStatusList.html")
    Call<List<OverTimeModel>> getRefuseList(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 更新水工保护
     *
     * @return
     */
    @POST("w001_dataformdataUpdate.html")
    Call<ServerModel> updateWater(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 更新水工保护
     *
     * @return
     */
    @POST("w005_dataformdataUpdate.html")
    Call<ServerModel> updateBuildingRecord(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 更新视频监控
     *
     * @return
     */
    @POST("w010_dataformdataUpdate.html")
    Call<ServerModel> updateVideoRecord(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 更细水工施工检查日志
     *
     * @return
     */
    @POST("w015_dataformdataUpdate.html")
    Call<ServerModel> updateWaterCheck(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 更新区域阴保
     *
     * @return
     */
    @POST("w012_dataformdataUpdate.html")
    Call<ServerModel> updateZone(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 更新耦合器
     *
     * @return
     */
    @POST("w014_dataformdataUpdte.html")
    Call<ServerModel> updateDevice(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 更新绝缘件性能
     *
     * @return
     */
    @POST("w013_dataformdataUpdate.html")
    Call<ServerModel> updateProperty(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 下发任务
     *
     * @return
     */
    @POST("worktasksAdd.html")
    Call<ServerModel> addTask(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 获取下发任务待处理列表
     *
     * @return
     */
    @POST("worktasksSelectWait.html")
    Call<List<WaitingHandleTaskModel>> getWaitingHandleTask(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 获取下发任务待处理详情
     *
     * @return
     */
    @POST("worktasksSelectKey.html")
    Call<TaskDetailModel> getTaskDetail(@Header("TokenValue") String token, @Body JsonObject jsonObject);


    /**
     * 完成任务
     *
     * @return
     */
    @POST("worktasksFinish.html")
    Call<ServerModel> finishTask(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 任务查询
     *
     * @return
     */
    @POST("worktasksSelectMulit.html")
    Call<List<WaitingHandleTaskModel>> workTaskList(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 审核未通过任务数量
     *
     * @return
     */
    @POST("worktasksSelectWaitCount.html")
    Call<WaitingTakModel> getWaitingTaskCount(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 接收任务待处理消息数量
     *
     * @return
     */
    @POST("DataFormMultiWaitStatusListCount.html")
    Call<WaitingTakModel> refuseTaskCount(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 未完成事件
     *
     * @return
     */
    @POST("EventMultiGetListCount.html")
    Call<WaitingTakModel> unFinishCount(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 更新app
     *
     * @return
     */
    @POST("AppUpdateVersion.html")
    Call<UpdateModel> appUpdate(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 获取桩号
     *
     * @return
     */
    @POST("GetElecTestStakesByEmp.html")
    Call<List<TestStakeModel>> getStakes(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 添加阴保测试
     *
     * @return
     */
    @POST("w020_dataformdataAdd.html")
    Call<ServerModel> commitElectricityRecord(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 获取阴保详情
     *
     * @return
     */
    @POST("W020GetdataByKey.html")
    Call<ElectricityRecordDetailModel> getElectricityRecordDetail(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 修改阴保记录
     *
     * @return
     */
    @POST("w020_dataformdataUpdte.html")
    Call<ServerModel> updateElectricityRecord(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 获取上传事件列表
     *
     * @return
     */
    @POST("EventMultiGetList.html")
    Call<List<UploadEventModel>> getEventList(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 获取事件详情
     *
     * @return
     */
    @POST("GetReportEventByKey.html")
    Call<EventDetailModel> getEventDetail(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 获取事件上报的测试桩号
     *
     * @return
     */
    @POST("GetpipestakeinfoByOwners.html")
    Call<List<UploadEventStakeModel>> getUploadStakeList(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 维护上报事件
     *
     * @return
     */
    @POST("reportEventAdd.html")
    Call<ServerModel> addUploadEvent(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 维护上报事件
     *
     * @return
     */
    @POST("reportEventUpdate.html")
    Call<ServerModel> UpdateUploadEvent(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 增加处理记录
     *
     * @return
     */
    @POST("reportEventRecordAdd.html")
    Call<ServerModel> addEventRecord(@Header("TokenValue") String token, @Body JsonObject jsonObject);


    /**
     * 事件历史记录
     *
     * @return
     */
    @POST("EventRecordMultiGetList.html")
    Call<List<EventHistoryModel>> getEventHistory(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 事件历史记录详情
     *
     * @return
     */
    @POST("GetReportEventRecordByKey.html")
    Call<EventHistoryDetailModel> getEventHistoryDetail(@Header("TokenValue") String token, @Body JsonObject jsonObject);


    /**
     * 完成事件
     *
     * @return
     */
    @POST("SetReportEventStatusByKey.html")
    Call<ServerModel> finishEvent(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 删除管道
     *
     * @return
     */
    @POST("pipestakeinfoDelete.html")
    Call<ServerModel> deletePipe(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 搜索水工
     *
     * @return
     */
    @POST("waterprojectByEmpDptSearch.html")
    Call<List<WaterModel>> getWaterStation(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 风向标、宣教栏、视频监控、其他 增加接口：
     *
     * @return
     */
    @POST("pipestakeothersAdd.html")
    Call<ServerModel> addSomeOthers(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 风向标、宣教栏、视频监控、其他详情
     *
     * @return
     */
    @POST("pipestakeothersSelectKey.html")
    Call<OtherDetailModel> otherDetail(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 风向标、宣教栏、视频监控、其他详情
     *
     * @return
     */
    @POST("pipestakeothersdelete.html")
    Call<ServerModel> deleteSomeOthers(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 添加工程
     *
     * @return
     */
    @POST("projectManageAdd.html")
    Call<ServerModel> addProject(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 更新工程
     *
     * @return
     */
    @POST("projectManageUpdate.html")
    Call<ServerModel> updateProject(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 添加工程
     *
     * @return
     */
    @POST("getProjectByDeptId.html")
    Call<List<ProjectModel>> getProjectList(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 获取添加的工程详情
     *
     * @return
     */
    @POST("getProjectByPrimaryKey.html")
    Call<ProjectDetailModel> getProjectDetail(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 获取进度列表
     *
     * @return
     */
    @POST("getProjectRecordsByprjid.html")
    Call<List<ProgressModel>> getProjectProgressList(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 获取进度列表
     *
     * @return
     */
    @POST("projectRecordAdd.html")
    Call<ServerModel> addProjectRecord(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 获取待审批的管道桩列表
     *
     * @return
     */
    @POST("pipestakeapprovalWait.html")
    Call<List<StationNoApproveModel>> getNoApproveStation(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 审批管道桩台账
     *
     * @return
     */
    @POST("pipestakeapprovalHandle.html")
    Call<ServerModel> approveStation(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 审批管道桩台账
     *
     * @return
     */
    @POST("pipestakeapprovalWaitCount.html")
    Call<StationApproveModel> approveStationCount(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 违章违建待审批数量
     *
     * @return
     */
    @POST("accountapprovalWaitCount.html")
    Call<StationApproveModel> approveBuildingCount(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 获取标准化文件类型
     *
     * @return
     */
    @POST("standardfiletypeGetAll.html")
    Call<List<StandardFileModel>> getStandardFiles(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 获取标准化文件列表
     *
     * @return
     */
    @POST("standardfileGetAllByType.html")
    Call<List<FileDetailModel>> getStandardFilesList(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 删除文件
     *
     * @return
     */
    @POST("standardfileDelete.html")
    Call<ServerModel> deleteFile(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 新增文件
     *
     * @return
     */
    @POST("standardfileAdd.html")
    Call<ServerModel> addFile(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 新增考核录入
     *
     * @return
     */
    @POST("kpiassessmentAdd.html")
    Call<ServerModel> addKpi(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 获取季度分数
     *
     * @return
     */
    @POST("selectKPIsummary.html")
    Call<List<ScoreModel>> getSeasonScore(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 获取平均分数
     *
     * @return
     */
    @POST("selectKPIagv.html")
    Call<List<ScoreModel>> getAverageScore(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 获取kpi列表
     *
     * @return
     */
    @POST("kpiassessmentGetKPIforSeason.html")
    Call<List<KpiModel>> getKpiList(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 删除kpi
     *
     * @return
     */
    @POST("kpiassessmentDelete.html")
    Call<ServerModel> deleteKpi(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * 删除kpi
     *
     * @return
     */
    @POST("kpiassessmentUpdate.html")
    Call<ServerModel> updateKpi(@Header("TokenValue") String token, @Body JsonObject jsonObject);


}


