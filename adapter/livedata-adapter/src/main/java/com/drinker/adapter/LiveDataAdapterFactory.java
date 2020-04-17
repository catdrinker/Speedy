package com.drinker.adapter;

import com.drinker.speedy.CallAdapter;

public class LiveDataAdapterFactory implements CallAdapter.Factory {

    /**
     * isAsync method with http
     */
    private boolean isAsync;

    private LiveDataAdapterFactory(boolean isAsync) {
        this.isAsync = isAsync;
    }

    public static LiveDataAdapterFactory create(boolean isAsync) {
        return new LiveDataAdapterFactory(isAsync);
    }

    public static LiveDataAdapterFactory create(){
        return new LiveDataAdapterFactory(true);
    }


    @Override
    public <T, LiveT> CallAdapter<T, LiveT> adapter() {
        return (CallAdapter<T, LiveT>) new LiveDataAdapter<T>(isAsync);
    }
}
