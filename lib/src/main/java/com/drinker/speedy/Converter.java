package com.drinker.speedy;

import java.io.IOException;

public interface Converter<T, R> {

    R transform(T value) throws IOException;

    interface Factory {

        <T> RequestBodyConverter reqBodyConverter(TypeToken2<T> token);

        <T> ResponseBodyConverter respBodyConverter(TypeToken2<T> token);
    }
}

