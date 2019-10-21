package com.drinker.speedy;

import okhttp3.ResponseBody;

public class Response<T> {

    private okhttp3.Response rawResponse;
    private T body;
    private ResponseBody errorBody;

    public Response(okhttp3.Response rawResponse, T body, ResponseBody errorBody) {
        this.rawResponse = rawResponse;
        this.body = body;
        this.errorBody = errorBody;
    }

}
