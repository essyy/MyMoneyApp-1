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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class EditIncomeActivity extends AppCompatActivity {
    private EditText incomeName;
    private EditText amount;
    private Button sendIncome;

    String inc_ID = null;

    private DatabaseReference incomeRef;
    private FirebaseAuth mAuth;
    private ProgressDialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_income);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        incomeRef = FirebaseDatabase.getInstance().getReference().child("income").child(mAuth.getCurrentUser().getUid());
        loader = new ProgressDialog(this);

        inc_ID = getIntent().getExtras().getString("incomeID");

        incomeName = findViewById(R.id.income_name);
        amount = findViewById(R.id.incomeAmount);
        sendIncome = findViewById(R.id.btn_add_income);

        incomeRef.child(inc_ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String incName = (String) snapshot.child("income_title").getValue();
                String amt = (String) snapshot.child("amount").getValue();

                incomeName.setText(incName);
                amount.setText(amt);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        sendIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String incTitle = incomeName.getText().toString().trim();
                final String incAmt = amount.getText().toString().trim();

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("income_title", incTitle);
                hashMap.put("amount", incAmt);

                incomeRef.child(inc_ID).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditIncomeActivity.this, "Income Updated", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(EditIncomeActivity.this, com.emmanuelmuturia.mymoneyapp.IncomeActivity.class);
                            startActivity(intent);
                        } else{
                            Toast.makeText(EditIncomeActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }

                        loader.dismiss();
                    }
                });
            }
        });


    }
}