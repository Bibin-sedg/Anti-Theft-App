package com.beta.trackphone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth auth;

    EditText editTxtEmail,editTxtPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTxtEmail = (EditText)findViewById(R.id.editTextEmailAddress);
        editTxtPwd = (EditText)findViewById(R.id.editTextPassword);
        auth = FirebaseAuth.getInstance();
    }

    public void login(View v)
    {
        auth.signInWithEmailAndPassword(editTxtEmail.getText().toString(),editTxtPwd.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                          //  Toast.makeText(getApplicationContext(),"User Logged In Successfully",Toast.LENGTH_LONG).show();
                            FirebaseUser user = auth.getCurrentUser();
                            if(user.isEmailVerified())
                            {
                                Intent intent = new Intent(LoginActivity.this, UserMainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Email is Not Verified yet",Toast.LENGTH_SHORT).show();
                            }

                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Wrong Email or Password",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}