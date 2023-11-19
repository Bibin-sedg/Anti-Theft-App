package com.beta.trackphone;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class NameActivity extends AppCompatActivity {
    String email,password;
    EditText editTxtName;
    CircleImageView circleImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        editTxtName = (EditText)findViewById(R.id.editTextName);
        circleImageView = (CircleImageView)findViewById(R.id.circleImageView);
        Intent intent = getIntent();
        if(intent!=null)
        {
            email = intent.getStringExtra("email");
            password = intent.getStringExtra("password");
        }
    }
    public void generateCode(View v)
    {
        Date myDate = new Date();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.getDefault());
        String date = format1.format(myDate);
        Random r = new Random();
        int n = 100000 + r.nextInt(900000);
        String code = String.valueOf(n);
        String txtName = editTxtName.getText().toString();     //newly added
        if(!txtName.isEmpty())
        {
            Intent intent = new Intent(NameActivity.this, RegisterUserActivity.class);
            intent.putExtra("name",editTxtName.getText().toString());
            intent.putExtra("email",email);
            intent.putExtra("password",password);
            intent.putExtra("date",date);
            intent.putExtra("isSharing","false");
            intent.putExtra("code",code);

            startActivity(intent);
            finish();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Please Enter Your Name",Toast.LENGTH_SHORT).show();
        }

    }
}