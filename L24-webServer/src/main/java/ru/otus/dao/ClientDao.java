package ru.otus.dao;

import java.util.Optional;

import ru.otus.model.Client;

public interface ClientDao {

    Optional<Client> findById(long id);

    Optional<Client> findByLogin(String login);
}
