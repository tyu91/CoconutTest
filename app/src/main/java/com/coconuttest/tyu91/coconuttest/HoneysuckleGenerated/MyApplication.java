package com.coconuttest.tyu91.coconuttest.HoneysuckleGenerated;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

    static private Application myApplication = null;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
    }

    static Application getInstance() {
        return myApplication;
    }

    public static Context getContext() {
        if (getInstance() != null) {
            return getInstance().getApplicationContext();
        } else {
            return null;
        }
    }
}
