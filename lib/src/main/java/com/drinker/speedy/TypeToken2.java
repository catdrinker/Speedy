package com.drinker.speedy;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * crop from Gson, the destination is get the class type at return or parameter
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
