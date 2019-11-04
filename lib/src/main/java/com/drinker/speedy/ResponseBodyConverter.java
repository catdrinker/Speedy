package com.drinker.speedy;

import okhttp3.ResponseBody;

interface ResponseBodyConverter<T> extends Converter<ResponseBody, T> {

}
