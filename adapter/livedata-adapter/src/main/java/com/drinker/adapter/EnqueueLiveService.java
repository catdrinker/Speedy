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
                // TODO 先兼容旧版本，下个版本再更新这里
                okhttp3.Response rawResponse = response.getRawResponse();
                if (rawResponse.isSuccessful()) {
                    if (response.getBody() != null) {
                        liveResult.postValue(Result.success(response.getBody()));
                    } else {
                        liveResult.postValue(Result.<T>failure(new HttpException("don't have a response body" + rawResponse.code())));
                    }
                } else {
                    liveResult.postValue(Result.<T>failure(new HttpException("response not success code = " + rawResponse.code() + " message = " + rawResponse.message())));
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
