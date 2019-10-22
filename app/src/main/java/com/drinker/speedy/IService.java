package com.drinker.speedy;

import com.drinker.annotation.Get;
import com.drinker.annotation.Param;
import com.drinker.annotation.Post;
import com.drinker.annotation.Service;

import okhttp3.Call;

@Service
public interface IService {

    @Get(url = "/user/login")
    Call getLogin(@Param("first") String firstParam, @Param("second") String secondParam);


    @Post(url = "/user/sign_up")
    Call getSign(@Param("key") String keyParam, @Param("value") String valueParam);
}
