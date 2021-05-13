package com.p2aau.virtualworkoutv2;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class LobbyWorkoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_workout);

        VideoView videoView = findViewById(R.id.exercise_video);
        String videoPath = "android.resource://"+getPackageName()+"/"+R.raw.jumpingjack;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);

        //MediaController mediaController = new MediaController(this); 
        //videoView.setMediaController(mediaController);
        // mediaController.setAnchorView(videoView);

        videoView.start();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
            @Override
            public void  onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });

    }
}