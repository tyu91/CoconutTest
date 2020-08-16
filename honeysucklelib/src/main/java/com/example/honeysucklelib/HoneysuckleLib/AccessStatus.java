package com.example.honeysucklelib.HoneysuckleLib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class AccessStatus {
    private static HashMap<Object, String> recordingObjectMap;
    private static AccessStatus accessStatus;
    private AccessStatus() {
        recordingObjectMap = new HashMap<>();
    }

    public static AccessStatus getInstance() {
        if (accessStatus == null) {
            accessStatus = new AccessStatus();
        }
        return accessStatus;
    }

    public void trackRecordingObject(Object recordingObject, String ID) {
        recordingObjectMap.put(recordingObject, ID);
    }

    public void releaseRecordingObject(Object recordingObject) {
        recordingObjectMap.remove(recordingObject);
    }

    public String findIDByRecordingObject (Object recordingObject) {
        if (recordingObjectMap.containsKey(recordingObject)) {
            return recordingObjectMap.get(recordingObject);
        } else {
            return null;
        }
    }
}
