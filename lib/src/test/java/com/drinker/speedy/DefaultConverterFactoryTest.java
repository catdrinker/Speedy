package com.drinker.speedy;

import org.junit.Test;

import okhttp3.RequestBody;

public class DefaultConverterFactoryTest {

    private Converter.Factory factory = new DefaultConverterFactory();


    @Test
    public void reqBodyConverter() {
        // reqBodyConverter();
        User<Home> homeUser = new User<>();
        Home home = new Home();


        TypeToken2<User<Home>> typeToken2 = new TypeToken2<User<Home>>() {
        };

        Converter<User<Home>, RequestBody> converter = factory.reqBodyConverter(typeToken2);


    }

    @Test
    public void respBodyConverter() {

    }


    static class User<T> {
        T data;
        String name;
        String pwd;

    }

    static class Home {
        String address;
        int number;
    }
}