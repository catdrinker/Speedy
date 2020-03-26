package com.drinker.speedy;


import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

public class ServiceTest {

    private MockWebServer server = new MockWebServer();

    private OkHttpClient client = new OkHttpClient.Builder()
            .callTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build();

    @Test
    public void testSync200() throws IOException {
        server.enqueue(new MockResponse().setBody("hahahaha"));
        HttpUrl url = server.url("/");

        Speedy speedy = new Speedy.Builder()
                .baseUrl(url.toString())
                .callFactory(client)
                .build();

        CallService service = speedy.getService(CallService.class);
        Call<ResponseBody> login = service.getLogin();
        Response<ResponseBody> execute = login.execute();

        okhttp3.Response rawResponse = execute.getRawResponse();
        ResponseBody body = execute.getBody();
        ResponseBody errorBody = execute.getErrorBody();

        assert rawResponse.isSuccessful();
        assert body != null;
        assert errorBody == null;
        assert body.string().equals("hahahaha");
    }


}
