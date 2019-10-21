package com.drinker.speedy;

public interface Call<T> extends Cloneable {

    Response<T> execute();

    void enqueue(Callback<T> callback);


}
