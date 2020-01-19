package com.drinker.converter;

import com.drinker.speedy.Converter;
import com.drinker.speedy.RequestBodyConverter;
import com.drinker.speedy.ResponseBodyConverter;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

public class GsonConverterFactory implements Converter.Factory {

    private final Gson gson;

    public static GsonConverterFactory create() {
        return new GsonConverterFactory();
    }

    public static GsonConverterFactory create(Gson gson) {
        return new GsonConverterFactory(gson);
    }

    private GsonConverterFactory() {
        this.gson = new Gson();
    }

    public GsonConverterFactory(Gson gson) {
        this.gson = gson;
    }


    @Override
    public <T> RequestBodyConverter<T> reqBodyConverter(Class<T> Type) {
        return null;
    }

    @Override
    public <T> ResponseBodyConverter<T> respBodyConverter(Class<T> type) {
        TypeAdapter<T> adapter = gson.getAdapter(type);
        return GsonConverter.create(gson, adapter);
    }
}
