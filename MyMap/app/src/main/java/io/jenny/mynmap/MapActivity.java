package io.jenny.mynmap;

import android.content.Intent;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;

import dji.common.battery.BatteryState;
import dji.common.flightcontroller.FlightControllerState;
import dji.common.flightcontroller.GPSSignalLevel;
import dji.sdk.base.BaseProduct;
import dji.sdk.flightcontroller.FlightController;
import dji.sdk.products.Aircraft;

import static java.lang.Thread.sleep;


public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;

    NaverMap nMap;
    double droneLocationLng,droneLocationLat;
    Marker aircraft;
    MapFragment mapFragment;
    private FlightController mFlightController;
    private String batter_percent;
    TextView battery_text;
    ImageView battery_img;
    TextView gps_text;
    ImageView gps_img;
    MapActivity parent;

    private int[] battery_imgs={
            R.drawable.battery_white0,
            R.drawable.battery_white1,
            R.drawable.battery_white2,
            R.drawable.battery_white3,
            R.drawable.battery_white4,

    };
    private int[] gps_imgs={
            R.drawable.satellite0,
            R.drawable.satellite1,
            R.drawable.satellite2,
            R.drawable.satellite3,
            R.drawable.satellite4,


    };

    GPSSignalLevel gpsLevl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        battery_text = findViewById(R.id.battery_text);
        battery_img = findViewById(R.id.battery_img);
        aircraft = new Marker();
        gps_img = findViewById(R.id.gps_img);
        gps_text = findViewById(R.id.gps_text);


         mapFragment = (MapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this); //네이버 맵 객체 가지고 오기 -> 콜백 onMapReady
        parent = this;




        getGPSLevel();

        MApplication.getProductInstance().getBattery().setStateCallback(new BatteryState.Callback() {
            @Override
            public void onUpdate(BatteryState batteryState) {

                int percent = batteryState.getChargeRemainingInPercent();
                final int idx = percent/25;
                batter_percent = Integer.toString(percent)+"%";

                battery_text.post(new Runnable() {
                    @Override
                    public void run() {
                        battery_text.setText(batter_percent);
                        battery_img.setImageResource(battery_imgs[idx]);

                    }
                });


            }
        });

    }

    public void getGPSLevel(){

        BaseProduct product = MApplication.getProductInstance();
        if (product != null && product.isConnected()) {
            if (product instanceof Aircraft) {
                mFlightController = ((Aircraft) product).getFlightController();
            }
        }
        if(mFlightController != null){
            mFlightController.setStateCallback(new FlightControllerState.Callback() {
                @Override
                public void onUpdate(@NonNull FlightControllerState flightControllerState) {
                    gpsLevl = flightControllerState.getGPSSignalLevel();
                    final int idx = gpsLevl.value();

                    gps_img.post(new Runnable() {
                        @Override
                        public void run() {

//                            Toast.makeText(MApplication.getInstance(), "gpsLevel : "+gpsLevl.value(), Toast.LENGTH_SHORT).show();


                            gps_text.setText(Integer.toString(idx));
                            if(idx == 0 || idx ==255){
                                gps_img.setImageResource(gps_imgs[0]);
                            }else if(idx ==1 || idx == 2){
                                gps_img.setImageResource(gps_imgs[1]);
                            }else{
                                gps_img.setImageResource(gps_imgs[idx]);
                            }
                        }
                    });


                }
            });
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        nMap = naverMap;
        nMap.setLocationSource(locationSource);
        nMap.setLocationTrackingMode(LocationTrackingMode.Follow);



        setFlightControllerCallback();

    }

    private void setFlightControllerCallback() {

        BaseProduct product = MApplication.getProductInstance();
        if (product != null && product.isConnected()) {
            if (product instanceof Aircraft) {
                mFlightController = ((Aircraft) product).getFlightController();
            }
        }

        if (mFlightController != null) {
            mFlightController.setStateCallback(new FlightControllerState.Callback() {

                @Override
                public void onUpdate(FlightControllerState djiFlightControllerCurrentState) {
                    droneLocationLat = djiFlightControllerCurrentState.getAircraftLocation().getLatitude();
                    droneLocationLng = djiFlightControllerCurrentState.getAircraftLocation().getLongitude();


                    Toast.makeText(MApplication.getInstance(), "getLocation1111 : lat:"+ Double.toString(droneLocationLat)+", lng : "+Double.toString(droneLocationLng), Toast.LENGTH_SHORT).show();

                    new Thread(){
                        @Override
                        public void run() {
                            try{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        OverlayImage image = OverlayImage.fromResource(R.drawable.aircraft);
                                        aircraft.setIcon(image);
                                        aircraft.setWidth(120);
                                        aircraft.setHeight(120);
                                        aircraft.setAnchor(new PointF(0.5f, 0.5f));
                                        aircraft.setPosition(new LatLng(37.485426, 126.968357));
                                        aircraft.setMap(nMap);
                                        mapFragment.getMapAsync(parent);

                                    }
                                });
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }.start();

                }
            });
        }
    }

//    public void drawLocation(){
//
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                if(nMap == null) {
//                    return;
//                }
//
//                aircraft.setPosition(new LatLng(37.485426, 126.968357));
//                aircraft.setMap(nMap);
//
//                Toast.makeText(MApplication.getInstance(), "getLocation 2222: lat:"+ Double.toString(droneLocationLat)+", lng : "+Double.toString(droneLocationLng), Toast.LENGTH_SHORT).show();
//
//            }
//        });
//    }





//    public static boolean checkGpsCoordination(double latitude, double longitude) {
//        return (latitude > -90 && latitude < 90 && longitude > -180 && longitude < 180) && (latitude != 0f && longitude != 0f);
//    }
}