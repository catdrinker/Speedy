package com.drinker.converter;

import com.drinker.speedy.Converter;
import com.drinker.speedy.RequestBodyConverter;
import com.drinker.speedy.ResponseBodyConverter;
import com.drinker.speedy.TypeToken2;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

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
    public <T> RequestBodyConverter<T> reqBodyConverter(TypeToken2<T> type) {
        TypeAdapter<T> adapter = gson.getAdapter(new TypeToken<T>() {
        });

        return GsonRequestBodyConverter.create(gson, adapter);
    }

    @Override
    public <T> ResponseBodyConverter respBodyConverter(TypeToken2<T> token) {
        TypeAdapter<T> adapter = gson.getAdapter(new TypeToken<T>() {
        });
        return GsonConverter.create(gson, adapter);
    }
}
