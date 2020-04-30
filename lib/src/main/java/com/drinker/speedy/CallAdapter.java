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


import javax.annotation.Nonnull;

public interface CallAdapter<T, R> {

    R adapt(@Nonnull Call<T> call);

    interface Factory {
        <T, R> CallAdapter<T, R> adapter();
    }

    final class DefaultCallAdapter<T> implements CallAdapter<T, Call<T>> {

        @Override
        public Call<T> adapt(@Nonnull Call<T> call) {
            return call;
        }
    }

    final class DefaultCallAdapterFactory implements CallAdapter.Factory {
        @Override
        public <T, R> CallAdapter<T, R> adapter() {
            return (CallAdapter<T, R>) new DefaultCallAdapter<T>();
        }
    }

}
