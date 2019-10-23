package com.drinker.speedy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import okhttp3.Call;
import okhttp3.OkHttpClient;

public class Speedy {

    private final Call.Factory callFactory;
    private final BaseHttpUrl baseHttpUrl;

    public Speedy(Builder builder) {
        callFactory = builder.callFactory;
        baseHttpUrl = builder.baseHttpUrl;
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
            if(constructors.length != 1) return null;
            Constructor<?> serviceConstructor = implService.getConstructor(okhttp3.Call.Factory.class, String.class);
            Object newInstance = serviceConstructor.newInstance(callFactory, baseHttpUrl.baseUrl);
            return (T) newInstance;
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException e) {
            throw new InitSpeedyFailException("can't init service cause " + e);
        }
    }

    public static class Builder {
        private Call.Factory callFactory;
        private BaseHttpUrl baseHttpUrl;

        public Builder callFactory(Call.Factory callFactory) {
            this.callFactory = callFactory;
            return this;
        }

        public Builder baseUrl(BaseHttpUrl baseHttpUrl) {
            this.baseHttpUrl = baseHttpUrl;
            return this;
        }

        public Speedy build() {
            if (callFactory == null) {
                callFactory = new OkHttpClient();
            }
            if(baseHttpUrl == null || baseHttpUrl.baseUrl.isEmpty()){
                throw new NullPointerException("baseHttp url can't be null" +baseHttpUrl);
            }
            return new Speedy(this);
        }
    }
}
