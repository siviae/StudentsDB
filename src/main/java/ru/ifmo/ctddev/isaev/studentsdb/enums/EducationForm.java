package ru.ifmo.ctddev.isaev.studentsdb.enums;

import ru.ifmo.ctddev.isaev.studentsdb.converter.MyEnumDbConverterUtils;

import javax.persistence.AttributeConverter;


/**
 * @author iisaev
 */
public enum EducationForm implements MyEnum {
    FULL_TIME("Очная", "F"),
    PART_TIME("Заочная", "P"),
    LATE_TIME("Вечерняя", "L");

    private final String dbKey;

    private final String name;

    EducationForm(String name, String dbKey) {
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

    public static class Converter implements AttributeConverter<EducationForm, String> {

        @Override
        public String convertToDatabaseColumn(EducationForm attribute) {
            return MyEnumDbConverterUtils.convertToDatabaseColumn(attribute);
        }

        @Override
        public EducationForm convertToEntityAttribute(String dbData) {
            return MyEnumDbConverterUtils.convertToEntityAttribute(dbData, values());
        }
    }

}
