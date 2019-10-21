package com.drinker.speedy;

public class BaseUrl {

    public final String baseUrl;

    private BaseUrl(String baseUrl){
        this.baseUrl = baseUrl;
    }

    public static BaseUrl get(String baseUrl){
        return new BaseUrl(baseUrl);
    }

}
