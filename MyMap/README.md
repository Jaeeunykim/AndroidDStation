### to do 
1. gralde 맵라이브러리 버전 변경 
2. gralde project maven추가 
3. mapActivity 상속삭제 
4. 네이버 맵키 매니패스트에 등록
5. multidex 설정 추가
6. adb 에서 apk파일 삭제 후 실행 (오류해결)
7. Mainfest에 fragment 추가
8. MapActivity의 onCreate에서 setContentView설정 


- - - 
adb 실행방법 .bash_profile 확인후
adb shell 
명령어 호출 


---

### 3/8(FRI) To do 
* implement onMapReadyCallback 제대로 이해하기 [완료]
    Async : 비동기 , sync : 동기(대기)
* 
* 앱 실행시 첫화면으로 회사 근처 맵그리고, 마커(copter)생성 [완료]
  1. drawable에 이미지 추가 
  2. 아이콘을 사용을 위해서 overlayImage 객체 생성
  3. marker의 setIcon 메소드 사용하여 마커 생성 


* 앱 실행시 맵 화면과 마커만 표시 되도록 수정(핸드폰배너정보제거) [완료]
* 배터리정보, GPS, Position(비행정보), 자전거모드
  1. DJI Sample code 실행 (View확인)
  2. png이미지 
  3. 앱 실행시 가로로 보이도록: Manifest, Activity설정에
     android:screenOrientation="landscape" 추가 
  4. 상태바 지우기
  5. 지도유형: 하이브리드(자전거)
     naverMap.setMapType(NaverMap.MapType.Hybrid);
    
  6. 반투명 레이어 추가(배터리정보, GPS, Position)  
     android:background="#FF000000" 80 50로 투명도
----
### 3/9(SAT) To do 
1. 레이아웃 비율에 따라 화면 뷰 나타내는 방법 확인 
2. layout 속성들(weight, gravity...)
3. DJI sample code에서 기체, 컨트롤러 정보 받아 오는 부분 확인
4. 그 값을 상태바에 텍스트자리에 넣기
5. Android UART API확인
6. NaverTestMap에 마커의 위치값 forloop으로 update
   - Thread변수 생성 (timer(call-sleep-update)) :호출될때마다 위도경도 +0.5
   - 마커의 위치값으로 전달  
----
### 3/12(Tue)
* SDK API 
* 배터리 정보 가지고 오는 :getChargeRemainingInPercent(common/battery)
* GPS정보 : etGPSSignalLevel(FlightControllerState)
* 조종기모드(ex포지션) : getFlightMode() -> 결과 (enum)  -> String 비교해서 포지션 그려줌 
* 위도경도 :getAircraftLocation(check) /getLocationCoordinate (FlightControllerState)
* 배터리 관련 정보해서 랜딩..상태: BatteryThresholdBehavior
* isFlying() : 현재 비행기 날고 있는지 상태 
* getHeading() :  북 0 이고 동쪽으로 이동 1도씩 180씩 가는 경우  북 서 쪽으로 -180도까지 비행기의 헤딩을 그방향에 맞춰서  get 해서 180을 받앗다면 동서남북 기준으로 몇도 회전? 확인 가능
* component classes가 중요 / 비행들어가면 미션클래스 보고 
* manager 가지고와서 비행상태 뿌려줌 !! do it!
* API에서 해당 메소드 호출되는 부분 찾아서 어떻게 뿌려 주는지 확인해보고(sample code)

----
### 3/13(Wed)
* ####  DJI BaseComponet 정보 가지고와서 UI View에 보여주기  
 1. DJI 정보 가지고오기   
    1. DJIManager.getInstance(DJI Object접근자)  
    2. getProduct
    3. getBaseComponent(필요한 컴포넌트)
       ex) battery.getBatteryPercent하면 안됨! 그럼? stateCallback으로!
    4. setStateCallback(new BaseCompState.callback){update}
    5. getNeedInfo(필요한정보)

2.  View에 그려주기   
    (뷰는 메인쓰레드만 접근 가능하니, Post이용해서!)
    1. Post(){new Runnable... run(){findR.id... = 정보.toString..}}
 
 * #### TestActivity생성해서 컴포넌트 정보들 text로 확인
