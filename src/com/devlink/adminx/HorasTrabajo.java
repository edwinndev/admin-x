package com.devlink.adminx;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class HorasTrabajo extends JFrame {

    public HorasTrabajo() {
        setTitle("Registro de Horas");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Cabecera con información del usuario
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        headerPanel.setBackground(new Color(220, 240, 230));

        // Imagen del usuario
        JLabel userImage = new JLabel();
        userImage.setPreferredSize(new Dimension(100, 100));
        userImage.setIcon(new ImageIcon("../icons/user.png")); // Asegúrate de tener la imagen en el camino especificado
        userImage.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        headerPanel.add(userImage, BorderLayout.WEST);

        // Información del usuario
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.setBackground(new Color(220, 240, 230));
        userInfoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel userName = new JLabel("Nombre del Usuario");
        JLabel userDetails = new JLabel("Detalles del Usuario");

        userInfoPanel.add(userName);
        userInfoPanel.add(userDetails);

        headerPanel.add(userInfoPanel, BorderLayout.CENTER);

        // Panel para horas esperadas, registradas y extra
        JPanel hoursPanel = new JPanel(new GridLayout(1, 3));
        hoursPanel.setBackground(new Color(220, 240, 230));
        hoursPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        hoursPanel.add(createHourPanel("Horas Esperadas", Color.BLUE));
        hoursPanel.add(createHourPanel("Horas Registradas", Color.GREEN));
        hoursPanel.add(createHourPanel("Horas Extra", Color.ORANGE));

        headerPanel.add(hoursPanel, BorderLayout.SOUTH);

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Tabla para el registro de horas
        String[] columnNames = {"Día", "Inicio", "Fin", "Pausa", "Total", ""};
        Object[][] data = {
                {"Lunes 01", "", "", "", "", "+"},
                {"Martes 02", "", "", "", "", "+"},
                {"Miércoles 03", "", "", "", "", "+"},
                {"Jueves 04", "", "", "", "", "+"},
                {"Viernes 05", "", "", "", "", "+"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(model);
        table.setRowHeight(40);
        table.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JTextField()));

        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createHourPanel(String text, Color color) {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(220, 240, 230));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createLineBorder(color, 2));

        JLabel label = new JLabel(text);
        label.setForeground(color);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(label);
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HorasTrabajo().setVisible(true));
    }
}

// Renderizador y Editor para el botón en la tabla
class ButtonRenderer extends JButton implements TableCellRenderer {

    public ButtonRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value == null) ? "" : value.toString());
        return this;
    }
}

class ButtonEditor extends DefaultCellEditor {
    private String label;
    private boolean isPushed;

    public ButtonEditor(JTextField textField) {
        super(textField);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        label = (value == null) ? "" : value.toString();
        JButton button = new JButton(label);
        button.setOpaque(true);
        button.addActionListener(e -> fireEditingStopped());
        isPushed = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            // Acción cuando se presiona el botón
            JOptionPane.showMessageDialog(null, "Botón presionado");
        }
        isPushed = false;
        return label;
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }
}

