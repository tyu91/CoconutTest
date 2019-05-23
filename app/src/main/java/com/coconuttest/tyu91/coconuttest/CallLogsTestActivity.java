package com.coconuttest.tyu91.coconuttest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import me.tianshili.annotationlib.calllogs.CallLogsAnnotation;
import me.tianshili.annotationlib.calllogs.CallLogsDataType;
import me.tianshili.annotationlib.calllogs.CallLogsPurpose;
import me.tianshili.annotationlib.commons.Visibility;

public class CallLogsTestActivity extends AppCompatActivity {
    private int MY_PERMISSIONS_REQUEST_READ_CALL_LOGS = 1;
    private RecyclerView rvCallLogs;
    private CallLogsTestAdapter callLogsTestAdapter;
    private ArrayList<String> callLogsResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_logs_test);

        callLogsResults = new ArrayList<String>();
        rvCallLogs = findViewById(R.id.rvCallLogs);
        callLogsTestAdapter = new CallLogsTestAdapter(callLogsResults);
        rvCallLogs.setAdapter(callLogsTestAdapter);
        rvCallLogs.setLayoutManager(new LinearLayoutManager(this));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CALL_LOG},
                    MY_PERMISSIONS_REQUEST_READ_CALL_LOGS);
        }

        String[] projectionCallLog = new String[]{
//                CallLog.Calls.getLastOutgoingCall(this),
                CallLog.Calls.CACHED_NAME,
//                CallLog.Calls.DATE,
//                CallLog.Calls.DURATION,
                CallLog.Calls.NUMBER,
//                CallLog.Calls.VOICEMAIL_URI,
//                CallLog.Calls.TRANSCRIPTION
        };

        @CallLogsAnnotation(
                purpose = {CallLogsPurpose.UNKNOWN},
                purposeDescription = {""},
                dataType = {CallLogsDataType.CACHED_DATA, CallLogsDataType.NUMBER},
                visibility = {Visibility.UNKNOWN})
        Cursor f;
        f = this.getBaseContext().getContentResolver().query(CallLog.Calls.CONTENT_URI, projectionCallLog, null, null, null);

        while (f.moveToNext()) {
            callLogsResults.add(f.getString(1));
        }
        f.close();
    }
}
