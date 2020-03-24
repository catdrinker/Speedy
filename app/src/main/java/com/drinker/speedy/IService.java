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
import com.drinker.annotation.PartMap;
import com.drinker.annotation.Path;
import com.drinker.annotation.Post;
import com.drinker.annotation.Put;
import com.drinker.annotation.Service;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

@Service
public interface IService {

    @Get("pl.json?rand=635840524184357321")
    LiveResult<Value> getLogin();

    @Get("{node}/pl.json/{rand}?rand=635840524184357321")
    LiveResult<Value> getLoginView(@Path("node") String node, @Param("rand") String rand);

    @Get("node/pl.json/{map}/rand=635840524184357321")
    LiveResult<Value> getLoginVi(@Path("map") String map, @Param("rands") String rand);

    @Get("node/pl.json/let/{map}")
    LiveResult<Value> getLoginVie(@Path("map") String map, @Param("rand") String rand);

    @Get("node/pl.json/let/{map}")
    LiveResult<Value> getLoginVie(@Path("map") String map, @ParamMap Map<String, String> maps);

    @Form
    @Post("/user/sign_up")
    LiveResult<Home> getSign(@Param("key") String keyParam, @Param("value") String valueParam);

    @FormMap
    @Post("/user/sign_up/{name}/")
    LiveResult<Home> getSign1(@Path("name") String name, @ParamMap Map<String, String> map);

    @Post("/user/sign_up")
    LiveResult<Home> getSign2(@Body RequestBody body);

    @MultiPart("multipart/form-data; charset=utf-8")
    @Post("/user/sign_up/{name}")
    LiveResult<Home> getSign3(@Path("name") String name, @Part MultipartBody.Part body);

    @MultiPart("multipart/form-data; charset=utf-8")
    @Post("/user/sign_up/{name}")
    LiveResult<Home> getSign4(@Path("name") String name, @PartMap List<MultipartBody.Part> parts);

    @Form
    @Post("/user/sign_up/{key}/let")
    LiveResult<Home> getSigns(@Path("key") String keyParam, @Param("value") String valueParam);

    @Put("/user/sign")
    LiveResult<Home> get(@Body RequestBody body);

    @Delete("/user/index.jsp")
    LiveResult<Home> deleteJsp();

    @Delete("/user/deleteBody")
    LiveResult<Home> deleteBody(@Body RequestBody body);

}
