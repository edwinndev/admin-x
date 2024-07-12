package com.devlink.adminx.view;

import com.devlink.adminx.model.Work;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class WorkView extends JDialog {
    public JLabel labelNames;
    public JLabel labelCode;
    public JLabel labelEmail;
    public JLabel labelExpectedHours;
    public JLabel labelCurrentHours;
    public JLabel labelTotalImport;
    public JButton btnSendMail;
    public JButton btnAddTime;
    public JButton btnDelete;
    public JButton btnSave;
    public JProgressBar progressBar;
    private final ViewFactory viewFactory;
    public JTable worksTable;
    public DefaultTableModel tableModel;

    public WorkView(Frame owner, boolean modal) {
        super(owner, modal);
        setTitle("Registro de Horas");
        setSize(800, 600);
        setLocationRelativeTo(owner);
        viewFactory = new ViewFactory();

        // Información del usuario
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        headerPanel.setBackground(new Color(220, 240, 230));

        // Imagen del usuario
        JLabel userImage = new JLabel();
        userImage.setPreferredSize(new Dimension(100, 100));
        userImage.setIcon(new ImageIcon("resources/images/logo.png"));
        userImage.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        headerPanel.add(userImage, BorderLayout.WEST);

        // Horas del empleado
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.setBackground(new Color(220, 240, 230));
        userInfoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        Font font = new Font("Arial", Font.BOLD, 18);
        labelCode = new JLabel("");
        labelNames = new JLabel("");
        labelEmail = new JLabel("");
        btnSendMail = viewFactory.createStyledButton("Enviar Reporte");
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);

        labelNames.setFont(font);
        labelCode.setFont(font);
        labelEmail.setFont(font);

        userInfoPanel.add(labelCode);
        userInfoPanel.add(labelNames);
        userInfoPanel.add(labelEmail);
        userInfoPanel.add(btnSendMail);
        userInfoPanel.add(progressBar);

        headerPanel.add(userInfoPanel, BorderLayout.CENTER);

        // Panel para horas esperadas, registradas y extra
        JPanel hoursPanel = new JPanel(new GridLayout(1, 3));
        hoursPanel.setBackground(new Color(220, 240, 230));
        hoursPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        labelExpectedHours = new JLabel("");
        labelCurrentHours = new JLabel("");
        labelTotalImport = new JLabel("");
        hoursPanel.add(createHourPanel("Horas Esperadas", labelExpectedHours, Color.BLUE));
        hoursPanel.add(createHourPanel("Horas Registradas", labelCurrentHours, Color.MAGENTA));
        hoursPanel.add(createHourPanel("Total importe", labelTotalImport, Color.ORANGE));

        headerPanel.add(hoursPanel, BorderLayout.SOUTH);

        JPanel menuPanel = new JPanel(new BorderLayout());
        btnSave = viewFactory.createStyledButton("Guargar");
        btnDelete = viewFactory.createStyledButton("Eliminar");
        btnAddTime = viewFactory.createStyledButton("Agregar");
        menuPanel.add(btnSave, BorderLayout.EAST);
        menuPanel.add(btnDelete, BorderLayout.CENTER);
        menuPanel.add(btnAddTime, BorderLayout.WEST);

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        //Tabla para el registro de horas
        worksTable = buildTable();
        worksTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor());

        JScrollPane scrollPane = new JScrollPane(worksTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel otherPanel = new JPanel(new BorderLayout());
        otherPanel.add(menuPanel, BorderLayout.NORTH);
        otherPanel.add(mainPanel, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);
        add(otherPanel, BorderLayout.CENTER);
    }

    private JTable buildTable() {
        String[] columnNames = {"Mes", "Año", "Horas inputadas", "Pago", "Enviar"};

        tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        table.setRowHeight(25);
        return table;
    }

    public void addWorkToTable(Work work) {
        tableModel.addRow(new Object[]{
                work.getMonth(), work.getYear(), work.getHours(), work.getTotal()
        });
    }

    private JPanel createHourPanel(String title, JLabel labelValue, Color color) {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(220, 240, 230));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        //panel.setBorder(BorderFactory.createLineBorder(color, 2));

        JLabel labelTitle = new JLabel(title);
        labelTitle.setForeground(color);
        labelTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        Font font = new Font("Arial", Font.BOLD, 22);
        labelValue.setForeground(color);
        labelValue.setFont(font);
        labelValue.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(labelTitle, BorderLayout.NORTH);
        panel.add(labelValue, BorderLayout.SOUTH);
        return panel;
    }

    class ButtonEditor extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {
        private final JButton button;

        public ButtonEditor() {
            button = viewFactory.createStyledButton("Enviar");
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            button.addActionListener(e -> fireEditingStopped());
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return button;
        }
    }
}