---

### 3/14(Thu)

#### DJI 조정기 정보 (비행체정보)가지고 오는 부분
  1. DJISDKManager 클래스가 DJI 제품과 SDK를 사용하는데 EntryPoint 임.  
      이 클래스가 제품에 접근 및 연결을 그리고 sdk register하는데 사용된다.   
  2. SDK Admin /  **Product Connection** / Debug and Logging / Get Log Path / Managers / LDM  
  3.  Product Connection  클래스 -> startConnectionToProduct
       1) startConnectionToProduct는 DJI제품과 SDK를 연결를 시작한다.
            단, registration App이 성공한뒤에 한번 호출가능하며,
            이 동작에 DJI제품과 모바일 기기 사이의 데이터 연결이 있다.
       2) 데이터 연결은 USB/Wift 혹은 블루투스이다. 
           - USB/Wift연결은 SDK외부에서 설정되어야 한다 (android Menifest에서 리소스연결)
           - 블루투스는 getBluetoothProductConnector로 설정 한다(bluetoothProductConnector 객체 있어야한다)
       3) 2번의 연결이 성공하면 **onProductConnect**가 호출
       4) startConnectionToProduct 에서 시작한 모든 연결이 성공하면 **true**값 반환 

## To do   
- 정리 방법 : BaseComponent의 someMthod, Post방법으로 뷰에 적용
### Map : 지도에 비행체 표시 [1시간 소요예상]
- [x] DJI heading 정보 확인 
  - BaseComponent : Compass
  - Method : getHeading()
  - Post로 textView에 정보 값 저장
- [x] 이미지 찾기 : aircraft.png
- [x] 위치 적용 
  - BaseComponent : flightControllerState
  - Method : getLatitude(), getLongitude()
  - 받아온값 check :latitude > -90 && latitude < 90 && longitude > -180 && longitude < 180) && (latitude != 0f && longitude != 0f)
  - runOnUiThread로 맵뷰에 해당 마커 그려줌
  - marker.setAngle()


### 3/15(Fri)
- To do 
  - 배터리 정보 받아온 값 사용해서 상태바에 그려주기 
    - <s>레이아웃 상태바 추가(MyMap)
    - 배터리,GPS imageView,TextView생성 </s>
    - 가지고 온 배터리 퍼센트 텍스뷰에 넣기 
  - 현재 비행체 위치 맵에 마커로 그려주기 
    - <s>맵에 현재 위치 받아와서 : getLat, getLon</s>
    - 비행체 마커 그려주기 : (NaverMap) [test필요]
    - 이후 Heading 설정
  - 아이콘 만들기(GPS)


### 3/16(Sat)
- To do 
  - 앱실행시 사용자 위치 표시 (mobile) -> naverMap
    - 1. ManiFest권한 확인 
    - 2. setLocationSource() 호출해서 LocationSource구현제 지정(LocationSource는 네이버 위치제공 인터페이스)
    - 3. builde.gradle에 play-services-location의존성 추가 
    - 4. onMapReady에서  nMap.setLocationSource 호출,setLocationTrackingMode 모드설정
  - GPS Singal Level
    - BaseCompnet : FlightControllerState
    - Method : getGPSSignalLevel()
    - return : LEVEL_0~5 , None
    - getGPSLevel() 메소드 구현함

  - NaverMap에 dji에서 가지고온 위도 경도 위치에 마커 오버레이
    - 네이버맵 생성 순서 :   
    onCraete에서 getMapFragment -->   
    getMapAsync를 호출해서 네이버맵 객체 가지고옴 -->  
    콜백 메소드로 onMapReady 호출되고 여기서 맵관련 마커,오버레이등을 설정 함 
    - onCreate에서 네이버 맵 객체를 가지고 올때 Parent에 이 객체저장해둠
    - onMapReady에서 DJI위치정보를 가지고오는 setFlightControllerCallback메소드 호출
    - stateCallback 안에서 쓰레드로 마커 업데이트 해줌
    - 맵을 업데이트 해주기 위해서 getMapAsync(parent)호출 (상황반복됨)
    