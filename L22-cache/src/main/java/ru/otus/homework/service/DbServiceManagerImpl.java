package ru.otus.homework.service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.MyCache;
import ru.otus.homework.core.repository.DataTemplate;
import ru.otus.homework.core.sessionmanager.TransactionRunner;
import ru.otus.homework.model.Manager;

public class DbServiceManagerImpl implements DBServiceManager {
    private static final Logger log = LoggerFactory.getLogger(DbServiceManagerImpl.class);

    private final DataTemplate<Manager> managerDataTemplate;
    private final TransactionRunner transactionRunner;
    private final MyCache<Long, Manager> myCache;

    public DbServiceManagerImpl(
            TransactionRunner transactionRunner,
            DataTemplate<Manager> managerDataTemplate,
            MyCache<Long, Manager> myCache) {
        this.transactionRunner = transactionRunner;
        this.managerDataTemplate = managerDataTemplate;
        this.myCache = myCache;
    }

    @Override
    public Manager saveManager(Manager manager) {
        return transactionRunner.doInTransaction(connection -> {
            if (manager.getNo() == null) {
                var managerNo = managerDataTemplate.insert(connection, manager);
                var createdManager = new Manager(managerNo, manager.getLabel(), manager.getParam1());
                log.info("created manager: {}", createdManager);
                return createdManager;
            }
            managerDataTemplate.update(connection, manager);
            log.info("updated manager: {}", manager);
            myCache.put(manager.getNo(), manager);
            return manager;
        });
    }

    @Override
    public Optional<Manager> getManager(long no) {
        Manager manager = myCache.get(no);
        return manager != null
                ? Optional.of(manager)
                : transactionRunner.doInTransaction(connection -> {
                    var managerOptional = managerDataTemplate.findById(connection, no);
                    log.info("manager: {}", managerOptional);
                    return managerOptional;
                });
    }

    @Override
    public List<Manager> findAll() {
        return transactionRunner.doInTransaction(connection -> {
            var managerList = managerDataTemplate.findAll(connection);
            log.info("managerList:{}", managerList);
            return managerList;
        });
    }
}
