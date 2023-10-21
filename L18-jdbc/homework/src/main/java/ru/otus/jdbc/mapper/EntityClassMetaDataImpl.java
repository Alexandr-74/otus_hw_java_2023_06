package ru.otus.jdbc.mapper;

import ru.otus.crm.model.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    Field idField;
    List<Field> otherFields = new ArrayList<>();
    String className;
    Constructor<T> constructor;
    public EntityClassMetaDataImpl(Class<T> clazz) {
        className = clazz.getSimpleName();
        try {
        constructor = clazz.getConstructor();
        } catch (NoSuchMethodException ex) {
            throw new IllegalStateException();
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                if (idField!=null) {
                    throw new IllegalStateException();
                } else {
                    idField = field;
                }
            } else {
                otherFields.add(field);
            }
        }
    }

    @Override
    public String getName() {
        return className;
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        var res = new ArrayList<>(otherFields);
        res.add(0,idField);
        return res;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return otherFields;
    }
}
