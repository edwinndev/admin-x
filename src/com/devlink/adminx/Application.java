package com.devlink.adminx;

import com.devlink.adminx.controller.LoginController;
import com.devlink.adminx.model.User;
import com.devlink.adminx.view.LoginView;
import javax.swing.*;

public class Application {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginView view = new LoginView();
            LoginController loginController = new LoginController(new User(), view);
            loginController.launch();
        });
    }
}
