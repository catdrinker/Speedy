package com.drinker.processor.writter;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import static com.drinker.processor.SpeedyClassName.CALL_ADAPTER;
import static com.drinker.processor.SpeedyClassName.OK_HTTP_CALL;
import static com.drinker.processor.SpeedyClassName.REQUEST;
import static com.drinker.processor.SpeedyClassName.REQUEST_BODY_BUILDER;
import static com.drinker.processor.SpeedyClassName.SPEEDY_CALL;
import static com.drinker.processor.SpeedyClassName.SPEEDY_TYPE_TOKEN;
import static com.drinker.processor.SpeedyClassName.SPEEDY_WRAPPER_CALL;

public final class NoBodyMethodWriter extends MethodWriter {

    @Override
    public MethodSpec write(ExecutableElement executableElement, List<? extends VariableElement> parameters, String method, TypeMirror returnType, TypeName generateType, StringBuilder urlString) {
        return MethodSpec.overriding(executableElement)
                .addCode("$T request = new $T()\n", REQUEST, REQUEST_BODY_BUILDER)
                .addCode("." + method + "()\n")
                .addCode(".url(" + urlString.toString() + ")\n")
                .addStatement(".build()")
                .addStatement("$T newCall = client.newCall(request)", OK_HTTP_CALL)
                .addStatement("$T<$T> wrapperCall = new $T<>(converterFactory.respBodyConverter(new $T<$T>(){}), delivery, newCall, client, request)", SPEEDY_CALL, generateType, SPEEDY_WRAPPER_CALL, SPEEDY_TYPE_TOKEN, generateType)
                .addStatement("$T<$T,$T> callAdapter = callAdapterFactory.adapter()",CALL_ADAPTER,generateType,TypeName.get(returnType))
                .addStatement("return callAdapter.adapt(wrapperCall)")
                .returns(TypeName.get(returnType))
                .build();
    }
}
