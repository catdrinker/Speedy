package com.drinker.converter;

import com.drinker.speedy.RequestBodyConverter;
import com.google.protobuf.MessageLite;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ProtoRequestBodyConverter<T> implements RequestBodyConverter<T> {

    private static final MediaType MEDIA_TYPE = MediaType.get("application/x-protobuf");

    @Nullable
    @Override
    public RequestBody transform(@Nonnull T value) throws IOException {
        if (value instanceof MessageLite) {
            MessageLite messageLite = (MessageLite) value;
            byte[] bytes = messageLite.toByteArray();
            return RequestBody.create(MEDIA_TYPE, bytes);
        }
        return null;
    }
}
