package ru.otus.homework.mapper;

import java.lang.reflect.Field;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {
    EntityClassMetaData<?> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        return "select * from " + entityClassMetaData.getName();
    }

    @Override
    public String getSelectByIdSql() {
        return "select * from " + entityClassMetaData.getName()
                + " where "
                + entityClassMetaData.getIdField().getName() + "  = ?";
    }

    @Override
    public String getInsertSql() {
        return "insert into " + entityClassMetaData.getName() + "("
                + String.join(
                        ", ",
                        entityClassMetaData.getFieldsWithoutId().stream()
                                .map(Field::getName)
                                .toList()) + ") values ("
                + String.join(
                        ", ",
                        entityClassMetaData.getFieldsWithoutId().stream()
                                .map(Field::getName)
                                .map(s -> "?")
                                .toList())
                + ")";
    }

    @Override
    public String getUpdateSql() {
        return "update " + entityClassMetaData.getName() + " set " + entityClassMetaData.getIdField() + " = ? where "
                + entityClassMetaData.getIdField().getName() + " = ?";
    }
}
