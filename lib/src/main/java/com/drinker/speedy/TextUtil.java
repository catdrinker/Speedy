package com.drinker.speedy;

public class TextUtil {

    public static boolean isEmpty(CharSequence charSequence) {
        return charSequence == null || charSequence.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence charSequence) {
        return !isEmpty(charSequence);
    }

    public static boolean contains(CharSequence charSequence, String value) {
        return charSequence.toString().contains(value);
    }
}
