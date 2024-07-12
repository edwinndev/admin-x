package com.devlink.adminx.view;

import com.devlink.adminx.model.Employee;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class EmployeeView extends JFrame {
    private JTextField codigoField;
    private JTextField cedulaField;
    private JTextField nombreField;
    private JTextField apellidoField;
    private JTextField direccionField;
    private JTextField telefonoField;
    private JTextField fechaIngresoField;
    private JTextField cargoField;
    private JTextField departamentoField;
    private JTextField salarioField;
    private JButton saveButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton addButton; // Nuevo botón de "Agregar"
    private JTable employeeTable;
    private DefaultTableModel tableModel;

    public EmployeeView() {
        setTitle("Employee Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(900, 600));

        // Crear y configurar paneles
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel formPanel = createFormPanel();
        JPanel buttonPanel = createButtonPanel();
        JPanel tablePanel = createTablePanel();

        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.WEST); // Cambiado a WEST para la alineación de botones
        mainPanel.add(tablePanel, BorderLayout.CENTER); // Cambiado a CENTER para la alineación de la tabla

        getContentPane().add(mainPanel);
        pack();
        setLocationRelativeTo(null); // Centrar ventana en la pantalla
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Datos del Empleado"));
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 10, 10);

        codigoField = createRoundedTextField();
        addField(panel, "Codigo:", codigoField, gbc);

        cedulaField = createRoundedTextField();
        addField(panel, "Cedula:", cedulaField, gbc);

        nombreField = createRoundedTextField();
        addField(panel, "Nombre:", nombreField, gbc);

        apellidoField = createRoundedTextField();
        addField(panel, "Apellido:", apellidoField, gbc);

        direccionField = createRoundedTextField();
        addField(panel, "Direccion:", direccionField, gbc);

        telefonoField = createRoundedTextField();
        addField(panel, "Telefono:", telefonoField, gbc);

        fechaIngresoField = createRoundedTextField();
        addField(panel, "Fecha de Ingreso:", fechaIngresoField, gbc);

        cargoField = createRoundedTextField();
        addField(panel, "Cargo:", cargoField, gbc);

        departamentoField = createRoundedTextField();
        addField(panel, "Departamento:", departamentoField, gbc);

        salarioField = createRoundedTextField();
        addField(panel, "Salario:", salarioField, gbc);

        return panel;
    }

    private JTextField createRoundedTextField() {
        JTextField textField = new JTextField(20);
        textField.setFont(new Font("Arial", Font.PLAIN, 16));
        textField.setPreferredSize(new Dimension(250, 30));
        textField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        textField.setOpaque(false);
        textField.setBackground(new Color(240, 240, 240));
        return textField;
    }

    private void addField(JPanel panel, String labelText, JTextField textField, GridBagConstraints gbc) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 16));

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(textField, gbc);

        gbc.gridy++;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 0, 20)); // GridLayout para los botones
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        saveButton = createStyledButton("Guardar");
        updateButton = createStyledButton("Actualizar");
        deleteButton = createStyledButton("Eliminar");
        addButton = createStyledButton("Agregar"); // Botón de "Agregar"

        panel.add(saveButton);
        panel.add(updateButton);
        panel.add(deleteButton);
        panel.add(addButton); // Agregar botón de "Agregar"

        return panel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(120, 40));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(51, 153, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Lista de Empleados"));
        panel.setBackground(Color.WHITE);

        tableModel = new DefaultTableModel(new Object[]{"Codigo", "Cedula", "Nombre", "Apellido", "Direccion", "Telefono", "Fecha de Ingreso", "Cargo", "Departamento", "Salario"}, 0);
        employeeTable = new JTable(tableModel);
        employeeTable.setFont(new Font("Arial", Font.PLAIN, 14));
        employeeTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        scrollPane.setPreferredSize(new Dimension(800, 200));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    public void addEmployeeToTable(Employee employee) {
        tableModel.addRow(new Object[]{
                employee.getCodigo(), employee.getCedula(), employee.getNombre(),
                employee.getApellido(), employee.getDireccion(), employee.getTelefono(),
                employee.getFechaIngreso(), employee.getCargo(), employee.getDepartamento(), employee.getSalario()
        });
    }

    public void updateEmployeeInTable(Employee employee) {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow >= 0) {
            tableModel.setValueAt(employee.getCedula(), selectedRow, 1);
            tableModel.setValueAt(employee.getNombre(), selectedRow, 2);
            tableModel.setValueAt(employee.getApellido(), selectedRow, 3);
            tableModel.setValueAt(employee.getDireccion(), selectedRow, 4);
            tableModel.setValueAt(employee.getTelefono(), selectedRow, 5);
            tableModel.setValueAt(employee.getFechaIngreso(), selectedRow, 6);
            tableModel.setValueAt(employee.getCargo(), selectedRow, 7);
            tableModel.setValueAt(employee.getDepartamento(), selectedRow, 8);
            tableModel.setValueAt(employee.getSalario(), selectedRow, 9);
        }
    }

    public void deleteEmployeeFromTable() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow >= 0) {
            tableModel.removeRow(selectedRow);
        }
    }

    public Employee getEmployeeFromFields() {
        return new Employee(
                codigoField.getText(), cedulaField.getText(), nombreField.getText(), apellidoField.getText(),
                direccionField.getText(), telefonoField.getText(), fechaIngresoField.getText(),
                cargoField.getText(), departamentoField.getText(), Double.parseDouble(salarioField.getText())
        );
    }

    public void setFields(Employee employee) {
        codigoField.setText(employee.getCodigo());
        cedulaField.setText(employee.getCedula());
        nombreField.setText(employee.getNombre());
        apellidoField.setText(employee.getApellido());
        direccionField.setText(employee.getDireccion());
        telefonoField.setText(employee.getTelefono());
        fechaIngresoField.setText(employee.getFechaIngreso());
        cargoField.setText(employee.getCargo());
        departamentoField.setText(employee.getDepartamento());
        salarioField.setText(String.valueOf(employee.getSalario()));
    }

    public JButton getSaveButton() {
        return saveButton;
    }

    public JButton getUpdateButton() {
        return updateButton;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }

    public JButton getAddButton() {
        return addButton;
    }

    public JTable getEmployeeTable() {
        return employeeTable;
    }

    public void addSaveListener(ActionListener listener) {
        saveButton.addActionListener(listener);
    }

    public void addUpdateListener(ActionListener listener) {
        updateButton.addActionListener(listener);
    }

    public void addDeleteListener(ActionListener listener) {
        deleteButton.addActionListener(listener);
    }

    public void addAddListener(ActionListener listener) {
        addButton.addActionListener(listener);
    }

    public void addEmployeeSelectionListener(ListSelectionListener listener) {
        employeeTable.getSelectionModel().addListSelectionListener(listener);
    }

    public JTextField getCodigoField() {
        return codigoField;
    }

    public JTextField getCedulaField() {
        return cedulaField;
    }

    public JTextField getNombreField() {
        return nombreField;
    }

    public JTextField getApellidoField() {
        return apellidoField;
    }

    public JTextField getDireccionField() {
        return direccionField;
    }

    public JTextField getTelefonoField() {
        return telefonoField;
    }

    public JTextField getFechaIngresoField() {
        return fechaIngresoField;
    }

    public JTextField getCargoField() {
        return cargoField;
    }

    public JTextField getDepartamentoField() {
        return departamentoField;
    }

    public JTextField getSalarioField() {
        return salarioField;
    }
}
