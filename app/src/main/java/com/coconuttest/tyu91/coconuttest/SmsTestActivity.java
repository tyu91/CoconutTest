package com.coconuttest.tyu91.coconuttest;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.Telephony;
import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.coconuttest.tyu91.coconuttest.HoneysuckleLib.*;

import org.json.JSONObject;

import java.util.ArrayList;

import me.tianshili.annotationlib.network.NetworkAnnotation;
import me.tianshili.annotationlib.sms.SMSSource;
import me.tianshili.annotationlib.sms.SMSSink;

public class SmsTestActivity extends AppCompatActivity {

    private int MY_PERMISSIONS_REQUEST_READ_SMS = 1;
    private ArrayList<String> smsResults = new ArrayList<>();

    private RequestQueue mRequestQueue;
    private String currentWeatherURL = "";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_test);

        //set up SMS recycler view and adapter


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
            final Activity currentActivity = this; PermissionNotice.showDialog(HSStatus.getApplicationContext(), PersonalDataGroup.SMS, new DialogInterface.OnCancelListener() {@Override public void onCancel(DialogInterface dialog) { ActivityCompat.requestPermissions(currentActivity,
                            new String[]{Manifest.permission.READ_SMS},
                            MY_PERMISSIONS_REQUEST_READ_SMS); }});
            /*
             * HS generated code ends
             */
            return;
        }

        //define projection query field
        String[] projectionSms = new String[]{
                Telephony.TextBasedSmsColumns.CREATOR,
                Telephony.TextBasedSmsColumns.BODY,
        };

        @SMSSource(
                ID = "SMSSource-0",
                purposes = {"Read the verification code automatically for two-factor authentication"})
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

        @NetworkAnnotation(
                destinations = {"the app server"})
        @SMSSink(
                IDs = {"SMSSource-0"},
                purposes = {"Send the verification code to the server to check the validity"},
                dataTypes = {"the verification code"})
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
    }

}
