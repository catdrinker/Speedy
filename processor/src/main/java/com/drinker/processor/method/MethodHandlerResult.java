package com.drinker.processor.method;

import com.squareup.javapoet.MethodSpec;

public class MethodHandlerResult {
    private MethodSpec methodSpec;
    private boolean handle;

    public MethodHandlerResult(MethodSpec methodSpec, boolean handle) {
        this.methodSpec = methodSpec;
        this.handle = handle;
    }
}
