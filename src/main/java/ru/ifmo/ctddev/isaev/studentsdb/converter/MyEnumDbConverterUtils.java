package ru.ifmo.ctddev.isaev.studentsdb.converter;

import ru.ifmo.ctddev.isaev.studentsdb.enums.MyEnum;

import java.util.Arrays;


/**
 * @author iisaev
 */
public class MyEnumDbConverterUtils {

    public static <T extends MyEnum> String convertToDatabaseColumn(T attribute) {
        return attribute == null ? null : attribute.getDbKey();
    }

    public static <T extends MyEnum> T convertToEntityAttribute(String dbData, T... values) {
        return Arrays.stream(values)
                .filter(enumValue -> enumValue.getDbKey().equals(dbData))
                .findFirst()
                .orElse(null);
    }
}