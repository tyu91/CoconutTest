package com.coconuttest.tyu91.coconuttest;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.Telephony;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.honeysucklelib.HoneysuckleLib.HSStatus;
import com.example.honeysucklelib.HoneysuckleLib.PermissionNotice;
import com.example.honeysucklelib.HoneysuckleLib.PersonalDataGroup;

import org.json.JSONObject;

import java.util.ArrayList;

import me.tianshili.annotationlib.LocalOnly;
import me.tianshili.annotationlib.network.NetworkAnnotation;
import me.tianshili.annotationlib.sms.SMSSource;
import me.tianshili.annotationlib.sms.SMSSink;

import com.example.honeysucklelib.HoneysuckleLib.*;


public class SmsTestActivity extends AppCompatActivity {

    private int MY_PERMISSIONS_REQUEST_READ_SMS = 1;
    private RecyclerView rvSms;
    private SmsTestAdapter smsTestAdapter;
    private ArrayList<String> smsResults;

    private RequestQueue mRequestQueue;
    private String currentWeatherURL = "";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_test);

        //set up SMS recycler view and adapter
        smsResults = new ArrayList<String>();
        rvSms = findViewById(R.id.rvSms);
        smsTestAdapter = new SmsTestAdapter(smsResults);
        rvSms.setAdapter(smsTestAdapter);
        rvSms.setLayoutManager(new LinearLayoutManager(this));


        //set up SMS permission request
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {

            /*
             * HS commented code
             * ActivityCompat.requestPermissions(this,
             *       new String[]{Manifest.permission.READ_SMS},
             *       MY_PERMISSIONS_REQUEST_READ_SMS);
             */


            /*
             * HS generated code begins
             */
            final Activity currentActivity = this;
            PermissionNotice.showDialog(HSStatus.getApplicationContext(), PersonalDataGroup.SMS,
                    new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    ActivityCompat.requestPermissions(currentActivity,
                            new String[]{Manifest.permission.READ_SMS},
                            MY_PERMISSIONS_REQUEST_READ_SMS);
                }
            });
            /*
             * HS generated code ends
             */
            return;
        }

        //define projection query field
        String[] projectionSms = new String[]{
//                Telephony.BaseMmsColumns.SEEN,
//                Telephony.BaseMmsColumns.CONTENT_LOCATION,
//                Telephony.BaseMmsColumns.CREATOR,
//                Telephony.BaseMmsColumns.DATE,
//                Telephony.BaseMmsColumns.DATE_SENT,
//                Telephony.BaseMmsColumns.DELIVERY_TIME,
//                Telephony.BaseMmsColumns.MESSAGE_ID,
//                Telephony.BaseMmsColumns.READ,
//                Telephony.BaseMmsColumns.READ_STATUS,
//                Telephony.BaseMmsColumns.RESPONSE_STATUS,
//                Telephony.BaseMmsColumns.RESPONSE_TEXT,
//                Telephony.BaseMmsColumns.READ_REPORT,
//                Telephony.BaseMmsColumns.RETRIEVE_TEXT,
//                Telephony.BaseMmsColumns.RETRIEVE_TEXT_CHARSET,
//                Telephony.BaseMmsColumns.SUBJECT,
//                Telephony.BaseMmsColumns.SUBJECT_CHARSET,
//                Telephony.BaseMmsColumns.MESSAGE_BOX,
//
//                Telephony.CanonicalAddressesColumns.ADDRESS,
//
//                Telephony.Mms.SEEN,
//                Telephony.Mms.CONTENT_LOCATION,
//                Telephony.Mms.CREATOR,
//                Telephony.Mms.DATE,
//                Telephony.Mms.DATE_SENT,
//                Telephony.Mms.DELIVERY_TIME,
//                Telephony.Mms.MESSAGE_ID,
//                Telephony.Mms.READ,
//                Telephony.Mms.READ_STATUS,
//                Telephony.Mms.RESPONSE_STATUS,
//                Telephony.Mms.RESPONSE_TEXT,
//                Telephony.Mms.READ_REPORT,
//                Telephony.Mms.RETRIEVE_TEXT,
//                Telephony.Mms.RETRIEVE_TEXT_CHARSET,
//                Telephony.Mms.SUBJECT,
//                Telephony.Mms.SUBJECT_CHARSET,
//                Telephony.Mms.MESSAGE_BOX,
//
//                String.valueOf(Telephony.MmsSms.CONTENT_CONVERSATIONS_URI),
//                String.valueOf(Telephony.MmsSms.SEARCH_URI),
//                String.valueOf(Telephony.MmsSms.CONTENT_CONVERSATIONS_URI),
//                String.valueOf(Telephony.MmsSms.CONTENT_DRAFT_URI),
//                String.valueOf(Telephony.MmsSms.CONTENT_FILTER_BYPHONE_URI),
//                String.valueOf(Telephony.MmsSms.CONTENT_LOCKED_URI),
//                String.valueOf(Telephony.MmsSms.CONTENT_UNDELIVERED_URI),
//                String.valueOf(Telephony.MmsSms.CONTENT_URI),
//
//                Telephony.Sms.SEEN,
//                Telephony.Sms.CREATOR,
//                Telephony.Sms.DATE,
//                Telephony.Sms.DATE_SENT,
//                Telephony.Sms.READ,
//                Telephony.Sms.SUBJECT,
//
//                Telephony.TextBasedSmsColumns.SEEN,
                Telephony.TextBasedSmsColumns.CREATOR,
//                Telephony.TextBasedSmsColumns.DATE,
//                Telephony.TextBasedSmsColumns.DATE_SENT,
//                Telephony.TextBasedSmsColumns.READ,
//                Telephony.TextBasedSmsColumns.SUBJECT,
                Telephony.TextBasedSmsColumns.BODY,

//                Telephony.Threads.DATE,


        };

        @SMSSource(
                ID = "SMSSource-1",
                purposes = {"Check verification code"})
        Cursor cursor;

        //query for SMS results
        cursor = this.getBaseContext()
                .getContentResolver()
                .query(
                        Telephony.Sms.CONTENT_URI, projectionSms, null, null, null
                );

        //populate SMS recycler view with query results
        while (cursor.moveToNext()) {
            smsResults.add(cursor.getString(1));
        }
        cursor.close();

        StringBuilder smsResultsString = new StringBuilder();
        for (String smsResult: smsResults) {
            smsResultsString.append(smsResult);
        }

        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

        BasicNetwork network = new BasicNetwork(new HurlStack());

        mRequestQueue = new RequestQueue(cache, network);
        mRequestQueue.start();

        @SMSSink(
                IDs = {"SMSSource-1"},
                purposes = {"SMS data will be sent to the network because TODO"},
                dataTypes = {"Not specified by developer"})
        @LocalOnly(
                IDs = {})
        @NetworkAnnotation(
                destinations = {"Not specified by developer"})
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                currentWeatherURL + smsResultsString + "test",
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        mRequestQueue.add(request);

        //TODO: figure out a way to parse Uri.parse instead of CONTENT_URI
        @SMSSource(
                ID = "SMSSource-0",
                purposes = {"Not specified by developer"})
        Cursor uriCursor;
        uriCursor = this.getBaseContext().getContentResolver().query(Uri.parse("content://sms/inbox"), projectionSms, null, null, null);
    }


}
