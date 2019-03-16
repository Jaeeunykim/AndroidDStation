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
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;

import dji.common.battery.BatteryState;
import dji.common.flightcontroller.FlightControllerState;
import dji.sdk.base.BaseProduct;
import dji.sdk.flightcontroller.FlightController;
import dji.sdk.products.Aircraft;


public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    NaverMap nMap;
    double droneLocationLng;
    double droneLocationLat;
    Marker aircraft;
    MapActivity parent;
    MapFragment mapFragment;
    private FlightController mFlightController;
    private String batter_percent;
    TextView battery_text;
    ImageView battery_img;

    private int[] battery_imgs={
            R.drawable.battery_white0,
            R.drawable.battery_white1,
            R.drawable.battery_white2,
            R.drawable.battery_white3,
            R.drawable.battery_white4,

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        battery_text = findViewById(R.id.battery_text);
        battery_img = findViewById(R.id.battery_img);


         mapFragment = (MapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);
        //parent = this;

        //setFlightControllerCallback();

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






    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        nMap = naverMap;
//        if(aircraft != null){
//            drawAircraft();
//        }
//        aircraft.setMap(nMap);
        Toast.makeText(MApplication.getInstance(), "onMapReady", Toast.LENGTH_SHORT).show();
    }

    private void setFlightControllerCallback() {

        Toast.makeText(MApplication.getInstance(), "setFlightController", Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(MApplication.getInstance(), "getLocation", Toast.LENGTH_SHORT).show();
                    aircraft = new Marker();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mapFragment.getMapAsync(parent);
                        }
                    });

                }
            });
        }
    }


//
//    public void drawAircraft() {
//
////            aircraft = new Marker();
//        aircraft.setPosition(new LatLng(droneLocationLng, droneLocationLat));
//            OverlayImage image = OverlayImage.fromResource(R.drawable.aircraft);
//            //Create MarkerOptions object
//
//            aircraft.setIcon(image);
//            aircraft.setWidth(85);
//            aircraft.setHeight(85);
//            aircraft.setAnchor(new PointF(0.5f, 0.5f));
//            aircraft.setMap(nMap);
//
//    }
//
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                mapFragment.getMapAsync(parent);
////
////                if (aircraft != null) {
////                    aircraft.setMap(null);
////                }
////
////                if (checkGpsCoordination(droneLocationLat, droneLocationLat)) {
////                    aircraft.setPosition(new LatLng(droneLocationLat, droneLocationLng));
////                    aircraft.setMap(nMap);
////                    mapFragment.getMapAsync(parent);
////                    Toast.makeText(MApplication.getInstance(), "drawmap", Toast.LENGTH_SHORT).show();
//
//                }
//            }
//        });


    public static boolean checkGpsCoordination(double latitude, double longitude) {
        return (latitude > -90 && latitude < 90 && longitude > -180 && longitude < 180) && (latitude != 0f && longitude != 0f);
    }
}