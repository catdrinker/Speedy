package com.drinker.adapter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.drinker.speedy.Call;
import com.drinker.speedy.Callback;
import com.drinker.speedy.Response;

import java.io.IOException;

public class EnqueueLiveResult<T> implements LiveResult<T> {
    @Override
    public LiveData<Result<T>> service(Call<T> call) {
        final MutableLiveData<Result<T>> liveData = new MutableLiveData<>();
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                liveData.postValue(Result.success(response.getBody()));
            }

            @Override
            public void onFailure(Call<T> call, IOException e) {
                liveData.postValue(Result.<T>failure(e));
            }
        });
        return liveData;
    }
}
