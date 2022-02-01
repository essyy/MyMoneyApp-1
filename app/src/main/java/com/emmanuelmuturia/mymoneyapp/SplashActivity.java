package com.emmanuelmuturia.mymoneyapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {
    //We declare a variable to hold the currently authenticated user
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //We get currently logged in user
        mAuth = FirebaseAuth.getInstance();
        registerUser();

        //Transition from Splash Activity to Main Activity
        final Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        }, 1500);
    }
    //Allows user to sign in without providing email and password
    public void registerUser(){
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //Sign in successful
                            FirebaseUser user = mAuth.getCurrentUser();
                        }else{
                            //Sign in unsuccessful
                            Toast.makeText(SplashActivity.this,"Sign in Successful!",Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }
                });
    }
}