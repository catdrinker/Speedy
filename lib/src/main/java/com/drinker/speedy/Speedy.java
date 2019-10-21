package com.drinker.speedy;

import okhttp3.Call;
import okhttp3.OkHttpClient;

public class Speedy {

    final Call.Factory callFactory;

    public Speedy(Builder builder) {
        callFactory = builder.callFactory;
    }

    public <T> T getService(Class<T> clazz) {
        //
        if (!clazz.isInterface()) {
            throw new IllegalArgumentException("service must be an interface");
        }
        String name = clazz.getName();
        String implName = name + "_impl";
        try {
            Class<?> implService = Class.forName(implName);
            Object instance = implService.newInstance();
            return (T) instance;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw new InitSpeedyFailException("can't init service cause " + e);
        }
    }

    public static class Builder {
        private Call.Factory callFactory;
        private BaseUrl baseUrl;

        public Builder callFactory(Call.Factory callFactory) {
            this.callFactory = callFactory;
            return this;
        }

        public Builder baseUrl(BaseUrl baseUrl){
            this.baseUrl =baseUrl;
            return this;
        }

        public Speedy build() {
            if (callFactory == null) {
                callFactory = new OkHttpClient();
            }
            return new Speedy(this);
        }
    }
}
