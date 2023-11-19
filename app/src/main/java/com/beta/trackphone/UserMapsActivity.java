package com.beta.trackphone;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.beta.trackphone.databinding.ActivityUserMapsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserMapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private ActivityUserMapsBinding binding;
    FirebaseAuth auth;
    FirebaseUser user;
    LocationRequest request;
    LatLng latLng;
    double lat;
    double lng;

    private DatabaseReference reference;
    private LocationManager manager;
    String userId;
    private final int MIN_TIME = 1000;
    private final int MIN_DISTANCE = 1;
    Marker myMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        manager = (LocationManager)getSystemService(LOCATION_SERVICE);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        getLocationUpdates();
        readChanges();
    }

    private void readChanges()
    {
        userId = user.getUid();

        reference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    try {
                        MyLocation location = snapshot.getValue(MyLocation.class);
                        if (location != null)
                        {
                            lat = location.getLatitude();
                            lng = location.getLongitude();
                            myMarker.setPosition(new LatLng(location.getLatitude(),location.getLongitude()));


                        }
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(UserMapsActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    private void getLocationUpdates()
    {
        if(manager != null)
        {
            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this::onLocationChanged);
                } else if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this::onLocationChanged);
                } else {
                    Toast.makeText(this, "No Provider Enabled", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},101);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 101)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                getLocationUpdates();
            }
            else
            {
                Toast.makeText(this,"Permission Required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng loc = new LatLng(-34, 151);
        myMarker = mMap.addMarker(new MarkerOptions().position(loc).title("Current Location"));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
    }

    public void shareLoc(View v)
    {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT,"My Location is : "+"https://www.google.com/maps/@"+ lat+","+lng+",17z");
        startActivity(i.createChooser(i,"Share using: "));
    }
    @Override
    public void onLocationChanged(@NonNull Location location) {
        if(location != null)
        {
            saveLocation(location);
            latLng = new LatLng(location.getLatitude(),location.getLongitude());
        }
        else
        {
            Toast.makeText(this,"No Provider Found",Toast.LENGTH_SHORT).show();
        }
    }

    private void saveLocation(Location location)
    {
        userId = user.getUid();
        reference.child(userId).setValue(location);

    }


}