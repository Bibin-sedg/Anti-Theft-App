package com.beta.trackphone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PersonEdit extends AppCompatActivity implements View.OnClickListener {

    private Button save, delete;
    private String mode;
    private EditText number, name;
    private String id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_edit);


        if (this.getIntent().getExtras() != null){
            Bundle bundle = this.getIntent().getExtras();
            mode = bundle.getString("mode");
        }


        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(this);
        delete = (Button) findViewById(R.id.delete);
        delete.setOnClickListener(this);

        number = (EditText) findViewById(R.id.number);
        name = (EditText) findViewById(R.id.name);



        if(mode.trim().equalsIgnoreCase("add")){
            delete.setEnabled(false);
        }

        else{
            Bundle bundle = this.getIntent().getExtras();
            id = bundle.getString("rowId");
            loadCountryInfo();
        }

    }

    public void onClick(View v) {


        String myNumber = number.getText().toString();
        String myName = name.getText().toString();
        switch (v.getId()) {
            case R.id.save:
                ContentValues values = new ContentValues();
                values.put(Person.COL_NO, myNumber);
                values.put(Person.COL_FIRSTNAME, myName);
                ;


                if(mode.trim().equalsIgnoreCase("add")){
                    getContentResolver().insert(MyContentProvider.CONTENT_URI, values);
                }

                else {
                    Uri uri = Uri.parse(MyContentProvider.CONTENT_URI + "/" + id);
                    getContentResolver().update(uri, values, null, null);
                }
                finish();
                break;

            case R.id.delete:
                // delete a record
                Uri uri = Uri.parse(MyContentProvider.CONTENT_URI + "/" + id);
                getContentResolver().delete(uri, null, null);
                finish();
                break;


        }
    }


    private void loadCountryInfo(){

        String[] projection = {
                Person.COL_FIRSTNAME,

                Person.COL_NO
        };
        Uri uri = Uri.parse(MyContentProvider.CONTENT_URI + "/" + id);
        Cursor cursor = getContentResolver().query(uri, projection, null, null,
                null);
        if (cursor != null) {
            cursor.moveToFirst();

            String myName = cursor.getString(cursor.getColumnIndexOrThrow(Person.COL_FIRSTNAME));

            String myNumber = cursor.getString(cursor.getColumnIndexOrThrow(Person.COL_NO));
            number.setText(myNumber);
            name.setText(myName);
        }


    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}