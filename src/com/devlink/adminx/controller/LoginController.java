package com.devlink.adminx.controller;

import com.devlink.adminx.model.EmployeeManager;
import com.devlink.adminx.model.UserModel;
import com.devlink.adminx.model.UserRepository;
import com.devlink.adminx.view.EmployeeView;
import com.devlink.adminx.view.LoginView;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginController {
    private final UserModel userModel;
    private final UserRepository userRepository;
    private final LoginView view;

    public LoginController(UserModel userModel, LoginView view) {
        this.userModel = userModel;
        this.userRepository = new UserRepository();
        this.view = view;

        this.view.addLoginListener(new LoginListener());
    }

    public void launch() {
        this.view.setVisible(true);
    }

    class LoginListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String username = view.getUsername();
            String password = view.getPassword();
            userModel.setUsername(username);
            userModel.setPassword(password);

            if (userRepository.authenticate(username, password)) {
                JOptionPane.showMessageDialog(null, "Login exitoso!", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
                view.dispose();

                EmployeeView view = new EmployeeView();
                EmployeeManager model = new EmployeeManager();
                EmployeeController employeeController = new EmployeeController(view, model);
                employeeController.launch();
            } else {
                JOptionPane.showMessageDialog(null, "Usuario o contraseña inválidos.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                System.out.println("Usuario o contraseña inválidos.");
            }
        }
    }
}
