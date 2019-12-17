package com.drinker.adapter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.drinker.speedy.Call;
import com.drinker.speedy.Response;

import java.io.IOException;

public class ExecuteLiveResult<T> implements LiveResult<T> {
    @Override
    public LiveData<Result<T>> service(Call<T> call) {
        MutableLiveData<Result<T>> liveData = new MutableLiveData<>();

        try {
            Response<T> response = call.execute();
            liveData.postValue(Result.success(response.getBody()));
        } catch (IOException e) {
            liveData.postValue(Result.<T>failure(e));
        }
        return liveData;
    }
}
