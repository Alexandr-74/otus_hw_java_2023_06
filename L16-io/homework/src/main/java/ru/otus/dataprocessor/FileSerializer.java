package ru.otus.dataprocessor;

import com.google.gson.Gson;
import java.io.FileWriter;
import java.util.Map;
import lombok.SneakyThrows;

public class FileSerializer implements Serializer {

    private final String file;

    public FileSerializer(String fileName) {
        file = fileName;
    }

    @Override
    @SneakyThrows
    public void serialize(Map<String, Double> data) {
        // формирует результирующий json и сохраняет его в файл
        Gson gson = new Gson();
        try (FileWriter fileWriter = new FileWriter(file)) {
            gson.toJson(data, fileWriter);
        }
    }
}
