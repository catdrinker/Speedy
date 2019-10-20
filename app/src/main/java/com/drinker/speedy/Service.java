package com.drinker.speedy;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Service implements IService {

    private OkHttpClient client = new OkHttpClient.Builder()
            .build();

    @Override
    public Call getLogin(String first, String second) {
        Request request = new Request.Builder()
                .get()
                .url("")
                .build();

        return client.newCall(request);
    }

    @Override
    public Call getSign(String key, String value) {
        RequestBody formBody = new FormBody.Builder()
                .add("key", key)
                .add("value", value)
                .build();

        Request request = new Request.Builder()
                .url("")
                .build();

        return client.newCall(request);
    }
}
