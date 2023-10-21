package ru.otus.db.repository;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.otus.db.repository.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;

import java.util.List;
import java.util.stream.Collectors;

public class ClientRepository {
    protected SessionFactory sessionFactory;
    protected TransactionManagerHibernate transactionManager;

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
    protected DataTemplateHibernate<Client> clientTemplate;


    public void setUp() {
        String dbUrl = "jdbc:postgresql://localhost:5430/demoDB";
        String dbUserName = "usr";
        String dbPassword = "pwd";

        var migrationsExecutor = new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword);
        migrationsExecutor.executeMigrations();

        Configuration configuration = new Configuration().configure(HIBERNATE_CFG_FILE);
        configuration.setProperty("hibernate.connection.url", dbUrl);
        configuration.setProperty("hibernate.connection.username", dbUserName);
        configuration.setProperty("hibernate.connection.password", dbPassword);

        sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);

        transactionManager = new TransactionManagerHibernate(sessionFactory);
        clientTemplate = new DataTemplateHibernate<>(Client.class);
    }


    public Client createClient(Client client)  {
        return transactionManager.doInTransaction(session -> {
            clientTemplate.insert(session, client);
            return client;
        });
    }

    public Client findById(Long id) {
        return transactionManager.doInReadOnlyTransaction(
                session -> clientTemplate.findById(session, id).map(Client::clone)).orElseThrow();
    }

    public List<Client> findAll() {
        return transactionManager.doInReadOnlyTransaction(
                session -> clientTemplate.findAll(session).stream().map(Client::clone)).collect(Collectors.toList());
    }
}
