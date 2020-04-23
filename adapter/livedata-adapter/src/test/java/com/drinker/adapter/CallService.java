package com.drinker.adapter;

import com.drinker.annotation.Get;
import com.drinker.annotation.Service;

@Service
public interface CallService {

    @Get("ur/s")
    LiveResult<User<Home>> getService();


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


