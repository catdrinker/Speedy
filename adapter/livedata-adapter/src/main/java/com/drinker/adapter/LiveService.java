package com.drinker.adapter;

import com.drinker.speedy.Call;

public interface LiveService<T> {

    /**
     * service to execute http request and return a liveData
     *
     * @param call wrapped call
     * @return a adapter liveData and it wrapped with #{Result}
     */
    LiveResult<T> service(Call<T> call);

}
