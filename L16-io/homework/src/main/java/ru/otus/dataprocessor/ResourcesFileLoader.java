package ru.otus.dataprocessor;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import java.io.*;
import java.net.URISyntaxException;
import java.util.*;
import ru.otus.model.Measurement;

public class ResourcesFileLoader implements Loader {

    private final String textFile;

    public ResourcesFileLoader(String fileName) {
        textFile = fileName;
    }

    @Override
    public List<Measurement> load() {

        try (JsonReader reader =
                new JsonReader(
                        new FileReader(
                                Objects.requireNonNull(
                                                getClass().getClassLoader().getResource(textFile))
                                        .toURI()
                                        .getPath()))) {
            return new Gson().fromJson(reader, new TypeToken<List<Measurement>>() {}.getType());

        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
