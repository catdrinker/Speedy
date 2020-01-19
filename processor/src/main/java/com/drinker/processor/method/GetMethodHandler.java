package com.drinker.processor.method;

import com.drinker.annotation.Get;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import java.util.List;

import javax.annotation.processing.Messager;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import static com.drinker.processor.OkHttpClassName.OK_HTTP_CALL;
import static com.drinker.processor.OkHttpClassName.REQUEST;
import static com.drinker.processor.OkHttpClassName.REQUEST_BODY_BUILDER;
import static com.drinker.processor.OkHttpClassName.SPEEDY_CALL;
import static com.drinker.processor.OkHttpClassName.SPEEDY_WRAPPER_CALL;


public class GetMethodHandler implements IHttpMethodHandler {


    @Override
    public MethodSpec process(ExecutableElement executableElement, List<? extends VariableElement> parameters, TypeMirror returnType, TypeName generateType, Messager messager) {
        Get getAnnotation = executableElement.getAnnotation(Get.class);
        if (getAnnotation != null) {
            return MethodSpec.overriding(executableElement)
                    .addCode("$T request = new $T()\n", REQUEST, REQUEST_BODY_BUILDER)
                    .addCode(".get()\n")
                    .addCode(".url(baseHttpUrl+$S)\n", getAnnotation.value())
                    .addStatement(".build()")
                    .addStatement("$T newCall = client.newCall(request)", OK_HTTP_CALL)
                    .addStatement("$T<$T> wrapperCall = new $T<>(converterFactory.<$T>respBodyConverter($T.class), newCall, client, request)", SPEEDY_CALL, generateType, SPEEDY_WRAPPER_CALL, generateType, generateType)
                    .addStatement("return ($T)callAdapter.adapt(wrapperCall)", TypeName.get(returnType))

                    .returns(TypeName.get(returnType))
                    .build();
        }
        return null;
    }
}
