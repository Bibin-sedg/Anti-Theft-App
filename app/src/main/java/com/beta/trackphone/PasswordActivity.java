package com.beta.trackphone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PasswordActivity extends AppCompatActivity {
    String email;
    EditText editTxtPassword2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        editTxtPassword2 = (EditText)findViewById(R.id.editTextPassword2);

        Intent intent = getIntent();
        if(intent!=null)
        {
            email = intent.getStringExtra("email");
        }
    }
    public void goToNamePicActivity(View v)
    {
        if(editTxtPassword2.getText().toString().length()>6)
        {
            Intent intent = new Intent(PasswordActivity.this,NameActivity.class);
            intent.putExtra("email",email);
            intent.putExtra("password",editTxtPassword2.getText().toString());
            startActivity(intent);
            finish();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Password Length should be more than 6 characters",Toast.LENGTH_SHORT).show();
        }
    }
}