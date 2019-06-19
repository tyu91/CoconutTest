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

/**
 * Tests Coconut's ability to annotate APIs related to sensor data
 *
 * This activity simply activates all possible sensors and registers listeners.
 * 
 * It is not intended for actual use.
 *
 * @author Elijah Neundorfer 6/17/19
 * @version 6/19/19
 */
public class SensorTestActivity extends AppCompatActivity {

    //Manages all sensors
    private SensorManager sensorManager;

    //Variable for every sensor we need to access
    private Sensor accelerometerSensor;
    private Sensor accelerometerUncalibratedSensor;
    private Sensor ambientTemperatureSensor;
    private Sensor devicePrivateBaseSensor;
    private Sensor gameRotationVectorSensor;
    private Sensor geomagneticRotationVectorSensor;
    private Sensor gravitySensor;
    private Sensor gyroscopeSensor;
    private Sensor gyroscopeUncalibratedSensor;
    private Sensor heartBeatSensor;
    private Sensor heartRateSensor;
    private Sensor lightSensor;
    private Sensor linearAccelerationSensor;
    private Sensor lowLatencyOffBodyDetectionSensor;
    private Sensor magneticFieldSensor;
    private Sensor magneticFieldUncalibratedSensor;
    private Sensor motionDetectSensor;
    private Sensor pose6DOFSensor;
    private Sensor pressureSensor;
    private Sensor proximitySensor;
    private Sensor relativeHumiditySensor;
    private Sensor rotationVectorSensor;
    private Sensor significantMotionSensor;
    private Sensor stationaryDetectSensor;
    private Sensor stepCounterSensor;
    private Sensor stepDetectorSensor;

    //Handler necessary for some methods annotated by Coconut
    Handler handler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_test);

        //Requesting the BODY_SENSOR permission (needed for heart sensors and other body specific sensors)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BODY_SENSORS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.BODY_SENSORS},
                    1);
        }


        //Initializing our SensorManager
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        //Initializing and assigning the sensors we're concerned with
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        accelerometerUncalibratedSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER_UNCALIBRATED);
        ambientTemperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        devicePrivateBaseSensor = sensorManager.getDefaultSensor(Sensor.TYPE_DEVICE_PRIVATE_BASE);
        gameRotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        geomagneticRotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR);
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        gyroscopeUncalibratedSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE_UNCALIBRATED);
        heartBeatSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_BEAT);
        heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        linearAccelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        lowLatencyOffBodyDetectionSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LOW_LATENCY_OFFBODY_DETECT);
        magneticFieldSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        magneticFieldUncalibratedSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED);
        motionDetectSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MOTION_DETECT);
        pose6DOFSensor = sensorManager.getDefaultSensor(Sensor.TYPE_POSE_6DOF);
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        relativeHumiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        significantMotionSensor = sensorManager.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION);
        stationaryDetectSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STATIONARY_DETECT);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
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

        if(accelerometerSensor != null) sensorManager.registerListener(sensorEventListener,accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        if(accelerometerUncalibratedSensor != null) sensorManager.registerListener(sensorEventListener, accelerometerUncalibratedSensor, SensorManager.SENSOR_DELAY_NORMAL);
        if(ambientTemperatureSensor != null) sensorManager.registerListener(sensorEventListener, ambientTemperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        if(devicePrivateBaseSensor != null) sensorManager.registerListener(sensorEventListener, devicePrivateBaseSensor, SensorManager.SENSOR_DELAY_NORMAL);
        if(gameRotationVectorSensor != null) sensorManager.registerListener(sensorEventListener, gameRotationVectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
        if(geomagneticRotationVectorSensor != null) sensorManager.registerListener(sensorEventListener, geomagneticRotationVectorSensor, SensorManager.SENSOR_DELAY_NORMAL);

        if(gravitySensor != null) sensorManager.registerListener(sensorEventListener, gravitySensor, SensorManager.SENSOR_DELAY_NORMAL, 5);
        if(gyroscopeSensor != null) sensorManager.registerListener(sensorEventListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL,5);
        if(gyroscopeUncalibratedSensor != null) sensorManager.registerListener(sensorEventListener, gyroscopeUncalibratedSensor, SensorManager.SENSOR_DELAY_NORMAL, 5);
        //Ensuring we have permissions to use the heart rate sensor
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BODY_SENSORS) == PackageManager.PERMISSION_GRANTED) {
            if(heartBeatSensor != null) sensorManager.registerListener(sensorEventListener, heartBeatSensor, SensorManager.SENSOR_DELAY_NORMAL, 5);
            if(heartRateSensor != null) sensorManager.registerListener(sensorEventListener, heartRateSensor, SensorManager.SENSOR_DELAY_NORMAL, 5);
        }
        if(lightSensor != null) sensorManager.registerListener(sensorEventListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL, handler);
        if(linearAccelerationSensor != null) sensorManager.registerListener(sensorEventListener, linearAccelerationSensor, SensorManager.SENSOR_DELAY_NORMAL, handler);
        if(lowLatencyOffBodyDetectionSensor != null) sensorManager.registerListener(sensorEventListener, lowLatencyOffBodyDetectionSensor, SensorManager.SENSOR_DELAY_NORMAL, handler);
        if(magneticFieldSensor != null) sensorManager.registerListener(sensorEventListener, magneticFieldSensor, SensorManager.SENSOR_DELAY_NORMAL, handler);
        if(magneticFieldUncalibratedSensor != null) sensorManager.registerListener(sensorEventListener, magneticFieldUncalibratedSensor, SensorManager.SENSOR_DELAY_NORMAL, handler);

        if(pose6DOFSensor != null) sensorManager.registerListener(sensorEventListener, pose6DOFSensor, SensorManager.SENSOR_DELAY_NORMAL, 5, handler);
        if(pressureSensor != null) sensorManager.registerListener(sensorEventListener, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL, 5, handler);
        if(proximitySensor != null) sensorManager.registerListener(sensorEventListener, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL, 5, handler);
        if(relativeHumiditySensor != null) sensorManager.registerListener(sensorEventListener, relativeHumiditySensor, SensorManager.SENSOR_DELAY_NORMAL, 5, handler);
        if(rotationVectorSensor != null) sensorManager.registerListener(sensorEventListener, rotationVectorSensor, SensorManager.SENSOR_DELAY_NORMAL, 5, handler);
        if(stepCounterSensor != null) sensorManager.registerListener(sensorEventListener, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL, 5, handler);
        if(stepDetectorSensor != null) sensorManager.registerListener(sensorEventListener, stepDetectorSensor, SensorManager.SENSOR_DELAY_NORMAL, 5, handler);


        if(motionDetectSensor != null) sensorManager.requestTriggerSensor(triggerEventListener, motionDetectSensor);
        if(significantMotionSensor != null) sensorManager.requestTriggerSensor(triggerEventListener, significantMotionSensor);
        if(stationaryDetectSensor != null) sensorManager.requestTriggerSensor(triggerEventListener, stationaryDetectSensor);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Clearing out our listeners
        sensorManager.unregisterListener(sensorEventListener);
        sensorManager.cancelTriggerSensor(triggerEventListener, motionDetectSensor);
        sensorManager.cancelTriggerSensor(triggerEventListener, significantMotionSensor);
        sensorManager.cancelTriggerSensor(triggerEventListener, stationaryDetectSensor);
    }
}
