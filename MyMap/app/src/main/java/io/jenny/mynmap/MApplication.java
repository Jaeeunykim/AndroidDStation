package io.jenny.mynmap;

import android.app.Application;
import android.content.Context;

import com.secneo.sdk.Helper;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import dji.sdk.base.BaseProduct;
import dji.sdk.sdkmanager.DJISDKManager;

public class MApplication extends Application {

    private static BaseProduct product;
    private static Application app = null;

    private static Bus bus = new Bus(ThreadEnforcer.ANY);





    public static synchronized BaseProduct getProductInstance() {
        product = DJISDKManager.getInstance().getProduct();
        return product;
    }

    public static Application getInstance() {
        return MApplication.app;
    }


    @Override
    protected void attachBaseContext(Context paramContext) {
        super.attachBaseContext(paramContext);
        Helper.install(MApplication.this);
        app = this;
    }

    public static Bus getEventBus() {
        return bus;
    }

}
