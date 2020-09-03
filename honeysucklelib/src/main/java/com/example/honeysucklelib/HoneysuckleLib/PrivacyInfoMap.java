package com.example.honeysucklelib.HoneysuckleLib;

import java.util.ArrayList;
import java.util.Map;

public class PrivacyInfoMap {
    public Map<String, PrivacyInfo> privacyInfoMap;
    public Map<String, Integer> IDToNotificationIDMap;

    public PrivacyInfoMap(){}
    public PrivacyInfoMap(PrivacyInfoMap privacyInfoMap) {
        this.privacyInfoMap = privacyInfoMap.privacyInfoMap;
        IDToNotificationIDMap = privacyInfoMap.IDToNotificationIDMap;
    }
    public PrivacyInfo getPrivacyInfoByID(String ID) {
        return privacyInfoMap.get(ID);
    }

    public SourcePrivacyInfo[] getSourcePrivacyInfoListByDataGroup(PersonalDataGroup dataGroup) {
        ArrayList<SourcePrivacyInfo> privacyInfoArrayList = new ArrayList<>();
        for (Map.Entry<String, PrivacyInfo> item : privacyInfoMap.entrySet()) {
            if (item.getValue() instanceof SourcePrivacyInfo) {
                SourcePrivacyInfo sourcePrivacyInfo = (SourcePrivacyInfo) item.getValue();
                if (sourcePrivacyInfo.dataGroup == dataGroup) {
                    privacyInfoArrayList.add((SourcePrivacyInfo) item.getValue());
                }
            }
        }
        return privacyInfoArrayList.toArray(new SourcePrivacyInfo[0]);
    }

    public PersonalDataGroup [] getAccessedDataGroups() {
        ArrayList<PersonalDataGroup> accessedDataGroupArrayList = new ArrayList<>();
        for (PrivacyInfo privacyInfo : privacyInfoMap.values()) {
            if (privacyInfo instanceof SourcePrivacyInfo) {
                SourcePrivacyInfo sourcePrivacyInfo = (SourcePrivacyInfo) privacyInfo;
                if (!accessedDataGroupArrayList.contains(sourcePrivacyInfo.dataGroup)) {
                    accessedDataGroupArrayList.add(sourcePrivacyInfo.dataGroup);
                }
            }
        }
        return accessedDataGroupArrayList.toArray(new PersonalDataGroup[0]);
    }
}
