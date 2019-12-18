package com.drinker.speedy;

import java.io.IOException;

import okhttp3.ResponseBody;

public class DefaultConverter implements ResponseBodyConverter<ResponseBody> {
    @Override
    public ResponseBody transform(ResponseBody value) throws IOException {
        return value;
    }
}
