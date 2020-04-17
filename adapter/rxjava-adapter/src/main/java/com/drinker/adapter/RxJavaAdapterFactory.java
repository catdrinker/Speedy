package com.drinker.adapter;

import com.drinker.speedy.CallAdapter;

public final class RxJavaAdapterFactory implements CallAdapter.Factory {
    private boolean isAsync;

    private RxJavaAdapterFactory(boolean isAsync) {
        this.isAsync = isAsync;
    }

    public static RxJavaAdapterFactory create() {
        return new RxJavaAdapterFactory(true);
    }

    public static RxJavaAdapterFactory create(boolean isAsync) {
        return new RxJavaAdapterFactory(isAsync);
    }

    @Override
    public <T, RxT> CallAdapter<T, RxT> adapter() {
        return (CallAdapter<T, RxT>) new RxJavaAdapter<T>(isAsync);
    }
}
