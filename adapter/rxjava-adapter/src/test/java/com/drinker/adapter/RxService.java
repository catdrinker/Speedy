package com.drinker.adapter;

import com.drinker.adapter.RxJavaAdapterTest.Home;
import com.drinker.adapter.RxJavaAdapterTest.User;
import com.drinker.annotation.Get;
import com.drinker.annotation.Post;
import com.drinker.annotation.Service;

import rx.Observable;

@Service
public interface RxService {

    @Get("/login")
    Observable<User<Home>> getUser();

    @Post("/lo/ding/als")
    Observable<Home> getHome();

    @Post("/lo/ding/als")
    Observable<String> getStr();

}
