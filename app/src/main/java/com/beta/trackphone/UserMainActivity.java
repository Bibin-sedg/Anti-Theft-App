package com.beta.trackphone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserMainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    LatLng latLng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

    }
    public void user_Location(View v)
    {
        Intent intent = new Intent(UserMainActivity.this,UserMapsActivity.class);
        startActivity(intent);
    }
    public void locatePhone(View v)
    {
        Intent intent = new Intent(UserMainActivity.this,LocatePhoneActivity.class);
        startActivity(intent);

    }
    public void shutdown(View v)
    {
        Intent intent = new Intent(UserMainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }
    public void smsSetting(View v)
    {
        Intent intent = new Intent(UserMainActivity.this, SmsMainActivity.class);
        startActivity(intent);
    }
    public void signOut(View v)
    {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            auth.signOut();
            finish();

            Intent intent = new Intent(UserMainActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}