package com.emmanuelmuturia.mymoneyapp;

public class Daily {
    //Declaring member variables
    private String expenseTitle;
    private String Spend;
    private String date;

    //A constructor for the daily expenses model

    public Daily(String expenseTitle, String spend, String date) {
        this.expenseTitle = expenseTitle;
        Spend = spend;
        this.date = date;
    }

    //Setters and Getters

    public String getExpenseTitle() {
        return expenseTitle;
    }

    public void setExpenseTitle(String expenseTitle) {
        this.expenseTitle = expenseTitle;
    }

    public String getSpend() {
        return Spend;
    }

    public void setSpend(String spend) {
        Spend = spend;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
