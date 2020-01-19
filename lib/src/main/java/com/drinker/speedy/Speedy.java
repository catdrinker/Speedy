package com.drinker.speedy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import okhttp3.Call;
import okhttp3.OkHttpClient;

public class Speedy {

    private final Call.Factory callFactory;
    private final BaseHttpUrl baseHttpUrl;
    private final Converter.Factory converterFactory;
    private final CallAdapter<?, ?> callAdapter;

    private Speedy(Builder builder) {
        baseHttpUrl = builder.baseHttpUrl;
        callFactory = builder.callFactory;
        converterFactory = builder.converterFactory;
        callAdapter = builder.callAdapter;
    }

    public <T> T getService(Class<T> clazz) {
        if (!clazz.isInterface()) {
            throw new IllegalArgumentException("service must be an interface");
        }
        String name = clazz.getName();
        String implName = name + "_impl";
        try {
            Class<?> implService = Class.forName(implName);
            Constructor<?>[] constructors = implService.getConstructors();
            if (constructors.length != 1) return null;
            Constructor<?> serviceConstructor = implService.getConstructor(okhttp3.Call.Factory.class, String.class, Converter.Factory.class, CallAdapter.class);
            Object newInstance = serviceConstructor.newInstance(callFactory, baseHttpUrl.baseUrl, converterFactory, callAdapter);
            return (T) newInstance;
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException e) {
            throw new InitSpeedyFailException("can't init service cause " + e);
        }
    }

    public static class Builder {
        private Call.Factory callFactory;
        private BaseHttpUrl baseHttpUrl;
        private Converter.Factory converterFactory;
        private CallAdapter callAdapter;

        public Builder callFactory(Call.Factory callFactory) {
            this.callFactory = callFactory;
            return this;
        }

        public Builder baseUrl(BaseHttpUrl baseHttpUrl) {
            this.baseHttpUrl = baseHttpUrl;
            return this;
        }

        public Builder converterFactroy(Converter.Factory converterFactory) {
            this.converterFactory = converterFactory;
            return this;
        }

        public Builder callAdapter(CallAdapter callAdapter) {
            this.callAdapter = callAdapter;
            return this;
        }

        public Speedy build() {
            if (callFactory == null) {
                callFactory = new OkHttpClient();
            }
            if (baseHttpUrl == null || baseHttpUrl.baseUrl.isEmpty()) {
                throw new NullPointerException("baseHttp url can't be null" + baseHttpUrl);
            }
            if (converterFactory == null) {
                converterFactory = new DefaultConverterFactory();
            }
            if (callAdapter == null) {
                callAdapter = new CallAdapter.DefaultCallAdapter();
            }
            return new Speedy(this);
        }
    }
}
