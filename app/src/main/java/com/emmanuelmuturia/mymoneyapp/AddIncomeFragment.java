package com.emmanuelmuturia.mymoneyapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddIncomeFragment extends Fragment {
    private EditText incomeName;
    private EditText amount;
    private Button sendIncome;

    private DatabaseReference incomeRef;
    private FirebaseAuth mAuth;
    private ProgressDialog loader;

    //Cosntructor for the AddIncomeFragment
    public AddIncomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_income, container, false);

        mAuth = FirebaseAuth.getInstance();
        incomeRef = FirebaseDatabase.getInstance().getReference().child("income").child(mAuth.getCurrentUser().getUid());
        loader = new ProgressDialog(getContext());

        incomeName = rootView.findViewById(R.id.income_name);
        amount = rootView.findViewById(R.id.incomeAmount);
        sendIncome = rootView.findViewById(R.id.btn_add_income);

        sendIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String incomename = incomeName.getText().toString().trim();
                final String incomeAmt = amount.getText().toString().trim();

                if (TextUtils.isEmpty(incomename)) {
                    incomeName.setError("Income Name Required!");
                    return;
                }

                if (TextUtils.isEmpty(incomeAmt)) {
                    amount.setError("Income Amount Required");
                }

                else {
                    loader.setMessage("Adding Income");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();



                    String id = incomeRef.push().getKey();

                    Income income = new Income(incomename, incomeAmt);
                    incomeRef.child(id).setValue(income).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Income Added", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getContext(), com.emmanuelmuturia.mymoneyapp.IncomeActivity.class);
                                startActivity(intent);
                            } else{
                                Toast.makeText(getContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }

                            loader.dismiss();
                        }
                    });

                }


            }
        });

        return rootView;
    }
}