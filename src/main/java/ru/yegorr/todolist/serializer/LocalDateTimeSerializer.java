package ru.yegorr.todolist.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * Сериализатор для LocalDateTime -> дд-ММ-гггг
 */
public class LocalDateTimeSerializer extends StdSerializer<LocalDateTime> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm-ss");

    /**
     * Конструктор
     */
    public LocalDateTimeSerializer() {
        this(null);
    }

    /**
     * Конструктор
     *
     * @param t Class
     */
    public LocalDateTimeSerializer(Class t) {
        super(t);
    }

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(formatter.format(value));
    }
}
