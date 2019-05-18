package com.coconuttest.tyu91.coconuttest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Build;

import java.util.ArrayList;

import me.tianshili.annotationlib.calllogs.CallLogsAnnotation;
import me.tianshili.annotationlib.calllogs.CallLogsDataType;
import me.tianshili.annotationlib.calllogs.CallLogsPurpose;
import me.tianshili.annotationlib.commons.Visibility;
import me.tianshili.annotationlib.contacts.ContactsAnnotation;
import me.tianshili.annotationlib.contacts.ContactsDataType;
import me.tianshili.annotationlib.contacts.ContactsPurpose;
import me.tianshili.annotationlib.sms.SMSAnnotation;
import me.tianshili.annotationlib.sms.*;

public class ContactsTestActivity extends AppCompatActivity {
    private final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private final int MY_PERMISSIONS_REQUEST_WRITE_CONTACTS = 2;
    private String[] projection;
    private String selection;
    private RecyclerView rvContacts;
    private ContactsTestAdapter contactsTestAdapter;
    private ArrayList<String> contactsResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_test);

        contactsResults = new ArrayList<String>();
        rvContacts = findViewById(R.id.rvContacts);
        contactsTestAdapter = new ContactsTestAdapter(contactsResults);
        rvContacts.setAdapter(contactsTestAdapter);
        rvContacts.setLayoutManager(new LinearLayoutManager(this));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_CONTACTS)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_CONTACTS},
                        MY_PERMISSIONS_REQUEST_WRITE_CONTACTS);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        }

        String[] projectionSms = new String[] {
                Telephony.BaseMmsColumns.SEEN,
                Telephony.BaseMmsColumns.CONTENT_LOCATION,
                Telephony.BaseMmsColumns.CREATOR,
                Telephony.BaseMmsColumns.DATE,
                Telephony.BaseMmsColumns.DATE_SENT,
                Telephony.BaseMmsColumns.DELIVERY_TIME,
                Telephony.BaseMmsColumns.MESSAGE_ID,
                Telephony.BaseMmsColumns.READ,
                Telephony.BaseMmsColumns.READ_STATUS,
                Telephony.BaseMmsColumns.RESPONSE_STATUS,
                Telephony.BaseMmsColumns.RESPONSE_TEXT,
                Telephony.BaseMmsColumns.READ_REPORT,
                Telephony.BaseMmsColumns.RETRIEVE_TEXT,
                Telephony.BaseMmsColumns.RETRIEVE_TEXT_CHARSET,
                Telephony.BaseMmsColumns.SUBJECT,
                Telephony.BaseMmsColumns.SUBJECT_CHARSET,
                Telephony.BaseMmsColumns.MESSAGE_BOX,

                Telephony.CanonicalAddressesColumns.ADDRESS,

                Telephony.Mms.SEEN,
                Telephony.Mms.CONTENT_LOCATION,
                Telephony.Mms.CREATOR,
                Telephony.Mms.DATE,
                Telephony.Mms.DATE_SENT,
                Telephony.Mms.DELIVERY_TIME,
                Telephony.Mms.MESSAGE_ID,
                Telephony.Mms.READ,
                Telephony.Mms.READ_STATUS,
                Telephony.Mms.RESPONSE_STATUS,
                Telephony.Mms.RESPONSE_TEXT,
                Telephony.Mms.READ_REPORT,
                Telephony.Mms.RETRIEVE_TEXT,
                Telephony.Mms.RETRIEVE_TEXT_CHARSET,
                Telephony.Mms.SUBJECT,
                Telephony.Mms.SUBJECT_CHARSET,
                Telephony.Mms.MESSAGE_BOX,

                String.valueOf(Telephony.MmsSms.CONTENT_CONVERSATIONS_URI),
                String.valueOf(Telephony.MmsSms.SEARCH_URI),
                String.valueOf(Telephony.MmsSms.CONTENT_CONVERSATIONS_URI),
                String.valueOf(Telephony.MmsSms.CONTENT_DRAFT_URI),
                String.valueOf(Telephony.MmsSms.CONTENT_FILTER_BYPHONE_URI),
                String.valueOf(Telephony.MmsSms.CONTENT_LOCKED_URI),
                String.valueOf(Telephony.MmsSms.CONTENT_UNDELIVERED_URI),
                String.valueOf(Telephony.MmsSms.CONTENT_URI),

                Telephony.Sms.SEEN,
                Telephony.Sms.CREATOR,
                Telephony.Sms.DATE,
                Telephony.Sms.DATE_SENT,
                Telephony.Sms.READ,
                Telephony.Sms.SUBJECT,

                Telephony.TextBasedSmsColumns.SEEN,
                Telephony.TextBasedSmsColumns.CREATOR,
                Telephony.TextBasedSmsColumns.DATE,
                Telephony.TextBasedSmsColumns.DATE_SENT,
                Telephony.TextBasedSmsColumns.READ,
                Telephony.TextBasedSmsColumns.SUBJECT,
                Telephony.TextBasedSmsColumns.BODY,

                Telephony.Threads.DATE,


        };

        String[] projection = new String[] {
//                ContactsContract.Data.RAW_CONTACT_ID,
//                ContactsContract.Data._ID,
//                ContactsContract.Data.LOOKUP_KEY,
//                ContactsContract.Data.NAME_RAW_CONTACT_ID,
//                ContactsContract.Data.PHOTO_FILE_ID,
//                ContactsContract.Data.PHOTO_ID,
//                ContactsContract.Data.PHOTO_THUMBNAIL_URI,
//                ContactsContract.Data.PHOTO_URI,
//                ContactsContract.Data.DISPLAY_NAME,
//                ContactsContract.Data.DISPLAY_NAME_ALTERNATIVE,
//                ContactsContract.Data.DISPLAY_NAME_PRIMARY,
//                ContactsContract.Data.DISPLAY_NAME_SOURCE,
//
//                ContactsContract.Contacts._ID,
//                ContactsContract.Contacts.LOOKUP_KEY,
//                ContactsContract.Contacts.NAME_RAW_CONTACT_ID,
//                ContactsContract.Contacts.PHOTO_FILE_ID,
//                ContactsContract.Contacts.PHOTO_ID,
//                ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,
//                ContactsContract.Contacts.PHOTO_URI,
                ContactsContract.Contacts.DISPLAY_NAME,
//                ContactsContract.Contacts.DISPLAY_NAME_ALTERNATIVE,
//                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
//                ContactsContract.Contacts.DISPLAY_NAME_SOURCE,
//
//
//                ContactsContract.Profile._ID,
//                ContactsContract.Profile.LOOKUP_KEY,
//                ContactsContract.Profile.NAME_RAW_CONTACT_ID,
//                ContactsContract.Profile.PHOTO_FILE_ID,
//                ContactsContract.Profile.PHOTO_ID,
//                ContactsContract.Profile.PHOTO_THUMBNAIL_URI,
//                ContactsContract.Profile.PHOTO_URI,
//               ContactsContract.Profile.DISPLAY_NAME,
//               ContactsContract.Profile.DISPLAY_NAME_ALTERNATIVE,
//                ContactsContract.Profile.DISPLAY_NAME_PRIMARY,
//                ContactsContract.Profile.DISPLAY_NAME_SOURCE,
//
//                ContactsContract.RawContacts._ID,
//                ContactsContract.RawContacts.ACCOUNT_NAME,
//                ContactsContract.RawContacts.DISPLAY_NAME_ALTERNATIVE,
//                ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY,
//                ContactsContract.RawContacts.DISPLAY_NAME_SOURCE,
//                ContactsContract.RawContacts.SOURCE_ID

        };

