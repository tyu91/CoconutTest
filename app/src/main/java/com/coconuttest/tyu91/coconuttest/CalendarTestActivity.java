package com.coconuttest.tyu91.coconuttest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import me.tianshili.annotationlib.calendar.CalendarAnnotation;
import me.tianshili.annotationlib.calendar.CalendarDataType;
import me.tianshili.annotationlib.calendar.CalendarPurpose;
import me.tianshili.annotationlib.commons.*;

public class CalendarTestActivity extends AppCompatActivity {

    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private String[] projection;
    private String selection;
    private String androidID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_test);

//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.READ_CONTACTS},
//                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
//        }
//
//        String displayName = CalendarContract.Calendars.CALENDAR_DISPLAY_NAME;
//
//        projection = new String[]
//                {CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
//                        CalendarContract.Calendars.OWNER_ACCOUNT,
//                        CalendarContract.Events.TITLE,
//                        CalendarContract.Events.DESCRIPTION
//                };
//
//        String[] projectionPrime = new String[]
//                {CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
//                        CalendarContract.Calendars.OWNER_ACCOUNT,
//                        CalendarContract.Attendees.ATTENDEE_RELATIONSHIP,
//                };
//
//        String[] projection1 = projection;
//        String[] projection1Prime = projectionPrime;
//
////        projection1 = new String[]{CalendarContract.Calendars.CALENDAR_DISPLAY_NAME};
//
//        String[] projection2 = projection1;
//        String[] projection2Prime = projection1Prime;
//
//        String[] projection3 = projection2;
//        String[] projection3Prime = projection2Prime;
//
//        //TODO: recursively resolve arguments to query string (do this in QueryUtil of privacyhelperplugin)
//        String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
//                + CalendarContract.Attendees.ATTENDEE_EMAIL + " = ?) AND ("
//                + CalendarContract.Events.TITLE + " = ?) AND ("
//                + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?))";
//        String selectionPrime = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
//                + CalendarContract.Attendees.ATTENDEE_EMAIL + " = ?) AND ("
//                + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?))";
//
//        String selection1 = selection;
//        String selection1Prime = selectionPrime;
//
//        String selection2 = selection1;
//        String selection2Prime = selection1Prime;
//
//        String selection3 = selection2;
//        String selection3Prime = selection2Prime;
//
//        String[] selectionArgs = new String[]{"hera@example.com", "com.example",
//                "hera@example.com"};
//        @CalendarAnnotation(
//                purpose = {CalendarPurpose.schedule},
//                purposeDescription = {"Dummy variable to test"},
//                dataType = {CalendarDataType.CALENDAR_DISPLAY_NAME, CalendarDataType.CALENDAR_OWNER, CalendarDataType.EVENT_TITLE, CalendarDataType.EVENT_DESCRIPTION},
//                visibility = {Visibility.UNKNOWN})
//        Cursor c;
//
//        c = this.getBaseContext().getContentResolver().query(CalendarContract.CONTENT_URI, projection3, selection3, null, null);
//
//        @CalendarAnnotation(
//                purpose = {CalendarPurpose.schedule},
//                purposeDescription = {"Dummy variable to test"},
//                dataType = {CalendarDataType.CALENDAR_DISPLAY_NAME, CalendarDataType.CALENDAR_OWNER, CalendarDataType.ATTENDEE_RELATIONSHIP},
//                visibility = {Visibility.UNKNOWN})
//        Cursor d;
//
//        d = this.getBaseContext().getContentResolver().query(CalendarContract.CONTENT_URI, projection3Prime, selection3Prime, null, null);
////
//
//
//        projection = new String[]
//                {CalendarContract.Events.ORGANIZER,
//                        CalendarContract.Attendees.ATTENDEE_NAME
//                };
//
//        projectionPrime = new String[]
//                {CalendarContract.Events.ORGANIZER
//                };
//        String id = Settings.Secure.getString(getContentResolver(), androidID);
    }
}
