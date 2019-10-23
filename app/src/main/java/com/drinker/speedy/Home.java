package com.drinker.speedy;

import androidx.annotation.NonNull;

public class Home {

    private String str;
    private int value;

    public Home(String str, int value) {
        this.str = str;
        this.value = value;
    }


    @NonNull
    @Override
    public String toString() {
        return "str = " + str + " value = " + value;
    }
}
