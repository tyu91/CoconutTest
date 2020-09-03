package com.example.honeysucklelib.HoneysuckleLib;

public class SourcePrivacyInfo implements PrivacyInfo {
    public PersonalDataGroup dataGroup;
    String ID;
    String [] purposes;
    AccessType accessType;
    JitNoticeFrequency jitNoticeFrequency;
    boolean enableAccessTracker;

    public SourcePrivacyInfo(String ID, PersonalDataGroup dataGroup, String [] purposes, AccessType accessType,
                       JitNoticeFrequency jitNoticeFrequency, boolean enableAccessTracker) {
        this.ID = ID;
        this.dataGroup = dataGroup;
        this.purposes = purposes;
        this.accessType = accessType;
        this.jitNoticeFrequency = jitNoticeFrequency;
        this.enableAccessTracker = enableAccessTracker;
    }
}
