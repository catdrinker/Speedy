package com.drinker.converter;

import com.drinker.speedy.Converter;
import com.drinker.speedy.TypeToken2;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import javax.annotation.Nonnull;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

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

    private GsonConverterFactory(Gson gson) {
        this.gson = gson;
    }


    @Nonnull
    @Override
    public <T> Converter<T, RequestBody> reqBodyConverter(@Nonnull TypeToken2<T> token) {
        TypeAdapter<T> adapter = (TypeAdapter<T>) gson.getAdapter(TypeToken.get(token.getType()));
        return new GsonRequestBodyConverter<>(gson, adapter);
    }

    @Nonnull
    @Override
    public <T> Converter<ResponseBody, T> respBodyConverter(@Nonnull TypeToken2<T> token) {
        TypeAdapter<T> adapter = (TypeAdapter<T>) gson.getAdapter(TypeToken.get(token.getType()));
        return new GsonResponseBodyConverter<>(gson, adapter);
    }
}
