package com.drinker.speedy;

import java.io.IOException;

public interface Converter<T, R> {

    R transform(T value) throws IOException;
}

