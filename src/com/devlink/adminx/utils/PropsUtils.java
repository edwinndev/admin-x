package com.devlink.adminx.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropsUtils {

    public static Properties loadProperties(String filePath) {
        try {
            Properties prop = new Properties();
            InputStream in = new FileInputStream(filePath);
            prop.load(in);
            in.close();
            return prop;
        } catch (IOException e) {
            e.printStackTrace(System.err);
            return new Properties();
        }
    }
}
