package com.drinker.speedy;

import okhttp3.ResponseBody;

public class Response<T> {

    public static <T> Response<T> success(okhttp3.Response rawResponse, T body) {
        if (!rawResponse.isSuccessful()) {
            throw new IllegalArgumentException("rawResponse with success response must be successful");
        }
        return new Response<>(rawResponse, body, null);
    }

    public static <T> Response<T> error(okhttp3.Response rawResponse, T body, ResponseBody errorBody) {
        int code = rawResponse.code();
        if (code < 400) {
            throw new IllegalArgumentException("error response code " + code);
        }
        return new Response<>(rawResponse, body, errorBody);
    }


    private okhttp3.Response rawResponse;
    private T body;
    private ResponseBody errorBody;

    private Response(okhttp3.Response rawResponse, T body, ResponseBody errorBody) {
        this.rawResponse = rawResponse;
        this.body = body;
        this.errorBody = errorBody;
    }

    public T getBody() {
        return body;
    }

    public ResponseBody getErrorBody() {
        return errorBody;
    }

    public okhttp3.Response getRawResponse() {
        return rawResponse;
    }

}
