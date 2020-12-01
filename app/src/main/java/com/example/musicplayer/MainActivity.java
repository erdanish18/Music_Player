package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //TextViews to show details of volume and brightness
    private TextView tVBrightness,tVVolume;
    //SeekBars to set volume and brightness
    private SeekBar sbVolume,sbBrightness;
    //AudioManager object, that will get and set volume
    private AudioManager audioManager;
    //Variable to store brightness value
    private int brightness;
    //Content resolver used as a handle to the system's settings
    private ContentResolver cResolver;
    //Window object, that will store a reference to the current window
    private Window window;
    int maxVolume=1;


    Button play,pause,stop;
    MediaPlayer mediaPlayer;
    int pauseCurrentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Suggests an audio stream whose volume should be changed by the hardware volume controls. 
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        initializeControls();





        play=(Button) findViewById(R.id.btn_play);
        pause=(Button) findViewById(R.id.btn_pause);
        stop=(Button) findViewById(R.id.btn_stop);

        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        stop.setOnClickListener(this);
    }

    private void initializeControls() {
        //get reference of the UI Controls
        sbVolume = (SeekBar) findViewById(R.id.sbVolume);

        tVVolume=(TextView)findViewById(R.id.tVVolume);


        try {

            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            //set max progress according to volume
            sbVolume.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            //get current volume
            sbVolume.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
            //Set the seek bar progress to 1
            sbVolume.setKeyProgressIncrement(1);
            //get max volume
            maxVolume = sbVolume.getMax();
            sbVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                    //Calculate the brightness percentage
                    float perc = (progress / (float) maxVolume) * 100;
                    //Set the brightness percentage
                    tVVolume.setText("Volume: " + (int) perc + " %");
                }
            });

        } catch (Exception e) {

        }


        //Get the content resolver
        cResolver = getContentResolver();

        //Get the current window
        window = getWindow();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.btn_play:
                if(mediaPlayer==null) {
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.music);
                    mediaPlayer.start();}

                    else if(!mediaPlayer.isPlaying()){
                        mediaPlayer.seekTo(pauseCurrentPosition);
                        mediaPlayer.start();

                }
                break;
            case R.id.btn_pause:
                if(mediaPlayer!=null){
                    mediaPlayer.pause();
                    pauseCurrentPosition=mediaPlayer.getCurrentPosition();
                }
                break;
            case R.id.btn_stop:
                if(mediaPlayer!=null) {
                    mediaPlayer.stop();
                    mediaPlayer= null;
                }
        }
    }
}
