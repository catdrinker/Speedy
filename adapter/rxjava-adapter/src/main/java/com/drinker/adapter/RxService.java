package com.drinker.adapter;

import com.drinker.speedy.Call;

import rx.Observable;

public interface RxService<T> {

    /**
     * service to execute http request and return a Observable
     *
     * @param call rawCall of speedy
     * @return Observable from RxJava
     */
    Observable<T> service(Call<T> call);
}
