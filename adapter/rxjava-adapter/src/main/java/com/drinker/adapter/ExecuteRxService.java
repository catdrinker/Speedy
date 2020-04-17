package com.drinker.adapter;

import com.drinker.speedy.Call;
import com.drinker.speedy.Response;

import java.io.IOException;

import rx.Emitter;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

public final class ExecuteRxService<T> implements RxService<T> {
    @Override
    public Observable<T> service(final Call<T> call) {
        return Observable.create(new Action1<Emitter<T>>() {
            @Override
            public void call(final Emitter<T> emitter) {
                emitter.setSubscription(new Subscription() {
                    @Override
                    public void unsubscribe() {
                        call.cancel();
                    }

                    @Override
                    public boolean isUnsubscribed() {
                        return call.isCancel();
                    }
                });
                try {
                    Response<T> response = call.execute();
                    emitter.onNext(response.getBody());
                } catch (IOException e) {
                    emitter.onError(e);
                } finally {
                    emitter.onCompleted();
                }
            }
        }, Emitter.BackpressureMode.LATEST);
    }
}
