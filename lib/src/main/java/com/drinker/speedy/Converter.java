package com.drinker.speedy;

import java.io.IOException;

public interface Converter<T, R> {

    R transform(T value) throws IOException;

    interface Factory {

        <T> RequestBodyConverter reqBodyConverter(Class<T> Type);

        <T> ResponseBodyConverter respBodyConverter(Class<T> type);
    }
}

