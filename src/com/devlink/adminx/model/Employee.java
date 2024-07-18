package com.devlink.adminx.model;

import com.devlink.adminx.utils.Environment;

public class Employee {
    private String code;
    private String email;
    private String names;
    private String address;
    private String phone;
    private String admissionDate;
    private String category;
    private double salary;

    public Employee(String[] data) {
        this.code = data[0];
        this.email = data[1];
        this.names = data[2];
        this.address = data[3];
        this.phone = data[4];
        this.admissionDate = data[5];
        this.category = data[6];
        this.salary = Double.parseDouble(data[7]);
    }

    public Employee(String code, String email, String names, String address,
                    String phone, String admissionDate, String category, double salary) {
        this.code = code;
        this.email = email;
        this.names = names;
        this.address = address;
        this.phone = phone;
        this.admissionDate = admissionDate;
        this.category = category;
        this.salary = salary;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(String admissionDate) {
        this.admissionDate = admissionDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        char separator = Environment.CSV_SEPARATOR;
        return code + separator +
                email + separator +
                names + separator +
                address + separator +
                phone + separator +
                admissionDate + separator +
                category + separator +
                salary;
    }
}
