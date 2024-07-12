package com.devlink.adminx.model;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class UserRepository {
    private final List<UserModel> users;

    public UserRepository() {
        users = new ArrayList<>();
        loadDefaultUsers();
    }

    private void loadDefaultUsers() {
        loadUsers("users/user1.ini");
    }

    public void loadUsers(String filePath) {
        try {
            Properties prop = new Properties();
            InputStream in = new FileInputStream("resources/data/user.properties");
            prop.load(in);

            String username = prop.getProperty("username");
            String password = prop.getProperty("password");
            UserModel model = new UserModel(username, password);
            users.add(model);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar usuarios desde el archivo: " + filePath,
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(System.err);
        }
    }

    public boolean authenticate(String username, String password) {
        for (UserModel user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
}
