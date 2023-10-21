package ru.otus.homework.service;

import java.util.List;
import java.util.Optional;
import ru.otus.homework.model.Manager;

public interface DBServiceManager {

    Manager saveManager(Manager client);

    Optional<Manager> getManager(long no);

    List<Manager> findAll();
}
