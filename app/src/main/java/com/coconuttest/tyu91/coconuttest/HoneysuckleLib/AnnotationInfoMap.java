package com.coconuttest.tyu91.coconuttest.HoneysuckleLib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AnnotationInfoMap {
    public final Map<String, AnnotationInfo> annotationInfoHashMap = new HashMap<>();
    public final Map<String, Integer> IDToNotificationIDMap = new HashMap<>();

    public AnnotationInfo getAnnotationInfoByID (String ID) {
        return annotationInfoHashMap.get(ID);
    }

    public AnnotationInfo [] getAnnotationInfoListByDataGroup(PersonalDataGroup dataGroup) {
        ArrayList<AnnotationInfo> annotationInfoArrayList = new ArrayList<>();
        for (Map.Entry<String, AnnotationInfo> item : annotationInfoHashMap.entrySet()) {
            if (item.getValue().dataGroup == dataGroup) {
                annotationInfoArrayList.add(item.getValue());
            }
        }
        return annotationInfoArrayList.toArray(new AnnotationInfo[0]);
    }

    public int getNotificationIDByID (String ID) {
        return this.IDToNotificationIDMap.get(ID);
    }

}
