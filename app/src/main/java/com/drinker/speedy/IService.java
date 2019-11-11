package com.drinker.speedy;

import android.service.autofill.UserData;

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

    @Get("/home/login?name={source}&pwd={pwd}")
    Call<Home> getLogin(@Param("first") String firstParam, @Param("second") String secondParam);

    @Post("/user/sign_up")
    Call<Home> getSign(@Param("key") String keyParam, @Param("value") String valueParam);

    @Put("/user/sign")
    Call<Home> get(@Body RequestBody body);

    @Delete("/user/index.jsp")
    Call<Home> deleteJsp();

    @Delete("/user/deleteBody")
    Call<Home> deleteBody(@Body RequestBody body);
}
