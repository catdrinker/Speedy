package com.drinker.speedy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import okhttp3.Call;
import okhttp3.OkHttpClient;

public class Speedy {

    private final Call.Factory callFactory;
    private final BaseHttpUrl baseHttpUrl;
    private final Converter converter;
    private final IDelivery delivery;
    private final CallAdapter<?, ?> callAdapter;

    private Speedy(Builder builder) {
        callFactory = builder.callFactory;
        baseHttpUrl = builder.baseHttpUrl;
        converter = builder.converter;
        delivery = builder.delivery;
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
            Constructor<?> serviceConstructor = implService.getConstructor(okhttp3.Call.Factory.class, String.class, Converter.class, IDelivery.class, CallAdapter.class);
            Object newInstance = serviceConstructor.newInstance(callFactory, baseHttpUrl.baseUrl, converter, delivery, callAdapter);
            return (T) newInstance;
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException e) {
            throw new InitSpeedyFailException("can't init service cause " + e);
        }
    }

    public static class Builder {
        private Call.Factory callFactory;
        private BaseHttpUrl baseHttpUrl;
        private Converter converter;
        private IDelivery delivery;
        private CallAdapter callAdapter;

        public Builder callFactory(Call.Factory callFactory) {
            this.callFactory = callFactory;
            return this;
        }

        public Builder baseUrl(BaseHttpUrl baseHttpUrl) {
            this.baseHttpUrl = baseHttpUrl;
            return this;
        }

        public Builder converter(Converter converter) {
            this.converter = converter;
            return this;
        }

        public Builder delivery(IDelivery delivery) {
            this.delivery = delivery;
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
            if (converter == null) {
                converter = new DefaultConverter();
            }
            return new Speedy(this);
        }
    }
}
