package com.drinker.speedy;

import com.drinker.adapter.LiveResult;
import com.drinker.annotation.Body;
import com.drinker.annotation.Delete;
import com.drinker.annotation.Form;
import com.drinker.annotation.FormMap;
import com.drinker.annotation.Get;
import com.drinker.annotation.MultiPart;
import com.drinker.annotation.Param;
import com.drinker.annotation.ParamMap;
import com.drinker.annotation.Part;
import com.drinker.annotation.Post;
import com.drinker.annotation.Put;
import com.drinker.annotation.Service;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

@Service
public interface IService {

    @Get("pl.json?rand=635840524184357321")
    LiveResult<Value> getLogin();

    @Get("{node}/pl.json/{rand}?rand=635840524184357321")
    LiveResult<Value> getLoginView(@Param("node") String node, @Param("rands") String rand);

    @Get("node/pl.json/{map}/rand=635840524184357321")
    LiveResult<Value> getLoginVi(@Param("map") String map, @Param("rand") String rand);

    @Get("node/pl.json/let/{map}")
    LiveResult<Value> getLoginVie(@Param("map") String map, @Param("rand") String rand);

    @Form
    @Post("/user/sign_up")
    LiveResult<Home> getSign(@Param("key") String keyParam, @Param("value") String valueParam);

    @FormMap
    @Post("/user/sign_up/{name}/")
    LiveResult<Home> getSign1(@Param("name") String name, @ParamMap Map<String, String> map);


    @Post("/user/sign_up")
    LiveResult<Home> getSign2(@Body RequestBody body);

    @MultiPart("multipart/form-data; charset=utf-8")
    @Post("/user/sign_up/{name}")
    LiveResult<Home> getSign3(@Param("name") String name, @Part("image/png") MultipartBody.Part body);

    @Form
    @Post("/user/sign_up/{key}/let")
    LiveResult<Home> getSigns(@Param("key") String keyParam, @Param("value") String valueParam);

    @Put("/user/sign")
    LiveResult<Home> get(@Body RequestBody body);

    @Delete("/user/index.jsp")
    LiveResult<Home> deleteJsp();

    @Delete("/user/deleteBody")
    LiveResult<Home> deleteBody(@Body RequestBody body);

}
