package com.drinker.speedy;


import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

public class ServiceTest {

    private static String BODY_VALUE = "success";

    private MockWebServer server = new MockWebServer();
    private HttpUrl url = server.url("/");

    private OkHttpClient client = new OkHttpClient.Builder()
            .callTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build();

    private Speedy speedy = new Speedy.Builder()
            .baseUrl(url.toString())
            .callFactory(client)
            .build();

    private CallService service = speedy.getService(CallService.class);


    @Test
    public void testSync200() throws IOException {
        server.enqueue(new MockResponse().setBody(BODY_VALUE));

        Call<ResponseBody> login = service.getLogin();
        Response<ResponseBody> execute = login.execute();

        okhttp3.Response rawResponse = execute.getRawResponse();
        ResponseBody body = execute.getBody();
        ResponseBody errorBody = execute.getErrorBody();

        assert rawResponse.isSuccessful();
        assert body != null;
        assert errorBody == null;
        assert body.string().equals(BODY_VALUE);
    }

    @Test
    public void testAsync200() throws InterruptedException, IOException {
        server.enqueue(new MockResponse().setBody(BODY_VALUE));
        Call<ResponseBody> login = service.getLogin();
        final CountDownLatch latch = new CountDownLatch(1);

        final AtomicReference<Response<ResponseBody>> atomicReference = new AtomicReference<>();

        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                latch.countDown();
                atomicReference.set(response);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable e) {
            }
        });

        latch.await(10, TimeUnit.SECONDS);
        Response<ResponseBody> response = atomicReference.get();
        okhttp3.Response rawResponse = response.getRawResponse();
        ResponseBody body = response.getBody();
        ResponseBody errorBody = response.getErrorBody();

        assert rawResponse.isSuccessful();
        assert body != null;
        assert errorBody == null;
        assert body.string().equals(BODY_VALUE);
    }

    @Test
    public void testSync404() throws IOException {
        server.enqueue(new MockResponse().setResponseCode(404));

        Call<ResponseBody> login = service.getLogin();
        Response<ResponseBody> execute = login.execute();

        okhttp3.Response rawResponse = execute.getRawResponse();
        ResponseBody body = execute.getBody();
        ResponseBody errorBody = execute.getErrorBody();

        assert rawResponse.code() == 404;
        assert errorBody != null;
        assert body == null;
    }


    @Test
    public void testAsync404() throws InterruptedException, IOException {
        server.enqueue(new MockResponse().setBody(""));
        Call<ResponseBody> login = service.getLogin();
        final CountDownLatch latch = new CountDownLatch(1);

        final AtomicReference<Response<ResponseBody>> atomicReference = new AtomicReference<>();

        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                latch.countDown();
                System.out.println("join here");
                atomicReference.set(response);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable e) {
                latch.countDown();
                System.out.println(":failure");
            }
        });

        latch.await(10, TimeUnit.SECONDS);
        Response<ResponseBody> response = atomicReference.get();
        okhttp3.Response rawResponse = response.getRawResponse();
        ResponseBody body = response.getBody();
        ResponseBody errorBody = response.getErrorBody();

        assert rawResponse.code() == 404;
        assert body == null;
        assert errorBody != null;
    }


}
