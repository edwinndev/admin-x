package com.devlink.adminx.model;

import com.devlink.adminx.utils.Var;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class EmployeeManager {
    private List<Employee> employees;

    public EmployeeManager() {
        try {
            Path path = Paths.get(Var.CSV_FILE_PATH);
            if(Files.notExists(path))
                Files.createFile(path);
            this.employees = Employee.readEmployeesFromCSV(path);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    public List<Employee> getEmployees() {
        if(employees == null)
            return new ArrayList<>(0);
        return employees;
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
        Employee.updateEmployeesInCSV(employees);
    }

    public void updateEmployee(Employee updatedEmployee) {
        for (Employee employee : employees) {
            if (employee.getCode().equals(updatedEmployee.getCode())) {
                employee.setEmail(updatedEmployee.getEmail());
                employee.setNames(updatedEmployee.getNames());
                employee.setAddress(updatedEmployee.getAddress());
                employee.setPhone(updatedEmployee.getPhone());
                employee.setAdmissionDate(updatedEmployee.getAdmissionDate());
                employee.setCategory(updatedEmployee.getCategory());
                employee.setSalary(updatedEmployee.getSalary());
                Employee.updateEmployeesInCSV(employees);
                break;
            }
        }
    }

    public boolean deleteEmployee(String code) {
        return employees.removeIf(employee -> employee.getCode().equals(code));
    }

    public boolean isDuplicate(Employee newEmployee) {
        for (Employee employee : employees) {
            if (employee.getCode().equalsIgnoreCase(newEmployee.getCode()))
                return true;
        }
        return false;
    }

    public Employee getEmployeeByCode(String code) {
        for (Employee employee : employees) {
            if (employee.getCode().equals(code)) {
                return employee;
            }
        }
        return null;
    }
}
