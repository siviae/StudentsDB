package ru.ifmo.ctddev.isaev.studentsdb.enums;

import ru.ifmo.ctddev.isaev.studentsdb.converter.MyEnumDbConverterUtils;

import javax.persistence.AttributeConverter;


/**
 * @author iisaev
 */
public enum MilitaryRank implements MyEnum {
    CAP_1_RANK("Капитан 1 ранга", "C1"),
    CAP_2_RANK("Капитан 2 ранга", "C2"),
    CAP_3_RANK("Капитан 3 ранга", "C3");

    private final String dbKey;

    private final String name;

    MilitaryRank(String name, String dbKey) {
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

    public static class Converter implements AttributeConverter<MilitaryRank, String> {

        @Override
        public String convertToDatabaseColumn(MilitaryRank attribute) {
            return MyEnumDbConverterUtils.convertToDatabaseColumn(attribute);
        }

        @Override
        public MilitaryRank convertToEntityAttribute(String dbData) {
            return MyEnumDbConverterUtils.convertToEntityAttribute(dbData, values());
        }
    }
}
