package com.drinker.adapter;

import androidx.annotation.Nullable;

import com.drinker.annotation.Get;
import com.drinker.annotation.Service;

@Service
public interface CallService {

    @Get("/login")
    LiveResult<User<Home>> getService();


    static class Home {
        int value;
        String h;

        @Override
        public String toString() {
            return "value = " + value
                    + " h = " + h;
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (obj instanceof Home) {
                Home other = (Home) obj;
                return other.h.equals(h) && other.value == value;
            }
            return false;
        }
    }

    static class User<T> {
        T data;
        String name;
        String value;

        @Override
        public String toString() {
            return "data = " + data
                    + " name = " + name
                    + " value = " + value;
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (obj instanceof User) {
                User<?> user = (User<?>) obj;
                return user.data.equals(data)
                        && user.name.equals(name)
                        && user.value.equals(value);

            }
            return false;
        }
    }
}


