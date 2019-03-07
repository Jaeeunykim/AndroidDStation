## NAVER지도 API 버전 

### 네이버 지도 API 버전 차이  
* 네이버 지도 API버전 1,2는 JindoJS프레임워크 사용  
* 버전 3에서는 DOM처리 코드 내장 (독립적으로 동작)
2017년 12월 이후는 v3만 지원 

### API 사용 방법 

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
    