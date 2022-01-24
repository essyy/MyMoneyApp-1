package com.emmanuelmuturia.mymoneyapp;

public class Expense {
    private String expenseTitle;
    private String expenseAmount;
    private String expenseNote;
    private String expenseDate;
    private String category;
    private int weeks;
    private int months;

    public Expense(String expenseTitle, String expenseAmount, String expenseNote, String expenseDate, String category, int weeks, int months) {
        this.expenseTitle = expenseTitle;
        this.expenseAmount = expenseAmount;
        this.expenseNote = expenseNote;
        this.expenseDate = expenseDate;
        this.category = category;
        this.weeks = weeks;
        this.months = months;
    }

    public Expense() {
    }

    public String getExpenseTitle() {
        return expenseTitle;
    }

    public void setExpenseTitle(String expenseTitle) {
        this.expenseTitle = expenseTitle;
    }

    public String getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(String expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public String getExpenseNote() {
        return expenseNote;
    }

    public void setExpenseNote(String expenseNote) {
        this.expenseNote = expenseNote;
    }

    public String getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(String expenseDate) {
        this.expenseDate = expenseDate;
    }

    public int getWeeks() {
        return weeks;
    }

    public void setWeeks(int weeks) {
        this.weeks = weeks;
    }

    public int getMonths() {
        return months;
    }

    public void setMonths(int months) {
        this.months = months;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
