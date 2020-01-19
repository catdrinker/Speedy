package com.drinker.speedy;

import com.drinker.adapter.LiveResult;
import com.drinker.annotation.Body;
import com.drinker.annotation.Delete;
import com.drinker.annotation.Get;
import com.drinker.annotation.Param;
import com.drinker.annotation.Post;
import com.drinker.annotation.Put;
import com.drinker.annotation.Service;

import okhttp3.RequestBody;

@Service
public interface IService {

    @Get("pl.json?rand=635840524184357321")
    LiveResult<Value> getLogin(@Param("first") String firstParam, @Param("second") String secondParam);

    @Post("/user/sign_up")
    LiveResult<Home> getSign(@Param("key") String keyParam, @Param("value") String valueParam);

    @Put("/user/sign")
    LiveResult<Home> get(@Body RequestBody body);

    @Delete("/user/index.jsp")
    LiveResult<Home> deleteJsp();

    @Delete("/user/deleteBody")
    LiveResult<Home> deleteBody(@Body RequestBody body);

}
