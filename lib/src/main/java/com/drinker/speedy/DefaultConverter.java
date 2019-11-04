package com.drinker.speedy;

import okhttp3.ResponseBody;

public class DefaultConverter<T> implements ResponseBodyConverter<T> {
    @Override
    public T transform(ResponseBody value) {
        return null;
    }
}
