package ru.ifmo.ctddev.isaev.studentsdb.enums;

import ru.ifmo.ctddev.isaev.studentsdb.converter.MyEnumDbConverterUtils;

import javax.persistence.AttributeConverter;


/**
 * @author iisaev
 */
public enum Language implements MyEnum {
    ENG("Анг.", "ENG"),
    GER("Нем.", "GER");

    private final String dbKey;

    private final String name;

    Language(String name, String dbKey) {
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

    @Override
    public String toString() {
        return getName();
    }

    public static class Converter implements AttributeConverter<Language, String> {

        @Override
        public String convertToDatabaseColumn(Language attribute) {
            return MyEnumDbConverterUtils.convertToDatabaseColumn(attribute);
        }

        @Override
        public Language convertToEntityAttribute(String dbData) {
            return MyEnumDbConverterUtils.convertToEntityAttribute(dbData, values());
        }
    }

}
