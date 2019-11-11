package com.drinker.speedy;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.ResponseBody;
import okio.Timeout;

public class WrapperCall<T> implements Call<T> {

    private okhttp3.Call rawCall;
    private okhttp3.Call.Factory callFactory;
    private Request rawRequest;
    private Converter<ResponseBody, T> converter;
    private IDelivery delivery;

    private boolean isExecuted;

    public WrapperCall(Converter<ResponseBody, T> converter, IDelivery delivery, okhttp3.Call rawCall, okhttp3.Call.Factory callFactory, Request rawRequest) {
        this.converter = converter;
        this.rawCall = rawCall;
        this.callFactory = callFactory;
        this.rawRequest = rawRequest;
        this.delivery = delivery;
    }

    @Override
    public Response<T> execute() throws IOException {
        assert rawCall != null;
        if (isExecuted) {
            throw new IllegalStateException("one call only can execute one time");
        }
        isExecuted = true;
        okhttp3.Response response = rawCall.execute();
        T body = converter.transform(response.body());
        if (response.isSuccessful()) {
            return Response.success(response, body);
        } else {
            return Response.error(response, null, response.body());
        }
    }

    @Override
    public void enqueue(final Callback<T> callback) {
        assert rawCall != null;
        if (isExecuted) {
            throw new IllegalStateException("one call only can execute one time");
        }
        isExecuted = true;

        rawCall.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                callback.onFailure(WrapperCall.this, e);
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                T body = converter.transform(response.body());
                callback.onResponse(WrapperCall.this, body);
            }
        });
    }

    public void cancel() {
        if (!rawCall.isCanceled()) {
            rawCall.cancel();
        }
    }

    public Timeout timeout() {
        return rawCall.timeout();
    }


    public okhttp3.Call.Factory getCallFactory() {
        return callFactory;
    }

    public Request getRawRequest() {
        return rawRequest;
    }

    public void setRawRequest(Request rawRequest) {
        this.rawRequest = rawRequest;
    }

}
