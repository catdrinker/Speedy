package com.drinker.speedy;

import com.drinker.annotation.Get;
import com.drinker.annotation.Service;

import okhttp3.ResponseBody;

@Service
public interface CallService {
    @Get("/")
    Call<ResponseBody> getLogin();
}
