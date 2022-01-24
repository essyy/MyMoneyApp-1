package com.emmanuelmuturia.mymoneyapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.MutableDateTime;
import org.joda.time.Weeks;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddExpenseFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private EditText TitleExp, NoteExp, ExpenseAmt;
    private Button sendExpense;
    private Spinner categorySpinner;
    public static String categoryItem;

    private DatabaseReference expenseRef, categoryRef;
    private FirebaseAuth mAuth;
    private ProgressDialog loader;

    //Constructor
    public AddExpenseFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_expense, container, false);

        mAuth = FirebaseAuth.getInstance();
        expenseRef = FirebaseDatabase.getInstance().getReference().child("expense").child(mAuth.getCurrentUser().getUid());
        categoryRef = FirebaseDatabase.getInstance().getReference().child("categories").child(mAuth.getCurrentUser().getUid());

        loader = new ProgressDialog(getContext());

        TitleExp = rootView.findViewById(R.id.title_expense);
        NoteExp = rootView.findViewById(R.id.note_expense);
        ExpenseAmt = rootView.findViewById(R.id.expense_amt);
        sendExpense = rootView.findViewById(R.id.btn_add_expense);


        if (categorySpinner != null) {
            categorySpinner.setOnItemSelectedListener(this);
        }

        categoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                final List<String> categories = new ArrayList<String>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String Category = dataSnapshot.child("category_Title").getValue(String.class);
                    categories.add(Category);
                }

                categorySpinner = rootView.findViewById(R.id.category_spinner);

                if (getContext() != null){
                    //Create an Array Adapter using the string array and default spinner layout
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categories);
                    //Specify the layout for the drop down menu
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    categorySpinner.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        sendExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String titleExp = TitleExp.getText().toString().trim();
                final String noteExp = NoteExp.getText().toString().trim();
                final String expenseAmt = ExpenseAmt.getText().toString().trim();
                final String catItem = categorySpinner.getSelectedItem().toString().trim();

                if (TextUtils.isEmpty(titleExp)) {
                    TitleExp.setError("Expense Title Required!");
                    return;
                }


                if (TextUtils.isEmpty(expenseAmt)) {
                    ExpenseAmt.setError("Expense Amount Required!");
                    return;
                }

                else {
                    loader.setMessage("Adding Expense");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    String id = expenseRef.push().getKey();
//                    String mDate = DateFormat.getDateInstance().format(new Date());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, dd MMM yyyy");
                    String mDate = simpleDateFormat.format(new Date());
                    Calendar cal = Calendar.getInstance();
                    String date = simpleDateFormat.format(cal.getTime());

                    MutableDateTime epoch = new MutableDateTime();
                    epoch.setDate(0);
                    DateTime now = new DateTime();
                    Weeks weeks = Weeks.weeksBetween(epoch, now);
                    Months months = Months.monthsBetween(epoch, now);

                    String input = "20210918";
                    String format = "yyyyMMdd";

                    SimpleDateFormat df = new SimpleDateFormat(format);
                    try {
                        Date tdate = df.parse(input);
                        Calendar cale = Calendar.getInstance();
                        cale.setTime(tdate);
                        int week = cale.get(Calendar.WEEK_OF_YEAR);
                        cale.setTime(tdate);

                        System.out.println("Week Number is: " + week + " Date is: " + tdate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Expense expense = new Expense(titleExp, expenseAmt, noteExp, mDate, catItem, weeks.getWeeks(), months.getMonths());
                    expenseRef.child(id).setValue(expense).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Expense Added", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getContext(), MainActivity.class);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}