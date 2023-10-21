package ru.otus.homework.service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.MyCache;
import ru.otus.homework.core.repository.DataTemplate;
import ru.otus.homework.core.sessionmanager.TransactionRunner;
import ru.otus.homework.model.Client;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final DataTemplate<Client> dataTemplate;
    private final TransactionRunner transactionRunner;
    private final MyCache<Long, Client> myCache;

    public DbServiceClientImpl(
            TransactionRunner transactionRunner, DataTemplate<Client> dataTemplate, MyCache<Long, Client> myCache) {
        this.transactionRunner = transactionRunner;
        this.dataTemplate = dataTemplate;
        this.myCache = myCache;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionRunner.doInTransaction(connection -> {
            if (client.getId() == null) {
                var clientId = dataTemplate.insert(connection, client);
                var createdClient = new Client(clientId, client.getName());
                log.info("created client: {}", createdClient);
                return createdClient;
            }
            dataTemplate.update(connection, client);
            log.info("updated client: {}", client);
            myCache.put(client.getId(), client);
            return client;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        Client client = myCache.get(id);

        return client != null
                ? Optional.of(client)
                : transactionRunner.doInTransaction(connection -> {
                    var clientOptional = dataTemplate.findById(connection, id);
                    log.info("client: {}", clientOptional);
                    clientOptional.ifPresent(c->myCache.put(c.getId(), c));
                    return clientOptional;
                });
    }

    @Override
    public List<Client> findAll() {
        return transactionRunner.doInTransaction(connection -> {
            var clientList = dataTemplate.findAll(connection);
            log.info("clientList:{}", clientList);
            return clientList;
        });
    }
}
