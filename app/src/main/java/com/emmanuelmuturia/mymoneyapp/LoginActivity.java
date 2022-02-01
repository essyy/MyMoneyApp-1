package com.emmanuelmuturia.mymoneyapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText loginEmail, loginPass;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        loginBtn = findViewById(R.id.loginBtn);
        loginEmail = findViewById(R.id.loginEmail);
        loginPass = findViewById(R.id.loginPassword);

        mAuth = FirebaseAuth.getInstance();

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "PROCESSING...", Toast.LENGTH_LONG).show();

                //We get email and password...
                String email = loginEmail.getText().toString().trim();
                String password = loginPass.getText().toString().trim();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    //Use Firebase auth instance you create and call the method signInWithEmailAndPassword method passing the email and pwd you got from the views...
                    //Call the addOnCompleteListener() method to handle the Auth result...
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //checkUserExistence();
                                Intent mainPage = new Intent(LoginActivity.this, SplashActivity.class);
                                startActivity(mainPage);
                            } else {
                                Toast.makeText(LoginActivity.this, "Couldn't log in, user not found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    //If email and pwd fields were not completed...
                    Toast.makeText(LoginActivity.this, "Complete all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Function to check if user exists...
    public void checkUserExistence() {
        //We check user existence of user using the user_id in users db reference...
        final String user_id = mAuth.getCurrentUser().getUid();
        //We call the method addValueEventListener on the db reference of the user to determine if the current userID supplied does exist in our db reference...
        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //We get a snapshot of the users db reference to determine if current user exists...
                if (snapshot.hasChild(user_id)) {
                    //If the user exists, direct them to Main Activity...
                    Intent mainPage = new Intent(LoginActivity.this, SplashActivity.class);
                    startActivity(mainPage);
                } else {
                    Toast.makeText(LoginActivity.this, "User not registered!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Database Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void Register(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}