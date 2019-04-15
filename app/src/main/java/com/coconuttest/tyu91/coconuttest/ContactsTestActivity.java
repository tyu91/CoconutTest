package com.coconuttest.tyu91.coconuttest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import me.tianshili.annotationlib.commons.Visibility;
import me.tianshili.annotationlib.contacts.ContactsAnnotation;
import me.tianshili.annotationlib.contacts.ContactsDataType;
import me.tianshili.annotationlib.contacts.ContactsPurpose;

public class ContactsTestActivity extends AppCompatActivity {
    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private int MY_PERMISSIONS_REQUEST_WRITE_CONTACTS = 2;
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


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CONTACTS}, MY_PERMISSIONS_REQUEST_WRITE_CONTACTS);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }

        String[] projection = new String[] {
                ContactsContract.Data.RAW_CONTACT_ID,
                ContactsContract.Data._ID,
                ContactsContract.Data.LOOKUP_KEY,
                ContactsContract.Data.NAME_RAW_CONTACT_ID,
                ContactsContract.Data.PHOTO_FILE_ID,
                ContactsContract.Data.PHOTO_ID,
                ContactsContract.Data.PHOTO_THUMBNAIL_URI,
                ContactsContract.Data.PHOTO_URI,
                ContactsContract.Data.DISPLAY_NAME,
                ContactsContract.Data.DISPLAY_NAME_ALTERNATIVE,
                ContactsContract.Data.DISPLAY_NAME_PRIMARY,
                ContactsContract.Data.DISPLAY_NAME_SOURCE,

                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.LOOKUP_KEY,
                ContactsContract.Contacts.NAME_RAW_CONTACT_ID,
                ContactsContract.Contacts.PHOTO_FILE_ID,
                ContactsContract.Contacts.PHOTO_ID,
                ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,
                ContactsContract.Contacts.PHOTO_URI,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.DISPLAY_NAME_ALTERNATIVE,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
                ContactsContract.Contacts.DISPLAY_NAME_SOURCE,


                ContactsContract.Profile._ID,
                ContactsContract.Profile.LOOKUP_KEY,
                ContactsContract.Profile.NAME_RAW_CONTACT_ID,
                ContactsContract.Profile.PHOTO_FILE_ID,
                ContactsContract.Profile.PHOTO_ID,
                ContactsContract.Profile.PHOTO_THUMBNAIL_URI,
                ContactsContract.Profile.PHOTO_URI,
                ContactsContract.Profile.DISPLAY_NAME,
                ContactsContract.Profile.DISPLAY_NAME_ALTERNATIVE,
                ContactsContract.Profile.DISPLAY_NAME_PRIMARY,
                ContactsContract.Profile.DISPLAY_NAME_SOURCE,

                ContactsContract.RawContacts._ID,
                ContactsContract.RawContacts.ACCOUNT_NAME,
                ContactsContract.RawContacts.DISPLAY_NAME_ALTERNATIVE,
                ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY,
                ContactsContract.RawContacts.DISPLAY_NAME_SOURCE,
                ContactsContract.RawContacts.SOURCE_ID

        };

        @ContactsAnnotation(
                purpose = {ContactsPurpose.UNKNOWN},
                purposeDescription = {"alkfja;sdlkfjas;ldkfja;sldkfjas;ldkfj;l"},
                dataType = {ContactsDataType.PHOTO},
                visibility = {Visibility.IN_BACKGROUND})
        Cursor d;

        d = this.getBaseContext().getContentResolver().query(
                ContactsContract.Profile.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }
}
