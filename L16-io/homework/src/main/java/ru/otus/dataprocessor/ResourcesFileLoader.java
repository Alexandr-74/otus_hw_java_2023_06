package ru.otus.dataprocessor;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import java.io.*;
import java.util.*;
import lombok.SneakyThrows;
import ru.otus.model.Measurement;

public class ResourcesFileLoader implements Loader {

    private final String textFile;

    public ResourcesFileLoader(String fileName) {
        textFile = fileName;
    }

    @Override
    @SneakyThrows
    public List<Measurement> load() {
        try (JsonReader reader =
                new JsonReader(
                        new FileReader(
                                Objects.requireNonNull(
                                                getClass().getClassLoader().getResource(textFile))
                                        .toURI()
                                        .getPath()))) {
            JsonArray rootObj = JsonParser.parseReader(reader).getAsJsonArray();
            List<Measurement> measurementList = new ArrayList<>();
            for (JsonElement jsonElement : rootObj) {
                measurementList.add(
                        new Measurement(
                                jsonElement.getAsJsonObject().get("name").getAsString(),
                                jsonElement.getAsJsonObject().get("value").getAsDouble()));
            }

            return measurementList;
        }
    }
}
