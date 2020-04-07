package com.drinker.speedy;

import java.io.IOException;

import javax.annotation.Nonnull;

import okhttp3.RequestBody;

public final class CatchExceptionRequestBody<T> {

    @Nonnull
    private Converter<T, okhttp3.RequestBody> requestBodyConverter;

    public CatchExceptionRequestBody(Converter<T, okhttp3.RequestBody> requestBodyConverter) {
        this.requestBodyConverter = requestBodyConverter;
    }

    public okhttp3.RequestBody getBody(T value) {
        try {
            RequestBody body = requestBodyConverter.transform(value);
            if (body == null) {
                throw new NullPointerException("cause of converter " + requestBodyConverter.getClass() + " we converter body is null");
            }
            return body;
        } catch (IOException e) {
            throw new IllegalStateException("can't transform with requestBody converter " + value);
        }
    }

}
