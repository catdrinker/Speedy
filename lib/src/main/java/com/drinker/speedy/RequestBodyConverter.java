package com.drinker.speedy;


import okhttp3.RequestBody;

public interface RequestBodyConverter<T> extends Converter<T, RequestBody> {

}
