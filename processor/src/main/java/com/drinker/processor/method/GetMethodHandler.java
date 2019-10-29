package com.drinker.processor.method;

import com.drinker.annotation.Get;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;


import java.util.List;

import javax.annotation.processing.Messager;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

import okhttp3.Request;


public class GetMethodHandler implements IHttpMethodHandler {


    @Override
    public MethodSpec process(ExecutableElement executableElement, List<? extends VariableElement> parameters, Messager messager) {
        Get getAnnotation = executableElement.getAnnotation(Get.class);
        if (getAnnotation != null) {
            return MethodSpec.overriding(executableElement)
                    .addCode("$T request = new $T.Builder()", Request.class, Request.class)
                    .addCode(".get()\n")
                    .addCode(".url(baseHttpUrl+$S)\n", getAnnotation.value())
                    .addStatement(".build()")
                    .addStatement("return client.newCall(request)")
                    .returns(ClassName.get("okhttp3", "Call"))
                    .build();
        }
        return null;
    }
}
