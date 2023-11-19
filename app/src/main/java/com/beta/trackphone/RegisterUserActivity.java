package com.beta.trackphone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUserActivity extends AppCompatActivity {
    String name, email, password, date, isSharing, code;
    ProgressDialog progressDialog;
    TextView trakingCode;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        trakingCode = (TextView) findViewById(R.id.userName);
        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        Intent intent = getIntent();

        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        if (intent != null) {
            name = intent.getStringExtra("name");
            email = intent.getStringExtra("email");
            password = intent.getStringExtra("password");
            code = intent.getStringExtra("code");
            isSharing = intent.getStringExtra("isSharing");
        }
        trakingCode.setText(name);
    }

    public void registerUser(View v) {
        progressDialog.setMessage("Creating Account, Please wait...");
        progressDialog.show();

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user = auth.getCurrentUser();
                            //insert value in realtime database
                            CreateUser createUser = new CreateUser(name, email, password, code, "false", "na", "na",user.getUid());

                            userId = user.getUid();

                            reference.child(userId).setValue(createUser)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                progressDialog.dismiss();
                                                //Toast.makeText(getApplicationContext(), "Email Send for verification. Verify Email and Login Again", Toast.LENGTH_SHORT).show();
                                                sendVerificationEmail();


                                            } else {
                                                progressDialog.dismiss();
//                                                Toast.makeText(getApplicationContext(), "Could Not Register User", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(RegisterUserActivity.this, MainActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                        }
                    }
                });

    }
    public void sendVerificationEmail()
    {
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(),"Email Send for verification. Verify Email and Login Again",Toast.LENGTH_SHORT).show();
                            finish();
                          //  auth.signOut();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Could not send Email",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}