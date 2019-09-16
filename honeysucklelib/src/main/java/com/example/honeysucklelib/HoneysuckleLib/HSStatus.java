package com.example.honeysucklelib.HoneysuckleLib;

import android.content.Context;

public class HSStatus {
    static private Context myApplicationContext = null;
    static private AnnotationInfoMap myAnnotationInfoMap = null;

    public static void setApplicationContext(Context applicationContext) {
        myApplicationContext = applicationContext;
    }

    public static void setAnnotationInfoMap(AnnotationInfoMap annotationInfoMap) {
        myAnnotationInfoMap = annotationInfoMap;
    }

    public static Context getApplicationContext() {
        return myApplicationContext;
    }

    public static AnnotationInfoMap getMyAnnotationInfoMap() {
        return myAnnotationInfoMap;
    }
}
