package com.beta.trackphone;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.app.LoaderManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SmsMainActivity extends Activity implements LoaderManager.LoaderCallbacks < Cursor > , LocationListener {


    private SimpleCursorAdapter dataAdapter;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    FirebaseAuth auth;
    FirebaseUser user;
    private DatabaseReference reference;
    Marker myMarker;
    String provider;
    public double latitude, longitude;
    LatLng latLng;

    double lat;
    double lng;
    String userId;
    public ArrayList < String > valid_no = new ArrayList < > ();

    private void requestSmsPermission() {
        String permission = Manifest.permission.RECEIVE_SMS;
        int grant = ContextCompat.checkSelfPermission(this, permission);
        if (grant != PackageManager.PERMISSION_GRANTED) {
            String[] permission_list = new String[1];
            permission_list[0] = permission;
            ActivityCompat.requestPermissions(this, permission_list, 1);
        }
    }
    private void requestSmsPermission1() {
        String permission = Manifest.permission.READ_PHONE_STATE;
        int grant = ContextCompat.checkSelfPermission(this, permission);
        if (grant != PackageManager.PERMISSION_GRANTED) {
            String[] permission_list = new String[1];
            permission_list[0] = permission;
            ActivityCompat.requestPermissions(this, permission_list, 4);
        }
    }
    private void requestSmsPermission2() {
        String permission = Manifest.permission.SEND_SMS;
        int grant = ContextCompat.checkSelfPermission(this, permission);
        if (grant != PackageManager.PERMISSION_GRANTED) {
            String[] permission_list = new String[1];
            permission_list[0] = permission;
            ActivityCompat.requestPermissions(this, permission_list, 3);
        }
    }


    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    protected boolean gps_enabled, network_enabled;
    public boolean Exists(String no) {
        StringBuilder num = new StringBuilder(no);
        no = num.substring(3, num.length());


        if (valid_no.contains(no)) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_main);

        displayListView();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[] {
                    android.Manifest.permission.ACCESS_FINE_LOCATION
            }, 1); // 1 is a integer which will return the result in onRequestPermissionsResult
            ActivityCompat.requestPermissions(this,
                    new String[] {
                            Manifest.permission.READ_PHONE_STATE
                    },
                    2);
            ActivityCompat.requestPermissions(this,
                    new String[] {
                            Manifest.permission.READ_SMS
                    },
                    3);
            ActivityCompat.requestPermissions(this,
                    new String[] {
                            Manifest.permission.SEND_SMS
                    },
                    4);



        }

        requestSmsPermission();
        requestSmsPermission1();
        requestSmsPermission2();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);





        SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String sender) {


                if (Exists(sender)) {
                    String phoneNumber = sender;


                    String smsBody = "Your Phone Location is between: "+"https://www.google.com/maps/@"+ latitude +","+ longitude +",17z" + " AND "+"https://www.google.com/maps/@"+ lat +","+ lng +",17z";

                    SmsManager smsManager = SmsManager.getDefault();
                    // Send a text based SMS
                    smsManager.sendTextMessage(phoneNumber, null, smsBody, null, null);

                }

            }
        });


        Button add = (Button) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent personEdit = new Intent(getBaseContext(), PersonEdit.class);
                Bundle bundle = new Bundle();
                bundle.putString("mode", "add");
                personEdit.putExtras(bundle);
                startActivity(personEdit);

            }
        });
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
                        Toast.makeText(SmsMainActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        getLoaderManager().restartLoader(0, null, this);


    }

    private void displayListView() {


        String[] columns = new String[] {

                Person.COL_FIRSTNAME,
                Person.COL_NO


        };


        int[] to = new int[] {
                R.id.name,
                R.id.number,
        };


        dataAdapter = new SimpleCursorAdapter(
                this,
                R.layout.person_info,
                null,
                columns,
                to,
                0);


        ListView listView = (ListView) findViewById(R.id.personList);

        listView.setAdapter(dataAdapter);

        getLoaderManager().initLoader(0, null, this);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView < ? > listView, View view,
                                    int position, long id) {

                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                String col_id =
                        cursor.getString(cursor.getColumnIndexOrThrow(Person.COL_ID));
                Toast.makeText(getApplicationContext(),
                        col_id, Toast.LENGTH_SHORT).show();


                Intent personEdit = new Intent(getBaseContext(), PersonEdit.class);
                Bundle bundle = new Bundle();
                bundle.putString("mode", "update");
                bundle.putString("rowId", col_id);
                personEdit.putExtras(bundle);
                startActivity(personEdit);

            }
        });

    }

    @Override
    public Loader < Cursor > onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                Person.COL_ID,
                Person.COL_FIRSTNAME,
                Person.COL_NO
        };
        CursorLoader cursorLoader = new CursorLoader(this,
                MyContentProvider.CONTENT_URI, projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader < Cursor > loader, Cursor cursor) {


        dataAdapter.swapCursor(cursor);
        valid_no = new ArrayList < > ();
        cursor.moveToFirst();


        while (!cursor.isAfterLast()) {
            valid_no.add(cursor.getString(2));
            cursor.moveToNext();

        }


    }

    @Override
    public void onLoaderReset(Loader < Cursor > loader) {

        dataAdapter.swapCursor(null);

    }

    @Override
    public void onLocationChanged(Location location) {
        latLng = new LatLng(location.getLatitude(),location.getLongitude());
        latitude = location.getLatitude() ;
        longitude = location.getLongitude() ;

        Log.d("latitude", latitude + "");
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.d("Latitude", "disable");

    }

    @Override
    public void onProviderEnabled(String s) {
        Log.d("Latitude", "enable");

    }

    @Override
    public void onProviderDisabled(String s) {
        Log.d("Latitude", "status");

    }

}