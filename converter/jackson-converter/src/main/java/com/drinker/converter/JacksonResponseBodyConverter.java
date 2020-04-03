package com.drinker.converter;

import com.drinker.speedy.ResponseBodyConverter;
import com.fasterxml.jackson.databind.ObjectReader;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import okhttp3.ResponseBody;

public class JacksonResponseBodyConverter<T> implements ResponseBodyConverter<T> {
    private final ObjectReader reader;

    JacksonResponseBodyConverter(ObjectReader reader) {
        this.reader = reader;
    }

    @Nullable
    @Override
    public T transform(@Nonnull ResponseBody value) throws IOException {
        try {
            return reader.readValue(value.charStream());
        } finally {
            value.close();
        }
    }
}
