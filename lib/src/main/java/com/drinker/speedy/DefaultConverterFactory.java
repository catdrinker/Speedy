package com.drinker.speedy;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class DefaultConverterFactory implements Converter.Factory {

    @Override
    public <T> Converter<T, RequestBody> reqBodyConverter(final TypeToken2<T> token) {
        return new Converter<T, RequestBody>() {
            @Override
            public RequestBody transform(T value) {
                if (token.getType() == RequestBody.class) {
                    return (RequestBody) value;
                }
                throw new IllegalStateException("use wrapped type is not RequestBody but use default converter");
            }
        };
    }

    @Override
    public <T> Converter<ResponseBody, T> respBodyConverter(final TypeToken2<T> token) {
        return new Converter<ResponseBody, T>() {
            @Override
            public T transform(ResponseBody value) {
                if (token.type == ResponseBody.class) {
                    return (T) value;
                }
                throw new IllegalStateException("return wrapped type is not ResponseBody but use default converter");
            }
        };
    }
}
