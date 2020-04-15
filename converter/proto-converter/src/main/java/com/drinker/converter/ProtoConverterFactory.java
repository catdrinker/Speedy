package com.drinker.converter;

import com.drinker.speedy.Converter;
import com.drinker.speedy.TypeToken2;
import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.MessageLite;
import com.google.protobuf.Parser;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class ProtoConverterFactory implements Converter.Factory {

    private final @Nullable
    ExtensionRegistryLite registry;

    public static ProtoConverterFactory create() {
        return new ProtoConverterFactory(null);
    }

    public static ProtoConverterFactory create(@Nullable ExtensionRegistryLite registry) {
        return new ProtoConverterFactory(registry);
    }


    private ProtoConverterFactory(@Nullable ExtensionRegistryLite registry) {
        this.registry = registry;
    }

    @Nonnull
    @Override
    public <T> Converter<T, RequestBody> reqBodyConverter(@Nonnull TypeToken2<T> token) {
        Type type = token.getType();
        if (!(type instanceof Class<?>)) {
            throw new IllegalStateException("token type must be cast Class<?> with protobuf");
        }
        if (!MessageLite.class.isAssignableFrom((Class<?>) type)) {
            throw new IllegalStateException("token type must be assignableFrom MessageLite");
        }
        return new ProtoRequestBodyConverter<>();
    }

    @Nonnull
    @Override
    public <T> Converter<ResponseBody, T> respBodyConverter(@Nonnull TypeToken2<T> token) {
        Type type = token.getType();
        if (!(type instanceof Class<?>)) {
            throw new IllegalStateException("token type must be cast Class<?> with protobuf");
        }
        Class<?> c = (Class<?>) type;
        if (!MessageLite.class.isAssignableFrom(c)) {
            throw new IllegalStateException("token type must be assignableFrom MessageLite");
        }

        Parser<T> parser;
        try {
            Method method = c.getDeclaredMethod("parser");
            //noinspection unchecked
            parser = (Parser<T>) method.invoke(null);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getCause());
        } catch (NoSuchMethodException | IllegalAccessException ignored) {
            // If the method is missing, fall back to original static field for pre-3.0 support.
            try {
                Field field = c.getDeclaredField("PARSER");
                //noinspection unchecked
                parser = (Parser<T>) field.get(null);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new IllegalArgumentException("Found a protobuf message but "
                        + c.getName()
                        + " had no parser() method or PARSER field.", e);
            }
        }
        return new ProtoResponseBodyConverter<>(parser, registry);
    }
}
