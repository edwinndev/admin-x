package com.devlink.adminx.model;

import com.devlink.adminx.utils.Environment;

public class WorkingTime {
    private String month;
    private String year;
    private double baseAmount;
    private double retention;
    private double netAmount;

    public WorkingTime() {
    }

    //Getter - Setter

    public WorkingTime(String[] data) {
        this.month = data[0];
        this.year = data[1];
        this.baseAmount = Double.parseDouble(data[2]);
        this.retention = Double.parseDouble(data[3]);
        this.netAmount = Double.parseDouble(data[4]);
    }

    public WorkingTime(String month, String year, double baseAmount, double retention) {
        this.month = month;
        this.year = year;
        this.baseAmount = baseAmount;
        this.retention = retention;
    }

    public double calculateTotal() {
        return this.calculateTotal(this.baseAmount);
    }

    public double calculateTotal(double baseAmount) {
        double taxes = (baseAmount * this.retention / 100.00);
        return baseAmount - taxes;
    }

    public double calculateRetention() {
        return (baseAmount * this.retention / 100.00);
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public double getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(double baseAmount) {
        this.baseAmount = baseAmount;
    }

    public double getRetention() {
        return retention;
    }

    public void setRetention(double retention) {
        this.retention = retention;
    }

    public double getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(double netAmount) {
        this.netAmount = netAmount;
    }

    @Override
    public String toString() {
        char separator = Environment.CSV_SEPARATOR;
        return month + separator + year + separator + baseAmount + separator + retention + separator + netAmount;
    }
}
