package com.coconuttest.tyu91.coconuttest;

import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.IOException;

import static android.Manifest.permission;

public class MicrophoneTestActivity extends AppCompatActivity {

    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;

    boolean isRecording = false;
    boolean isPlaying = false;

    File mAudioFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_microphone_test);

        if (ActivityCompat.checkSelfPermission(this, permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{permission.RECORD_AUDIO},
                    1);
        }

        try {
            mAudioFile = createAudioFile(this, "demo");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static File createAudioFile(Context context, String audioName) throws IOException {
        File storageDir = context.getCacheDir();
        File audio = File.createTempFile(
                audioName,      /* prefix */
                ".3gp",  /* suffix */
                storageDir      /* directory */
        );
        return audio;
    }

    public void toggleRecording(View view) {
        if (!isRecording) {

            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setOutputFile(mAudioFile.getAbsolutePath());
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            try {
                mRecorder.prepare();
                mRecorder.start();
                isRecording = true;
            } catch (IllegalStateException e) {
                // start: it is called before prepare()
                // prepare: it is called after start() or before setOutputFormat()
                Log.e("Audio", "recording failed");
            } catch (IOException e) {
                // prepare() fails
                Log.e("Audio", "prepare() failed");
            }
        } else if(isRecording) {
            isRecording = false;
            stopRecording();
        }
    }

    public void stopRecording() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.reset();
            mRecorder.release();
            mRecorder = null;
        }
    }

    public void togglePlaying(View view) {
        if (!isPlaying) {
            mPlayer = new MediaPlayer();
            try {
                mPlayer.setDataSource(mAudioFile.getAbsolutePath());
                mPlayer.prepare();
                mPlayer.start();
            } catch (IOException e) {
                Log.e("Audio", "prepare() failed");
            }
            isPlaying = true;
        } else if(isPlaying) {
            isPlaying = false;
            stopPlaying();
        }
    }

    /**
     * Stops the media player, then destroys it.
     */
    public void stopPlaying() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }

    /**
     * Overrides the onPause method so that when the activity is left the player and recorder will always stop.
     */
    @Override
    protected void onPause() {
        if (mPlayer != null) stopPlaying();
        if (mRecorder != null) stopRecording();
        super.onPause();
    }

    /**
     * Overrides the onDestroy method so that when the activity is destroyed the player and recorder will always stop.
     */
    @Override
    public void onDestroy() {
        if (mPlayer != null) stopPlaying();
        if (mRecorder != null) stopRecording();
        super.onDestroy();
    }
}
