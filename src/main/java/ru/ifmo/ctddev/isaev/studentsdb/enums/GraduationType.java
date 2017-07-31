package ru.ifmo.ctddev.isaev.studentsdb.enums;

import ru.ifmo.ctddev.isaev.studentsdb.converter.MyEnumDbConverterUtils;

import javax.persistence.AttributeConverter;


/**
 * @author iisaev
 */
public enum GraduationType implements MyEnum {
    HIGH("Высшее", "H"),
    UNFINISHED_HIGH("Незаконченное высшее", "U"),
    MID("Среднее", "M");

    private final String dbKey;

    private final String name;

    GraduationType(String name, String dbKey) {
        this.name = name;
        this.dbKey = dbKey;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDbKey() {
        return dbKey;
    }

    public static class Converter implements AttributeConverter<GraduationType, String> {

        @Override
        public String convertToDatabaseColumn(GraduationType attribute) {
            return MyEnumDbConverterUtils.convertToDatabaseColumn(attribute);
        }

        @Override
        public GraduationType convertToEntityAttribute(String dbData) {
            return MyEnumDbConverterUtils.convertToEntityAttribute(dbData, values());
        }
    }
}
