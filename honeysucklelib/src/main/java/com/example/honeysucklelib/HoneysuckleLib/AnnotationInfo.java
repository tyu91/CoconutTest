package com.example.honeysucklelib.HoneysuckleLib;

public class AnnotationInfo {
    public PersonalDataGroup dataGroup;
    String ID;
    String [] purposes;
    String [] destinations;
    String [] leakedDataTypes;
    AccessType accessType;
    JitNoticeFrequency jitNoticeFrequency;
    boolean enableAccessTracker;

    boolean dataLeaked;

    public AnnotationInfo(String ID, PersonalDataGroup dataGroup, String [] purposes, String [] destinations,
                   String [] leakedDataTypes, boolean dataLeaked, AccessType accessType,
                          JitNoticeFrequency jitNoticeFrequency, boolean enableAccessTracker) {
        this.ID = ID;
        this.dataGroup = dataGroup;
        this.purposes = purposes;
        this.destinations = destinations;
        this.leakedDataTypes = leakedDataTypes;
        this.dataLeaked = dataLeaked;
        this.accessType = accessType;
        this.jitNoticeFrequency = jitNoticeFrequency;
        this.enableAccessTracker = enableAccessTracker;
    }
}
