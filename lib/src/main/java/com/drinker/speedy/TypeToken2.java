/*
  Copyright (C) 2008 Google Inc.
  <p>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p>
  http://www.apache.org/licenses/LICENSE-2.0
  <p>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.drinker.speedy;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * crop from Gson TypeToken, the destination is get the class type at return or parameter
 *
 * @param <T>
 */
public class TypeToken2<T> {
    private final Class<? super T> rawType;
    final Type type;
    private final int hashCode;

    protected TypeToken2() {
        this.type = getSuperclassTypeParameter(getClass());
        this.rawType = (Class<? super T>) $Gson$Types.getRawType(type);
        this.hashCode = type.hashCode();
    }

    /**
     * Returns the type from super class's type parameter in {@link $Gson$Types#canonicalize
     * canonical form}.
     */
    private static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }


    /**
     * Returns the raw (non-generic) type for this type.
     */
    public final Class<? super T> getRawType() {
        return rawType;
    }

    /**
     * Gets underlying {@code Type} instance.
     */
    public final Type getType() {
        return type;
    }


}
