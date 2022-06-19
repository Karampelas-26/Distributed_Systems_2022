package com.example.distributedsystemsapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.Nullable;

import com.example.distributedsystemsapp.domain.Util;

import java.io.File;

public class VideoActivity extends Activity {

    VideoView video;
    Button backBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_dialog);

        Intent intent = getIntent();
//        String filepath = intent.getStringExtra("path");
        String filename = intent.getStringExtra("filename");
//        File file = new File(filepath,filename);
        video = findViewById(R.id.video);
//        video.setVideoURI(Uri.fromFile(file));


        File path = getApplicationContext().getFilesDir();
        File readFrom = new File(path, filename);

        MediaController mediaController= new MediaController(this);
        mediaController.setAnchorView(video);


        video.setMediaController(mediaController);
        video.setVideoURI(Uri.fromFile(readFrom));
        video.requestFocus();
        video.start();


        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
