package io.subak.navertestMap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapView;

public class MainActivity extends NMapActivity {

    private NMapView mMapView;
    private final String CLIENT_ID="juqony0mqu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMapView = new NMapView(this);
        setContentView(mMapView);
        mMapView.setClientId(CLIENT_ID);

        mMapView.setClickable(true);
        mMapView.setEnabled(true);
        mMapView.setFocusable(true);
        mMapView.setFocusableInTouchMode(true);
        mMapView.requestFocus();



    }
}
