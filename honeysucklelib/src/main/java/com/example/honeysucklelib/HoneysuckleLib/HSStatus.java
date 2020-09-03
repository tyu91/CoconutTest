package com.example.honeysucklelib.HoneysuckleLib;

import android.content.Context;

public class HSStatus {
    static private Context myApplicationContext = null;
    static private PrivacyInfoMap myPrivacyInfoMap = null;
    static public boolean isAppInForeground = false;

    public static void setApplicationContext(Context applicationContext) {
        myApplicationContext = applicationContext;
    }

    public static void setPrivacyInfoMap(PrivacyInfoMap privacyInfoMap) {
        myPrivacyInfoMap = privacyInfoMap;
    }

    public static Context getApplicationContext() {
        return myApplicationContext;
    }

    public static PrivacyInfoMap getMyPrivacyInfoMap() {
        return myPrivacyInfoMap;
    }
}
