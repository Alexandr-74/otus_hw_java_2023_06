package ru.otus.homework.mapper;

import ru.otus.homework.core.repository.DataTemplate;
import ru.otus.homework.core.repository.DataTemplateException;
import ru.otus.homework.core.repository.executor.DbExecutor;
import ru.otus.homework.model.Id;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/** Сохратяет объект в базу, читает объект из базы */
@SuppressWarnings("java:S1068")
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final Class<T> tClass;
    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, Class<T> tClass) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.tClass = tClass;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {

        List<Field> fields = new ArrayList<>(Arrays.asList(tClass.getDeclaredFields()));
        return Optional.of(dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), Collections.singletonList(id), rs -> initResult(fields, rs))
                .orElseThrow(() -> new RuntimeException("Unexpected error")));
    }

    @Override
    public List<T> findAll(Connection connection) {
        List<Field> fields = new ArrayList<>(Arrays.asList(tClass.getDeclaredFields()));
        return dbExecutor
                .executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
                    var clientList = new ArrayList<T>();
                        while (true) {
                            try {
                                if (!rs.next()) break;
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                            clientList.add(initResult(fields, rs));
                        }
                        return clientList;

                })
                .orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    private T initResult(List<Field> fields, ResultSet rs) {
        T obj = null;
        try {
            obj = tClass.getConstructor().newInstance();
            int i = 0;
            for (Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (rs.next()) {
                    field.set(obj, rs.getObject(fields.get(i).getName(), fields.get(i).getType()));
                    i += 1;
                }
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                 SQLException e) {
            throw new RuntimeException(e);
        }
        return obj;
    }

    @Override
    public long insert(Connection connection, T client) {
        List<Object> idField = getFieldsValue(client);

        try {
            return dbExecutor.executeStatement(
                    connection, entitySQLMetaData.getInsertSql(), idField);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    private List<Object> getFieldsValue(T client) {
        var tClass = client.getClass();
        List<Object> idField = new ArrayList<>();
        for (Field field : tClass.getDeclaredFields()) {
            if (!field.isAnnotationPresent(Id.class)) {
                String str = extract(client, field);
                idField.add(str);
            }
        }
        return idField;
    }

    private String extract(T client, Field field) {
        String str = null;
        try {
            field.setAccessible(true);
            str = (String) field.get(client);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return str;
    }

    @Override
    public void update(Connection connection, T client) {
        try {
            dbExecutor.executeStatement(
                    connection, entitySQLMetaData.getUpdateSql(), List.of(client, client));
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }
}
