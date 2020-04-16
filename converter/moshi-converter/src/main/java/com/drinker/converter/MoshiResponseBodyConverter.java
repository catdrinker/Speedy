package com.drinker.converter;

import com.drinker.speedy.ResponseBodyConverter;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonDataException;
import com.squareup.moshi.JsonReader;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.ByteString;

public class MoshiResponseBodyConverter<T> implements ResponseBodyConverter<T> {

    private static final ByteString UTF8_BOM = ByteString.decodeHex("EFBBBF");

    private final JsonAdapter<T> adapter;

    MoshiResponseBodyConverter(JsonAdapter<T> adapter) {
        this.adapter = adapter;
    }

    @Nullable
    @Override
    public T transform(@Nonnull ResponseBody value) throws IOException {
        BufferedSource source = value.source();
        try {
            // Moshi has no document-level API so the responsibility of BOM skipping falls to whatever
            // is delegating to it. Since it's a UTF-8-only library as well we only honor the UTF-8 BOM.
            if (source.rangeEquals(0, UTF8_BOM)) {
                source.skip(UTF8_BOM.size());
            }
            JsonReader reader = JsonReader.of(source);
            T result = adapter.fromJson(reader);
            if (reader.peek() != JsonReader.Token.END_DOCUMENT) {
                throw new JsonDataException("JSON document was not fully consumed.");
            }
            return result;
        } finally {
            value.close();
        }
    }
}
