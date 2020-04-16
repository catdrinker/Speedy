package com.drinker.converter;

import com.drinker.speedy.RequestBodyConverter;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonWriter;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;

public class MoshiRequestBodyConverter<T> implements RequestBodyConverter<T> {
    private static final MediaType MEDIA_TYPE = MediaType.get("application/json; charset=UTF-8");

    private final JsonAdapter<T> adapter;

    MoshiRequestBodyConverter(JsonAdapter<T> adapter) {
        this.adapter = adapter;
    }

    @Nullable
    @Override
    public RequestBody transform(@Nonnull T value) throws IOException {
        Buffer buffer = new Buffer();
        JsonWriter writer = JsonWriter.of(buffer);
        adapter.toJson(writer, value);
        return RequestBody.create(MEDIA_TYPE, buffer.readByteString());
    }
}
