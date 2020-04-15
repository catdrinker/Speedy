package com.drinker.converter;

import com.drinker.speedy.ResponseBodyConverter;
import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Parser;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import okhttp3.ResponseBody;

public class ProtoResponseBodyConverter<T> implements ResponseBodyConverter<T> {

    private final Parser<T> parser;
    private final @Nullable ExtensionRegistryLite registry;

    ProtoResponseBodyConverter(Parser<T> parser, @Nullable ExtensionRegistryLite registry) {
        this.parser = parser;
        this.registry = registry;
    }

    @Nullable
    @Override
    public T transform(@Nonnull ResponseBody value) throws IOException {
        try {
            return registry == null ? parser.parseFrom(value.byteStream())
                    : parser.parseFrom(value.byteStream(), registry);
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        } finally {
            value.close();
        }
    }
}
