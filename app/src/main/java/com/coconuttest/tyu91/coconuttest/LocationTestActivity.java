package com.coconuttest.tyu91.coconuttest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import me.tianshili.annotationlib.location.LocationSource;
import com.coconuttest.tyu91.coconuttest.HoneysuckleGenerated.MyApplication;
import com.coconuttest.tyu91.coconuttest.HoneysuckleLib.AccessHistory;
import com.coconuttest.tyu91.coconuttest.HoneysuckleLib.NotificationUtils;
import com.coconuttest.tyu91.coconuttest.HoneysuckleLib.PermissionNotice;
import com.coconuttest.tyu91.coconuttest.HoneysuckleLib.PersonalDataGroup;
import com.coconuttest.tyu91.coconuttest.HoneysuckleLib.SharedDataLoggingUtils;

public class LocationTestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(@LocationSource(
                    ID = "LocationSource-0",
                    purposes = {"Not specified by developer"})
                                          Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });

//        locationManager.removeUpdates();
    }
}
