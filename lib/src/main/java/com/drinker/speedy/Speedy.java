package com.drinker.speedy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.annotation.Nullable;

import okhttp3.Call;
import okhttp3.OkHttpClient;

public final class Speedy {

    @Nullable private final Call.Factory callFactory;
    private final String baseHttpUrl;
    @Nullable private final Converter.Factory converterFactory;
    @Nullable private final IDelivery delivery;
    @Nullable private final CallAdapter<?, ?> callAdapter;

    private Speedy(Builder builder) {
        baseHttpUrl = builder.baseHttpUrl;
        callFactory = builder.callFactory;
        converterFactory = builder.converterFactory;
        delivery = builder.delivery;
        callAdapter = builder.callAdapter;
    }

    public <T> T getService(Class<T> clazz) {
        // TODO 不采用反射的方式而直接替换成asm 字节码插桩方式来，消除性能损耗
        if (!clazz.isInterface()) {
            throw new IllegalArgumentException("service must be an interface");
        }
        String name = clazz.getName();
        String implName = name + "_impl";
        try {
            Class<?> implService = Class.forName(implName);
            Constructor<?>[] constructors = implService.getConstructors();
            if (constructors.length != 1) //noinspection ConstantConditions
                return null;
            Constructor<?> serviceConstructor = implService.getConstructor(okhttp3.Call.Factory.class, String.class, Converter.Factory.class, IDelivery.class, CallAdapter.class);
            Object newInstance = serviceConstructor.newInstance(callFactory, baseHttpUrl, converterFactory, delivery, callAdapter);
            return (T) newInstance;
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException e) {
            throw new InitSpeedyFailException("can't init service cause " + e);
        }
    }

    public static class Builder {
        @Nullable private Call.Factory callFactory;
        private String baseHttpUrl ="";
        @Nullable private Converter.Factory converterFactory;
        @Nullable private IDelivery delivery;
        @Nullable private CallAdapter callAdapter;

        public Builder callFactory(Call.Factory callFactory) {
            this.callFactory = callFactory;
            return this;
        }

        public Builder baseUrl(String baseHttpUrl) {
            this.baseHttpUrl = baseHttpUrl;
            return this;
        }

        public Builder converterFactory(Converter.Factory converterFactory) {
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
            if (baseHttpUrl.isEmpty()) {
                throw new NullPointerException("baseHttp url can't be null" + baseHttpUrl);
            }
            if (converterFactory == null) {
                converterFactory = new DefaultConverterFactory();
            }
            if (callAdapter == null) {
                callAdapter = new CallAdapter.DefaultCallAdapter();
            }
            if (delivery == null) {
                delivery = new IDelivery.DefaultDelivery();
            }
            return new Speedy(this);
        }
    }
}
