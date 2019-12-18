package com.example.converter;

import com.drinker.speedy.Converter;
import com.drinker.speedy.ResponseBodyConverter;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;

public class GsonConverter<T> implements ResponseBodyConverter<T> {

    private final Gson gson;
    private final TypeAdapter<T> adapter;


    private GsonConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    // TODO 改成每个方法创建一个新的Converter，这里还是要传入factory
    public static GsonConverter<?> create(Type type){
        Gson gson = new Gson();
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new GsonConverter<>(gson,adapter);
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
