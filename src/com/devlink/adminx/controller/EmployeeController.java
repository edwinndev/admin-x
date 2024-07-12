package com.devlink.adminx.controller;

import com.devlink.adminx.view.WorkView;
import com.devlink.adminx.model.Employee;
import com.devlink.adminx.model.EmployeeManager;
import com.devlink.adminx.utils.Security;
import com.devlink.adminx.view.EmployeeView;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EmployeeController {
    private final EmployeeView view;
    private final EmployeeManager model;

    public EmployeeController(EmployeeView view, EmployeeManager model) {
        this.view = view;
        this.model = model;

        // Asignar eventos a los botones y a la tabla
        this.view.addSaveEmployeeListener(new SaveEmployeeListener());
        this.view.addUpdateEmployeeListener(new UpdateEmployeeListener());
        this.view.addDeleteEmployeeListener(new DeleteEmployeeListener());
        this.view.addEmployeeSelectionListener(new EmployeeSelectionListener());
        this.view.addAddEmployeeListener(new AddEmployeeListener());
        this.view.addSendMailEmployeeListener(new AddHourWorkEmployeeListener());

        // Cargar los empleados existentes en la tabla al inicio
        loadEmployeesToTable();
    }

    private void loadEmployeesToTable() {
        for (Employee employee : model.getEmployees()) {
             view.addEmployeeToTable(employee);
        }
    }

    //Hacer visible la ventana
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

                // Limpiar campos después de agregar
                clearFields();
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
            String code = view.getEmployeeFromFields().getCode();

            if(code != null) {
                int option = JOptionPane.showConfirmDialog(view, "¿Eliminar empleado?", "System", JOptionPane.YES_NO_OPTION);
                if(option == JOptionPane.YES_OPTION) {
                    boolean deleted = model.deleteEmployee(code);
                    if(deleted) {
                        Employee.updateEmployeesInCSV(model.getEmployees());
                        view.deleteEmployeeFromTable();
                        clearFields();
                    } else {
                        JOptionPane.showMessageDialog(view, "¡Error al eliminar el empleado!");
                    }
                }
            }
        }
    }

    class EmployeeSelectionListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) { // Para evitar que se llame dos veces
                view.btnAddHours.setEnabled(false);
                int selectedRow = view.tableEmployees.getSelectedRow();
                if (selectedRow >= 0) {
                    view.btnAddHours.setEnabled(true);
                    String code = view.tableEmployees.getValueAt(selectedRow, 0).toString();
                    Employee selectedEmployee = model.getEmployeeByCode(code);
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
            clearFields();
            view.txtCode.setText(Security.generateUniqueCode() + "");
        }
    }

    class AddHourWorkEmployeeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Employee employee = view.getEmployeeFromFields();
            if(employee != null && employee.getCode() != null) {
                WorkView workView = new WorkView(view, true);
                WorkController workController = new WorkController(employee, workView);
                workController.launch();
            }
        }
    }

    private void clearFields() {
        view.txtCode.setText("");
        view.txtEmail.setText("");
        view.txtNames.setText("");
        view.txtAddress.setText("");
        view.txtPhone.setText("");
        view.txtAdmissionDate.setText("");
        view.txtCategory.setText("");
        view.txtSalary.setText("");
        view.btnAddHours.setEnabled(false);
    }
}
