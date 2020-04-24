package com.drinker.adapter;

import com.drinker.speedy.Call;
import com.drinker.speedy.Response;

import java.io.IOException;

public class ExecuteLiveService<T> implements LiveService<T> {
    @Override
    public LiveResult<T> service(Call<T> call) {
        LiveResult<T> result = new LiveResult<>();
        try {
            Response<T> response = call.execute();
            // TODO 先兼容旧版本，下个版本再更新这里
            okhttp3.Response rawResponse = response.getRawResponse();
            if (rawResponse.isSuccessful()) {
                T body = response.getBody();
                if (body != null) {
                    result.postValue(Result.success(response.getBody()));
                } else {
                    result.postValue(Result.<T>failure(new HttpException("don't have a response body" + rawResponse.code())));
                }
            } else {
                result.postValue(Result.<T>failure(new HttpException("response not success code = " + rawResponse.code() + " message = " + rawResponse.message())));
            }
        } catch (IOException e) {
            result.postValue(Result.<T>failure(e));
        }
        return result;
    }
}
