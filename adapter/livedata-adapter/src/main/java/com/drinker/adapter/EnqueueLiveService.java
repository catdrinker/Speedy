package com.drinker.adapter;

import com.drinker.speedy.Call;
import com.drinker.speedy.Callback;
import com.drinker.speedy.Response;

public class EnqueueLiveService<T> implements LiveService<T> {
    @Override
    public LiveResult<T> service(Call<T> call) {
        final LiveResult<T> liveResult = new LiveResult<>();
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.getBody() != null) {
                    liveResult.postValue(Result.success(response.getBody()));
                } else {
                    liveResult.postValue(Result.<T>failure(new NullPointerException("don't have a response body")));
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable e) {
                liveResult.postValue(Result.<T>failure(e));
            }
        });
        return liveResult;
    }
}
