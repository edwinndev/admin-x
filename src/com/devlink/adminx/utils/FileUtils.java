package com.devlink.adminx.utils;

import com.devlink.adminx.model.Work;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    private static final String CSV_SPLIT = ",";

    public static List<Work> readHoursFromCSVByUser(Path path) {
        List<Work> data = new ArrayList<>();

        try (FileReader fileReader = new FileReader(path.toFile().getPath());
             BufferedReader br = new BufferedReader(fileReader)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] details = line.split(CSV_SPLIT);
                Work work = new Work(details);
                data.add(work);
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        return data;
    }

    public static void updateWorksCSV(List<String> works, String employeeCode) {
        try {
            Path path = Paths.get(Var.RESOURCE_DATA.concat(employeeCode).concat(".csv"));
            Files.write(path, works, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }
}
