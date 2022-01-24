package com.emmanuelmuturia.mymoneyapp;

public class Category {
    //Declaring member variables
    private String category_Title;
    private String category_amt;

    Category(String category_Title, String category_amt) {
        this.category_Title = category_Title;
        this.category_amt = category_amt;
    }

    public Category(String category_Title) {
        this.category_Title = category_Title;
    }

    public Category() {
    }

    //Getters
    public String getCategory_Title() {
        return category_Title;
    }

    public String getCategory_amt() {
        return category_amt;
    }
}
