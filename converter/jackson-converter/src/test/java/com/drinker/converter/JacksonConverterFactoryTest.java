package com.drinker.converter;


import com.drinker.speedy.Converter;
import com.drinker.speedy.TypeToken2;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class JacksonConverterFactoryTest {

    private ObjectMapper mapper;

    private User<Home> user;

    @Before
    public void setUp() {
        user = new User<>();
        Home home = new Home();
        home.h = "hahaah";
        home.value = 1;
        user.data = home;
        user.name = "1321";
        user.value = "12321321";

        SimpleModule module = new SimpleModule();
        mapper = new ObjectMapper();
        mapper.registerModule(module);
        mapper.configure(MapperFeature.AUTO_DETECT_GETTERS, false);
        mapper.configure(MapperFeature.AUTO_DETECT_SETTERS, false);
        mapper.configure(MapperFeature.AUTO_DETECT_IS_GETTERS, false);
        mapper.setVisibility(mapper.getSerializationConfig()
                .getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY));
    }

    @Test
    public void testConverterRequest() throws IOException {
        String s = "{\"data\":{\"value\":1,\"h\":\"hahaah\"},\"name\":\"1321\",\"value\":\"12321321\"}";
        JacksonConverterFactory converterFactory = JacksonConverterFactory.create(mapper);
        Converter<User<Home>, RequestBody> converter = converterFactory.reqBodyConverter(new TypeToken2<User<Home>>() {
        });
        RequestBody body = converter.transform(user);

        assert body != null;
        assert body.contentLength() == s.length();
    }

    @Test
    public void testConverterResponse() throws IOException {
        String s = "{\"data\":{\"value\":1,\"h\":\"hahaah\"},\"name\":\"1321\",\"value\":\"12321321\"}";
        JacksonConverterFactory converterFactory = JacksonConverterFactory.create(mapper);

        Converter<ResponseBody, User<Home>> converter = converterFactory.respBodyConverter(new TypeToken2<User<Home>>() {
        });
        ResponseBody responseBody = ResponseBody.create(MultipartBody.FORM, s);
        User<Home> homeUser = converter.transform(responseBody);

        assert homeUser != null;
        assert homeUser.data != null;
        assert homeUser.data.h != null;
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