package com.drinker.speedy;


import javax.annotation.Nonnull;

public interface CallAdapter<T, R> {

    R adapt(@Nonnull Call<T> call);

    class DefaultCallAdapter<T> implements CallAdapter<T, Call<T>> {

        @Override
        public Call<T> adapt(@Nonnull Call<T> call) {
            return call;
        }
    }

}
