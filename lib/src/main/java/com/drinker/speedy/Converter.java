package com.drinker.speedy;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public interface Converter<T, R> {

    @Nullable R transform(T value) throws IOException;

    interface Factory {

        @Nonnull
        <T> Converter<T, RequestBody> reqBodyConverter(@Nonnull TypeToken2<T> token);

        @Nonnull
        <T> Converter<ResponseBody, T> respBodyConverter(@Nonnull TypeToken2<T> token);
    }
}

