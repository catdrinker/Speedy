package com.drinker.processor;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

public class Log {

    private static Messager messager;

    public static void init(Messager messager) {
        if (messager == null) {
            throw new NullPointerException("messager logger mus't be null");
        }
        Log.messager = messager;

    }


    public static void i(String str) {
        messager.printMessage(Diagnostic.Kind.NOTE, str);
    }

    public static void v(String str) {
        messager.printMessage(Diagnostic.Kind.OTHER, str);
    }

    public static void w(String str) {
        messager.printMessage(Diagnostic.Kind.WARNING, str);
    }

    public static void e(String str) {
        messager.printMessage(Diagnostic.Kind.ERROR, str);
    }


}
