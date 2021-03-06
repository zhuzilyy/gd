package com.gd.form.net;

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
import com.gd.form.model.FormModel;
import com.gd.form.model.HiddenDetailModel;
import com.gd.form.model.HighZoneDetailModel;
import com.gd.form.model.HighZoneModel;
import com.gd.form.model.HikingDetailModel;
import com.gd.form.model.InsulationDetailModel;
import com.gd.form.model.Jobs;
import com.gd.form.model.LoginModel;
import com.gd.form.model.MeasureModel;
import com.gd.form.model.NextStationModel;
import com.gd.form.model.NoApproveModel;
import com.gd.form.model.OssModel;
import com.gd.form.model.OverTimeModel;
import com.gd.form.model.Pipelineinfo;
import com.gd.form.model.Pipemploys;
import com.gd.form.model.ResultMsg;
import com.gd.form.model.SearchArea;
import com.gd.form.model.SearchBuildingModel;
import com.gd.form.model.SearchForm;
import com.gd.form.model.SearchPerson;
import com.gd.form.model.SearchPipeInfoModel;
import com.gd.form.model.SearchPipeModel;
import com.gd.form.model.SearchStationModel;
import com.gd.form.model.ServerModel;
import com.gd.form.model.StakeModel;
import com.gd.form.model.StationDetailInfo;
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
 * <p>????????????????????????
 * <p>????????????wh
 * <p>???????????????2019/6/20
 */
public interface Api {

    /**
     * ????????????
     *
     * @param jsonObject ????????????
     */
    @POST("pipemploysVerification.html")
    Call<LoginModel> login(@Body JsonObject jsonObject);

    @POST("androidTroubleformController.do?getAllDeparts")
    Call<String> getAllDeparts();

    /**
     * ????????????????????????
     *
     * @return
     */
    @GET("professionalinfoget.html")
    Call<List<Jobs>> professionalinfoget(@Header("TokenValue") String token);


