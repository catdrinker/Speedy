package com.drinker.speedy;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitService {

    @GET("pl.json?rand=635840524184357321")
    public Call<ResponseBody> reqValue();

}
