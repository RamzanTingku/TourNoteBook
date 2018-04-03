package com.example.user.tourassistant.firebase;

/**
 * Created by user on 12/10/2017.
 */

public class Events {
    private String Destination;
    private String FromDate;
    private String ToDate;
    private double budget;

    public Events() {
    }

    public Events(String destination, String fromDate, String toDate, double budget) {
        Destination = destination;
        FromDate = fromDate;
        ToDate = toDate;
        this.budget = budget;
    }

    public String getDestination() {
        return Destination;
    }

    public void setDestination(String destination) {
        Destination = destination;
    }

    public String getFromDate() {
        return FromDate;
    }

    public void setFromDate(String fromDate) {
        FromDate = fromDate;
    }

    public String getToDate() {
        return ToDate;
    }

    public void setToDate(String toDate) {
        ToDate = toDate;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }
}
