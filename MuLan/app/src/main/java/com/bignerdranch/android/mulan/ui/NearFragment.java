package com.bignerdranch.android.mulan.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.bignerdranch.android.mulan.R;
import com.bignerdranch.android.mulan.activity.MarkerDetailActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class NearFragment extends Fragment  {

    //变量
    //定位SDK核心类
    private LocationClient locationClient;
    //定位监听
    public MyLocationListenner myListener = new MyLocationListenner();
    //百度地图控件
    private MapView mapView;
    //百度地图对象
    private BaiduMap baiduMap;
    // 是否首次定位
    boolean isFirstLoc = true;

    private MyOnGetPoiSearchResultListener listener=new MyOnGetPoiSearchResultListener();
    private FloatingActionButton scenic;
    private FloatingActionButton hotel;
    private FloatingActionButton restaurant;
    private FloatingActionButton loc;
    private OverlayOptions options;
    private PoiSearch mPoiSearch;
    private PoiNearbySearchOption nearbySearchOption;
    private String tag;

    private LatLng latLng;
    private List<PoiInfo> poiInfos;
//    private List<LatLng> latLngList = new ArrayList<>();
    private List<OverlayOptions> optionsList = new ArrayList<>();
    private String name;
    private String address;
    private String phoneNum;
    private Intent in ;

    private PoiSearch poiSearch;
    private SuggestionSearch suggestionSearch;
    private AutoCompleteTextView autoCompleteTextView;
    private AutoCompleteTextView auto_city;
    private Button query_button;

    public NearFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getActivity().getApplicationContext());
        View view=inflater.inflate(R.layout.fragment_near, container, false);

        in= getActivity().getIntent();

        scenic=view.findViewById(R.id.fab_scenic);
        hotel=view.findViewById(R.id.fab_hotel);
        restaurant =view.findViewById(R.id.fab_restaurant);
        loc=view.findViewById(R.id.fab_location);

        //地图初始化
        //获取地图控件引用
        mapView =view.findViewById(R.id.bmapView);
        // 不显示百度地图Logo
        mapView.removeViewAt(1);
        //获取百度地图对象
        baiduMap = mapView.getMap();
        // 开启定位图层
        baiduMap.setMyLocationEnabled(true);


        //定位初始化
        //声明定位SDK核心类
        locationClient = new LocationClient(getActivity());

        //LocationClientOption类是配置信息类，设置定位服务的配置信息，比如定位时间间隔、是否使用GPS等等
        initLocation();

        //注册监听
        locationClient.registerLocationListener(myListener);
        //开启定位
        locationClient.start();

        scenic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tag="scenic";
                Search(tag);
            }
        });

        hotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tag="hotel";
                Search(tag);
            }
        });

        restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tag="restaurant";
                Search(tag);
            }
        });

        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(latLng));
            }
        });

        auto_city=view.findViewById(R.id.et_city);
        autoCompleteTextView=view.findViewById(R.id.et_keyword);
        query_button=view.findViewById(R.id.query_button);
        query_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //POI搜索模块
                poiSearch = PoiSearch.newInstance();
                //增加监听：POI搜索结果PoiSearchListener
                poiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
                    @Override
                    public void onGetPoiResult(PoiResult poiResult) {

                    }

                    @Override
                    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
                        if (poiDetailResult == null
                                || poiDetailResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
                            Toast.makeText(getActivity(), "未找到结果", Toast.LENGTH_LONG)
                                    .show();
                            return;
                        }
                        if (poiDetailResult.error == SearchResult.ERRORNO.NO_ERROR) {
                            //搜索到POI
                            baiduMap.clear();

                            Bundle mBundle = new Bundle();
                            mBundle.putString("name", poiDetailResult.name);
                            mBundle.putString("address",poiDetailResult.address);
                            mBundle.putString("phoneNum", poiDetailResult.telephone);

                            tag="scenic";

                            baiduMap.addOverlay(new MarkerOptions()
                                    .extraInfo(mBundle)
                                    .position(poiDetailResult.location)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.overlay))
                                    .zIndex(9)
                                    .draggable(true)
                            );

                            //将该POI点设置为地图中心
                            baiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(poiDetailResult.location));
                            Toast.makeText(getActivity(), "搜索中...", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (poiDetailResult.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
                        }
                    }

                    @Override
                    public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {

                    }

                    @Override
                    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

                    }
                });
                //模糊搜索
                suggestionSearch = SuggestionSearch.newInstance();
                //增加监听：模糊搜索查询结果
                suggestionSearch.setOnGetSuggestionResultListener(new OnGetSuggestionResultListener() {
                    @Override
                    public void onGetSuggestionResult(final SuggestionResult suggestionResult) {
                        if (suggestionResult == null || suggestionResult.getAllSuggestions() == null) {
                            return;
                        }
                        List<String> suggest = new ArrayList<>();
                        for (SuggestionResult.SuggestionInfo suggestionInfo : suggestionResult.getAllSuggestions()) {
                            if (suggestionInfo.key != null) {
                                suggest.add(suggestionInfo.key);
                            }
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, suggest);
                        autoCompleteTextView.setAdapter(adapter);
                        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                SuggestionResult.SuggestionInfo info = suggestionResult.getAllSuggestions().get(position);
                                poiSearch.searchPoiDetail(new PoiDetailSearchOption().poiUid(info.uid));
                            }
                        });
                        adapter.notifyDataSetChanged();
                    }
                });
                SuggestionSearchOption suggestionSearchOption=new SuggestionSearchOption();
                suggestionSearchOption.city(auto_city.getText().toString());
                suggestionSearchOption.keyword(autoCompleteTextView.getText().toString());
                suggestionSearchOption.citylimit(true);
                suggestionSearch.requestSuggestion(suggestionSearchOption);
                suggestionSearch.destroy();
            }
        });

        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                //通过这个方法获取到，你前面塞的值
                Bundle extraInfo = marker.getExtraInfo();
                if (marker.getExtraInfo() == null) {
                    Toast.makeText(getActivity(), "没有您要找的地方", Toast.LENGTH_SHORT).show();
                } else {
                    baiduMap.hideInfoWindow();
                    name = extraInfo.getString("name");
                    address = extraInfo.getString("address");
                    phoneNum = extraInfo.getString("phoneNum");

                    //点击maker弹出的详情框
                    View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.show_overlay_info, null);
                    TextView overlay_name = view1.findViewById(R.id.overlay_name);
                    TextView overlay_address = view1.findViewById(R.id.overlay_address);
                    TextView overlay_detail = view1.findViewById(R.id.overlay_detail);
                    overlay_name.setText(name);
                    overlay_address.setText(address);
                    LatLng ll = marker.getPosition();
                    InfoWindow infoWin = new InfoWindow(view1, ll, -30);
                    baiduMap.showInfoWindow(infoWin);

                    //点击查看当前货源详情
                    overlay_detail.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Intent intent1 = new Intent(getActivity(), MarkerDetailActivity.class);
                            intent1.putExtra("tag", tag);
                            intent1.putExtra("Username", in.getStringExtra("Username"));
                            intent1.putExtra("name", name);
                            intent1.putExtra("address", address);
                            intent1.putExtra("phoneNum", phoneNum);
                            startActivity(intent1);
                            baiduMap.hideInfoWindow();
                        }
                    });
                }
                //不remove此marker会被执行多bai次
