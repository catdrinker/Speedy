package com.drinker.speedy;

import okhttp3.ResponseBody;

public class DefaultConverter implements ResponseBodyConverter<ResponseBody> {
    @Override
    public ResponseBody transform(ResponseBody value) {
        return value;
    }
}
