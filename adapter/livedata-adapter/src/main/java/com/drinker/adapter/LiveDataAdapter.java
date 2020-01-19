package com.drinker.adapter;

import com.drinker.speedy.Call;
import com.drinker.speedy.CallAdapter;

public class LiveDataAdapter<T> implements CallAdapter<T, LiveResult<T>> {

    public static LiveDataAdapter create(boolean isAsync) {
        return new LiveDataAdapter(isAsync);
    }

    /**
     * isAsync method with http
     */
    private boolean isAsync;

    private LiveDataAdapter(boolean isAsync) {
        this.isAsync = isAsync;
    }

    @Override
    public LiveResult<T> adapt(Call<T> call) {
        LiveService<T> liveService;
        if (isAsync) {
            liveService = new EnqueueLiveService<>();
        } else {
            liveService = new ExecuteLiveService<>();
        }
        return liveService.service(call);
    }
}
