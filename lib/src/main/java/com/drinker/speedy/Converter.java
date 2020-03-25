package com.drinker.speedy;

import java.io.IOException;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public interface Converter<T, R> {

    R transform(T value) throws IOException;

    interface Factory {

        <T> Converter<T, RequestBody> reqBodyConverter(TypeToken2<T> token);

        <T> Converter<ResponseBody, T> respBodyConverter(TypeToken2<T> token);
    }
}