    /**
     * ??????????????????.??????
     *
     * @return
     */
    @POST("professionalinfoAdd.html")
    Call<ResultMsg> professionalinfoAdd(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????
     *
     * @return
     */
    @POST("professionalinfodelete.html")
    Call<ResultMsg> professionalinfodelete(@Header("TokenValue") String token, @Body JsonObject jsonObject);


    /**
     * ????????????????????????
     *
     * @return
     */
    @GET("pipelineinfosget.html")
    Call<List<Pipelineinfo>> pipelineinfosget(@Header("TokenValue") String token);

    /**
     * ????????????????????????
     *
     * @return
     */
    @POST("GetlinesByDptid.html")
    Call<List<Pipelineinfo>> pipelineinfosgetById(@Header("TokenValue") String token, @Body JsonObject jsonObject);


    /**
     * ????????????id????????????????????????
     *
     * @return
     */
    @POST("GetlinesByemployid.html")
    Call<List<Pipelineinfo>> getLinesByUserId(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????id??????????????????????????????
     *
     * @return
     */
    @POST("pipestakeinfogetMulti.html")
    Call<List<StationNoModel>> pipestakeinfoget(@Header("TokenValue") String token, @Body JsonObject jsonObject);


    /**
     * ????????????????????????
     *
     * @return
     */
    @GET("pipemploysGetListUI.html")
    Call<List<Pipemploys>> pipemploysGetList(@Header("TokenValue") String token);

    /**
     * ????????????????????????
     *
     * @return
     */
    @GET("pipedepartmentinfoGetList.html")
    Call<List<Department>> pipedepartmentinfoGetList(@Header("TokenValue") String token);

    /**
     * ?????????????????????
     *
     * @return
     */
    @POST("pipemploysAdd.html")
    Call<ResultMsg> pipemploysAdd(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ????????????
     *
     * @return
     */
    @POST("pipemploysDelete.html")
    Call<ResultMsg> pipemploysDelete(@Header("TokenValue") String token, @Body JsonObject jsonObject);


    /**
     * ????????????id?????????????????????
     *
     * @return
     */
    @POST("pipemploysGetListUIByPrimaryKey.html")
    Call<List<Pipemploys>> pipemploysGetListByPrimaryKey(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ????????????????????????
     *
     * @return
     */
    @POST("w001_dataformdataAdd.html")
    Call<ServerModel> commitWaterProtection(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ????????????????????????
     *
     * @return
     */
    @POST("w002_dataformdataAdd.html")
    Call<ServerModel> commitTunnel(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ????????????????????????
     *
     * @return
     */
    @POST("w002_dataformdataUpdate.html")
    Call<ServerModel> updateTunnelRecord(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????
     *
     * @return
     */
    @POST("w004_dataformdataAdd.html")
    Call<ServerModel> commitWeightCar(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ????????????????????????
     *
     * @return
     */
    @POST("w005_dataformdataAdd.html")
    Call<ServerModel> commitIllegalBuilding(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ????????????????????????
     *
     * @return
     */
    @POST("w006_dataformdataAdd.html")
    Call<ServerModel> commitHiking(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ????????????
     *
     * @return
     */
    @POST("w016_dataformdataAdd.html")
    Call<ServerModel> commitConcealedWork(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ???????????????????????????
     *
     * @return
     */
    @POST("w009_dataformdataAdd.html")
    Call<ServerModel> commitHighZone(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ????????????????????????
     *
     * @return
     */
    @POST("w010_dataformdataAdd.html")
    Call<ServerModel> commitVideo(@Header("TokenValue") String token, @Body JsonObject jsonObject);


    /**
     * ?????????????????????????????????
     *
     * @return
     */
    @POST("w012_dataformdataAdd.html")
    Call<ServerModel> commitZoneElectricity(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ????????????????????????????????????
     *
     * @return
     */
    @POST("w013_dataformdataAdd.html")
    Call<ServerModel> commitProperty(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????
     *
     * @return
     */
    @POST("w014_dataformdataAdd.html")
    Call<ServerModel> commitDevice(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????????????????
     *
     * @return
     */
    @POST("w015_dataformdataAdd.html")
    Call<ServerModel> commitWaterInsurance(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????
     *
     * @return
     */
    @POST("pipestakeinfoAdd.html")
    Call<ServerModel> addPipeStakeInfo(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????
     *
     * @return
     */
    @POST("pipestakeinfoUpdateBaseinfo.html")
    Call<ServerModel> updatePipeStakeInfo(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????
     *
     * @return
     */
    @POST("measurerecordsAdd.html")
    Call<ServerModel> addMeasureRecords(@Header("TokenValue") String token, @Body JsonObject jsonObject);


    /**
     * ????????????????????????
     *
     * @return
     */
    @POST("measurerecordsselectByStakeDate.html")
    Call<List<MeasureModel>> getMeasureRecordDetail(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ?????????????????????pipeid????????????
     *
     * @return
     */
    @POST("pipestakeinfogetForPrimaryKey.html")
    Call<List<SearchStationModel>> searchStation(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ???????????????
     *
     * @return
     */
    @POST("StakeupdateForpipeowners.html")
    Call<ServerModel> addPrincipal(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ???????????????
     *
     * @return
     */
    @POST("StakeUpdateForwindvanes.html")
    Call<ServerModel> addWindVane(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ????????????????????????
     *
     * @return
     */
    @POST("StakeUpdateForstations.html")
    Call<ServerModel> addStation(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ?????????????????????
     *
     * @return
     */
    @POST("StakeUpdateForpedurail.html")
    Call<ServerModel> addAdvocacyBoard(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????
     *
     * @return
     */
    @POST("StakeUpdateForviewmonitor.html")
    Call<ServerModel> addVideo(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????
     *
     * @return
     */
    @POST("StakeUpdateForwaterprotect.html")
    Call<ServerModel> addWaterProtect(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ????????????(?????????)???
     *
     * @return
     */
    @POST("StakeUpdateFormanpile.html")
    Call<ServerModel> addWell(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ????????????
     *
     * @return
     */
    @POST("StakeUpdateForothers.html")
    Call<ServerModel> addOther(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????
     *
     * @return
     */
    @POST("highareasinfoupdate.html")
    Call<ServerModel> updateHighZone(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ????????????????????????
     *
     * @return
     */
    @POST("highareasinfoGetByKey.html")
    Call<List<HighZoneModel>> getHighZoneData(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ????????????????????????
     *
     * @return
     */
    @POST("highareasinfoGetByKey.html")
    Call<List<HighZoneModel>> searchHighZoneData(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????
     *
     * @return
     */
    @POST("measurerecordsAdd.html")
    Call<ServerModel> addMeasureRecord(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????
     *
     * @return
     */
    @POST("measurerecordsselectListBystakeid.html")
    Call<List<MeasureModel>> getMeasureRecords(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????
     *
     * @return
     */
    @POST("lllegalconstructionsinfoGetByKey.html")
    Call<List<BuildingModel>> getBuildingData(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????
     *
     * @return
     */
    @POST("lllegalconstructionsinfoUpdate.html")
    Call<ServerModel> updateBuilding(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????
     *
     * @return
     */
    @POST("lllegalconstructionsinfoGetMultiKey.html")
    Call<List<BuildingModel>> searchBuilding(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????
     *
     * @return
     */
    @POST("highareasinfoGetMultiKey.html")
    Call<List<HighZoneModel>> searchHighZone(@Header("TokenValue") String token, @Body JsonObject jsonObject);


    /**
     * ????????????
     *
     * @return
     */
    @POST("pipeaccountupdate.html")
    Call<ServerModel> updateTunnel(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ????????????
     *
     * @return
     */
    @POST("pipeaccountGetMultiKey.html")
    Call<List<TunnelModel>> searchTunnel(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ????????????
     *
     * @return
     */
    @POST("pipeaccountGetByKey.html")
    Call<List<TunnelModel>> getTunnelData(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ???????????????????????????
     *
     * @return
     */
    @POST("PipedepartmentinfoGetByemployid.html")
    Call<List<SearchArea>> getSearchArea(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ????????????????????????
     *
     * @return
     */
    @POST("pipemploysGetListBydeptid.html")
    Call<List<SearchPerson>> getSearchPerson(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????
     *
     * @return
     */
    @POST("dataformbaseinfogetAll.html")
    Call<List<SearchForm>> getSearchForm(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????????????????
     *
     * @return
     */
    @POST("DataFormMultiGetList.html")
    Call<List<FormModel>> getAllForms(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ?????????????????????
     *
     * @return
     */
    @POST("W001GetdataByKey.html")
    Call<WaterProtectionModel> getWaterProtectionDetail(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ?????????????????????
     *
     * @return
     */
    @POST("W002GetdataByKey.html")
    Call<TunnelDetailModel> getTunnelDetail(@Header("TokenValue") String token, @Body JsonObject jsonObject);


    /**
     * ????????????
     *
     * @return
     */
    @POST("W004GetdataByKey.html")
    Call<WeightCarDetailModel> getWeightCarDetail(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ????????????
     *
     * @return
     */
    @POST("W005GetdataByKey.html")
    Call<BuildingDetailModel> getBuildingDetail(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ????????????
     *
     * @return
     */
    @POST("W006GetdataByKey.html")
    Call<HikingDetailModel> getHikingDetail(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????
     *
     * @return
     */
    @POST("W015GetdataByKey.html")
    Call<WaterDetailModel> getWaterDetail(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????
     *
     * @return
     */
    @POST("W016GetdataByKey.html")
    Call<HiddenDetailModel> getHiddenDetail(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ????????????
     *
     * @return
     */
    @POST("W009GetdataByKey.html")
    Call<HighZoneDetailModel> getHighZoneDetail(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ????????????????????????
     *
     * @return
     */
    @POST("W010GetdataByKey.html")
    Call<VideoDetailModel> getVideoDetail(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ????????????????????????
     *
     * @return
     */
    @POST("W012GetdataByKey.html")
    Call<ElectricityDetailModel> getElectricityDetail(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ???????????????????????????
     *
     * @return
     */
    @POST("W013GetdataByKey.html")
    Call<InsulationDetailModel> getInsulationDetail(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????
     *
     * @return
     */
    @POST("W014GetdataByKey.html")
    Call<DeviceDetailModel> getDeviceDetail(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ???????????????
     *
     * @return
     */
    @POST("DataFormMultiGetApprovalList.html")
    Call<List<WaitingApproveModel>> waitingApproveList(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ????????????
     *
     * @return
     */
    @POST("DataFormMultiGetApprovalList.html")
    Call<List<NoApproveModel>> noApproveList(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????
     *
     * @return
     */
    @POST("dataformapprovalUpdate.html")
    Call<ServerModel> approve(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ????????????
     *
     * @return
     */
    @POST("DataFormMultiWaitStatusList.html")
    Call<List<OverTimeModel>> overTimeList(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ????????????
     *
     * @return
     */
    @POST("DataFormGetExpectList.html")
    Call<List<OverTimeModel>> waitingHandleList(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????
     *
     * @return
     */
    @POST("GetTasksTotal.html")
    Call<TaskCountModel> getTaskTotal(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ????????????
     *
     * @return
     */
    @POST("pipemploysUpdatePwd.html")
    Call<ServerModel> changePwd(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ???????????????
     *
     * @return
     */
    @POST("PipedepartmentinfoGetByemployid.html")
    Call<List<Department>> getDepartmentById(@Header("TokenValue") String token, @Body JsonObject jsonObject);


    /**
     * ?????????????????????????????????
     *
     * @return
     */
    @POST("GetpipestakeinfoByIDandPipeidandKey.html")
    Call<List<StationNoModel>> getStationByFullParams(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????
     *
     * @return
     */
    @POST("GetpipestakeinfoBydptid.html")
    Call<SearchPipeModel> getPipeInfo(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????
     *
     * @return
     */
    @POST("GetpipestakeinfoBySection.html")
    Call<SearchPipeModel> getPipeInfoByStationId(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????
     *
     * @return
     */
    @POST("waterProtectAdd.html")
    Call<ServerModel> addWaterProtection(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????
     *
     * @return
     */
    @POST("waterProtectupdate.html")
    Call<ServerModel> updateWaterProtection(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ????????????????????????
     *
     * @return
     */
    @POST("waterProtectSelectKey.html")
    Call<WaterInsuranceDetailModel> waterProtectionDetail(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ???????????????
     *
     * @return
     */
    @POST("StakeDeleteForwindvanes.html")
    Call<ServerModel> deleteWindVane(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ???????????????
     *
     * @return
     */
    @POST("StakeUpdateForpedurail.html")
    Call<ServerModel> addBoard(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ???????????????
     *
     * @return
     */
    @POST("StakeDeleteForpedurail.html")
    Call<ServerModel> deleteBoard(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????
     *
     * @return
     */
    @POST("StakeUpdateForviewmonitor.html")
    Call<ServerModel> addVideoMonitoring(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ???????????????
     *
     * @return
     */
    @POST("StakeDeleteForviewmonitor.html")
    Call<ServerModel> deleteVideo(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ????????????
     *
     * @return
     */
    @POST("StakeUpdateForothers.html")
    Call<ServerModel> addOthers(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ???????????????
     *
     * @return
     */
    @POST("StakeDeleteForothers.html")
    Call<ServerModel> deleteOther(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????
     *
     * @return
     */
    @POST("pipestakeinfoGetNextByKey.html")
    Call<NextStationModel> getStationInfo(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????
     *
     * @return
     */
    @POST("pipestakeinfogetForPrimaryKey.html")
    Call<List<StationDetailInfo>> getStationDetailInfo(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????
     *
     * @return
     */
    @POST("pipestakeinfoUpdateBaseinfo.html")
    Call<ServerModel> updateStation(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????
     *
     * @return
     */
    @POST("GetpipestakeinfoByDptidAndPipeidandKey.html")
    Call<List<StationNoModel>> getStationByDptidAndPipeIdAndKey(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ?????????????????????
     *
     * @return
     */
    @POST("pipemploysGetListUIByDpt.html")
    Call<List<Pipemploys>> getPipeManager(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ?????????????????????
     *
     * @return
     */
    @POST("pipemploysGetListUIByEmpID.html")
    Call<List<Pipemploys>> getPipeManagerByUserId(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????
     *
     * @return
     */
    @POST("waterprojectByEmpDpt.html")
    Call<List<WaterModel>> getWaterStations(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????
     *
     * @return
     */
    @POST("waterProtectCheckSelectKey.html")
    Call<StationWaterDetailModel> getWaterStationDetail(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ????????????
     *
     * @return
     */
    @POST("pipeaccountGetAll.html")
    Call<List<SearchPipeInfoModel>> getAllTunnelList(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ?????????????????????
     *
     * @return
     */
    @POST("pipemploysGetMaster.html")
    Call<List<DepartmentPerson>> getTunnelDefaultManager(@Header("TokenValue") String token, @Body JsonObject jsonObject);


    /**
     * ???????????????????????????
     *
     * @return
     */
    @POST("lllegalconstructionsinfoGetByDpt.html")
    Call<List<SearchBuildingModel>> getBuildings(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ????????????????????????????????????
     *
     * @return
     */
    @POST("lllegalconstructionsinfoGetTitleByKey.html")
    Call<BuildingModel> getBuildingDees(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ????????????????????????????????????
     *
     * @return
     */
    @POST("StationsGetByDpt.html")
    Call<List<String>> getStations(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????
     *
     * @return
     */
    @POST("StationsGetByDptType.html")
    Call<List<StakeModel>> getStationsByDepartmentType(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????
     *
     * @return
     */
    @POST("waterprojectByEmpDpt.html")
    Call<List<WaterModel>> getWatersByDepartment(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ????????????????????????
     *
     * @return
     */
    @POST("waterProtectSelectTitleKey.html")
    Call<WaterInsuranceDetailModel> getWatersByKey(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ???????????????oss??????
     *
     * @return
     */
    @GET("ossapiconnectionget.html")
    Call<OssModel> getOssData(@Header("TokenValue") String token);

    /**
     * ??????????????????
     *
     * @return
     */
    @POST("DataFormGetAllMsg.html")
    Call<TaskCountModel> getTaskCount(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ?????????????????????
     *
     * @return
     */
    @POST("DataFormMultiWaitStatusList.html")
    Call<List<OverTimeModel>> getRefuseList(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????
     *
     * @return
     */
    @POST("w001_dataformdataUpdate.html")
    Call<ServerModel> updateWater(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????
     *
     * @return
     */
    @POST("w005_dataformdataUpdate.html")
    Call<ServerModel> updateBuildingRecord(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????
     *
     * @return
     */
    @POST("w010_dataformdataUpdate.html")
    Call<ServerModel> updateVideoRecord(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????????????????
     *
     * @return
     */
    @POST("w015_dataformdataUpdate.html")
    Call<ServerModel> updateWaterCheck(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????
     *
     * @return
     */
    @POST("w012_dataformdataUpdate.html")
    Call<ServerModel> updateZone(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ???????????????
     *
     * @return
     */
    @POST("w014_dataformdataUpdte.html")
    Call<ServerModel> updateDevice(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ?????????????????????
     *
     * @return
     */
    @POST("w013_dataformdataUpdate.html")
    Call<ServerModel> updateProperty(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ????????????
     *
     * @return
     */
    @POST("worktasksAdd.html")
    Call<ServerModel> addTask(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ?????????????????????????????????
     *
     * @return
     */
    @POST("worktasksSelectWait.html")
    Call<List<WaitingHandleTaskModel>> getWaitingHandleTask(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ?????????????????????????????????
     *
     * @return
     */
    @POST("worktasksSelectKey.html")
    Call<TaskDetailModel> getTaskDetail(@Header("TokenValue") String token, @Body JsonObject jsonObject);


    /**
     * ????????????
     *
     * @return
     */
    @POST("worktasksFinish.html")
    Call<ServerModel> finishTask(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????
     *
     * @return
     */
    @POST("worktasksSelectMulit.html")
    Call<List<WaitingHandleTaskModel>> workTaskList(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????
     *
     * @return
     */
    @POST("worktasksSelectWaitCount.html")
    Call<WaitingTakModel> getWaitingTaskCount(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????
     *
     * @return
     */
    @POST("DataFormMultiWaitStatusListCount.html")
    Call<WaitingTakModel> refuseTaskCount(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????app
     *
     * @return
     */
    @POST("AppUpdateVersion.html")
    Call<UpdateModel> appUpdate(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ????????????
     *
     * @return
     */
    @POST("GetElecTestStakesByEmp.html")
    Call<List<TestStakeModel>> getStakes(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????
     *
     * @return
     */
    @POST("w020_dataformdataAdd.html")
    Call<ServerModel> commitElectricityRecord(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????
     *
     * @return
     */
    @POST("W020GetdataByKey.html")
    Call<ElectricityRecordDetailModel> getElectricityRecordDetail(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????
     *
     * @return
     */
    @POST("w020_dataformdataUpdte.html")
    Call<ServerModel> updateElectricityRecord(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ????????????????????????
     *
     * @return
     */
    @POST("EventMultiGetList.html")
    Call<List<UploadEventModel>> getEventList(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????
     *
     * @return
     */
    @POST("GetReportEventByKey.html")
    Call<EventDetailModel> getEventDetail(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ?????????????????????????????????
     *
     * @return
     */
    @POST("GetpipestakeinfoByOwners.html")
    Call<List<UploadEventStakeModel>> getUploadStakeList(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????
     *
     * @return
     */
    @POST("reportEventAdd.html")
    Call<ServerModel> addUploadEvent(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????
     *
     * @return
     */
    @POST("reportEventUpdate.html")
    Call<ServerModel> UpdateUploadEvent(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ??????????????????
     *
     * @return
     */
    @POST("reportEventRecordAdd.html")
    Call<ServerModel> addEventRecord(@Header("TokenValue") String token, @Body JsonObject jsonObject);


    /**
     * ??????????????????
     *
     * @return
     */
    @POST("EventRecordMultiGetList.html")
    Call<List<EventHistoryModel>> getEventHistory(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ????????????????????????
     *
     * @return
     */
    @POST("GetReportEventRecordByKey.html")
    Call<EventHistoryDetailModel> getEventHistoryDetail(@Header("TokenValue") String token, @Body JsonObject jsonObject);


    /**
     * ????????????
     *
     * @return
     */
    @POST("SetReportEventStatusByKey.html")
    Call<ServerModel> finishEvent(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ????????????
     *
     * @return
     */
    @POST("pipestakeinfoDelete.html")
    Call<ServerModel> deletePipe(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ????????????
     *
     * @return
     */
    @POST("waterprojectByEmpDptSearch.html")
    Call<List<WaterModel>> getWaterStation(@Header("TokenValue") String token, @Body JsonObject jsonObject);

    /**
     * ????????????????????????????????????????????? ???????????????
     *
     * @return
     */
    @POST("pipestakeothersAdd.html")
    Call<ServerModel> addSomeOthers(@Header("TokenValue") String token, @Body JsonObject jsonObject);
}


