package br.com.timetracker.auth.configurations.mongo;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class LocalDateTimeWriteConverter implements Converter<LocalDateTime, Date> {
    @Override
    public Date convert(LocalDateTime localDateTime) {
        return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
    }
}