//        String[] projectionCallLog = new String[]{
//                CallLog.Calls.getLastOutgoingCall(this),
//                CallLog.Calls.CACHED_NAME,
//                CallLog.Calls.DATE,
//                CallLog.Calls.DURATION,
//                CallLog.Calls.NUMBER,
//                CallLog.Calls.VOICEMAIL_URI,
//                CallLog.Calls.TRANSCRIPTION
//        };



        @ContactsAnnotation(
                purpose = {ContactsPurpose.UNKNOWN},
                purposeDescription = {""},
                dataType = {ContactsDataType.DISPLAY_NAME},
                visibility = {Visibility.UNKNOWN})
        Cursor d;

        d = this.getBaseContext().getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,
                projection,
                null,
                null,
                null);
        while (d.moveToNext()) {
            contactsResults.add(d.getString(0));
        }
        d.close();


//        @SMSAnnotation(
//                purpose = {SMSPurpose.UNKNOWN},
//                purposeDescription = {""},
//                dataType = {SMSDataType.STATUS, SMSDataType.LOCATION, SMSDataType.CREATOR, SMSDataType.DATE_AND_TIME, SMSDataType.MESSAGES, SMSDataType.ADDRESS, SMSDataType.THREADS},
//                visibility = {Visibility.UNKNOWN})
//        Cursor e;
//
//        e = this.getBaseContext().getContentResolver().query(Uri.parse("content://sms/inbox"), projectionSms, null, null, null);
//
//        @CallLogsAnnotation(
//                purpose = {CallLogsPurpose.UNKNOWN},
//                purposeDescription = {""},
//                dataType = {CallLogsDataType.LAST_OUTGOING_CALL, CallLogsDataType.CACHED_DATA, CallLogsDataType.DATE, CallLogsDataType.DURATION, CallLogsDataType.VOICEMAIL_DATA},
//                visibility = {Visibility.UNKNOWN})
//        Cursor f;
//        f = this.getBaseContext().getContentResolver().query(CallLog.Calls.CONTENT_URI, projectionCallLog, null, null, null);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            case MY_PERMISSIONS_REQUEST_WRITE_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }


}


