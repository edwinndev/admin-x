package com.devlink.adminx.model;

public class Work {
    private String month;
    private String year;
    private double hours;
    private double total;

    public Work() {
    }

    public Work(String month, String year, double hours, double total) {
        this.month = month;
        this.year = year;
        this.hours = hours;
        this.total = total;
    }

    public Work(String[] details) {
        this.month = details[0];
        this.year = details[1];
        this.hours = Double.parseDouble(details[2]);
        this.total = Double.parseDouble(details[3]);
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

    public double getHours() {
        return hours;
    }

    public void setHours(double hours) {
        this.hours = hours;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return month + ',' + year + ',' + hours + ',' + total;
    }
}
