package com.coconuttest.tyu91.coconuttest.HoneysuckleLib;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccessHistory {
    static HashMap<String, ArrayList<AccessRecord>> accessRecordMap;
    static AccessHistory accessHistory = null;
    final long ONE_HOUR_TIME = 60 * 60 * 1000;

    AccessHistory() {
        accessRecordMap = new HashMap<>();
    }

    public static AccessHistory getInstance() {
        if (accessHistory == null) {
            accessHistory = new AccessHistory();
        }
        return accessHistory;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int getAccessTimesInLastHour(String ID) {
        return accessRecordMap.getOrDefault(ID, new ArrayList<AccessRecord>()).size();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<AccessRecord> getAccessRecordListByID(String ID) {
        return accessRecordMap.getOrDefault(ID, new ArrayList<AccessRecord>());
    }

    public class AccessRecord {
        long beingTimestamp = -1;
        long endTimestamp = -1;
        AccessRecord (long beingTimestamp) {
            this.beingTimestamp = beingTimestamp;
        }

        void setEndTimestamp(long endTimestamp) {
            this.endTimestamp = endTimestamp;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    synchronized
    public void beginAccessRecord(Context context, String ID) {
        // Only maintain at most the access history of the past hour in the memory
        while (!accessRecordMap.getOrDefault(ID, new ArrayList<AccessRecord>()).isEmpty() &&
                System.currentTimeMillis() - accessRecordMap.get(ID).get(0).beingTimestamp > ONE_HOUR_TIME) {
            accessRecordMap.get(ID).remove(0);
        }
        if (!accessRecordMap.containsKey(ID)) {
            accessRecordMap.put(ID, new ArrayList<AccessRecord>());
        }
        accessRecordMap.get(ID).add(new AccessRecord(System.currentTimeMillis()));
    }

    synchronized
    public void endLastAccessRecord(String ID) {
        if (!accessRecordMap.containsKey(ID) || accessRecordMap.get(ID).isEmpty()) {
            return;
        }
        accessRecordMap.get(ID).get(accessRecordMap.get(ID).size() - 1)
                .setEndTimestamp(System.currentTimeMillis());
    }
}
