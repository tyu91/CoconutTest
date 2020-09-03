package com.example.honeysucklelib.HoneysuckleLib;

public class SinkPrivacyInfo implements PrivacyInfo {
    public String dataGroup;
    String ID;
    String [] sourceIdsOfSink; // null if this entry is a source
    String [] purposes;
    AccessType accessType;
    JitNoticeFrequency jitNoticeFrequency;
    boolean enableAccessTracker;

    public SinkPrivacyInfo(String ID, String[] sourceIdsOfSink, String dataGroup, String [] purposes, AccessType accessType,
                             JitNoticeFrequency jitNoticeFrequency, boolean enableAccessTracker) {
        this.ID = ID;
        this.sourceIdsOfSink = sourceIdsOfSink;
        this.dataGroup = dataGroup;
        this.purposes = purposes;
        this.accessType = accessType;
        this.jitNoticeFrequency = jitNoticeFrequency;
        this.enableAccessTracker = enableAccessTracker;
    }
}
