package com.coconuttest.tyu91.coconuttest.HoneysuckleGenerated;

import com.coconuttest.tyu91.coconuttest.HoneysuckleLib.AnnotationInfo;
import com.coconuttest.tyu91.coconuttest.HoneysuckleLib.PersonalDataGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.coconuttest.tyu91.coconuttest.HoneysuckleLib.AccessType.ONE_TIME;

public class AnnotationInfoMap {

    private static final Map<String, AnnotationInfo> annotationInfoHashMap =
            Collections.unmodifiableMap(
            new HashMap<String, AnnotationInfo>() {{
                put("SMSSource-0", new AnnotationInfo(
                        "SMSSource-0",
                        PersonalDataGroup.SMS,
                        new String[]{"Not specified by developer"},
                        new String[]{},
                        new String[]{},
                        false, ONE_TIME));
                put("SMSSource-1", new AnnotationInfo(
                        "SMSSource-1",
                        PersonalDataGroup.SMS,
                        new String[]{"backup data backup data backup data backup data backup data backup data backup data", "quality control quality control quality control quality control quality control quality control quality control quality control"},
                        new String[]{"app server"},
                        new String[]{"anonymized messages"},
                        true, ONE_TIME));
            }}
    );


    public static final Map<String, Integer> IDToNotificationIDMap = Collections.unmodifiableMap(
            new HashMap<String, Integer>() {{
                put("SMSSource-0", 1000);
                put("SMSSource-1", 1001);
            }}
    );

    public static AnnotationInfo getAnnotationInfoByID (String ID) {
        return annotationInfoHashMap.get(ID);
    }

    public static AnnotationInfo [] getAnnotationInfoListByDataGroup(PersonalDataGroup dataGroup) {
        ArrayList<AnnotationInfo> annotationInfoArrayList = new ArrayList<>();
        for (Map.Entry<String, AnnotationInfo> item : annotationInfoHashMap.entrySet()) {
            if (item.getValue().dataGroup == dataGroup) {
                annotationInfoArrayList.add(item.getValue());
            }
        }
        return annotationInfoArrayList.toArray(new AnnotationInfo[0]);
    }

    public static int getNotificationIDByID (String ID) {
        return IDToNotificationIDMap.get(ID);
    }

}
