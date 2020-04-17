package com.drinker.speedy;


import javax.annotation.Nonnull;

public interface CallAdapter<T, R> {

    R adapt(@Nonnull Call<T> call);

    interface Factory {
        <T, R> CallAdapter<T, R> adapter();
    }

    final class DefaultCallAdapter<T> implements CallAdapter<T, Call<T>> {

        @Override
        public Call<T> adapt(@Nonnull Call<T> call) {
            return call;
        }
    }

    final class DefaultCallAdapterFactory implements CallAdapter.Factory {
        @Override
        public <T, R> CallAdapter<T, R> adapter() {
            return (CallAdapter<T, R>) new DefaultCallAdapter<T>();
        }
    }

}
