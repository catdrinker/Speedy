package com.drinker.speedy;

import javax.annotation.Nonnull;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public final class DefaultConverterFactory implements Converter.Factory {

    @Nonnull
    @Override
    public <T> Converter<T, RequestBody> reqBodyConverter(@Nonnull final TypeToken2<T> token) {
        return new Converter<T, RequestBody>() {
            @Override
            public RequestBody transform(@Nonnull T value) {
                if (token.getType() == RequestBody.class) {
                    return (RequestBody) value;
                }
                throw new IllegalStateException("use wrapped type is not RequestBody but use default converter");
            }
        };
    }

    @Nonnull
    @Override
    public <T> Converter<ResponseBody, T> respBodyConverter(@Nonnull final TypeToken2<T> token) {
        return new Converter<ResponseBody, T>() {
            @Override
            public T transform(@Nonnull ResponseBody value) {
                if (token.type == ResponseBody.class) {
                    return (T) value;
                }
                throw new IllegalStateException("return wrapped type is not ResponseBody but use default converter");
            }
        };
    }
}
