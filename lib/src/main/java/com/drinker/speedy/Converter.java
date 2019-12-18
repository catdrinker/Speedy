package com.drinker.speedy;

import java.io.IOException;
import java.lang.reflect.Type;

public interface Converter<T, R> {

    R transform(T value) throws IOException;

    interface Factory{

        <T>ResponseBodyConverter<T> respBodyConverter(Type type);

        <T>RequestBodyConverter<T> reqBodyConverter(Type type);
    }
}

