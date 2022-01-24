package com.emmanuelmuturia.mymoneyapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class MonthlyFragment extends Fragment {
    private RecyclerView monthlyRecyclerView;
    private List<Expense> myDataList;
    private MonthlyAdapter monthlyAdapter;

    View rootView;
    private DatabaseReference mExpDat;
    private FirebaseAuth mAuth;

    private Integer key_amt, value_amt;
    HashMap hashMap = new HashMap();
    private ArrayList<Integer> monthlyData;
    private ArrayList<String> monthlyDate;
    private ArrayList<Integer> weeklyAmts;


    public MonthlyFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_monthly, container, false);

        monthlyRecyclerView = rootView.findViewById(R.id.recycler_monthly);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);

        monthlyRecyclerView.setHasFixedSize(true);
        monthlyRecyclerView.setLayoutManager(linearLayoutManager);

        monthlyData = new ArrayList<Integer>();
        monthlyDate = new ArrayList<String>();

        monthlyAdapter = new MonthlyAdapter(getContext(), monthlyData, monthlyDate);
        monthlyRecyclerView.setAdapter(monthlyAdapter);

        createHash();

        return rootView;
    }

    public void createHash() {
        mAuth = FirebaseAuth.getInstance();
        mExpDat = FirebaseDatabase.getInstance().getReference().child("expense").child(mAuth.getCurrentUser().getUid());

        mExpDat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                final List<Integer> months = new ArrayList<>();
                final List<Date> dates = new ArrayList();
                final List<Integer> amt = new ArrayList<>();
                int month = 0;
                int totAmt = 0;
                int totamt = 0;

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    Object objDate = map.get("expenseDate");

                    SimpleDateFormat df = new SimpleDateFormat("E, dd MMM yyyy");

                    try {
                        Date date = df.parse((String) objDate);
                        System.out.println("Monthly Date is: "+date);

                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        month = cal.get(Calendar.MONTH);

                        System.out.println("The month is "+month);

                        months.add(month);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (month == 8) {
                        Object objAmt = map.get("expenseAmount");
                        int pTotal = Integer.parseInt(String.valueOf(objAmt));

                        totamt += pTotal;
                        hashMap.put("September", totamt);
                    } else if (month == 9) {
                        Object objamt = map.get("expenseAmount");
                        int Total = Integer.parseInt(String.valueOf(objamt));

                        totAmt += Total;
                        hashMap.put("October", totAmt);
                    }
                }

                List<Integer> list = new ArrayList<Integer>(hashMap.values()); //Most likely dataSnapshot.getValues()

                monthlyData.clear();
                monthlyDate.clear();

                for (Object key : hashMap.keySet()) {
                    System.out.println("Monthly keys from Bind are: "+key);
                    monthlyDate.add((String) key);

                }

                for (Object key : hashMap.values()) {
                    System.out.println("Monthly values are: "+key);
                    monthlyData.add((Integer) key);
                }

                monthlyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}