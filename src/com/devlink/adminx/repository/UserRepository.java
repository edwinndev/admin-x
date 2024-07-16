package com.devlink.adminx.repository;

import com.devlink.adminx.model.User;
import com.devlink.adminx.utils.Environment;
import com.devlink.adminx.utils.Security;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class UserRepository {

    public UserRepository() {}

    public boolean authenticate(User user)  {
        try {
            Properties properties = readPropertiesFile();

            String username = properties.getProperty("username");
            String encryptedPassword = properties.getProperty("password");

            if (username == null || encryptedPassword == null) {
                return createDefaultUser(properties, user);
            } else {
                Security security = new Security();
                String decryptedPassword = security.decrypt(encryptedPassword);

                return user.getUsername().equals(username)
                        && user.getPassword().equals(decryptedPassword);
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return false;
    }

    public Properties readPropertiesFile() throws IOException {
        Properties properties = new Properties();

        Path path = Paths.get(Environment.PROPS_USER_PATH);
        if(Files.notExists(path))
            Files.createFile(path);

        InputStream in = new FileInputStream(Environment.PROPS_USER_PATH);
        properties.load(in);
        return properties;
    }

    private boolean createDefaultUser(Properties props, User user) throws Exception {
        FileOutputStream fileOut = new FileOutputStream(Environment.PROPS_USER_PATH);

        Security security = new Security();
        String encryptedPassword = security.encrypt(user.getPassword());

        props.setProperty("username", user.getUsername());
        props.setProperty("password", encryptedPassword);
        props.store(fileOut, null);
        fileOut.close();
        return true;
    }
}
