package com.drinker.processor.method;

import com.drinker.annotation.Get;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import java.util.List;

import javax.annotation.processing.Messager;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import static com.drinker.processor.OkHttpClassName.REQUEST;
import static com.drinker.processor.OkHttpClassName.REQUEST_BODY_BUILDER;


public class GetMethodHandler implements IHttpMethodHandler {


    @Override
    public MethodSpec process(ExecutableElement executableElement, List<? extends VariableElement> parameters, Messager messager) {
        Get getAnnotation = executableElement.getAnnotation(Get.class);
        if (getAnnotation != null) {
            TypeMirror returnType = executableElement.getReturnType();
            return MethodSpec.overriding(executableElement)
                    .addCode("$T request = new $T()\n", REQUEST, REQUEST_BODY_BUILDER)
                    .addCode(".get()\n")
                    .addCode(".url(baseHttpUrl+$S)\n", getAnnotation.value())
                    .addStatement(".build()")
                    .addStatement("okhttp3.Call newCall = client.newCall(request)")
                    .addStatement("return new WrapperCall<>(respConverter, newCall, client, request)")
                    .returns(TypeName.get(returnType))
                    .build();
        }
        return null;
    }
}
