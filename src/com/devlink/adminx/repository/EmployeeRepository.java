package com.devlink.adminx.repository;

import com.devlink.adminx.model.Employee;
import com.devlink.adminx.utils.Environment;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRepository {
    private List<Employee> employees;

    public EmployeeRepository() {
        try {
            Path path = Paths.get(Environment.CSV_EMPLOYEES_PATH);
            if(Files.notExists(path))
                Files.createFile(path);
            this.employees = readEmployeesFromCSV(path);
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
        updateEmployeesInCSV(employees);
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
                updateEmployeesInCSV(employees);
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

    // Leer empleados del archivo CSV
    public List<Employee> readEmployeesFromCSV(Path path) {
        List<Employee> employees = new ArrayList<>();

        try (FileReader fileReader = new FileReader(path.toFile().getPath());
             BufferedReader br = new BufferedReader(fileReader)) {

            String separator = Environment.CSV_SEPARATOR + "";  //Separador = , (coma)
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(separator);
                Employee employee = new Employee(data);
                employees.add(employee);
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        return employees;
    }

    // Guardar datos del empleado en el archivo CSV
    public void updateEmployeesInCSV(List<Employee> employees) {
        try {
            Path path = Paths.get(Environment.CSV_EMPLOYEES_PATH);
            List<String> strings = new ArrayList<>(employees.size());
            for (Employee employee : employees)
                strings.add(employee.toString());

            Files.write(path, strings, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }
}
