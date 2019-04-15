package com.coconuttest.tyu91.coconuttest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import me.tianshili.annotationlib.calendar.CalendarAnnotation;
import me.tianshili.annotationlib.calendar.CalendarDataType;
import me.tianshili.annotationlib.calendar.CalendarPurpose;
import me.tianshili.annotationlib.commons.Visibility;

public class CalendarTestActivity extends AppCompatActivity {
    private int MY_PERMISSIONS_REQUEST_READ_CALENDAR = 1;
    private int MY_PERMISSIONS_REQUEST_WRITE_CALENDAR = 2;
    private String[] projection;
    private String selection;
    private String androidID;
    private RecyclerView rvCalendar;
    private CalendarTestAdapter calendarTestAdapter;
    private ArrayList<String> calendarResults;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_test);

        calendarResults = new ArrayList<String>();
        rvCalendar = findViewById(R.id.rvCalendar);
        calendarTestAdapter = new CalendarTestAdapter(calendarResults);
        rvCalendar.setAdapter(calendarTestAdapter);
        rvCalendar.setLayoutManager(new LinearLayoutManager(this));


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR}, MY_PERMISSIONS_REQUEST_WRITE_CALENDAR);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CALENDAR},
                    MY_PERMISSIONS_REQUEST_READ_CALENDAR);
        }
        projection = new String[] { CalendarContract.Events.CALENDAR_DISPLAY_NAME,
        CalendarContract.Events.TITLE,
        CalendarContract.Events.EVENT_LOCATION,
        CalendarContract.Events.DTSTART,
        CalendarContract.Events.DTEND};

//        //TODO: recursively resolve arguments to query string (do this in QueryUtil of privacyhelperplugin)
        String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
                + CalendarContract.Attendees.ATTENDEE_EMAIL + " = ?) AND ("
                + CalendarContract.Events.TITLE + " = ?) AND ("
                + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?))";

        String selectionPrime = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
                + CalendarContract.Attendees.ATTENDEE_EMAIL + " = ?) AND ("
                + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?))";
        
        @CalendarAnnotation(
                purpose = {CalendarPurpose.UNKNOWN},
                purposeDescription = {""},
                dataType = {CalendarDataType.UNKNOWN},
                visibility = {Visibility.UNKNOWN})
        Cursor c;

        c = this.getBaseContext().getContentResolver().query(CalendarContract.Events.CONTENT_URI, projection, null, null, null);

        while (c.moveToNext()) {
            calendarResults.add(c.getString(1));
        }
        c.close();

    }

}
