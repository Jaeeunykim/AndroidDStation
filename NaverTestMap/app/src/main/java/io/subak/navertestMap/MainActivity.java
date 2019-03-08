package io.subak.navertestMap;

import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);


    }


    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {


        Marker marker = new Marker();
        marker.setPosition(new LatLng(37.485426, 126.968357));

        OverlayImage image = OverlayImage.fromResource(R.drawable.copter);
        marker.setIcon(image);
        marker.setWidth(150);
        marker.setHeight(150);
        marker.setAnchor(new PointF(0.5f, 0.5f));

        marker.setMap(naverMap);


    }
}
