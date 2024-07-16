package com.devlink.adminx.controller;

import com.devlink.adminx.model.Employee;
import com.devlink.adminx.model.WorkingTime;
import com.devlink.adminx.utils.EmailSender;
import com.devlink.adminx.utils.FileUtils;
import com.devlink.adminx.utils.Environment;
import com.devlink.adminx.view.WorkingTimeView;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class WorkingTimeController {
    private final Employee employee;
    private final WorkingTimeView view;
    private final EmailSender emailSender;
    private final Path employeeHoursPath;

    public WorkingTimeController(Employee employee, WorkingTimeView view) {
        this.employee = employee;
        this.view = view;
        this.emailSender = new EmailSender();

        String filePath = Environment.SRC_DATA_PATH
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

        List<WorkingTime> workingTimes = FileUtils.readHoursFromCSVByUser(employeeHoursPath);
        for (WorkingTime workingTime : workingTimes) {
            totalWorkHours += workingTime.getHours();
            view.addWorkToTable(workingTime);
        }

        double totalImport = employee.getSalary() / totalWorkHours;

        view.labelExpectedHours.setText(String.format("%.2f", totalHours));
        view.labelCurrentHours.setText(String.format("%.2f", totalWorkHours));
        view.labelTotalImport.setText(String.format("%.2f", totalImport));
    }

    private void startTask(ActionEvent e) {
        view.btnSendMail.setEnabled(false);

        List<WorkingTime> workingTimes = FileUtils.readHoursFromCSVByUser(employeeHoursPath);
        String finalHtml = getFinalHtml(workingTimes);

        SwingWorker<Void, Integer> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                view.progressBar.setVisible(true);

                for (int i = 0; i <= 100; i++) {
                    Thread.sleep(10);
                    publish(i);
                }
                boolean send = emailSender.sendEmail(employee, "Reporte de Horas", finalHtml);
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

    private String getFinalHtml(List<WorkingTime> workingTimes) {
        StringBuilder html = new StringBuilder();
        double totalImport = 0.0;
        for (WorkingTime workingTime : workingTimes) {
            String item = String.format(
                    """
                    <tr>
                           <td>%s</td>
                           <td>%s</td>
                           <td>%.2f</td>
                           <td>USD %.2f</td>
                           <td>USD %.2f</td>
                    </tr>
                    """, workingTime.getMonth(), workingTime.getYear(), workingTime.getHours(), employee.getSalary(), workingTime.getTotal());
            html.append(item);
            totalImport += workingTime.getTotal();
        }

        return String.format(
                """
               <table>
                   <thead>
                       <tr>
                           <th>Mes</th>
                           <th>Año</th>
                           <th>Horas inputadas</th>
                           <th>Importe Base</th>
                           <th>Importe Extra</th>
                       </tr>
                   </thead>
                   <tbody>
                       %s
                       <tr>
                           <td colspan="4" style="text-align: right;">Total</td>
                           <td>USD %.2f</td>
                       </tr>
                   </tbody>
               </table>
               """, html, totalImport);
    }

    class AddTimeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.addWorkToTable(new WorkingTime());
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

            WorkingTime workingTime = new WorkingTime(month, year, hours, total);
            data.add(workingTime.toString());
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
