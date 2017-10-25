package de.invesdwin.util.marshallers.jackson;

import java.io.IOException;

import javax.annotation.concurrent.Immutable;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;

import de.invesdwin.util.time.fdate.FDate;
import de.invesdwin.util.time.fdate.FDates;

@Immutable
public final class FDateSerializer extends JsonSerializer<FDate> {
    /**
     * Default instance that is used when no contextual configuration is needed.
     */
    public static final FDateSerializer INSTANCE = new FDateSerializer();

    private FDateSerializer() {}

    @Override
    public void serialize(final FDate value, final JsonGenerator gen, final SerializerProvider serializers)
            throws IOException {
        DateSerializer.instance.serialize(FDates.toDate(value), gen, serializers);
    }
}