package com.gd.form.activity;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.gd.form.R;
import com.gd.form.base.BaseActivity;
import com.jaeger.library.StatusBarUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class Map1Activity extends BaseActivity {

    @BindView(R.id.mapView)
    MapView mapView;


    private AMap aMap;

    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient = null;
    private MyLocationStyle myLocationStyle;
    private Marker locationMarker;
    private GeocodeSearch geocodeSearch;
    private String area;
    private String province;
    private String city;
    private String provinceCode;
    private String cityCode;
    private LatLng latLng;
    private String toView;

    //声明定位回调监听器
    private AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            Log.i("tag",amapLocation.getErrorCode()+"====");
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //定位成功回调信息，设置相关消息
                    aMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition(new LatLng(amapLocation.getLatitude(),amapLocation.getLongitude()), 10, 0, 0)));
                    latLng=new LatLng(amapLocation.getLatitude(),amapLocation.getLongitude());
                    getAddressByLatLng(latLng);
                }
            }
        }
    };

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_map1;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapView.onCreate(savedInstanceState);
        //初始化定位
        mLocationClient = new AMapLocationClient(this);
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        setUpMap();
        initPointStyle();
        if (aMap == null) {
            aMap = mapView.getMap();
            //定义一个UiSettings对象
            UiSettings mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象
            mUiSettings.setRotateGesturesEnabled(false);
            mUiSettings.setTiltGesturesEnabled(false);
            aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
            // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
            aMap.setMyLocationEnabled(false);
        }

//        geocodeSearch=new GeocodeSearch(this);
//        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
//            //根据经纬度获取地址
//            @Override
//            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
//                RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
//                area=regeocodeAddress.getFormatAddress();
////                locationMarker.setTitle(area);
//                locationMarker.showInfoWindow();
//                province=regeocodeAddress.getProvince();
//                city=regeocodeAddress.getCity();
//                getAdCode(province);
//                getAdCode(city);
//            }
//
//            //根据地址获取区域code
//            @Override
//            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
//                if (i==1000){
//                    if (geocodeResult!=null && geocodeResult.getGeocodeAddressList()!=null && geocodeResult.getGeocodeAddressList().size()>0){
//                        GeocodeAddress geocodeAddress = geocodeResult.getGeocodeAddressList().get(0);
//                        if(geocodeResult.getGeocodeQuery().getLocationName().equals(province)){
//                            provinceCode=geocodeAddress.getAdcode();
//                        }
//                        if(geocodeResult.getGeocodeQuery().getLocationName().equals(city)){
//                            cityCode=geocodeAddress.getAdcode();
//                        }
//                    }
//                }
//            }
//        });
//
//        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
//
//            @Override
//
//            public void onCameraChange(CameraPosition cameraPosition) {
//
//            }
//
//
//
//            @Override
//            public void onCameraChangeFinish(CameraPosition cameraPosition) {
//                latLng=cameraPosition.target;
//                getAddressByLatLng(latLng);
//            }
//        });
//        aMap.setOnMapLoadedListener(this::addMarkerInScreenCenter);
    }




    /**
     * 根据经纬度获取地址
     * @param latLng 经纬度
     */
    private void getAddressByLatLng( LatLng latLng) {
            LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
            RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 50f, GeocodeSearch.AMAP);
            //异步查询
            geocodeSearch.getFromLocationAsyn(query);
    }

    private void getAdCode(String cityName){
        GeocodeQuery geocodeQuery=new GeocodeQuery(cityName.trim(),"");
        geocodeSearch.getFromLocationNameAsyn(geocodeQuery);
    }

    private void setUpMap() {
        //初始化定位参数
        //声明mLocationOption对象
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();

    }

    private void initPointStyle() {
        myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(), R.mipmap.map_location_point)));
        //边框线
//        myLocationStyle.strokeColor(Color.parseColor("#88A5D2F5"));
        //填充颜色
//        myLocationStyle.radiusFillColor(Color.parseColor("#88E0F2FF"));
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
    }

    private void addMarkerInScreenCenter() {
        LatLng latLng = aMap.getCameraPosition().target;
        Point screenPosition = aMap.getProjection().toScreenLocation(latLng);
        locationMarker = aMap.addMarker(new MarkerOptions()
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_location_point)));
        //设置Marker在屏幕上,不跟随地图移动
        locationMarker.setPositionByPixels(screenPosition.x,screenPosition.y);
        locationMarker.setZIndex(1);
    }



    @OnClick({
        R.id.mapBackImageView,
        R.id.mapSureImageView
    })
    protected void onClick(View view){
        switch (view.getId()){
            case R.id.mapBackImageView:
                finish();
                break;
            case R.id.mapSureImageView:
                finish();
                break;
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }



    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }



    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);

    }


    @Override
    protected void onStop() {
        super.onStop();
        mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
        if(null != mLocationClient){
            mLocationClient.onDestroy();
        }
    }

}
