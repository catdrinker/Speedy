package com.drinker.speedy;

import androidx.lifecycle.LiveData;

import com.drinker.adapter.Result;
import com.drinker.annotation.Body;
import com.drinker.annotation.Delete;
import com.drinker.annotation.Get;
import com.drinker.annotation.Param;
import com.drinker.annotation.Post;
import com.drinker.annotation.Put;
import com.drinker.annotation.Service;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

@Service
public interface IService {

    @Get("/home/login?name={source}&pwd={pwd}")
    LiveData<Result<ResponseBody>> getLogin(@Param("first") String firstParam, @Param("second") String secondParam);

    @Post("/user/sign_up")
    LiveData<Result<Home>> getSign(@Param("key") String keyParam, @Param("value") String valueParam);

    @Put("/user/sign")
    LiveData<Result<Home>> get(@Body RequestBody body);

    @Delete("/user/index.jsp")
    LiveData<Result<Home>> deleteJsp();

    @Delete("/user/deleteBody")
    LiveData<Result<Home>> deleteBody(@Body RequestBody body);
}
