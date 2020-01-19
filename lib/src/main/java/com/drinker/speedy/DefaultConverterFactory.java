package com.drinker.speedy;

import okhttp3.ResponseBody;

public class DefaultConverterFactory implements Converter.Factory {

    @Override
    public <T> RequestBodyConverter<T> reqBodyConverter(Class<T> Type) {
        return null;
    }

    @Override
    public <T> ResponseBodyConverter<T> respBodyConverter(final Class<T> type) {
        return new ResponseBodyConverter<T>() {
            @Override
            public T transform(ResponseBody value) {
                if (type == ResponseBody.class) {
                    return (T) value;
                }
                throw new IllegalStateException("return wrapped type is not ResponseBody but use default converter");
            }
        };
    }
}
