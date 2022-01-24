package com.emmanuelmuturia.mymoneyapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class DashboardActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    private DatabaseReference mIncome, mExpense, mBalance;
    private FirebaseAuth mAuth;
    private TextView totalBalance;
    private TextView totalExpense;

    String incKey, expKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

//        Dashboard expenses
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_daily));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_weekly));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_monthly));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = findViewById(R.id.view_pager);

        final com.emmanuelmuturia.mymoneyapp.ExpenseFragmentAdapter expenseFragmentAdapter = new com.emmanuelmuturia.mymoneyapp.ExpenseFragmentAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(expenseFragmentAdapter);

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
//        End of dashboard expenses

        bottomNavigationView = findViewById(R.id.bottom_nav);

        bottomNavigationView.setSelectedItemId(R.id.dashboard);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                int id = item.getItemId();

                switch (id) {
                    case R.id.categories:
                        Intent intent = new Intent(DashboardActivity.this, com.emmanuelmuturia.mymoneyapp.CategoryActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.expense:
                        Intent intent2 = new Intent(DashboardActivity.this, MainActivity.class);
                        startActivity(intent2);
                        break;

                    case R.id.income:
                        Intent intent1 = new Intent(DashboardActivity.this, com.emmanuelmuturia.mymoneyapp.IncomeActivity.class);
                        startActivity(intent1);
                        break;
                }
                return false;
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mIncome = FirebaseDatabase.getInstance().getReference().child("income").child(mAuth.getCurrentUser().getUid());
        mExpense = FirebaseDatabase.getInstance().getReference().child("expense").child(mAuth.getCurrentUser().getUid());
        mBalance = FirebaseDatabase.getInstance().getReference();

        totalBalance = findViewById(R.id.balTot);
        totalExpense = findViewById(R.id.expTot);
    }

    @Override
    public void onStart() {
        super.onStart();

        Query incQuery = mIncome.orderByChild("income");

        final int[] totIncome = {0};
        incQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Map<String, Object> map = (Map<String, Object>) data.getValue();
                    Object total = map.get("amount");
                    int pTotal = Integer.parseInt(String.valueOf(total));
                    totIncome[0] += pTotal;
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final int[] totExpense = {0};
        Query expQuery = mExpense.orderByChild("expense");
        expQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {


                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Map<String, Object> map = (Map<String, Object>) data.getValue();
                    Object total = map.get("expenseAmount");
                    int pTotal = Integer.parseInt(String.valueOf(total));
                    totExpense[0] += pTotal;
                }
                totalExpense.setText("Ksh. " + totExpense[0]);
                totalBalance.setText("Ksh. "+(totIncome[0]-totExpense[0]));
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_help) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:12345678"));
            startActivity(intent);
        } else if (id == R.id.log_out) {
            mAuth.signOut();
            Intent intent = new Intent(DashboardActivity.this, com.emmanuelmuturia.mymoneyapp.LoginActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}