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

import java.io.IOException;

import javax.annotation.Nonnull;

import okhttp3.RequestBody;

public final class CatchExceptionRequestBody<T> {

    @Nonnull
    private Converter<T, okhttp3.RequestBody> requestBodyConverter;

    public CatchExceptionRequestBody(Converter<T, okhttp3.RequestBody> requestBodyConverter) {
        this.requestBodyConverter = requestBodyConverter;
    }

    public okhttp3.RequestBody getBody(T value) {
        try {
            RequestBody body = requestBodyConverter.transform(value);
            if (body == null) {
                throw new NullPointerException("cause of converter " + requestBodyConverter.getClass() + " we converter body is null");
            }
            return body;
        } catch (IOException e) {
            throw new IllegalStateException("can't transform with requestBody converter " + value);
        }
    }

}
