package com.emmanuelmuturia.mymoneyapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    //Declare instances of the views
    private Button registerBtn;
    private EditText emailField, passwordField;
    private TextView loginTxtView;
    //Declare an instance of Firebase Authentication
    private FirebaseAuth mAuth;
    //Declare an instance of Firebase Database
    private FirebaseDatabase database;
    //Declare an instance of Firebase Database Reference
    //A Database reference is a node in our database, e.g the node users to store user details
    private DatabaseReference userDetailsReference;

    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Inflate the toolbar
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        //Initialize the views
        loginTxtView = findViewById(R.id.loginTxtView);
        registerBtn = findViewById(R.id.registerBtn);
        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);

        //Initialize an Instance of Firebase Auth by calling the getInstance() method
        mAuth = FirebaseAuth.getInstance();
        //Initialize an Instance of Firebase Db by calling the getInstance() method
        database = FirebaseDatabase.getInstance();
        //Initialize an Instance of Firebase reference by calling the database instance,
        // getting a reference using the get reference() method on the database, and
        // creating a new child node, in our case "Users" where we will store details of registered users
        userDetailsReference = database.getReference().child("Users");

        //For already registered users, we want to redirect them to the login page directly without registering them again
        //For this function, setOnClickListener on the textview object of redirecting user to LoginActivity
        loginTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(RegisterActivity.this, com.emmanuelmuturia.mymoneyapp.LoginActivity.class);
                startActivity(loginIntent);
            }
        });

        //Set an onClickListener on your register btn, on clicking we want to get: username, email, password
        //We also want to open a new activity called ProfileActivity where we will allow our users to set a custom
        // display name and their profile image
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create a Toast
                Toast.makeText(RegisterActivity.this, "LOADING...", Toast.LENGTH_LONG).show();

                //get username, email, password, pwd
                final String email = emailField.getText().toString().trim();
                final String password = passwordField.getText().toString().trim();

                //Validate to ensure that the user has entered email and username
                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                //Create a string var to get the user_id of currently registered users
                                String user_id = mAuth.getCurrentUser().getUid();
                                //Create a child node database reference to attach the user_id to the users node
                                DatabaseReference current_user_db = userDetailsReference.child(user_id);
                                //Set username and image on the users' unique path(current_users_db)
                                current_user_db.child("Image").setValue("Default");

                                Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                //Launch the ProfileActivity for user to set their preferred profile
                                Intent profIntent = new Intent(RegisterActivity.this, com.emmanuelmuturia.mymoneyapp.LoginActivity.class);
                                profIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(profIntent);
                            } else {

                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "Authentication failed.Please Try Again!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(RegisterActivity.this, "Complete all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void Login(View view) {
        Intent intent = new Intent(RegisterActivity.this, com.emmanuelmuturia.mymoneyapp.LoginActivity.class);
        startActivity(intent);
    }
}