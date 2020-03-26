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
            T body = response.getBody();
            if (body != null) {
                result.postValue(Result.success(response.getBody()));
            } else {
                result.postValue(Result.<T>failure(new NullPointerException("don't have a response body")));
            }
        } catch (IOException e) {
            result.postValue(Result.<T>failure(e));
        }
        return result;
    }
}
