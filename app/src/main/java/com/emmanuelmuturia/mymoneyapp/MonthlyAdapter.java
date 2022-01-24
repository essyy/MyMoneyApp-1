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
import java.util.List;

public class MonthlyAdapter extends RecyclerView.Adapter<MonthlyAdapter.myViewHolder>{

    private Context mContext;
    private List<Integer> myDataList;
    private ArrayList<String> dateList;

    public MonthlyAdapter(Context mContext, ArrayList<Integer> myDataList, ArrayList<String> dateList) {
        this.mContext = mContext;
        this.myDataList = (List<Integer>) myDataList;
        this.dateList = dateList;
    }

    @NonNull
    @NotNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weekly_list_view, null);

        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull myViewHolder holder, int position) {
        Integer expense = myDataList.get(position);
        String exp = dateList.get(position);

        holder.setMontlyAmount("Ksh. "+expense);
        holder.setMonthlyDate(""+exp);

    }

    @Override
    public int getItemCount() {
        return myDataList.size();
    }

    protected static class myViewHolder extends RecyclerView.ViewHolder{
        View mview;

        public myViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            mview = itemView;
        }

        private void setMonthlyDate(String expTitle) {
            TextView mWeek = mview.findViewById(R.id.weekly_date);
            mWeek.setText(expTitle);
        }

        private void setMontlyAmount(String spend) {
            TextView mspend = mview.findViewById(R.id.week_spend);
            mspend.setText(spend);
        }
    }
}
