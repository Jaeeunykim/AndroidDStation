## NAVER지도

### 네이버 지도 API 버전 차이  
* 네이버 지도 API버전 1,2는 JindoJS프레임워크 사용  
* 버전 3에서는 DOM처리 코드 내장 (독립적으로 동작)
2017년 12월 이후는 v3만 지원 

### API 사용 방법 
참조 링크 
> https://docs.ncloud.com/ko/naveropenapi_v3/maps/android-sdk/v3/start.html

1. naver Client ID 발급
2. gradle(project) 저장소 설정 
```
 allprojects {
   ....

   maven {
        //네이버 지도 저장소  
       url 'https://navercorp.bintray.com/maps'
       }    
}
```
3. gradle(Module:app)에 dependencies 추가 
```
dependencies {
    //네이버 지도 SDK  
    implementation 'com.naver.maps:map-sdk:3.2.1'
} 
```
 4. Client ID설정 방법 (선택)
    1.  Manifest.xml meta data 추가     
    ```
    <manifest>
        <application>
            <meta-data
            android:name="com.naver.maps.map.CLIENT_ID"
            android:value="YOUR_CLIENT_ID_HERE" />
        </application>
        </manifest>
    ```
    
    2. onCreate안에서 API호출 
    ```
    public class YourApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //네이버 API 호출 
        NaverMapSdk.getInstance(this).setClient(
        new NaverMapSdk.NaverCloudPlatformClien("YOUR_CLIENT_ID_HERE));
      }    
    }
    ```
5. 지도표시 Activtiy.xml에 fragment추가 
```
<fragment android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:id="@+id/map"
    android:name="com.naver.maps.map.MapFragment" />
```


### NaverMapAPI활용

* 고정된 위치에 마커 그리기 
* 핸들러 위치에 아이콘 생성 
* 타이머사용하여 1초동안 위도경도를 받아 맵상의 이미지 움직이도록 하기 

#### 고정된 위치에 마커 그리기 
1. 지도 뷰 선택  
뷰 : MapFragment 혹은 MapView를 사용해서 뷰 생성  
(fragment사용권장)   
인터페이스 : NaverMap를 사용

2. 지도 뷰 생성  
* MapFragment 사용 시  
  *  Fragment 생성 
  *  Frragment의 layout.xml (frameLayout)생성
* MapView 사용시  
  *  MapView의 layout.xml (frameLayout)생성  
* MapFramgent,Mapview사용시 차이점 
  * mapView의 경우 해당 뷰가 포함된 액티비디 라이프 사이클에 맞우서 호출해야함 
  onCreate(), onStart(), onResume(), onPause(), onStop(), onDestroy(), onSaveInstanceState(), onLowMemory()  
  
  * MapFragment사용시 이러한 절차 필요 없음 
3. NaverMap객체 얻어오기 (인터페이스)
* MapActivity에서 FragmentActivity extends 받고, OnMapReadyCallback implements 받는다
* on Create에서 MapFragment생성 (R.id...) 후 Transaction으로 add, commit..
* getMapAsync(this)
4. onMapRead에서 마커 생성후 좌표 전달하여 표시 
5. Manifest에 화면에 초기 맵 그려지는 좌표 설정 

-----
### 참고 자료
좌표 : 37.484876,126.970673

naverMap.setMapType(NaverMap.MapType.Hybrid);

naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BICYCLE, true);

xml
<fragment xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/map"
    android:name="com.naver.maps.map.MapFragment"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:navermap_latitude="35.1798159"
    app:navermap_longitude="129.0750222"
    app:navermap_zoom="8"
    app:navermap_mapType="Terrain" />

    marker.setIcon(OverlayImage.fromResource(R.drawable.night_icon));

    UiSettings uiSettings = naverMap.getUiSettings();

    uiSettings.setCompassEnabled(false);
    uiSettings.setLocationButtonEnabled(true);


    uiSettings.setScrollGesturesEnabled(true);
    uiSettings.zoomGesturesEnabled(true);

    naverMap.setOnMapClickListener((point, coord) ->
    Toast.makeText(this, coord.latitude + ", " + coord.longitude, Toast.LENGTH_SHORT).show());

    naverMap.setOnMapLongClickListener((point, coord) ->
    Toast.makeText(this, coord.latitude + ", " + coord.longitude, Toast.LENGTH_SHORT).show());

    marker.setOnClickListener(overlay -> {
        Toast.makeText(this, "마커 1 클릭", Toast.LENGTH_SHORT).show();
        return true; // 이벤트 소비, OnMapClick 이벤트는 발생하지 않음
    });

    사용용도 : 더블탭시 마커 취소기능
    naverMap.setOnMapDoubleTapListener((point, coord) -> {
        Toast.makeText(this, coord.latitude + ", " + coord.longitude, Toast.LENGTH_SHORT).show();
        return true;
    });

    정밀한 위치 추적 
    dependencies {
    implementation 'com.google.android.gms:play-services-location:15.0.1'
    }

    Marker marker = new Marker();

    marker.setMap(naverMap);


    오버레이제거 
    marker.setMap(null);

    OverlayImage image = OverlayImage.fromResource(R.drawable.marker_icon);
    marker.setIcon(image);

    OverlayImage image = OverlayImage.fromBitmap(bitmap)
    marker1.setIcon(image);
    marker2.setIcon(image);

    마커를 여러번 찍었을때, 우선순위(마지막찍은 곳)인 곳이 최상위 계층에 보이도록 
    yellowMarker.setZIndex(100);
    greenMarker.setZIndex(0);
    blueMarker.setZIndex(-10);

    overlay.setVisible(false);

    overlay.setOnClickListener(o -> {
        Toast.makeText(context, "오버레이 클릭됨", Toast.LENGTH_SHORT).show();
        return true;
    });

    overlay.setOnClickListener(null);   

    지도 onClickeListener에서 getPosition해서 가지고 오고 인자로 전달 
    Marker marker = new Marker();
    marker.setPosition(new LatLng(37.5670135, 126.9783740));
    marker.setMap(naverMap);

    marker.setWidth(50);
    marker.setHeight(80);
    
    marker.setWidth(Marker.SIZE_AUTO);
    marker.setHeight(Marker.SIZE_AUTO);

    용어 정리)
    사용자인터페이스 : 네이버 지도에서 제공 해주는 컨트롤러(줌, 나침반 등..)
    심벌 : 건물이름