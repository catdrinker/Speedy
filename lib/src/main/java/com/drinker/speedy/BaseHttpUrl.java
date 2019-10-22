package com.drinker.speedy;

public class BaseHttpUrl {

    public final String baseUrl;

    private BaseHttpUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public static BaseHttpUrl get(String baseUrl) {
        return new BaseHttpUrl(baseUrl);
    }

    @Override
    public String toString() {
        return "baseHttpUrl = " + baseUrl;
    }
}
