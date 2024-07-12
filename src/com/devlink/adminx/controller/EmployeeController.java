package com.devlink.adminx.controller;

import com.devlink.adminx.model.Employee;
import com.devlink.adminx.model.EmployeeManager;
import com.devlink.adminx.view.EmployeeView;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EmployeeController {
    private EmployeeView view;
    private EmployeeManager model;

    public EmployeeController(EmployeeView view, EmployeeManager model) {
        this.view = view;
        this.model = model;

        // Asignar listeners a los botones y a la tabla
        this.view.addSaveListener(new SaveEmployeeListener());
        this.view.addUpdateListener(new UpdateEmployeeListener());
        this.view.addDeleteListener(new DeleteEmployeeListener());
        this.view.addEmployeeSelectionListener(new EmployeeSelectionListener());
        this.view.addAddListener(new AddEmployeeListener());

        loadEmployeesToTable(); // Cargar los empleados existentes en la tabla al inicio
    }

    private void loadEmployeesToTable() {
        for (Employee employee : model.getEmployees()) {
            view.addEmployeeToTable(employee);
        }
    }

    public void launch() {
        this.view.setVisible(true);
    }

    class SaveEmployeeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Employee employee = view.getEmployeeFromFields();
            if (!model.isDuplicate(employee)) {
                model.addEmployee(employee);
                view.addEmployeeToTable(employee);
                clearFields(); // Limpiar campos después de agregar
            } else {
                JOptionPane.showMessageDialog(view, "¡Empleado con este código ya existe!");
            }
        }
    }

    class UpdateEmployeeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Employee employee = view.getEmployeeFromFields();
            model.updateEmployee(employee);
            view.updateEmployeeInTable(employee);
        }
    }

    class DeleteEmployeeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String codigo = view.getEmployeeFromFields().getCodigo();
            model.deleteEmployee(codigo);
            view.deleteEmployeeFromTable();
        }
    }

    class EmployeeSelectionListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) { // Para evitar que se llame dos veces
                int selectedRow = view.getEmployeeTable().getSelectedRow();
                if (selectedRow >= 0) {
                    String codigo = (String) view.getEmployeeTable().getValueAt(selectedRow, 0);
                    Employee selectedEmployee = model.getEmployeeByCodigo(codigo);
                    if (selectedEmployee != null) {
                        view.setFields(selectedEmployee);
                    }
                }
            }
        }
    }

    class AddEmployeeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            clearFields(); // Limpiar campos al presionar "Agregar"
        }
    }

    private void clearFields() {
        view.getCodigoField().setText("");
        view.getCedulaField().setText("");
        view.getNombreField().setText("");
        view.getApellidoField().setText("");
        view.getDireccionField().setText("");
        view.getTelefonoField().setText("");
        view.getFechaIngresoField().setText("");
        view.getCargoField().setText("");
        view.getDepartamentoField().setText("");
        view.getSalarioField().setText("");
    }
}