//                baiduMap.removeMarkerClickListener(markerListener);
                return false;
            }
        });
        return view;
    }

    private void initLocation(){
        //定位配置信息，LocationClientOption类用来设置定位方式
        LocationClientOption option = new LocationClientOption();
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        // 设置坐标类型,默认gcj02，设置返回的定位结果坐标系
        // 坐标类型：gcj02：国测局坐标/bd09：百度墨卡托坐标/bd09ll：百度经纬度坐标；
        option.setCoorType("bd09ll");
        int span=1000;
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setScanSpan(span);
        //可选，设置是否需要地址信息，默认不需要
        option.setIsNeedAddress(true);
        //可选，默认false,设置是否使用gps
        option.setOpenGps(true);
        //可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setLocationNotify(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIsNeedLocationPoiList(true);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setIgnoreKillProcess(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        option.setEnableSimulateGps(false);
        locationClient.setLocOption(option);
    }

    //定位SDK监听函数
    // BDLocationListener接口即可获得定位数据，那么在该接口内可以对定位数据进行处理、或者存储等等
    public class MyLocationListenner implements BDLocationListener {

        //BDLocation类
        // 封装了定位SDK的定位结果，在BDLocationListener的onReceive方法中获取。
        // 通过该类用户可以获取error code，位置的坐标，精度半径等信息
        @Override
        public void onReceiveLocation(final BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            baiduMap.setMyLocationData(locData);

            if (isFirstLoc) {
                isFirstLoc = false;
                latLng = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(latLng).zoom(18.0f);
                baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
            if (poiLocation == null) {
                return;
            }
        }
    }

    //查询POI结果回调监听器，周边返回的搜索结果
    public class MyOnGetPoiSearchResultListener implements OnGetPoiSearchResultListener {

        //获取Poi检索列表
        @Override
        public void onGetPoiResult(PoiResult poiResult) {
            //poi列表
            // PoiInfo中包含了经纬度、城市、地址信息、poi名称、uid、邮编、电话等等信息；
            poiInfos = poiResult.getAllPoi();
            optionsList=new ArrayList<>();

            BitmapDescriptor bitmap=BitmapDescriptorFactory.fromResource(R.drawable.overlay);;
//            if(tag.equals("scenic")){
//                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.gym);
//            }else if(tag.equals("hotel")){
//                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.hotel);
//            }else if (tag.equals("restaurant")){
//                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.restaurant);
//            }

            if(poiInfos.isEmpty()){
                Toast.makeText(getActivity(), "一百公里内没有您要找的地方", Toast.LENGTH_SHORT).show();
            }else {
                for(int i=0;i<poiInfos.size();i++){

                    //传值 识别点击的是哪个marker
                    Bundle mBundle = new Bundle();
                    mBundle.putString("name", poiInfos.get(i).name);
                    mBundle.putString("address",poiInfos.get(i).address);
                    mBundle.putString("phoneNum", poiInfos.get(i).phoneNum);

                    // 构建markerOption，用于在地图上添加marker
                    options = new MarkerOptions()
                            .extraInfo(mBundle) //这里bundle 跟maker关联上
                            .position(poiInfos.get(i).location)// 设置marker的位置
                            .icon(bitmap)// 设置marker的图标
                            .zIndex(9)// 設置marker的所在層級
                            .draggable(true);// 设置手势拖拽
                    optionsList.add(options);
                    // 在地图上添加marker，并显示
                    baiduMap.addOverlays(optionsList);
//                marker = (Marker) baiduMap.addOverlay(options);
                }
            }

            // PoiAddrInfo：只包含地址、经纬度、名称
//            List<PoiAddrInfo> PoiAddrInfos = poiResult.getAllAddr();
        }

        //获取某个Poi详细信息
        @Override
        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
            // 当执行以下请求时，此方法回调
            // PoiDetailSearchOption detailSearchOption = new PoiDetailSearchOption();
            // detailSearchOption.poiUid(poiInfo.uid);//设置要查询的poi的uid
            // mPoiSearch.searchPoiDetail(detailSearchOption);//查询poi详细信息
            //  poiDetailResult里面包含poi的信息
        }

        @Override
        public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {

        }

        //查询室内poi检索结果回调
        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
            // 当执行以下请求时，此方法回调
            // PoiIndoorOption indoorOption = new PoiIndoorOption();
            // indoorOption.poiFloor(floor);//楼层
            // mPoiSearch.searchPoiDetail(indoorOption);
        }
    }

    public void Search(String tag){
        //先清除 地图上的覆盖物
        baiduMap.clear();
        poiInfos=new ArrayList<>();
        //初始化POI检索
        mPoiSearch = PoiSearch.newInstance();
        //注册搜索事件监听
        mPoiSearch.setOnGetPoiSearchResultListener(listener);

        //搜索位置点周边POI
        //POI附近检索参数设置类
        nearbySearchOption = new PoiNearbySearchOption();
        if(tag.equals("scenic")){
            nearbySearchOption.keyword("景区");//搜索关键字
        }else if(tag.equals("hotel")){
            nearbySearchOption.keyword("酒店");
        }else if (tag.equals("restaurant")){
            nearbySearchOption.keyword("餐厅");
        }
        nearbySearchOption.location(latLng);//搜索的位置点
        nearbySearchOption.radius(100000);//搜索覆盖半径
        nearbySearchOption.sortType(PoiSortType.distance_from_near_to_far);//搜索类型，从近至远
        nearbySearchOption.pageNum(1);//查询第几页：POI量可能会很多，会有分页查询;
        nearbySearchOption.pageCapacity(10);//设置每页查询的个数，默认10个
        mPoiSearch.searchNearby(nearbySearchOption);//查询

        mPoiSearch.destroy();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        // 退出时销毁定位
        locationClient.stop();
        // 关闭定位图层
        baiduMap.setMyLocationEnabled(false);
        mapView.onDestroy();
        mapView = null;
        super.onDestroy();
    }

}
