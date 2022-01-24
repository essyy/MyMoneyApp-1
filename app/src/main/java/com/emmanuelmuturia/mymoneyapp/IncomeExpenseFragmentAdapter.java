package com.emmanuelmuturia.mymoneyapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import org.jetbrains.annotations.NotNull;

public class IncomeExpenseFragmentAdapter extends FragmentStatePagerAdapter {
    int myNumberOfTabs;

    public IncomeExpenseFragmentAdapter(@NonNull @NotNull FragmentManager fm, int numOfTabs) {
        super(fm, numOfTabs);
        this.myNumberOfTabs = numOfTabs;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new AddIncomeFragment();

            case 1:
                return new com.emmanuelmuturia.mymoneyapp.AddExpenseFragment();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return myNumberOfTabs;
    }
}
