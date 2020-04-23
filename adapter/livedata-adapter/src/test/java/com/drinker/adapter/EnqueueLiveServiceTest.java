package com.drinker.adapter;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.drinker.adapter.CallService.User;
import com.drinker.speedy.Call;
import com.drinker.speedy.Callback;
import com.drinker.speedy.Response;
import com.drinker.speedy.Speedy;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import okhttp3.Protocol;
import okhttp3.Request;

public class EnqueueLiveServiceTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();


    private CallService callService;

    @Before
    public void setUp() throws Exception {
        Speedy speedy = new Speedy.Builder()
                .baseUrl("")
                .callAdapterFactory(LiveDataAdapterFactory.create())
                .build();
        callService = speedy.getService(callService.getClass());
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void serviceEnqueueSuccessTest() {
        LiveResult<User<CallService.Home>> liveResult = callService.getService();

        liveResult.observeForever(new Observer<Result<User<CallService.Home>>>() {
            @Override
            public void onChanged(Result<User<CallService.Home>> userResult) {

            }
        });
        // todo

    }

    @Test
    public void serviceEnqueueFailTest() {

    }


    public <T> T mock(Class<T> clazz) {
        return Mockito.mock(clazz);
    }

}