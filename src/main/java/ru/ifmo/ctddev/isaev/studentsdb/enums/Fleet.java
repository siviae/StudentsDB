package ru.ifmo.ctddev.isaev.studentsdb.enums;

import ru.ifmo.ctddev.isaev.studentsdb.converter.MyEnumDbConverterUtils;

import javax.persistence.AttributeConverter;


/**
 * @author iisaev
 */
public enum Fleet implements MyEnum {
    BSF("ЧФ", "BSF"),
    POF("ТОФ", "POF"),
    CS("ЧЦП", "CS");

    private final String dbKey;

    private final String name;

    Fleet(String name, String dbKey) {
        this.name = name;
        this.dbKey = dbKey;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getDbKey() {
        return dbKey;
    }

    public static class Converter implements AttributeConverter<Fleet, String> {

        @Override
        public String convertToDatabaseColumn(Fleet attribute) {
            return MyEnumDbConverterUtils.convertToDatabaseColumn(attribute);
        }

        @Override
        public Fleet convertToEntityAttribute(String dbData) {
            return MyEnumDbConverterUtils.convertToEntityAttribute(dbData, values());
        }
    }
}
