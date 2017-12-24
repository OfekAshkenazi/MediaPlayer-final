package com.example.ofek.cameraapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import Helpers.PermissionHelper;

public class MainActivity extends AppCompatActivity {
    Button picActivityBtn,vidActivityBtn,lastClicked=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        picActivityBtn=findViewById(R.id.picActivityBtn);
        vidActivityBtn=findViewById(R.id.vidActivityBtn);
        if (PermissionHelper.getExternalStoragePermission(this)){
            setListeners();
        }
    }

    private void setListeners() {
        picActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastClicked= (Button) view;
                if (PermissionHelper.getCameraPermission(MainActivity.this)) {
                    Intent intent = new Intent(MainActivity.this, PictureActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(MainActivity.this, "camera permission isn't granted", Toast.LENGTH_SHORT).show();
                }
            }
        });
        vidActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastClicked= (Button) view;
                if (PermissionHelper.getCameraPermission(MainActivity.this)) {
                    Intent intent = new Intent(MainActivity.this, VideoActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(MainActivity.this, "camera permission isn't granted", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==PermissionHelper.REQUEST_WRITE_EXTERNAL_STORAGE){
            if (grantResults[0]== PackageManager.PERMISSION_GRANTED){
                setListeners();
            }else {
                Toast.makeText(this, "writing external permission denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        if (requestCode==PermissionHelper.REQUEST_CAMERA){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                if (lastClicked==null){
                    return;
                }
                if (lastClicked==picActivityBtn){
                    Intent intent = new Intent(MainActivity.this, PictureActivity.class);
                    startActivity(intent);
                }
                if (lastClicked==vidActivityBtn){
                    Intent intent = new Intent(MainActivity.this, VideoActivity.class);
                    startActivity(intent);
                }
            }else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
    }
}
