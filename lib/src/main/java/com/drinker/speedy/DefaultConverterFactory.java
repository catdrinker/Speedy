/*
 * Copyright (C) 2020 catdrinker.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.drinker.speedy;

import javax.annotation.Nonnull;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public final class DefaultConverterFactory implements Converter.Factory {

    @Nonnull
    @Override
    public <T> Converter<T, RequestBody> reqBodyConverter(@Nonnull final TypeToken2<T> token) {
        return new Converter<T, RequestBody>() {
            @Override
            public RequestBody transform(@Nonnull T value) {
                if (token.getType() == RequestBody.class) {
                    return (RequestBody) value;
                }
                throw new IllegalStateException("use wrapped type is not RequestBody but use default converter");
            }
        };
    }

    @Nonnull
    @Override
    public <T> Converter<ResponseBody, T> respBodyConverter(@Nonnull final TypeToken2<T> token) {
        return new Converter<ResponseBody, T>() {
            @Override
            public T transform(@Nonnull ResponseBody value) {
                if (token.type == ResponseBody.class) {
                    return (T) value;
                }
                throw new IllegalStateException("return wrapped type is not ResponseBody but use default converter");
            }
        };
    }
}
