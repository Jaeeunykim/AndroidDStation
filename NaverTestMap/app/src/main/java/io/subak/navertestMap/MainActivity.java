package io.subak.navertestMap;

import android.graphics.PointF;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    public Thread lt;
    double longitude, latitude;
    MapFragment mapFragment;
    MainActivity parent;
    Marker aircraft;
    NaverMap nMap;
    ImageView battery;
    TextView battery_text;

    private int[] battery_imgs = {
            R.drawable.battery_white0,
            R.drawable.battery_white1,
            R.drawable.battery_white2,
            R.drawable.battery_white3,
            R.drawable.battery_white4,

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        aircraft = new Marker();

        mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
        }

        longitude = 37.485426;
        latitude = 126.968357;

        mapFragment.getMapAsync(this);
//        parent = this;

        lt = new Thread()
        {
            @Override
            public void run()
            {
                //super.run();
                while(true) {

                    latitude += 0.001;
                    drawDrone2();
                    Log.e("called run()", "called");
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        lt.start();

        powerChange(100);
    }



    public void powerChange(int power) {

        battery= (ImageView)findViewById(R.id.battery_img);
        battery_text = (TextView)findViewById(R.id.battery_text);
        int idx = power/20;

        if(power == 100) {

            battery.setImageResource(battery_imgs[4]);
            battery_text.setText(power+"%");
        }else {

            battery.setImageResource(battery_imgs[idx]);
            battery_text.setText(power+"%");

        }
    }


    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {

        nMap = naverMap;
        Log.e("called onMapReady", "called");
            //Marker marker = new Marker(); --> aircraft




    }

    public void drawDrone2() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(nMap == null) { return ;}
//              mapFragment.getMapAsync(parent);
                OverlayImage image = OverlayImage.fromResource(R.drawable.copter);
                aircraft.setIcon(image);
                aircraft.setWidth(120);
                aircraft.setHeight(120);
                aircraft.setAnchor(new PointF(0.5f, 0.5f));
                aircraft.setPosition(new LatLng(longitude, latitude));
                aircraft.setMap(nMap);
            }
        });
    }

}
