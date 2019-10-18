package com.drinker.speedy;

import com.drinker.annotation.Get;
import com.drinker.annotation.Param;
import com.drinker.annotation.Post;

import okhttp3.Call;

public interface IService {

    @Get(url = "")
    Call getLogin();


    @Post(url = "")
    Call getSign(@Param("key") String key, @Param("value") String value);
}
