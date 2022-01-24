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

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private ArrayList<com.emmanuelmuturia.mymoneyapp.Category> mData;
    private LayoutInflater layoutInflater;

    CategoryAdapter(Context context, ArrayList<com.emmanuelmuturia.mymoneyapp.Category> data) {
        this.layoutInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.category_list_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CategoryAdapter.ViewHolder holder, int position) {
        //Getting the current view object using its position and populating it with data
        com.emmanuelmuturia.mymoneyapp.Category currCategory = mData.get(position);

        //populating the current view with data
        holder.bindTo(currCategory);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        //Declaring private member variables the viewholder will hold
        private TextView categoryTitle;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            categoryTitle = itemView.findViewById(R.id.category_title);
        }

        public void bindTo(com.emmanuelmuturia.mymoneyapp.Category currCategory) {
            categoryTitle.setText(currCategory.getCategory_Title());
        }
    }
}

