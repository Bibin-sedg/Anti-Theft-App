package com.beta.trackphone;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.beta.trackphone.databinding.ActivityUserMapsBinding;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.beta.trackphone.databinding.ActivityLocatePhoneBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LocatePhoneActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityLocatePhoneBinding binding;
    FirebaseAuth auth;
    FirebaseUser user;
    LocationRequest request;
    LatLng latLng;

    private DatabaseReference reference;
    private LocationManager manager;
    String userId;
    private final int MIN_TIME = 1000;
    private final int MIN_DISTANCE = 1;
    Marker myMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLocatePhoneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        manager = (LocationManager)getSystemService(LOCATION_SERVICE);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.loc_map);
        mapFragment.getMapAsync(this);
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
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
                            myMarker.setPosition(new LatLng(location.getLatitude(),location.getLongitude()));
                        }
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(LocatePhoneActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng loc = new LatLng(-34, 151);
        myMarker = mMap.addMarker(new MarkerOptions().position(loc).title("Your Phone Location"));

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        LatLng position = myMarker.getPosition();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(position, 1f); // set zoom level to 15

        mMap.moveCamera(cameraUpdate);
    }




}
