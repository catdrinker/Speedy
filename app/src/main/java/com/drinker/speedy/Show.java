package com.drinker.speedy;

import androidx.annotation.NonNull;

public class Show<T> {

    T value;

    @NonNull
    @Override
    public String toString() {
        return "value = "+value;
    }
}
