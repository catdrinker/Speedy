package com.drinker.adapter;

import com.drinker.speedy.Call;
import com.drinker.speedy.Callback;
import com.drinker.speedy.Response;

import rx.Emitter;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

public final class EnqueueRxService<T> implements RxService<T> {

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

                call.enqueue(new Callback<T>() {
                    @Override
                    public void onResponse(Call<T> call1, Response<T> response) {
                        emitter.onNext(response.getBody());
                        emitter.onCompleted();
                    }

                    @Override
                    public void onFailure(Call<T> call1, Throwable e) {
                        emitter.onError(e);
                        emitter.onCompleted();
                    }
                });
            }
        }, Emitter.BackpressureMode.BUFFER);
    }
}
