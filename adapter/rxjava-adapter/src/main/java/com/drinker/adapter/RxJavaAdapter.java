package com.drinker.adapter;

import com.drinker.speedy.Call;
import com.drinker.speedy.CallAdapter;

import javax.annotation.Nonnull;

import rx.Observable;

public final class RxJavaAdapter<T> implements CallAdapter<T, Observable<T>> {

    public static RxJavaAdapter create(boolean isAsync) {
        return new RxJavaAdapter(isAsync);
    }

    private boolean isAsync;

    RxJavaAdapter(boolean isAsync) {
        this.isAsync = isAsync;
    }

    @Override
    public Observable<T> adapt(@Nonnull Call<T> call) {
        RxService<T> service;
        if (isAsync) {
            service = new EnqueueRxService<>();
        } else {
            service = new ExecuteRxService<>();
        }
        return service.service(call);
    }
}
