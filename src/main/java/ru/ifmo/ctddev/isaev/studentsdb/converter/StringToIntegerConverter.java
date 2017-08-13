package ru.ifmo.ctddev.isaev.studentsdb.converter;

import javax.persistence.AttributeConverter;
import java.util.Optional;


/**
 * @author iisaev
 */
public class StringToIntegerConverter implements AttributeConverter<String, Integer> {
    @Override
    public Integer convertToDatabaseColumn(String attribute) {
        return Optional.ofNullable(attribute)
                .map(Integer::valueOf)
                .orElse(null);
    }

    @Override
    public String convertToEntityAttribute(Integer dbData) {
        return Optional.ofNullable(dbData)
                .map(String::valueOf)
                .orElse(null);
    }
}
