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

    @Get("{node}/pl.json/{rand}?rand=635840524184357321")
    LiveResult<Value> getLoginView(@Param("node") String node, @Param("rands") String rand);

    @Get("node/pl.json/{map}/rand=635840524184357321")
    LiveResult<Value> getLoginVi(@Param("map") String map, @Param("rand") String rand);

    @Get("node/pl.json/{map}/let")
    LiveResult<Value> getLoginVie(@Param("map") String map, @Param("rand") String rand);

    @Post("/user/sign_up")
    LiveResult<Home> getSign(@Param("key") String keyParam, @Param("value") String valueParam);

    @Post("/user/sign_up/{key}/let")
    LiveResult<Home> getSigns(@Param("key") String keyParam, @Param("value") String valueParam);


    @Put("/user/sign")
    LiveResult<Home> get(@Body RequestBody body);

    @Delete("/user/index.jsp")
    LiveResult<Home> deleteJsp();

    @Delete("/user/deleteBody")
    LiveResult<Home> deleteBody(@Body RequestBody body);

}
