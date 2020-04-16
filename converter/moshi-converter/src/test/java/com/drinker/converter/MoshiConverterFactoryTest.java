package com.drinker.converter;

import com.drinker.speedy.Converter;
import com.drinker.speedy.TypeToken2;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonQualifier;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class MoshiConverterFactoryTest {

    private User<Home> user;

    private MoshiConverterFactory moshiConverterFactory;

    @org.junit.Before
    public void setUp() throws Exception {
        user = new User<>();
        Home home = new Home();
        home.h = "hahaah";
        home.value = 1;
        user.data = home;
        user.name = "1321";
        user.value = "12321321";

        Moshi moshi = new Moshi.Builder()
                .add(new JsonAdapter.Factory() {
                    @Nullable
                    @Override
                    public JsonAdapter<?> create(@Nonnull Type type, @Nonnull Set<? extends Annotation> annotations, @Nonnull Moshi moshi) {
                        for (Annotation annotation : annotations) {
                            if (!annotation.annotationType().isAnnotationPresent(JsonQualifier.class)) {
                                throw new AssertionError("Non-@JsonQualifier annotation: " + annotation);
                            }
                        }
                        return null;
                    }
                }).build();

        moshiConverterFactory = MoshiConverterFactory.create(moshi);
    }

    @org.junit.After
    public void tearDown() throws Exception {
        moshiConverterFactory = null;
    }

    @org.junit.Test
    public void reqBodyConverterTest() throws IOException {
        String value = "{\"data\":{\"value\":1,\"h\":\"hahaah\"},\"name\":\"1321\",\"value\":\"12321321\"}";
        TypeToken2<User<Home>> typeToken2 = new TypeToken2<User<Home>>() {
        };
        Converter<User<Home>, RequestBody> reqConverter = moshiConverterFactory.reqBodyConverter(typeToken2);
        RequestBody requestBody = reqConverter.transform(user);
        assert requestBody != null;
        assert MediaType.get("application/json; charset=UTF-8").equals(requestBody.contentType());
        assert requestBody.contentLength() == value.length();
    }

    @org.junit.Test
    public void respBodyConverterTest() throws IOException {
        String value = "{\"data\":{\"value\":1,\"h\":\"hahaah\"},\"name\":\"1321\",\"value\":\"12321321\"}";
        Converter<ResponseBody, User<Home>> converter = moshiConverterFactory.respBodyConverter(new TypeToken2<User<Home>>() {
        });
        ResponseBody responseBody = ResponseBody.create(MultipartBody.FORM, value);
        User<Home> homeUser = converter.transform(responseBody);

        assert homeUser != null;
        assert homeUser.data != null;
        assert homeUser.data.h != null;
        assert homeUser.data.value == 1;
        assert "hahaah".equals(homeUser.data.h);
        assert "1321".equals(homeUser.name);
        assert "12321321".equals(homeUser.value);
    }

    static class Home {
        int value;
        String h;

        @Override
        public String toString() {
            return "value = " + value
                    + " h = " + h;
        }
    }

    static class User<T> {
        T data;
        String name;
        String value;

        @Override
        public String toString() {
            return "data = " + data
                    + " name = " + name
                    + " value = " + value;
        }
    }

}