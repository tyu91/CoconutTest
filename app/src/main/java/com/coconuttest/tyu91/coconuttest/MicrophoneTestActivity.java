package com.coconuttest.tyu91.coconuttest;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ThreadLocalRandom;

import static android.Manifest.permission;


/**
 *
 * @author Elijah Neundorfer
 * @version 6/7/19
 *
 */
public class MicrophoneTestActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener, View.OnClickListener {

    private MediaRecorder mRecorder;
    private AudioRecord mAudioRecord;
    private Thread mAudioRecordThread;

    File mAudioFile;
    Uri mAudioFileURI;
    FileOutputStream fos;

    Button recordWithMediaRecorder, stopRecording, recordWithAudioRecord, recordWithIntent, play;

    public static final int RECORD_REQUEST = 0;
    private static final String TAG = "MicrophoneDevFeedback";
    private boolean threadActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_microphone_test);

        recordWithMediaRecorder = this.findViewById(R.id.recordWithMediaRecorderBtn);
        recordWithMediaRecorder.setOnClickListener(this);

        recordWithAudioRecord = this.findViewById(R.id.recordWithAudioRecordBtn);
        recordWithAudioRecord.setOnClickListener(this);

        recordWithIntent = this.findViewById(R.id.recordWithIntentBtn);
        recordWithIntent.setOnClickListener(this);

        stopRecording = this.findViewById(R.id.stopRecordingBtn);
        stopRecording.setOnClickListener(this);
        stopRecording.setEnabled(false);

        play = this.findViewById(R.id.playBtn);
        play.setOnClickListener(this);
        play.setEnabled(false);

        if (ActivityCompat.checkSelfPermission(this, permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{permission.RECORD_AUDIO},
                    1);
        }

        if (ActivityCompat.checkSelfPermission(this, permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }

    }

    public void onClick(View v) {
        if (v == recordWithMediaRecorder) {
            disableAllButtons();
            recordWithMediaRecorder();
            stopRecording.setEnabled(true);
        } else if (v == recordWithAudioRecord) {
            disableAllButtons();
            recordWithAudioRecord();
            stopRecording.setEnabled(true);
        } else if (v == recordWithIntent) {
            disableAllButtons();
            try {
                Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                startActivityForResult(intent, RECORD_REQUEST);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "No third party available for recording.", Toast.LENGTH_SHORT).show();
                enableAllButtonsButStop();
            }
        } else if (v == stopRecording) {
            stopRecording();
            enableAllButtonsButStop();

        } else if (v == play) {
            disableAllButtons();
            MediaPlayer mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(mAudioFile.getAbsolutePath());
                mediaPlayer.setOnCompletionListener(this);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException ioe) {
                Log.e("MediaPlayer", "prepare() failed");
            }
            /*
            try{
                MediaPlayer mediaPlayer = MediaPlayer.create(this, mAudioFileURI);
                mediaPlayer.setOnCompletionListener(this);
                mediaPlayer.start();
            } catch (NullPointerException e) {
                Log.e("MediaPlayer", "Null Pointer Exception" + e);
            }
            MediaPlayer mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(mAudioFile.getAbsolutePath());
                mediaPlayer.setOnCompletionListener(this);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException ioe) {
                Log.e("MediaPlayer", "prepare() failed");
            }
            */
            byte[] audioData;
            try {
                InputStream inputStream = new FileInputStream(mAudioFileURI.getPath());

                int minBufferSize = AudioTrack.getMinBufferSize(44100, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
                audioData = new byte[minBufferSize];

                AudioTrack audioTrack = new AudioTrack.Builder()
                        .setAudioAttributes(new AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .build())
                        .setAudioFormat(new AudioFormat.Builder()
                                .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                                .setSampleRate(44100)
                                .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)
                                .build())
                        .setBufferSizeInBytes(minBufferSize)
                        .build();
                audioTrack.play();
                int i;

                while ((i = inputStream.read(audioData)) != -1) {
                    audioTrack.write(audioData, 0, i);
                }
            } catch (FileNotFoundException fe) {
                Log.e("AudioTrack", "File not found");
            } catch (IOException io) {
                Log.e("AudioTrack", "IO Exception");
            }
            enableAllButtonsButStop();
        }
    }


    private void disableAllButtons() {
        recordWithMediaRecorder.setEnabled(false);
        recordWithAudioRecord.setEnabled(false);
        recordWithIntent.setEnabled(false);
        stopRecording.setEnabled(false);
        play.setEnabled(false);
    }

    private void enableAllButtonsButStop() {
        recordWithMediaRecorder.setEnabled(true);
        recordWithAudioRecord.setEnabled(true);
        recordWithIntent.setEnabled(true);
        stopRecording.setEnabled(false);
        play.setEnabled(true);
    }

    /**
     * Creates a blank audio file with a given extension.
     *
     * @param context   The current context, necessary for getting the directory.
     * @param audioName The desired name of the audio file
     * @param suffix    The desired extension of the audio file
     * @return An audio file
     */
    private static File createAudioFile(Context context, String audioName, String suffix) {
        File storageDir = context.getCacheDir();
        File audio = null;
        try {
            audio = File.createTempFile(
                    audioName,      /* prefix */
                    suffix,         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return audio;
    }

    /**
     * Creates an MediaRecorder object and records.
     */
    public void recordWithMediaRecorder() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mAudioFile = createAudioFile(this, "demo", ".3gp");
        //In order to test Coconut on all three versions of setOutputFile while maintaining working code, we simply use a random version.
        int toUse = ThreadLocalRandom.current().nextInt(0, 3);
        switch (toUse) {
            case 0:
                String mAudioFileString = mAudioFile.getAbsolutePath();
                mRecorder.setOutputFile(mAudioFileString);
                break;
            case 1:
                mRecorder.setOutputFile(mAudioFile);
                break;
            case 2:
                FileDescriptor fd;
                try {
                    fos = new FileOutputStream(mAudioFile);
                    fd = fos.getFD();
                    mRecorder.setOutputFile(fd);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Cannot Set File", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Cannot Set File", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (IllegalStateException e) {
            // start: it is called before prepare()
            // prepare: it is called after start() or before setOutputFormat()
            Log.e("Audio", "recording failed");
            Toast.makeText(this, "Cannot Record Audio", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            // prepare() fails
            Log.e("Audio", "prepare() failed");
            Toast.makeText(this, "Cannot Record Audio", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Creates an AudioRecord object and records.
     */
    public void recordWithAudioRecord() {
        mAudioFile = createAudioFile(this, "AudioRecord", ".pcm");
        if(mAudioFile == null) {
            Log.d(TAG,"ERROR - No File found to record to");
            return;
        }


        mAudioRecordThread = new Thread(new Runnable() {
            public void run() {

                int sampleRate = 44100;
                int channelConfig = AudioFormat.CHANNEL_IN_MONO;
                int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
                int bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
                mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, channelConfig, audioFormat, bufferSize);

                if (mAudioRecord.getState() != AudioRecord.STATE_INITIALIZED) {
                    Log.d("AudioRecord", "Unable to initialize AudioRecord");
                    throw new RuntimeException("Unable to initialize AudioRecord");
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    if (android.media.audiofx.NoiseSuppressor.isAvailable()) {
                        android.media.audiofx.NoiseSuppressor noiseSuppressor = android.media.audiofx.NoiseSuppressor.create(mAudioRecord.getAudioSessionId());
                        if (noiseSuppressor != null) {
                            noiseSuppressor.setEnabled(true);
                        }
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    if (android.media.audiofx.AutomaticGainControl.isAvailable()) {
                        android.media.audiofx.AutomaticGainControl automaticGainControl = android.media.audiofx.AutomaticGainControl.create(mAudioRecord.getAudioSessionId());
                        if (automaticGainControl != null) {
                            automaticGainControl.setEnabled(true);
                        }
                    }
                }

                mAudioRecord.startRecording();

                byte bData[] = new byte[bufferSize];

                FileOutputStream os = null;
                try {
                    os = new FileOutputStream(mAudioFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                while(threadActive) {

                    //In order to test Coconut on all versions of AudioRecord.read, we initialize all the necessary variables and then call the method with every possible parameter combination.
                    //If this is not commented out, however, the recording feature will not work.
            /*
            short[] bufferInShort = new short[bufferElements2Rec * bytesPerElement / 4];
            float[] bufferInFloat = new float[bufferElements2Rec * bytesPerElement / 4];
            byte[] bufferInBytes = new byte[bufferElements2Rec * bytesPerElement];
            ByteBuffer byteBuffer = ByteBuffer.allocate(bufferElements2Rec * bytesPerElement);

            mAudioRecord.read(bufferInShort, 0, bufferElements2Rec * bytesPerElement / 4, AudioRecord.READ_NON_BLOCKING);
            mAudioRecord.read(byteBuffer, bufferElements2Rec * bytesPerElement, AudioRecord.READ_NON_BLOCKING);
            mAudioRecord.read(bufferInShort, 0, bufferElements2Rec * bytesPerElement / 4);
            mAudioRecord.read(bufferInFloat, 0, bufferElements2Rec * bytesPerElement / 4, AudioRecord.READ_NON_BLOCKING);
            mAudioRecord.read(bufferInBytes, 0, bufferElements2Rec * bytesPerElement, AudioRecord.READ_NON_BLOCKING);
            mAudioRecord.read(byteBuffer, bufferElements2Rec * bytesPerElement);
            mAudioRecord.read(bufferInBytes, 0, bufferElements2Rec * bytesPerElement);
            */


                    mAudioRecord.read(sData, 0, bufferElements2Rec);
                    System.out.println("Short wirting to file" + sData.toString());
                    try {
                        byte bData[] = short2byte(sData);
                        os.write(bData, 0, bufferElements2Rec * bytesPerElement);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, "AudioRecorder Thread");
        threadActive = true;
        mAudioRecordThread.start();
    }

    //convert short to byte
    private byte[] short2byte(short[] sData) {
        int shortArrsize = sData.length;
        byte[] bytes = new byte[shortArrsize * 2];
        for (int i = 0; i < shortArrsize; i++) {
            bytes[i * 2] = (byte) (sData[i] & 0x00FF);
            bytes[(i * 2) + 1] = (byte) (sData[i] >> 8);
            sData[i] = 0;
        }
        return bytes;

    }

    /**
     * As we have multiple possible ways to record in this activity, the stopRecording method checks every associated variable with each and stops everything.
     */
    public void stopRecording() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.reset();
            mRecorder.release();
            mRecorder = null;
            mAudioFileURI = Uri.fromFile(mAudioFile);
        }
        if (fos != null) {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (mAudioRecord != null) {
            threadActive = false;
            mAudioRecord.stop();
            mAudioRecord.release();
            mAudioRecord = null;
            mAudioRecordThread = null;
            mAudioFileURI = Uri.fromFile(mAudioFile);
        }
    }

    /**
     * Overrides the onPause method so that when the activity is left the player and recorder will always stop.
     */
    @Override
    protected void onPause() {
        if (mRecorder != null || mAudioRecord != null) stopRecording();
        super.onPause();
    }

    /**
     * Overrides the onDestroy method so that when the activity is destroyed the player and recorder will always stop.
     */
    @Override
    public void onDestroy() {
        if (mRecorder != null || mAudioRecord != null) stopRecording();
        super.onDestroy();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
        enableAllButtonsButStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == RECORD_REQUEST) {
            mAudioFileURI = data.getData();
            enableAllButtonsButStop();
        }
    }
}