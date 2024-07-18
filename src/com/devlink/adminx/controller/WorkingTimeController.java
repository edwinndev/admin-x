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

        double totalRetention = 0.0;
        double baseAccumulated = 0.0;
        double amountAccumulated = 0.0;

        List<WorkingTime> workingTimes = FileUtils.readDataFromCSVByUser(employeeHoursPath);
        for (WorkingTime workingTime : workingTimes) {
            totalRetention += workingTime.calculateRetention();
            baseAccumulated += workingTime.getBaseAmount();
            amountAccumulated += workingTime.getNetAmount();
            view.addWorkToTable(workingTime);
        }

        view.labelExpectedHours.setText(String.format("PEN %.2f", totalRetention));
        view.labelCurrentHours.setText(String.format("PEN %.2f", baseAccumulated));
        view.labelTotalImport.setText(String.format("PEN %.2f", amountAccumulated));
    }

    private void startTask(ActionEvent e) {
        view.btnSendMail.setEnabled(false);

        List<WorkingTime> workingTimes = FileUtils.readDataFromCSVByUser(employeeHoursPath);
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
            double retentionAmount = workingTime.calculateRetention();
            String item = String.format(
                    """
                    <tr>
                           <td>%s</td>
                           <td>%s</td>
                           <td>PEN %.2f</td>
                           <td>%.2f %%</td>
                           <td>PEN %.2f</td>
                           <td>PEN %.2f</td>
                    </tr>
                    """,
                    workingTime.getMonth(), workingTime.getYear(), workingTime.getBaseAmount(),
                    workingTime.getRetention(), retentionAmount, workingTime.getNetAmount());
            html.append(item);
            totalImport += workingTime.getNetAmount();
        }

        return String.format(
                """
               <table>
                   <thead>
                       <tr>
                           <th>Mes</th>
                           <th>Año</th>
                           <th>Monto Base</th>
                           <th>%% de Retencion</th>
                           <th>Monto de Retencion</th>
                           <th>Monto Neto</th>
                       </tr>
                   </thead>
                   <tbody>
                       %s
                       <tr>
                           <td colspan="5" style="text-align: right;"><b>Total</b></td>
                           <td><b>PEN %.2f</b></td>
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

            try {
                double baseAmount = Double.parseDouble(view.tableModel.getValueAt(i, 2).toString());
                double retention = Double.parseDouble(view.tableModel.getValueAt(i, 3).toString());

                WorkingTime workingTime = new WorkingTime(month, year, baseAmount, retention);
                double netAmount = workingTime.calculateTotal(baseAmount);
                workingTime.setNetAmount(netAmount);

                data.add(workingTime.toString());
            } catch (NumberFormatException | NullPointerException e) {
                JOptionPane.showMessageDialog(view, "Monto base o porcentaje de retencion deben ser numeros");
            }
        }
        FileUtils.updateWorksCSV(data, employee.getCode());
    }
}
