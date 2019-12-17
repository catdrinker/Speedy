package com.drinker.adapter;

import androidx.lifecycle.LiveData;

import com.drinker.speedy.Call;

public interface LiveResult <T>{

    /**
     * service to execute http request and return a liveData
     *
     * @param call wrapped call
     * @return a adapter liveData and it wrapped with #{Result}
     */
    LiveData<Result<T>> service(Call<T> call);

}
