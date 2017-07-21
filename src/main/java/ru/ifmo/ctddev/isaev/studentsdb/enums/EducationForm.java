package ru.ifmo.ctddev.isaev.studentsdb.enums;

import javax.persistence.AttributeConverter;
import java.util.Arrays;


/**
 * @author iisaev
 */
public enum EducationForm {
    FULL_TIME("Очная", "F"),
    PART_TIME("Заочная", "P"),
    LATE_TIME("Вечерняя", "L");

    private final String dbKey;

    private final String name;

    EducationForm(String name, String dbKey) {
        this.name = name;
        this.dbKey = dbKey;
    }

    public String getName() {
        return name;
    }

    public String getDbKey() {
        return dbKey;
    }

    public static class Converter implements AttributeConverter<EducationForm, String> {

        @Override
        public String convertToDatabaseColumn(EducationForm attribute) {
            return attribute == null ? null : attribute.getDbKey();
        }

        @Override
        public EducationForm convertToEntityAttribute(String dbData) {
            return Arrays.stream(EducationForm.values())
                    .filter(enumValue -> enumValue.getDbKey().equals(dbData))
                    .findFirst()
                    .orElse(null);
        }
    }
}
