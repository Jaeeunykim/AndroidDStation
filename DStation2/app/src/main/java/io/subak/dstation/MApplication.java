package io.subak.dstation;

import android.app.Application;
import android.content.Context;

import com.secneo.sdk.Helper;

public class MApplication  extends Application {
//전역변수로 사용 한다 : 액티비티가 내려가면 전역변수로 사용되어야
    //앱에 대한 정보들을 다 정리해둠 : 메모리에 최상에 떠있어야 액티비, 그자체에 대한 정보
   @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Helper.install(MApplication.this);
    }
}
