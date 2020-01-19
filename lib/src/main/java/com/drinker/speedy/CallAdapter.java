package com.drinker.speedy;


public interface CallAdapter<T, R> {

    R adapt(Call<T> call);

    class DefaultCallAdapter<T> implements CallAdapter<T, Call<T>> {

        @Override
        public Call<T> adapt(Call<T> call) {
            return call;
        }
    }

}
