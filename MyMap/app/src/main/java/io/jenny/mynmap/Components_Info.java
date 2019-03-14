package io.jenny.mynmap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import dji.common.battery.BatteryState;
import dji.sdk.sdkmanager.DJISDKManager;

public class Components_Info extends AppCompatActivity {

    protected StringBuffer stringBuffer;
    protected TextView battery_info_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MApplication.getEventBus().register(this);
        setContentView(R.layout.activity_components__info);
        stringBuffer = new StringBuffer();
        battery_info_text = (TextView) findViewById(R.id.battery_info_text);


        MApplication.getProductInstance().getBattery().setStateCallback(new BatteryState.Callback() {
            @Override
            public void onUpdate(BatteryState batteryState) {
                stringBuffer.delete(0,stringBuffer.length());
                stringBuffer.append("배터리 잔량 : ").append(batteryState.getChargeRemainingInPercent()).append("%\n");
                stringBuffer.append("현재전압 : ").append(batteryState.getVoltage()).append("mV\n");
                stringBuffer.append("현재전류 :").append(batteryState.getCurrent()).append("mA\n");

                battery_info_text.post(new Runnable() {
                    @Override
                    public void run() {
                        battery_info_text.setText(stringBuffer.toString());
                    }
                });
            }
        });
    }
}
