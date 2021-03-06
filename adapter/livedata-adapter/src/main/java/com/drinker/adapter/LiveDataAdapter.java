package com.drinker.adapter;

import com.drinker.speedy.Call;
import com.drinker.speedy.CallAdapter;

import javax.annotation.Nonnull;

public class LiveDataAdapter<T> implements CallAdapter<T, LiveResult<T>> {

    /**
     * isAsync method with http
     */
    private boolean isAsync;

    public LiveDataAdapter(boolean isAsync) {
        this.isAsync = isAsync;
    }

    @Override
    public LiveResult<T> adapt(@Nonnull Call<T> call) {
        LiveService<T> liveService;
        if (isAsync) {
            liveService = new EnqueueLiveService<>();
        } else {
            liveService = new ExecuteLiveService<>();
        }
        return liveService.service(call);
    }
}
