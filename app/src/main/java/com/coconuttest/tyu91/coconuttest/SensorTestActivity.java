package com.coconuttest.tyu91.coconuttest;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Tests Coconut's ability to annotate APIs related to sensor data
 *
 * @author Elijah Neundorfer 6/17/19
 * @version 6/18/19
 */
public class SensorTestActivity extends AppCompatActivity {

    //Manages all sensors
    private SensorManager sensorManager;

    //Will hold a reference to every sensor we need to access
    private Sensor[] sensors;

    //Handler necessary for some methods annotated by Coconut
    Handler handler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_test);

        //Requesting the BODY_SENSOR permission (not necessary for every sensor on this list)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BODY_SENSORS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.BODY_SENSORS},
                    1);
        }

        //Initializing our SensorManager
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        //Initializing and assigning the sensors we're concerned with
        sensors = new Sensor[26];
        sensors[0] = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensors[1] = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER_UNCALIBRATED);
        sensors[2] = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        sensors[3] = sensorManager.getDefaultSensor(Sensor.TYPE_DEVICE_PRIVATE_BASE);
        sensors[4] = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        sensors[5] = sensorManager.getDefaultSensor(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR);
        sensors[6] = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        sensors[7] = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensors[8] = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE_UNCALIBRATED);
        sensors[9] = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_BEAT);
        sensors[10] = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        sensors[11] = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensors[12] = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensors[13] = sensorManager.getDefaultSensor(Sensor.TYPE_LOW_LATENCY_OFFBODY_DETECT);
        sensors[14] = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensors[15] = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED);
        sensors[16] = sensorManager.getDefaultSensor(Sensor.TYPE_MOTION_DETECT);
        sensors[17] = sensorManager.getDefaultSensor(Sensor.TYPE_POSE_6DOF);
        sensors[18] = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        sensors[19] = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sensors[20] = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        sensors[21] = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        sensors[22] = sensorManager.getDefaultSensor(Sensor.TYPE_STATIONARY_DETECT);
        sensors[23] = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        sensors[24] = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        sensors[25] = sensorManager.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION);
    }

    //Listens for changes in our triggered sensors and tracks data
    TriggerEventListener triggerEventListener = new TriggerEventListener() {
        @Override
        public void onTrigger(TriggerEvent event) { //event is data we're annotating
            Log.d("Trigger Event Listener", "Sensor Triggered:" + event.sensor.getName());
            //Event variable contains the data we're looking at
        }
    };

    //Listens for changes in the sensors and tracks data
    SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) { //event is data we're annotating
            Log.d("Sensor Event Listener", "Sensor Changed:" + event.sensor.getName());
            //Event variable contains the data we're looking at
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            //Do work
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        //Iterates over our array of sensors and registers a listener for each
        for (int i = 0; i < sensors.length; i++) {
            //Ensures our device has the sensor available.
            if(sensors[i] != null) {
                Log.d("Sensors", "onResume: Sensor " + i + " is registering");
                //If the sensor is a trigger type sensor, we register a TriggerEventListener and continue
                if (sensors[i] == sensors[16] || sensors[i] == sensors[22] || sensors[i] == sensors[25]) {
                    sensorManager.requestTriggerSensor(triggerEventListener, sensors[i]);
                    continue;
                }
                //Since there are four versions of the registerListener method to annotate with Coconut, we test all four methods here
                int toUse = ThreadLocalRandom.current().nextInt(0, 4);
                switch (toUse) {
                    case 0:
                        sensorManager.registerListener(sensorEventListener,  sensors[i], sensorManager.SENSOR_DELAY_NORMAL);
                        break;
                    case 1:
                        sensorManager.registerListener(sensorEventListener, sensors[i], SensorManager.SENSOR_DELAY_NORMAL, 5);
                        break;
                    case 2:
                        sensorManager.registerListener(sensorEventListener, sensors[i], SensorManager.SENSOR_DELAY_NORMAL, handler);
                        break;
                    case 3:
                        sensorManager.registerListener(sensorEventListener, sensors[i], SensorManager.SENSOR_DELAY_NORMAL, 5, handler);
                        break;
                }
            } else {
                Log.d("Sensors", "onResume: Sensor " + i + " is null");
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Clearing out our listeners
        sensorManager.unregisterListener(sensorEventListener);
        sensorManager.cancelTriggerSensor(triggerEventListener, sensors[16]);
        sensorManager.cancelTriggerSensor(triggerEventListener, sensors[22]);
        sensorManager.cancelTriggerSensor(triggerEventListener, sensors[25]);
    }
}
