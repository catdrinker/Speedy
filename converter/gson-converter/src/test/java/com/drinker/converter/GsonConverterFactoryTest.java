package com.drinker.converter;

import com.drinker.speedy.TypeToken2;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import org.junit.Before;
import org.junit.Test;

public class GsonConverterFactoryTest {

    User<Home> user = new User<>();

    @Before
    public void setUp() throws Exception {
        Home home = new Home();
        home.h = "hahaah";
        home.value = 1;
        user.data = home;
        user.name = "1321";
        user.value = "12321321";
    }

    @Test
    public void create() {
        TypeToken2<User<Home>> token = new TypeToken2<User<Home>>() {
        };

        Gson gson = new Gson();
//        TypeAdapter<User<Home>> adapter = gson.getAdapter(com.google.gson.reflect.TypeToken.get(token.getType()));


        TypeAdapter<?> adapter = gson.getAdapter(new com.google.gson.reflect.TypeToken<User<Home>>() {

        });

//        String json = adapter.toJson(user);

//        System.out.println("json is " + json);


//        TypeAdapter<User<Home>> gsonAdapter = gson.getAdapter(token);


        String s = gson.toJson(user, token.getType());

        System.out.println("s is " + s);

//        System.out.println("type is " + type + type.getClass());
    }
}