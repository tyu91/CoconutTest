package com.coconuttest.tyu91.coconuttest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.coconuttest.tyu91.coconuttest.HoneysuckleLib.HSStatus;
import com.coconuttest.tyu91.coconuttest.HoneysuckleGenerated.*;
import com.coconuttest.tyu91.coconuttest.HoneysuckleLib.PrivacyNoticeCenterActivity;

public class MainActivity extends AppCompatActivity {

    Button smsBtn, PNCBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HSStatus.setAnnotationInfoMap(new MyAnnotationInfoMap());
        HSStatus.setApplicationContext(getApplicationContext());
        //go to sms test activity
        smsBtn = findViewById(R.id.smsBtn);
        smsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SmsTestActivity.class);
                startActivity(intent);
            }
        });


        PNCBtn = findViewById(R.id.PNCBtn);
        PNCBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PrivacyNoticeCenterActivity.class);
                startActivity(intent);
            }
        });
    }
}
