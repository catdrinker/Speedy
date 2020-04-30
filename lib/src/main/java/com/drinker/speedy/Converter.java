/*
 * Copyright (C) 2015 Square, Inc.
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
import javax.annotation.Nullable;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public interface Converter<T, R> {

    @Nullable R transform(T value) throws IOException;

    interface Factory {

        @Nonnull
        <T> Converter<T, RequestBody> reqBodyConverter(@Nonnull TypeToken2<T> token);

        @Nonnull
        <T> Converter<ResponseBody, T> respBodyConverter(@Nonnull TypeToken2<T> token);
    }
}

