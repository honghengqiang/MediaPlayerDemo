package com.hhq.mediaplayerdemo;

import android.app.Application;

/**
 * Created by Administrator on 2017/11/1.
 * @author  hhq
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //LeakCanary.install(this);
        CrashHandler.getInstance().init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
