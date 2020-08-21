package com.example.honeysucklelib.HoneysuckleLib;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccessHistory {
    static HashMap<String, ArrayList<AccessRecord>> accessRecordMap;
    static AccessHistory accessHistory = null;

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
    public boolean isFirstTimeAccess(String ID) {
        // FIXME: use the data in local storage
        return getAccessTimesInLastHour(ID) == 1;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int getAccessTimesInLastHour(String ID) {
        return accessRecordMap.getOrDefault(ID, new ArrayList<AccessRecord>()).size();
    }

    public List<Float> getAccessCountersInLastWeek(AccessType accessType, String ID) {
        if (accessType == AccessType.ONE_TIME || accessType == AccessType.RECURRING) {
            return getAccessTimesInLastWeek(ID);
        } else {
            return getAccessMinutesInLastWeek(ID);
        }
    }

    private List<Float> getAccessMinutesInLastWeek(String ID) {
        ArrayList<Float> accessHoursInLastWeek =new ArrayList<>(Arrays.asList(new Float[7]));
        Collections.fill(accessHoursInLastWeek, 0f);
        long week_begin = System.currentTimeMillis() - DataAccessRecordListAdapter.ONE_DAY_TIME * 7;
        for (Map.Entry<String, ArrayList<AccessRecord>> entry : accessRecordMap.entrySet()) {
            if (entry.getKey() != ID) {
                continue;
            }
            for (AccessRecord record : entry.getValue()) {
                if (record.beginTimestamp != -1 && record.endTimestamp != -1) {
                    int index = (int) ((record.beginTimestamp - week_begin) / DataAccessRecordListAdapter.ONE_DAY_TIME);
                    accessHoursInLastWeek.set(index,
                            accessHoursInLastWeek.get(index) + (record.endTimestamp - record.beginTimestamp)/(HSUtils.ONE_MINUTE_TIME * 1f));
                }
            }
        }
        return accessHoursInLastWeek;
    }

    private List<Float> getAccessTimesInLastWeek(String ID) {
        ArrayList<Float> accessTimesInLastWeek = new ArrayList<>(Arrays.asList(new Float[7]));
        Collections.fill(accessTimesInLastWeek, 0f);
        long week_begin = System.currentTimeMillis() - DataAccessRecordListAdapter.ONE_DAY_TIME * 7;
        for (Map.Entry<String, ArrayList<AccessRecord>> entry : accessRecordMap.entrySet()) {
            if (entry.getKey() != ID) {
                continue;
            }
            for (AccessRecord record : entry.getValue()) {
                int index = (int) ((record.beginTimestamp - week_begin) / DataAccessRecordListAdapter.ONE_DAY_TIME);
                accessTimesInLastWeek.set(index,
                        accessTimesInLastWeek.get(index) + 1);
            }
        }
        return accessTimesInLastWeek;
    }
    public static Comparator<AccessRecord> AccessRecordComparator = (accessRecord1, accessRecord2) -> {
        //descending order
        return (int) (accessRecord1.endTimestamp - accessRecord2.endTimestamp);
    };

    public ArrayList<AccessRecord> getLatestAccessRecordList() {
        ArrayList<AccessRecord> accessRecordArrayList = new ArrayList<>();
        for (Map.Entry<String, ArrayList<AccessRecord>> entry : accessRecordMap.entrySet()) {
            accessRecordArrayList.addAll(entry.getValue());
            Collections.sort(accessRecordArrayList, AccessRecordComparator);
        }
        return accessRecordArrayList;
    }

    public ArrayList<AccessRecord> getAccessRecordListByID(String ID) {
        if (accessRecordMap.containsKey(ID)) {
            return accessRecordMap.get(ID);
        } else {
            return new ArrayList<>();
        }
    }

    public AccessRecord getMostRecentAccessRecord(String ID) {
        if (getAccessRecordListByID(ID).isEmpty()) {
            return null;
        } else {
            return getAccessRecordListByID(ID).get(getAccessRecordListByID(ID).size() - 1);
        }
    }


    public class AccessRecord {
        String ID;
        long beginTimestamp = -1;
        long endTimestamp = -1;
        AccessRecord (String ID, long beginTimestamp) {
            this.ID = ID;
            this.beginTimestamp = beginTimestamp;
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
                System.currentTimeMillis() - accessRecordMap.get(ID).get(0).beginTimestamp > HSUtils.ONE_MINUTE_TIME) {
            accessRecordMap.get(ID).remove(0);
        }
        if (!accessRecordMap.containsKey(ID)) {
            accessRecordMap.put(ID, new ArrayList<AccessRecord>());
        }
        accessRecordMap.get(ID).add(new AccessRecord(ID, System.currentTimeMillis()));
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
