package com.drinker.speedy;

import com.drinker.annotation.Get;
import com.drinker.annotation.Param;
import com.drinker.annotation.Post;
import com.drinker.annotation.Service;

import okhttp3.Call;

@Service
public interface IService {

    @Get(url = "http://www.baidu.com")
    Call getLogin(@Param("first") String firstParam, @Param("second") String secondParam);


    @Post(url = "https://www.google.com")
    Call getSign(@Param("key") String keyParam, @Param("value") String valueParam);
}
