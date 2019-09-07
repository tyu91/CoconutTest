package com.coconuttest.tyu91.coconuttest.HoneysuckleLib;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;

public class AccessHistory {
    static HashMap<String, ArrayList<AccessRecord>> accessRecordArrayList;
    static AccessHistory accessHistory = null;

    AccessHistory() {
        accessRecordArrayList = new HashMap<>();
    }

    public static AccessHistory getInstance() {
        if (accessHistory == null) {
            accessHistory = new AccessHistory();
        }
        return accessHistory;
    }

    class AccessRecord {
        String ID;
        long beingTimestamp;
        long endTimestamp;
    }

    public void beginAccessRecord(Context context, String ID) {

    }

    public void endLastAccessRecord(String ID) {

    }
}
