package com.devlink.adminx.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.io.IOException;

public class LoginView extends JFrame {
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JButton loginButton;

    public LoginView() {
        // Configuración de la ventana
        setTitle("Sistema de acceso");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 450);
        setResizable(false);
        setLocationRelativeTo(null);

        // Crear panel principal
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        add(mainPanel);

        // Panel del logo
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(new Color(29, 150, 152));
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        ImageIcon logoIcon = createImageIcon("resources/images/logo-blanco.png", 300, 80);
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel titleLabel = new JLabel("<html><div style='text-align: center;'>Sistema para la gestión de recursos humanos - Pago a trabajadores</div></html>");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Sans Serif", Font.BOLD, 18)); // Ajustar el tamaño para que el texto entre completo
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        logoPanel.add(Box.createVerticalGlue());
        logoPanel.add(logoLabel);
        logoPanel.add(Box.createVerticalStrut(10));
        logoPanel.add(titleLabel);
        logoPanel.add(Box.createVerticalGlue());

        mainPanel.add(logoPanel);

        // Panel del formulario
        JPanel formPanel = new JPanel();
        formPanel.setBackground(new Color(43, 67, 89));
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel formTitleLabel = new JLabel("Iniciar Sesión");
        formTitleLabel.setForeground(Color.WHITE);
        formTitleLabel.setFont(new Font("Sans Serif", Font.BOLD, 24));
        formTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        usernameField = new RoundedTextField(18);
        passwordField = new RoundedPasswordField(18);
        loginButton = new RoundedButton("Iniciar Sesión");
        JCheckBox rememberMeCheckBox = new JCheckBox("Recuérdame");

        // Estilo de componentes
        Font customFont = new Font("Sans Serif", Font.PLAIN, 16); // Aumentar tamaño del texto
        usernameField.setFont(customFont);
        passwordField.setFont(customFont);
        loginButton.setFont(customFont);
        rememberMeCheckBox.setFont(customFont);
        rememberMeCheckBox.setForeground(Color.WHITE);
        rememberMeCheckBox.setOpaque(false);

        // Estilo del botón
        loginButton.setBackground(new Color(29, 150, 152));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Añadir componentes al panel del formulario
        formPanel.add(Box.createVerticalGlue());
        formPanel.add(formTitleLabel);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(createRow(createImageIcon("resources/icons/user_icon.png", 25, 25), usernameField));
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(createRow(createImageIcon("resources/icons/password_icon.png", 25, 25), passwordField));
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(rememberMeCheckBox);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(loginButton);
        formPanel.add(Box.createVerticalGlue());

        mainPanel.add(formPanel);
    }

    private ImageIcon createImageIcon(String path, int width, int height) {
        try {
            File imgFile = new File(path);
            if (!imgFile.exists()) {
                throw new IOException("File not found: " + path);
            }
            Image img = ImageIO.read(imgFile).getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar la imagen desde: " + path,
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return null;
        }
    }

    private JPanel createRow(ImageIcon icon, JComponent field) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5)); // Aumentar el espaciado horizontal
        row.setOpaque(false);
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setPreferredSize(new Dimension(30, 30));
        row.add(iconLabel);
        row.add(field);
        return row;
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public void addLoginListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    // Clases internas para redondear los componentes
    class RoundedTextField extends JTextField {
        private String placeholder;

        public RoundedTextField(int columns) {
            super(columns);
            this.placeholder = "nombre de usuario";
            setOpaque(false);
            setCursor(new Cursor(Cursor.TEXT_CURSOR));
            setForeground(new Color(192, 192, 192)); // Color gris claro para el texto de relleno
            setText(placeholder); // Establecer el texto de relleno inicialmente
            setFont(new Font("Sans Serif", Font.PLAIN, 17)); // Aumentar tamaño del texto

            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (getText().equals(placeholder)) {
                        setText("");
                        setForeground(Color.BLACK); // Cambiar el color del texto al negro al escribir
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if (getText().isEmpty()) {
                        setText(placeholder);
                        setForeground(new Color(192, 192, 192));
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(getBackground());
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
            g2d.dispose();
            super.paintComponent(g);
        }

        @Override
        protected void paintBorder(Graphics g) {
            // No hacer nada para no pintar el borde
        }

        @Override
        public void setSelectionColor(Color selectionColor) {
            // No hacer nada para evitar que se establezca el color de selección
        }
    }

    class RoundedPasswordField extends JPasswordField {
        private String placeholder;
        private boolean showingPlaceholder = true;

        public RoundedPasswordField(int columns) {
            super(columns);
            this.placeholder = "contraseña";
            setOpaque(false);
            setCursor(new Cursor(Cursor.TEXT_CURSOR));
            setForeground(new Color(192, 192, 192)); // Color gris claro para el texto de relleno
            setText(placeholder); // Establecer el texto de relleno inicialmente
            setFont(new Font("Sans Serif", Font.PLAIN, 17)); // Aumentar tamaño del texto

            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (showingPlaceholder) {
                        setText("");
                        setEchoChar('\u25CF'); // Usar • para mostrar el texto como contraseñas
                        setForeground(Color.BLACK); // Cambiar el color del texto al negro al escribir
                        showingPlaceholder = false;
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if (getPassword().length == 0) {
                        setText(placeholder);
                        setEchoChar((char) 0); // Mostrar texto normal cuando no hay contraseña escrita
                        setForeground(new Color(192, 192, 192));
                        showingPlaceholder = true;
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(getBackground());
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
            g2d.dispose();
            super.paintComponent(g);
        }

        @Override
        protected void paintBorder(Graphics g) {
            // No hacer nada para no pintar el borde
        }

        @Override
        public void setSelectionColor(Color selectionColor) {
            // No hacer nada para evitar que se establezca el color de selección
        }
    }

    class RoundedButton extends JButton {
        public RoundedButton(String text) {
            super(text);
            setOpaque(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(29, 150, 152), 1, true),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15) // Aumentar el espacio alrededor del texto
            ));
            setForeground(Color.WHITE);
            setFont(new Font("Sans Serif", Font.PLAIN, 17)); // Aumentar tamaño del texto
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (getModel().isArmed()) {
                g2d.setColor(getBackground().darker());
            } else {
                g2d.setColor(getBackground());
            }
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
            g2d.dispose();
            super.paintComponent(g);
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(new Color(29, 150, 152));
            g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, getHeight(), getHeight());
            g2d.dispose();
        }
    }
}