package com.drinker.speedy;

import java.io.IOException;

public interface Call<T> extends Cloneable {

    Response<T> execute() throws IOException;

    void enqueue(Callback<T> callback);

    void cancel();

    boolean isCancel();

}
