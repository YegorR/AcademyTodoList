package ru.yegorr.todolist.serializer;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.*;

/**
 * Десериализатор для LocalDate dd-MM-yyyy
 */
public class LocalDateDeserializer extends StdDeserializer<LocalDate> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    /**
     * Конструктор
     */
    public LocalDateDeserializer() {
        this(null);
    }

    /**
     * Конструктор
     *
     * @param vc class
     */
    public LocalDateDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String date = p.getText();
        try {
            return LocalDate.parse(date, formatter);
        } catch (DateTimeParseException ex) {
             throw new JsonParseException(p, ex.getParsedString(), ex);
        }
    }
}
