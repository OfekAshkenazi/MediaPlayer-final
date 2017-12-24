package com.example.ofek.cameraapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class VideoActivity extends AppCompatActivity {
    private static final String AUTHORITY = "com.example.ofek.cameraapp.fileprovider";
    private static final int REQUEST_TAKE_VIDEO = 54654;
    private String vidPath;
    private Uri vidUri;
    Button takeVidBtn;
    VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        takeVidBtn=findViewById(R.id.takeVidBtn);
        videoView=findViewById(R.id.videoView);
        takeVidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeVideo();
            }
        });
    }

    private File createMediaFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String fileName = "";
        String fileType = "";
        String dir = "";
        fileName = "VIDEO";
        fileType = ".mp3";
        dir = Environment.DIRECTORY_MOVIES;
        File storageDir = Environment.getExternalStoragePublicDirectory(dir);
        return new File(storageDir, fileName + fileType);
    }
    public void takeVideo(){
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        //check if there is a camera
        if(takeVideoIntent.resolveActivity(getPackageManager())!=null) {

            File photoFile = createMediaFile();
            if (photoFile != null) {
                vidPath = photoFile.getAbsolutePath();
                vidUri = FileProvider.getUriForFile(this, AUTHORITY,photoFile);
                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, vidUri);
                startActivityForResult(takeVideoIntent,REQUEST_TAKE_VIDEO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_TAKE_VIDEO&&resultCode==RESULT_OK){
            notifyVideoToGallery();
        }
    }

    private void notifyVideoToGallery() {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(vidPath);
        Uri contentUri = Uri.fromFile(file);
        intent.setData(contentUri);
        sendBroadcast(intent);
        displayVideo();
    }

    private void displayVideo() {
        videoView.setVideoPath(vidPath);
        videoView.setVisibility(View.VISIBLE);
        videoView.start();
    }
}
