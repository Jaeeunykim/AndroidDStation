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
