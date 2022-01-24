package com.emmanuelmuturia.mymoneyapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class EditCategoryActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    private EditText addCat;
    private Button addBtn;

    private DatabaseReference categoryRef;
    private FirebaseAuth mAuth;
    private ProgressDialog loader;

    String cat_ID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        addCat = findViewById(R.id.input_category);
        addBtn = findViewById(R.id.btn_add_category);

        loader = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        categoryRef = FirebaseDatabase.getInstance().getReference().child("categories").child(mAuth.getCurrentUser().getUid());

        cat_ID = getIntent().getExtras().getString("categoryID");

        categoryRef.child(cat_ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String categoryTitle = (String) snapshot.child("category_Title").getValue();

                addCat.setText(categoryTitle);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String catTitle = addCat.getText().toString().trim();

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("category_Title", catTitle);

                categoryRef.child(cat_ID).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditCategoryActivity.this, "Category Updated", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EditCategoryActivity.this, com.emmanuelmuturia.mymoneyapp.CategoryActivity.class);
                            startActivity(intent);
                        } else{
                            Toast.makeText(EditCategoryActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }

                        loader.dismiss();
                    }
                });
            }
        });
    }
}