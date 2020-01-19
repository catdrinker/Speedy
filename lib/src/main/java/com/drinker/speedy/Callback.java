package com.drinker.speedy;

public interface Callback<T> {

    void onResponse(Call<T> call, Response<T> response);

    void onFailure(Call<T> call, Throwable e);

}
