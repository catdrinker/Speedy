package com.drinker.converter;

import com.drinker.speedy.Converter;
import com.drinker.speedy.TypeToken2;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class GsonResponseBodyConverterFactoryTest {

    private User<Home> user;

    @Before
    public void setUp() throws Exception {
        user = new User<>();
        Home home = new Home();
        home.h = "hahaah";
        home.value = 1;
        user.data = home;
        user.name = "1321";
        user.value = "12321321";
    }

    @Test
    public void createResponseConverter() throws IOException {
        String s = "{\"data\":{\"value\":1,\"h\":\"hahaah\"},\"name\":\"1321\",\"value\":\"12321321\"}";
        Converter.Factory converterFactory = GsonConverterFactory.create();
        Converter<ResponseBody, User<Home>> converter = converterFactory.respBodyConverter(new TypeToken2<User<Home>>() {
        });

        ResponseBody responseBody = ResponseBody.create(MultipartBody.FORM, s);
        User<Home> homeUser = converter.transform(responseBody);

        assert homeUser != null;
        assert homeUser.data != null;
        assert homeUser.data.h != null;
    }


    @Test
    public void createRequestConverter() throws IOException {
        String s = "{\"data\":{\"value\":1,\"h\":\"hahaah\"},\"name\":\"1321\",\"value\":\"12321321\"}";
        Converter.Factory converterFactory = GsonConverterFactory.create();
        Converter<User<Home>, RequestBody> reqBodyConverter = converterFactory.reqBodyConverter(new TypeToken2<User<Home>>() {
        });

        RequestBody body = reqBodyConverter.transform(user);
        assert body != null;
        assert body.contentLength() == s.length();
    }

    static class Home {
        int value;

        String h;

    }

    static class User<T> {
        T data;
        String name;
        String value;
    }


}