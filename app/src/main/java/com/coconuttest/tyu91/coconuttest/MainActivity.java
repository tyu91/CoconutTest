package com.coconuttest.tyu91.coconuttest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    //TODO: add back buttons for each test activity back to this main activity
    //TODO: app crashes initially if permissions not enabled. This behavior is expected; for now let the app crash, give permission, and reopen app.

    Button calendarBtn;
    Button contactsBtn;
    Button smsBtn;
    Button callLogsBtn;
    Button mediaRecorderBtn;
    Button audioRecordBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //go to calendar test activity
        calendarBtn = findViewById(R.id.calendarBtn);
        calendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calendarActivityIntent = new Intent(MainActivity.this, CalendarTestActivity.class);
                startActivity(calendarActivityIntent);
                finish();
            }
        });

        //go to contacts test activity
        contactsBtn = findViewById(R.id.contactsBtn);
        contactsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contactsActivityIntent = new Intent(MainActivity.this, ContactsTestActivity.class);
                startActivity(contactsActivityIntent);
                finish();
            }
        });

        //go to sms test activity
        smsBtn = findViewById(R.id.smsBtn);
        smsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent smsActivityIntent = new Intent(MainActivity.this, SmsTestActivity.class);
                startActivity(smsActivityIntent);
                finish();
            }
        });

        //go to call logs test activity
        callLogsBtn = findViewById(R.id.callLogsBtn);
        callLogsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callLogsActivityIntent = new Intent(MainActivity.this, CallLogsTestActivity.class);
                startActivity(callLogsActivityIntent);
                finish();
            }
        });

        //go to media recorder test activity
        mediaRecorderBtn = findViewById(R.id.mediaRecorderBtn);
        mediaRecorderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mediaRecorderActivityIntent = new Intent(MainActivity.this, MicrophoneTestActivity.class);
                startActivity(mediaRecorderActivityIntent);
                finish();
            }
        });
    }
}
