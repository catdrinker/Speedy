package com.drinker.adapter;

import androidx.lifecycle.LiveData;

import com.drinker.speedy.Call;
import com.drinker.speedy.CallAdapter;

public class LiveDataAdapter<T> implements CallAdapter<T, LiveData<Result<T>>> {

    /**
     * isAsync method with http
     */
    private boolean isAsync;

    public LiveDataAdapter(boolean isAsync) {
        this.isAsync = isAsync;
    }

    @Override
    public LiveData<Result<T>> adapt(Call<T> call) {
        LiveResult<T> liveResult;
        if (isAsync) {
            liveResult = new EnqueueLiveResult<>();
        } else {
            liveResult = new ExecuteLiveResult<>();
        }
        return liveResult.service(call);
    }
}
