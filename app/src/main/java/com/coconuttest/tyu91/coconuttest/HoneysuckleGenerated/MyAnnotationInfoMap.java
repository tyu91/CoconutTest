package com.coconuttest.tyu91.coconuttest.HoneysuckleGenerated;

import com.coconuttest.tyu91.coconuttest.HoneysuckleLib.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import static com.coconuttest.tyu91.coconuttest.HoneysuckleLib.AccessType.*;
public class MyAnnotationInfoMap extends AnnotationInfoMap {

    public final Map<String, AnnotationInfo> annotationInfoHashMap =
            Collections.unmodifiableMap(
            new HashMap<String, AnnotationInfo>() {{
put("LocationSource-0", new AnnotationInfo(
"LocationSource-0",PersonalDataGroup.Location,new String[]{"Not specified by developer"},new String[]{"Not specified by developer"},new String[]{"Not specified by developer"},true,RECURRING));            }}
    );
    public final Map<String, Integer> IDToNotificationIDMap = Collections.unmodifiableMap(
            new HashMap<String, Integer>() {{
put("LocationSource-0", 1000);            }}
    );
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
    public PersonalDataGroup [] getAccessedDataGroups() {
        ArrayList<PersonalDataGroup> accessedDataGroupArrayList = new ArrayList<>();
        for (AnnotationInfo annotationInfo : annotationInfoHashMap.values()) {
            if (!accessedDataGroupArrayList.contains(annotationInfo.dataGroup)) {
                accessedDataGroupArrayList.add(annotationInfo.dataGroup);
            }
        }
        return accessedDataGroupArrayList.toArray(new PersonalDataGroup[0]);
    }
}