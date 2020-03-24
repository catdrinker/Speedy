package com.drinker.speedy;

import java.io.IOException;

public class RequestBody<T> {

    private RequestBodyConverter<T> requestBodyConverter;


    public RequestBody(RequestBodyConverter<T> requestBodyConverter) {
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
