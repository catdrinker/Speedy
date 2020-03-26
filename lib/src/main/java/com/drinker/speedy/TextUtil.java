package com.drinker.speedy;

import javax.annotation.Nullable;

public final class TextUtil {

    public static boolean isEmpty(@Nullable CharSequence charSequence) {
        return charSequence == null || charSequence.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence charSequence) {
        return !isEmpty(charSequence);
    }

    public static boolean contains(CharSequence charSequence, String value) {
        return charSequence.toString().contains(value);
    }
}
