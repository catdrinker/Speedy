package com.drinker.adapter;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.drinker.converter.GsonConverterFactory;
import com.drinker.speedy.Speedy;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

public class EnqueueLiveServiceTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private MockWebServer server = new MockWebServer();

    private CallService service;


    private CallService.User<CallService.Home> user = new CallService.User<>();

    @Before
    public void setUp() throws Exception {
        HttpUrl url = server.url("/");

        OkHttpClient client = new OkHttpClient.Builder()
                .callTimeout(5, TimeUnit.SECONDS)
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        Speedy speedy = new Speedy.Builder()
                .baseUrl(url.toString())
                .callAdapterFactory(LiveDataAdapterFactory.create())
                .converterFactory(GsonConverterFactory.create())
                .callFactory(client)
                .build();

        service = speedy.getService(CallService.class);

        CallService.Home home = new CallService.Home();
        home.value = 1;
        home.h = "hahaah";
        user.data = home;
        user.name = "1321";
        user.value = "12321321";
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void serviceEnqueueSuccessTest() throws InterruptedException {
        String s = "{\"data\":{\"value\":1,\"h\":\"hahaah\"},\"name\":\"1321\",\"value\":\"12321321\"}";
        server.enqueue(new MockResponse().setBody(s));

        final CountDownLatch latch = new CountDownLatch(1);
        LiveResult<CallService.User<CallService.Home>> liveResult = service.getService();

        final AtomicReference<Result<CallService.User<CallService.Home>>> atomicReference = new AtomicReference<>();

        liveResult.observeForever(new Observer<Result<CallService.User<CallService.Home>>>() {
            @Override
            public void onChanged(Result<CallService.User<CallService.Home>> userResult) {
                atomicReference.set(userResult);
                latch.countDown();
            }
        });
        latch.await(10, TimeUnit.SECONDS);
        Result<CallService.User<CallService.Home>> result = atomicReference.get();
        assert result != null;
        assert result.isSuccess();
        assert result.getException() == null;
        CallService.User<CallService.Home> homeUser = result.getResponse();
        assert user.equals(homeUser);
    }

    @Test
    public void serviceEnqueueFailTest() throws InterruptedException {
        server.enqueue(new MockResponse().setResponseCode(404));
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<Result<CallService.User<CallService.Home>>> atomicReference = new AtomicReference<>();

        LiveResult<CallService.User<CallService.Home>> liveResult = service.getService();
        liveResult.observeForever(new Observer<Result<CallService.User<CallService.Home>>>() {
            @Override
            public void onChanged(Result<CallService.User<CallService.Home>> userResult) {
                atomicReference.set(userResult);
                latch.countDown();
            }
        });
        latch.await(10, TimeUnit.SECONDS);
        Result<CallService.User<CallService.Home>> userResult = atomicReference.get();
        System.out.println("userresult " + userResult);
        assert userResult.isFailure();
        assert userResult.getException() != null;
        assert userResult.getResponse() == null;
        assert userResult.getException() instanceof HttpException;
        assert "response not success code = 404 message = Client Error".equals(userResult.getException().getLocalizedMessage());

    }

    @Test
    public void serviceEnqueueInterruptExceptionTest() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<Result<CallService.User<CallService.Home>>> atomicReference = new AtomicReference<>();

        LiveResult<CallService.User<CallService.Home>> liveResult = service.getService();
        liveResult.observeForever(new Observer<Result<CallService.User<CallService.Home>>>() {
            @Override
            public void onChanged(Result<CallService.User<CallService.Home>> userResult) {
                atomicReference.set(userResult);
                latch.countDown();
            }
        });
        latch.await(10, TimeUnit.SECONDS);
        Result<CallService.User<CallService.Home>> userResult = atomicReference.get();
        assert userResult.isFailure();
        assert userResult.getException() != null;
        assert userResult.getResponse() == null;
        assert userResult.getException() instanceof InterruptedIOException;
        assert "timeout".equals(userResult.getException().getMessage());
    }

    @Test
    public void serviceEnqueueIOExceptionTest() throws InterruptedException {
        String s = "{\"data\":{\"value\":1,\"h\":\"hahaah\"},\"name\":\"1321\",\"value\"}";
        server.enqueue(new MockResponse().setBody(s));
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<Result<CallService.User<CallService.Home>>> atomicReference = new AtomicReference<>();

        LiveResult<CallService.User<CallService.Home>> liveResult = service.getService();
        liveResult.observeForever(new Observer<Result<CallService.User<CallService.Home>>>() {
            @Override
            public void onChanged(Result<CallService.User<CallService.Home>> userResult) {
                atomicReference.set(userResult);
                latch.countDown();
            }
        });
        latch.await(10, TimeUnit.SECONDS);
        System.out.println(atomicReference.get());

    }

}