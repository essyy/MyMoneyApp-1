package com.emmanuelmuturia.mymoneyapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.ViewHolder> {
    private ArrayList<Income> mData;
    private LayoutInflater layoutInflater;

    IncomeAdapter(Context context, ArrayList<Income> data) {
        this.layoutInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.income_list_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull IncomeAdapter.ViewHolder holder, int position) {
        //Getting the current view object using its position and populating it with data
        Income currIncome = mData.get(position);

        //populating the current view with data
        holder.bindTo(currIncome);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        //Declaring private member variables the viewholder will hold
        private TextView incomeTitle;
        private TextView incomeAmt;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            incomeTitle = itemView.findViewById(R.id.income_title);
            incomeAmt = itemView.findViewById(R.id.income_amt);
        }

        public void bindTo(Income currIncome) {
            incomeTitle.setText(currIncome.getIncome_title());
            incomeAmt.setText(currIncome.getAmount());
        }
    }
}
