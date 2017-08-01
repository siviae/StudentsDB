package ru.ifmo.ctddev.isaev.studentsdb.enums;

import ru.ifmo.ctddev.isaev.studentsdb.converter.MyEnumDbConverterUtils;

import javax.persistence.AttributeConverter;


/**
 * @author iisaev
 */
public enum Nationality implements MyEnum {
    RUS("Рус.", "RUS"),
    BEL("Бел.", "BEL"),
    KAZ("Каз.", "KAZ"),
    TAT("Тат.", "TAT"),
    UZB("Узб.", "UZB");

    private final String dbKey;

    private final String name;


    Nationality(String name, String dbKey) {
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

    public static class Converter implements AttributeConverter<Nationality, String> {

        @Override
        public String convertToDatabaseColumn(Nationality attribute) {
            return MyEnumDbConverterUtils.convertToDatabaseColumn(attribute);
        }

        @Override
        public Nationality convertToEntityAttribute(String dbData) {
            return MyEnumDbConverterUtils.convertToEntityAttribute(dbData, values());
        }
    }
}
