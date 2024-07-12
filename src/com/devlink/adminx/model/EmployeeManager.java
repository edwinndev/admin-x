package com.devlink.adminx.model;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class EmployeeManager {
    private List<Employee> employees;
    private static final String CSV_FILE_PATH = "resources/data/employees.csv";

    public EmployeeManager() {
        try {
            Path path = Paths.get(CSV_FILE_PATH);
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
        Employee.writeEmployeesToCSV(employees, CSV_FILE_PATH);
    }

    public void updateEmployee(Employee updatedEmployee) {
        for (Employee employee : employees) {
            if (employee.getCodigo().equals(updatedEmployee.getCodigo())) {
                employee.setCedula(updatedEmployee.getCedula());
                employee.setNombre(updatedEmployee.getNombre());
                employee.setApellido(updatedEmployee.getApellido());
                employee.setDireccion(updatedEmployee.getDireccion());
                employee.setTelefono(updatedEmployee.getTelefono());
                employee.setFechaIngreso(updatedEmployee.getFechaIngreso());
                employee.setCargo(updatedEmployee.getCargo());
                employee.setDepartamento(updatedEmployee.getDepartamento());
                employee.setSalario(updatedEmployee.getSalario());
                break;
            }
        }
        Employee.writeEmployeesToCSV(employees, CSV_FILE_PATH);
    }

    public void deleteEmployee(String codigo) {
        employees.removeIf(employee -> employee.getCodigo().equals(codigo));
        Employee.writeEmployeesToCSV(employees, CSV_FILE_PATH);
    }

    public boolean isDuplicate(Employee newEmployee) {
        for (Employee employee : employees) {
            if (employee.getCodigo().equals(newEmployee.getCodigo())) {
                return true;
            }
        }
        return false;
    }

    public Employee getEmployeeByCodigo(String codigo) {
        for (Employee employee : employees) {
            if (employee.getCodigo().equals(codigo)) {
                return employee;
            }
        }
        return null;
    }
}
