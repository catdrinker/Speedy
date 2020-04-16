package com.drinker.converter;

import com.drinker.speedy.Converter;
import com.drinker.speedy.TypeToken2;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import javax.annotation.Nonnull;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class MoshiConverterFactory implements Converter.Factory {

    private final Moshi moshi;
    private final boolean lenient;
    private final boolean failOnUnknown;
    private final boolean serializeNulls;

    /**
     * Create an instance using a default {@link Moshi} instance for conversion.
     */
    public static MoshiConverterFactory create() {
        return create(new Moshi.Builder().build());
    }

    /**
     * Create an instance using {@code moshi} for conversion.
     */
    public static MoshiConverterFactory create(Moshi moshi) {
        if (moshi == null) throw new NullPointerException("moshi == null");
        return new MoshiConverterFactory(moshi, false, false, false);
    }

    private MoshiConverterFactory(Moshi moshi, boolean lenient, boolean failOnUnknown, boolean serializeNulls) {
        this.moshi = moshi;
        this.lenient = lenient;
        this.failOnUnknown = failOnUnknown;
        this.serializeNulls = serializeNulls;
    }

    @Nonnull
    @Override
    public <T> Converter<T, RequestBody> reqBodyConverter(@Nonnull TypeToken2<T> token) {
        JsonAdapter<T> adapter = moshi.adapter(token.getType());
        if (lenient) {
            adapter = adapter.lenient();
        }
        if (failOnUnknown) {
            adapter = adapter.failOnUnknown();
        }
        if (serializeNulls) {
            adapter = adapter.serializeNulls();
        }
        return new MoshiRequestBodyConverter<>(adapter);
    }

    @Nonnull
    @Override
    public <T> Converter<ResponseBody, T> respBodyConverter(@Nonnull TypeToken2<T> token) {
        JsonAdapter<T> adapter = moshi.adapter(token.getType());
        if (lenient) {
            adapter = adapter.lenient();
        }
        if (failOnUnknown) {
            adapter = adapter.failOnUnknown();
        }
        if (serializeNulls) {
            adapter = adapter.serializeNulls();
        }
        return new MoshiResponseBodyConverter<>(adapter);
    }
}
