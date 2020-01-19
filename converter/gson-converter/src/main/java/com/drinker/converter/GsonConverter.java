package com.drinker.converter;

import com.drinker.speedy.ResponseBodyConverter;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;

import okhttp3.ResponseBody;

public class GsonConverter<T> implements ResponseBodyConverter<T> {

    private final Gson gson;
    private TypeAdapter<T> adapter;


    private GsonConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    // TODO 改成每个方法创建一个新的Converter，这里还是要传入factory
    public static <T> GsonConverter<T> create(Gson gson, TypeAdapter<T> adapter) {
        return new GsonConverter<>(gson, adapter);
    }

    @Override
    public T transform(ResponseBody body) throws IOException {
        JsonReader jsonReader = gson.newJsonReader(body.charStream());
        try {
            T result = adapter.read(jsonReader);
            if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                throw new JsonIOException("JSON document was not fully consumed.");
            }
            return result;
        } finally {
            body.close();
        }
    }
}