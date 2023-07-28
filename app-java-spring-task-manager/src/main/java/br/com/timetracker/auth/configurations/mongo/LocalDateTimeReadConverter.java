package br.com.timetracker.auth.configurations.mongo;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class LocalDateTimeReadConverter implements Converter<Date, LocalDateTime> {
    @Override
    public LocalDateTime convert(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneOffset.UTC);
    }
}
