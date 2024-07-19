package com.devlink.adminx.view;

import com.devlink.adminx.model.Employee;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class EmployeeView extends JFrame {
    public JTextField txtCode;
    public JTextField txtEmail;
    public JTextField txtNames;
    public JTextField txtAddress;
    public JTextField txtPhone;
    public JTextField txtAdmissionDate;
    public JTextField txtCategory;
    public JTextField txtSalary;
    private JButton btnSave;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnAdd;
    public JButton btnAddHours;
    public JTable tableEmployees;
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
        mainPanel.add(buttonPanel, BorderLayout.WEST);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        getContentPane().add(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setExtendedState(MAXIMIZED_BOTH);
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

        txtCode = createRoundedTextField();
        txtCode.setEnabled(false);
        txtCode.setEditable(false);
        addField(panel, "Codigo:", txtCode, gbc);

        txtEmail = createRoundedTextField();
        addField(panel, "Email:", txtEmail, gbc);

        txtNames = createRoundedTextField();
        addField(panel, "Nombres:", txtNames, gbc);

        txtAddress = createRoundedTextField();
        addField(panel, "Direccion:", txtAddress, gbc);

        txtPhone = createRoundedTextField();
        addField(panel, "Telefono:", txtPhone, gbc);

        txtAdmissionDate = createRoundedTextField();
        addField(panel, "Fecha de Ingreso:", txtAdmissionDate, gbc);

        txtCategory = createRoundedTextField();
        addField(panel, "Categoria:", txtCategory, gbc);

        txtSalary = createRoundedTextField();
        addDecimalValidator(txtSalary);
        addField(panel, "Salario Mensual:", txtSalary, gbc);

        ViewFactory viewFactory = new ViewFactory();
        btnAddHours = viewFactory.createStyledButton("Ver honorarios");
        btnAddHours.setEnabled(false);
        addField(panel, "", btnAddHours, gbc);

        return panel;
    }

    private void addDecimalValidator(JTextComponent component) {
        component.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char input = e.getKeyChar();
                String text = component.getText();

                if (!Character.isDigit(input) && input != '.' && input != KeyEvent.VK_BACK_SPACE) {
                    e.consume();
                } else if (input == '.' && text.contains(".")) {
                    e.consume();
                }
            }
        });
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

    private void addField(JPanel panel, String labelText, JComponent textField, GridBagConstraints gbc) {
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
        JPanel panel = new JPanel(new GridLayout(4, 1, 0, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        ViewFactory viewFactory = new ViewFactory();

        btnSave = viewFactory.createStyledButton("Guardar");
        btnUpdate = viewFactory.createStyledButton("Actualizar");
        btnDelete = viewFactory.createStyledButton("Eliminar");
        btnAdd = viewFactory.createStyledButton("Agregar");

        panel.add(btnSave);
        panel.add(btnUpdate);
        panel.add(btnDelete);
        panel.add(btnAdd);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Lista de Empleados"));
        panel.setBackground(Color.WHITE);

        String[] columns = {"Codigo", "Email", "Nombres", "Direccion", "Telefono", "Fecha de Ingreso", "Categoria", "Salario"};
        tableModel = new DefaultTableModel(columns, 0);
        tableEmployees = new JTable(tableModel);
        tableEmployees.setFont(new Font("Arial", Font.PLAIN, 14));
        tableEmployees.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(tableEmployees);
        scrollPane.setPreferredSize(new Dimension(800, 200));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    public void addEmployeeToTable(Employee employee) {
        tableModel.addRow(new Object[]{
                employee.getCode(), employee.getEmail(), employee.getNames(), employee.getAddress(), employee.getPhone(),
                employee.getAdmissionDate(), employee.getCategory(), employee.getSalary()
        });
    }

    public void updateEmployeeInTable(Employee employee) {
        int selectedRow = tableEmployees.getSelectedRow();
        if (selectedRow >= 0) {
            tableModel.setValueAt(employee.getCode(), selectedRow, 0);
            tableModel.setValueAt(employee.getEmail(), selectedRow, 1);
            tableModel.setValueAt(employee.getNames(), selectedRow, 2);
            tableModel.setValueAt(employee.getAddress(), selectedRow, 3);
            tableModel.setValueAt(employee.getPhone(), selectedRow, 4);
            tableModel.setValueAt(employee.getAdmissionDate(), selectedRow, 5);
            tableModel.setValueAt(employee.getCategory(), selectedRow, 6);
            tableModel.setValueAt(employee.getSalary(), selectedRow, 7);
        }
    }

    public void deleteEmployeeFromTable() {
        int selectedRow = tableEmployees.getSelectedRow();
        if (selectedRow >= 0) {
            tableModel.removeRow(selectedRow);
        }
    }

    public Employee getEmployeeFromFields() {
        String strAdmissionDate = txtAdmissionDate.getText();
        if(strAdmissionDate.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar la fecha de ingreso del empleado");
            return null;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate currentDatePlus = LocalDate.now().plusDays(20);
            LocalDate admissionDate   = LocalDate.parse(strAdmissionDate, formatter);
            if(admissionDate.isAfter(currentDatePlus)) {
                JOptionPane.showMessageDialog(this, "La fecha de ingreso no debe ser mayor al " + formatter.format(currentDatePlus));
                return null;
            }

            return new Employee(
                    txtCode.getText(), txtEmail.getText(), txtNames.getText(),
                    txtAddress.getText(), txtPhone.getText(), admissionDate,
                    txtCategory.getText(), Double.parseDouble(txtSalary.getText()));
        } catch (DateTimeParseException | NullPointerException e) {
            JOptionPane.showMessageDialog(this, "La fecha de ingreso es incorrecta. Debe tener el siguiente formato: dd-MM-yyyy" );
            return null;
        }
    }

    public void setFields(Employee employee) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        txtCode.setText(employee.getCode());
        txtEmail.setText(employee.getEmail());
        txtNames.setText(employee.getNames());
        txtAddress.setText(employee.getAddress());
        txtPhone.setText(employee.getPhone());
        txtAdmissionDate.setText(formatter.format(employee.getAdmissionDate()));
        txtCategory.setText(employee.getCategory());
        txtSalary.setText(String.valueOf(employee.getSalary()));
    }

    public void addSaveEmployeeListener(ActionListener listener) {
        btnSave.addActionListener(listener);
    }

    public void addUpdateEmployeeListener(ActionListener listener) {
        btnUpdate.addActionListener(listener);
    }

    public void addDeleteEmployeeListener(ActionListener listener) {
        btnDelete.addActionListener(listener);
    }

    public void addAddEmployeeListener(ActionListener listener) {
        btnAdd.addActionListener(listener);
    }

    public void addAddTimeEmployeeListener(ActionListener listener) {
        btnAddHours.addActionListener(listener);
    }

    public void addEmployeeSelectionListener(ListSelectionListener listener) {
        tableEmployees.getSelectionModel().addListSelectionListener(listener);
    }
}
