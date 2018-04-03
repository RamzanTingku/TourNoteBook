package com.example.user.tourassistant.firebase;

/**
 * Created by user on 14/10/2017.
 */

public class Expense {
    private  String EDetails;
    private double Expense;
    private String Date;

    public Expense() {
    }

    public Expense(String EDetails, double expense, String date) {
        this.EDetails = EDetails;
        this.Expense = expense;
        this.Date = date;
    }

    public String getEDetails() {
        return EDetails;
    }

    public void setEDetails(String EDetails) {
        this.EDetails = EDetails;
    }

    public double getExpense() {
        return this.Expense;
    }

    public void setExpense(double expense) {
        this.Expense = expense;
    }

    public String getDate() {
        return this.Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
