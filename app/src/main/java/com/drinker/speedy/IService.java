package com.drinker.speedy;

import com.drinker.annotation.Body;
import com.drinker.annotation.Get;
import com.drinker.annotation.Param;
import com.drinker.annotation.Post;
import com.drinker.annotation.Put;
import com.drinker.annotation.Service;

import okhttp3.Call;
import okhttp3.RequestBody;

@Service
public interface IService {

    @Get("")
    Call getLogin(@Param("first") String firstParam, @Param("second") String secondParam);


    @Post("/user/sign_up")
    Call getSign(@Param("key") String keyParam, @Param("value") String valueParam);

    @Put("/user/sign")
    Call get(@Body RequestBody body);
}
