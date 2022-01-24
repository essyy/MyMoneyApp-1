package com.emmanuelmuturia.mymoneyapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class IncomeExpenseActivity extends AppCompatActivity {

    private EditText incomeName;
    private EditText amount;
    private Button sendIncome;

    private DatabaseReference incomeRef;
    private FirebaseAuth mAuth;
    private ProgressDialog loader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.income_expense);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = findViewById(R.id.income_expense_tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Income"));
        tabLayout.addTab(tabLayout.newTab().setText("Expense"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);

        final ViewPager viewPager = findViewById(R.id.income_expense_viewPager);

        final IncomeExpenseFragmentAdapter incomeExpenseFragmentAdapter = new IncomeExpenseFragmentAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(incomeExpenseFragmentAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mAuth = FirebaseAuth.getInstance();
        incomeRef = FirebaseDatabase.getInstance().getReference().child("income").child(mAuth.getCurrentUser().getUid());
        loader = new ProgressDialog(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.fragment_add_income, null);


        incomeName = view.findViewById(R.id.income_name);
        amount = view.findViewById(R.id.incomeAmount);
        sendIncome = view.findViewById(R.id.btn_add_income);



//        sendIncome.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "I was clicked", Toast.LENGTH_SHORT).show();
//            }
//        });

//        sendIncome.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final String incomename = incomeName.getText().toString().trim();
//                final String incomeAmt = amount.getText().toString().trim();
//
//                if (TextUtils.isEmpty(incomename)) {
//                    incomeName.setError("Income Name Required!");
//                    return;
//                }
//
//                else {
//                    loader.setMessage("Adding Income");
//                    loader.setCanceledOnTouchOutside(false);
//                    loader.show();
//
//                    Log.e("TAG", "Log message");
//
//                    String id = incomeRef.push().getKey();
//
//                    Income income = new Income(incomename, incomeAmt);
//                    incomeRef.child(id).setValue(income).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull @NotNull Task<Void> task) {
//                            if (task.isSuccessful()) {
//                                Toast.makeText(IncomeExpenseActivity.this, "IIncome Added", Toast.LENGTH_SHORT).show();
//                            } else{
//                                Toast.makeText(IncomeExpenseActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
//                            }
//
//                            loader.dismiss();
//                        }
//                    });
//
//                }
//
//
//            }
//        });
    }


    public void saveIncome(View view) {
        final String incomename = incomeName.getText().toString().trim();
        final String incomeAmt = amount.getText().toString().trim();

        if (TextUtils.isEmpty(incomename)) {
            incomeName.setError("Income Name Required!");
            return;
        } else {
            loader.setMessage("Adding Income");
            loader.setCanceledOnTouchOutside(false);
            loader.show();

            Log.e("TAG", "Log message");

            String id = incomeRef.push().getKey();

            Income income = new Income(incomename, incomeAmt);
            incomeRef.child(id).setValue(income).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(IncomeExpenseActivity.this, "IIncome Added", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(IncomeExpenseActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }

                    loader.dismiss();
                }
            });
        }
    }
}