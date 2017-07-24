package ru.ifmo.ctddev.isaev.studentsdb.enums;

import javax.persistence.AttributeConverter;
import java.util.Arrays;


/**
 * @author iisaev
 */
public enum GraduationType implements MyEnum{
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

    public String getDbKey() {
        return dbKey;
    }

    public static class Converter implements AttributeConverter<GraduationType, String> {

        @Override
        public String convertToDatabaseColumn(GraduationType attribute) {
            return attribute == null ? null : attribute.getDbKey();
        }

        @Override
        public GraduationType convertToEntityAttribute(String dbData) {
            return Arrays.stream(GraduationType.values())
                    .filter(enumValue -> enumValue.getDbKey().equals(dbData))
                    .findFirst()
                    .orElse(null);
        }
    }
}
