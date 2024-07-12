package com.devlink.adminx.model;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Employee {

    private String codigo;
    private String cedula;
    private String nombre;
    private String apellido;
    private String direccion;
    private String telefono;
    private String fechaIngreso;
    private String cargo;
    private String departamento;
    private double salario;

    public Employee(String codigo, String cedula, String nombre, String apellido, String direccion,
                    String telefono, String fechaIngreso, String cargo, String departamento, double salario) {
        this.codigo = codigo;
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.direccion = direccion;
        this.telefono = telefono;
        this.fechaIngreso = fechaIngreso;
        this.cargo = cargo;
        this.departamento = departamento;
        this.salario = salario;
    }

    public Employee(String[] userDetails) {
        this.codigo = userDetails[0];
        this.cedula = userDetails[1];
        this.nombre = userDetails[2];
        this.apellido = userDetails[3];
        this.direccion = userDetails[4];
        this.telefono = userDetails[5];
        this.fechaIngreso = userDetails[6];
        this.cargo = userDetails[7];
        this.departamento = userDetails[8];
        this.salario = Double.parseDouble(userDetails[9]);
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(String fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
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
    public static void writeEmployeesToCSV(List<Employee> employees, String filePath) {
        try {
            Path path = Paths.get(filePath);
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
        return codigo + "," +
                cedula + "," +
                nombre + "," +
                apellido + "," +
                direccion + "," +
                telefono + "," +
                fechaIngreso + "," +
                cargo + "," +
                departamento + "," +
                salario;
    }
}
