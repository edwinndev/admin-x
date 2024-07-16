package com.devlink.adminx.model;

import com.devlink.adminx.utils.Environment;

public class WorkingTime {
    private String month;
    private String year;
    private double hours;
    private double total;

    public WorkingTime() {
    }

    public WorkingTime(String month, String year, double hours, double total) {
        this.month = month;
        this.year = year;
        this.hours = hours;
        this.total = total;
    }

    public WorkingTime(String[] data) {
        this.month = data[0];
        this.year = data[1];
        this.hours = Double.parseDouble(data[2]);
        this.total = Double.parseDouble(data[3]);
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
        char separator = Environment.CSV_SEPARATOR;
        return month + separator + year + separator + hours + separator + total;
    }
}
