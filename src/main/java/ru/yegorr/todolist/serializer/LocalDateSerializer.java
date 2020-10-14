package ru.yegorr.todolist.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Serializer to serialize LocalDate to dd-MM-yyyy format
 */
public class LocalDateSerializer extends StdSerializer<LocalDate> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    /**
     * Constructor
     */
    public LocalDateSerializer() {
        this(null);
    }

    /**
     * Constructor
     *
     * @param t class
     */
    public LocalDateSerializer(Class t) {
        super(t);
    }

    @Override
    public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(formatter.format(value));
    }
}
