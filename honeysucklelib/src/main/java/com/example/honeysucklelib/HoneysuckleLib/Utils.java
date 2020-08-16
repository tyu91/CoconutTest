package com.example.honeysucklelib.HoneysuckleLib;

import android.content.Context;
import android.content.pm.ApplicationInfo;

public class Utils {
    public static String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }
}
