package com.devlink.adminx.controller;

import com.devlink.adminx.model.Employee;
import com.devlink.adminx.model.Work;
import com.devlink.adminx.utils.EmailSender;
import com.devlink.adminx.utils.FileUtils;
import com.devlink.adminx.utils.Var;
import com.devlink.adminx.view.WorkView;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class WorkController {
    private final Employee employee;
    private final WorkView view;
    private final EmailSender emailSender;
    private final Path employeeHoursPath;

    public WorkController(Employee employee, WorkView view) {
        this.employee = employee;
        this.view = view;
        this.emailSender = new EmailSender();

        String filePath = Var.RESOURCE_DATA
                .concat(employee.getCode())
                .concat(".csv");
        employeeHoursPath = Paths.get(filePath);

        view.labelCode.setText("CODIGO     :  " + employee.getCode());
        view.labelNames.setText("NOMBRES :  " + employee.getNames());
        view.labelEmail.setText("EMAIL         :  " + employee.getEmail());
        view.btnSave.addActionListener(new SaveTimeListener());
        view.btnDelete.addActionListener(new DeleteTimeListener());
        view.btnAddTime.addActionListener(new AddTimeListener());
        view.btnSendMail.addActionListener(this::startTask);

        // Rellenar la tabla con horas de trabajo
        fillData();
    }

    public void launch() {
        this.view.setVisible(true);
    }

    private void fillData() {
        try {
            if(Files.notExists(employeeHoursPath))
                Files.createFile(employeeHoursPath);

            updateWorksTable();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    private void updateWorksTable() {
        // Limpiar tabla antes de cargar datos
        view.tableModel.setRowCount(0);

        double totalHours = 156;
        double totalWorkHours = 0.0;

        List<Work> works = FileUtils.readHoursFromCSVByUser(employeeHoursPath);
        for (Work work : works) {
            totalWorkHours += work.getHours();
            view.addWorkToTable(work);
        }

        double totalImport = employee.getSalary() / totalWorkHours;

        view.labelExpectedHours.setText(String.format("%.2f", totalHours));
        view.labelCurrentHours.setText(String.format("%.2f", totalWorkHours));
        view.labelTotalImport.setText(String.format("%.2f", totalImport));
    }

    private void startTask(ActionEvent e) {
        view.btnSendMail.setEnabled(false);

        List<Work> works = FileUtils.readHoursFromCSVByUser(employeeHoursPath);
        StringBuilder html = new StringBuilder();
        for (Work work : works) {
            html.append("<li>")
                    .append(work.getMonth())
                    .append(" : ")
                    .append(work.getYear())
                    .append(" - ")
                    .append(work.getHours())
                    .append(" horas trabajadas")
                    .append(" - USD ")
                    .append(work.getTotal())
                    .append("</li>");
        }

        String finalHtml = html.toString();
        SwingWorker<Void, Integer> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                view.progressBar.setVisible(true);

                for (int i = 0; i <= 100; i++) {
                    Thread.sleep(10);
                    publish(i);
                }
                boolean send = emailSender.sendEmail(employee.getEmail(), "Reporte de Horas", finalHtml);
                if(send) {
                    JOptionPane.showMessageDialog(view, "Reporte de Horas enviado");
                } else {
                    JOptionPane.showMessageDialog(view, "Error cuando se intenta enviar Reporte de Horas. Verificar correo");
                }
                return null;
            }

            @Override
            protected void process(List<Integer> chunks) {
                for (int value : chunks) {
                    view.progressBar.setValue(value);
                }
            }

            @Override
            protected void done() {
                view.btnSendMail.setEnabled(true);
                view.progressBar.setVisible(false);
                try {
                    get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace(System.err);
                }
            }
        };

        worker.execute();
    }

    class AddTimeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.addWorkToTable(new Work());
        }
    }

    class SaveTimeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            updateDataCSV();
            updateWorksTable();
        }
    }

    private void updateDataCSV() {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < view.tableModel.getRowCount(); i++) {
            String month = view.tableModel.getValueAt(i, 0).toString();
            String year = view.tableModel.getValueAt(i, 1).toString();
            double hours = Double.parseDouble(view.tableModel.getValueAt(i, 2).toString());
            double total = Double.parseDouble(view.tableModel.getValueAt(i, 3).toString());

            Work work = new Work(month, year, hours, total);
            data.add(work.toString());
        }
        FileUtils.updateWorksCSV(data, employee.getCode());
    }

    class DeleteTimeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = view.worksTable.getSelectedRow();
            if (selectedRow != -1) {
                view.tableModel.removeRow(selectedRow);
                updateDataCSV();
                updateWorksTable();
            } else {
                JOptionPane.showMessageDialog(view, "Fila no seleccionada", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
