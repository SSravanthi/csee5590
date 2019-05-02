package com.example.lab4;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class HomeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


    }



    public void logout(View view) {
        Intent redirect = new Intent(HomeActivity.this,MainActivity.class);
        startActivity(redirect);
    }

    public void onNewsClick(View v) {
        //This code redirects to the news activity.
        Intent redirect = new Intent(HomeActivity.this, NewsActivity.class);
        startActivity(redirect);
    }

    public void maps(View view) {
        Intent redirect = new Intent(HomeActivity.this,MapsActivity.class);
        startActivity(redirect);
    }

    public void camera(View view) {
        Intent redirect = new Intent(HomeActivity.this,CameraActivity.class);
        startActivity(redirect);
    }
    public void onAudioClick(View v){
        Intent redirect = new Intent(HomeActivity.this, AudioRecordingActivity.class);
        startActivity(redirect);
    }

    public void onStorageClick(View v){
        Intent redirect = new Intent(HomeActivity.this, StorageActivity.class);
        startActivity(redirect);
    }
}