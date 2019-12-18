package com.drinker.speedy;

import okhttp3.ResponseBody;

public interface ResponseBodyConverter<T> extends Converter<ResponseBody, T> {

}
