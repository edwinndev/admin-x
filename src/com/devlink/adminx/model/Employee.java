package com.devlink.adminx.model;

import com.devlink.adminx.utils.Var;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Employee {
    private String code;
    private String email;
    private String names;
    private String address;
    private String phone;
    private String admissionDate;
    private String category;
    private double salary;

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

    public Employee(String[] userDetails) {
        this.code = userDetails[0];
        this.email = userDetails[1];
        this.names = userDetails[2];
        this.address = userDetails[3];
        this.phone = userDetails[4];
        this.admissionDate = userDetails[5];
        this.category = userDetails[6];
        this.salary = Double.parseDouble(userDetails[7]);
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

    //Leer empleados del archivo
    public static List<Employee> readEmployeesFromCSV(Path path) {
        List<Employee> employees = new ArrayList<>();
        String csvSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(path.toFile().getPath()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] userDetails = line.split(csvSplitBy);
                Employee user = new Employee(userDetails);
                employees.add(user);
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        return employees;
    }

    //Guardar datos del empleado en el archivo
    public static void updateEmployeesInCSV(List<Employee> employees) {
        try {
            Path path = Paths.get(Var.CSV_FILE_PATH);
            List<String> strings = new ArrayList<>(employees.size());
            for (Employee employee : employees)
                strings.add(employee.toString());

            Files.write(path, strings, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    @Override
    public String toString() {
        return code + "," +
                email + "," +
                names + "," +
                address + "," +
                phone + "," +
                admissionDate + "," +
                category + "," +
                salary;
    }
}
