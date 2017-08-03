package ru.ifmo.ctddev.isaev.studentsdb.enums;

import ru.ifmo.ctddev.isaev.studentsdb.converter.MyEnumDbConverterUtils;

import javax.persistence.AttributeConverter;


/**
 * @author iisaev
 */
public enum Fleet implements MyEnum {
    BSF("ЧФ"),
    POF("ТОФ"),
    NF("СФ"),
    CS("ЧЦП");

    private final String name;

    Fleet(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getDbKey() {
        return name();
    }

    @Override
    public String toString() {
        return getName();
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
