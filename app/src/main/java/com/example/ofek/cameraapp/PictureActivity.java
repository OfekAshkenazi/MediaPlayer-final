package com.example.ofek.cameraapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PictureActivity extends AppCompatActivity {
    private static final int REQUEST_TAKE_PHOTO = 4545;
    private static final String AUTHORITY = "com.example.ofek.cameraapp.fileprovider";
    Button cameraBtn;
    ImageView imageView;
    private String photoPath;
    private Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        cameraBtn=findViewById(R.id.cameraBtn);
        imageView=findViewById(R.id.imageView);
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });
    }

    public void takePhoto(){
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //check if there is a camera
        if(takePhotoIntent.resolveActivity(getPackageManager())!=null) {

            File photoFile = createMediaFile();
            if (photoFile != null) {
                photoPath= photoFile.getAbsolutePath();
                photoUri = FileProvider.getUriForFile(this, AUTHORITY,photoFile);
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                startActivityForResult(takePhotoIntent,REQUEST_TAKE_PHOTO);
            }
        }
    }


    private File createMediaFile(){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String fileName="";
        String fileType ="";
        String dir="";
        fileName="IMG_"+timeStamp;
        fileType=".jpeg";
        dir= Environment.DIRECTORY_PICTURES;
        File storageDir = Environment.getExternalStoragePublicDirectory(dir);
        return new File(storageDir,fileName+fileType);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        Log.v("Debug-------:","on activity result called");
        if (requestCode==REQUEST_TAKE_PHOTO&&resultCode==RESULT_OK){
            Log.v("Debug-------:","taking photo succeeded");
            notifyGalleryAboutPic(photoPath);
        }
    }


    private void notifyGalleryAboutPic(String photoPath) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(photoPath);
        Uri contentUri = Uri.fromFile(file);
        intent.setData(contentUri);
        sendBroadcast(intent);
        displayImage();
    }

    private void displayImage() {
        Picasso.with(this).load(photoUri).into(imageView);
    }
}
