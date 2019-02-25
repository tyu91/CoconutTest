package com.coconuttest.tyu91.coconuttest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.opengl.Visibility;
import android.provider.CalendarContract;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
}
