package ru.otus.homework.service;

import java.util.List;
import java.util.Optional;
import ru.otus.homework.model.Client;

public interface DBServiceClient {

    Client saveClient(Client client);

    Optional<Client> getClient(long id);

    List<Client> findAll();
}
