package com.drinker.converter;

import com.drinker.speedy.RequestBodyConverter;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import okhttp3.MediaType;
import okhttp3.RequestBody;

import static javax.xml.transform.OutputKeys.MEDIA_TYPE;

public class JacksonRequestBodyConverter<T> implements RequestBodyConverter<T> {

    private ObjectWriter writer;

    JacksonRequestBodyConverter(ObjectWriter writer) {
        this.writer = writer;
    }

    @Nullable
    @Override
    public RequestBody transform(@Nonnull T value) throws IOException {
        byte[] bytes = writer.writeValueAsBytes(value);
        return RequestBody.create(MediaType.parse(MEDIA_TYPE), bytes);
    }
}
