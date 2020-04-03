package com.drinker.converter;

import com.drinker.speedy.ResponseBodyConverter;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;

import javax.annotation.Nonnull;

import okhttp3.ResponseBody;

public class GsonResponseBodyConverter<T> implements ResponseBodyConverter<T> {

    private final Gson gson;
    private final TypeAdapter<T> adapter;

    GsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T transform(@Nonnull ResponseBody body) throws IOException {
        JsonReader jsonReader = gson.newJsonReader(body.charStream());
        try {
            T result = adapter.read(jsonReader);
            if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                throw new JsonIOException("JSON document was not fully consumed.");
            }
            return result;
        } finally {
            body.close();
        }
    }
}
