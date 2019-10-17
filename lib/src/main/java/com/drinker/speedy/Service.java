package com.drinker.speedy;


import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class Service implements IService {

    private OkHttpClient client = new OkHttpClient();

    @Override
    public Call getLogin() {
        return null;
    }

    @Override
    public Call getSign(String key, String value) {

        FormBody formBody = new FormBody.Builder()
                .add("key", key)
                .add("value", value)
                .build();

        Request request = new Request.Builder()
                .post(formBody)
                .url("")
                .build();

        return client.newCall(request);
    }
}
