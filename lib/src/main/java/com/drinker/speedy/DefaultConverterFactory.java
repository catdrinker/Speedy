package com.drinker.speedy;

import okhttp3.ResponseBody;

public class DefaultConverterFactory implements Converter.Factory {

    @Override
    public <T> RequestBodyConverter reqBodyConverter(TypeToken2<T> token) {
        return null;
    }

    @Override
    public <T> ResponseBodyConverter respBodyConverter(final TypeToken2<T> token) {

        return new ResponseBodyConverter<T>() {
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
