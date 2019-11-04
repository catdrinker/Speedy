package com.drinker.speedy;

public interface Converter<T, R> {

    R transform(T value);
}

