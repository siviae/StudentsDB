package ru.ifmo.ctddev.isaev.studentsdb.converter;

import javax.persistence.AttributeConverter;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;


/**
 * @author iisaev
 */
public class TimestampToLocalDateConverter implements AttributeConverter<LocalDate, Timestamp> {
    @Override
    public Timestamp convertToDatabaseColumn(LocalDate attribute) {
        return Optional.ofNullable(attribute)
                .map(date -> LocalDateTime.of(date, LocalTime.NOON))
                .map(Timestamp::valueOf)
                .orElse(null);
    }

    @Override
    public LocalDate convertToEntityAttribute(Timestamp dbData) {
        return Optional.ofNullable(dbData)
                .map(Timestamp::toLocalDateTime)
                .map(LocalDateTime::toLocalDate)
                .orElse(null);
    }
}
