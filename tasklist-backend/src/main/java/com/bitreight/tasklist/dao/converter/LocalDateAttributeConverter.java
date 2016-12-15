package com.bitreight.tasklist.dao.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Date;
import java.time.LocalDate;

@Converter(autoApply = true)
public class LocalDateAttributeConverter implements AttributeConverter<LocalDate, Date> {

    @Override
    public Date convertToDatabaseColumn(LocalDate date) {
        LocalDate localDateOfSqlMaxDate = new Date(Long.MAX_VALUE).toLocalDate();

        if(date != null) {
            if(date.isAfter(localDateOfSqlMaxDate)) {
                return Date.valueOf(localDateOfSqlMaxDate);

            } else {
                return Date.valueOf(date);
            }
        }

        return null;
    }

    @Override
    public LocalDate convertToEntityAttribute(Date date) {
        return (date == null ? null : date.toLocalDate());
    }
}
