package com.drinker.speedy;

import java.io.IOException;

public class CatchExceptionRequestBody<T> {

    private Converter<T, okhttp3.RequestBody> requestBodyConverter;


    public CatchExceptionRequestBody(Converter<T, okhttp3.RequestBody> requestBodyConverter) {
        this.requestBodyConverter = requestBodyConverter;
    }

    okhttp3.RequestBody getBody(T value) {
        try {
            return requestBodyConverter.transform(value);
        } catch (IOException e) {
            throw new IllegalStateException("can't transform with requestBody converter " + value);
        }
    }

}
