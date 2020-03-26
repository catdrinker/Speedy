package com.drinker.speedy;


import javax.annotation.Nullable;

import okhttp3.Headers;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.ResponseBody;

/**
 * copy from{@code retrofit.Response}
 * <p>
 * thanks for retrofit and thank's for retrofit https://github.com/square/retrofit
 */
public final class Response<T> {

    /**
     * Create a synthetic successful response with {@code body} as the deserialized body.
     */
    public static <T> Response<T> success(@Nullable T body) {
        return success(body, new okhttp3.Response.Builder() //
                .code(200)
                .message("OK")
                .protocol(Protocol.HTTP_1_1)
                .request(new Request.Builder().url("http://localhost/").build())
                .build());
    }

    /**
     * Create a synthetic successful response with an HTTP status code of {@code code} and
     * {@code body} as the deserialized body.
     */
    public static <T> Response<T> success(int code, @Nullable T body) {
        if (code < 200 || code >= 300) {
            throw new IllegalArgumentException("code < 200 or >= 300: " + code);
        }
        return success(body, new okhttp3.Response.Builder() //
                .code(code)
                .message("Response.success()")
                .protocol(Protocol.HTTP_1_1)
                .request(new Request.Builder().url("http://localhost/").build())
                .build());
    }

    /**
     * Create a synthetic successful response using {@code headers} with {@code body} as the
     * deserialized body.
     */
    public static <T> Response<T> success(@Nullable T body, Headers headers) {
        checkNotNull(headers, "headers == null");
        return success(body, new okhttp3.Response.Builder() //
                .code(200)
                .message("OK")
                .protocol(Protocol.HTTP_1_1)
                .headers(headers)
                .request(new Request.Builder().url("http://localhost/").build())
                .build());
    }


    private static <T> T checkNotNull(@Nullable T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }

    /**
     * Create a successful response from {@code rawResponse} with {@code body} as the deserialized
     * body.
     */
    public static <T> Response<T> success(@Nullable T body, okhttp3.Response rawResponse) {
        checkNotNull(rawResponse, "rawResponse == null");
        if (!rawResponse.isSuccessful()) {
            throw new IllegalArgumentException("rawResponse must be successful response");
        }
        return new Response<>(rawResponse, body, null);
    }

    /**
     * Create a synthetic error response with an HTTP status code of {@code code} and {@code body}
     * as the error body.
     */
    public static <T> Response<T> error(int code, ResponseBody body) {
        checkNotNull(body, "body == null");
        if (code < 400) throw new IllegalArgumentException("code < 400: " + code);
        return error(body, new okhttp3.Response.Builder() //
                .body(new WrapperCall.NoContentResponseBody(body.contentType(), body.contentLength()))
                .code(code)
                .message("Response.error()")
                .protocol(Protocol.HTTP_1_1)
                .request(new Request.Builder().url("http://localhost/").build())
                .build());
    }

    /**
     * Create an error response from {@code rawResponse} with {@code body} as the error body.
     */
    public static <T> Response<T> error(ResponseBody body, okhttp3.Response rawResponse) {
        checkNotNull(body, "body == null");
        checkNotNull(rawResponse, "rawResponse == null");
        if (rawResponse.isSuccessful()) {
            throw new IllegalArgumentException("rawResponse should not be successful response");
        }
        return new Response<>(rawResponse, null, body);
    }


    private okhttp3.Response rawResponse;
    private @Nullable T body;
    private @Nullable ResponseBody errorBody;

    private Response(okhttp3.Response rawResponse, @Nullable T body, @Nullable ResponseBody errorBody) {
        this.rawResponse = rawResponse;
        this.body = body;
        this.errorBody = errorBody;
    }

    public @Nullable T getBody() {
        return body;
    }

    public @Nullable ResponseBody getErrorBody() {
        return errorBody;
    }

    public okhttp3.Response getRawResponse() {
        return rawResponse;
    }

}
