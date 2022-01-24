package com.emmanuelmuturia.mymoneyapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ExpenseFragmentAdapter extends FragmentStatePagerAdapter {
    int myNumberOfTabs;

    public ExpenseFragmentAdapter(@NonNull FragmentManager fm, int numofTabs) {
        super(fm, numofTabs);
        this.myNumberOfTabs = numofTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new DailyFragment();

            case 1:
                return new WeeklyFragment();

            case 2:
                return new MonthlyFragment();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return myNumberOfTabs;
    }
}
