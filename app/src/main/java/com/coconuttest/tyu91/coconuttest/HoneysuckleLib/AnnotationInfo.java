package com.coconuttest.tyu91.coconuttest.HoneysuckleLib;

public class AnnotationInfo {
    public PersonalDataGroup dataGroup;
    String ID;
    String [] purposes;
    String [] destinations;
    String [] leakedDataTypes;
    AccessType accessType;
    boolean dataLeaked;

    public AnnotationInfo(String ID, PersonalDataGroup dataGroup, String [] purposes, String [] destinations,
                   String [] leakedDataTypes, boolean dataLeaked, AccessType accessType) {
        this.ID = ID;
        this.dataGroup = dataGroup;
        this.purposes = purposes;
        this.destinations = destinations;
        this.leakedDataTypes = leakedDataTypes;
        this.dataLeaked = dataLeaked;
        this.accessType = accessType;
    }
}
