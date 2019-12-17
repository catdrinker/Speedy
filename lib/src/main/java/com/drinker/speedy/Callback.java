package com.drinker.speedy;

import java.io.IOException;

public interface Callback<T> {

    void onResponse(Call<T> call, Response<T> response);

    void onFailure(Call<T> call, IOException e);

}
