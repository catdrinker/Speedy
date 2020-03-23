package com.drinker.processor.writter;

import com.drinker.annotation.Body;
import com.drinker.annotation.Param;
import com.drinker.processor.CheckUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import static com.drinker.processor.SpeedyClassName.OK_HTTP_CALL;
import static com.drinker.processor.SpeedyClassName.REQUEST;
import static com.drinker.processor.SpeedyClassName.REQUEST_BODY_BUILDER;
import static com.drinker.processor.SpeedyClassName.SPEEDY_CALL;
import static com.drinker.processor.SpeedyClassName.SPEEDY_WRAPPER_CALL;

public final class BodyWriter extends MethodWriter {
    @Override
    public MethodSpec write(ExecutableElement executableElement, List<? extends VariableElement> parameters, String method, TypeMirror returnType, TypeName generateType, StringBuilder urlString, List<Param> formatParams) {
        VariableElement bodyParam = getBodyParam(parameters);
        return MethodSpec.overriding(executableElement)
                .addCode("$T request = new $T()\n", REQUEST, REQUEST_BODY_BUILDER)
                .addCode(urlString.toString())
                .addCode("." + method + "(" + bodyParam.getSimpleName() + ")\n")
                .addCode(".build();\n")
                .addCode("")
                .addStatement("$T newCall = client.newCall(request)", OK_HTTP_CALL)
                .addStatement("$T<$T> wrapperCall = new $T<>(converterFactory.respBodyConverter($T.class), delivery, newCall, client, request)", SPEEDY_CALL, generateType, SPEEDY_WRAPPER_CALL, generateType)
                .addStatement("return ($T)callAdapter.adapt(wrapperCall)", TypeName.get(returnType))
                .returns(TypeName.get(returnType))
                .build();
    }

    private VariableElement getBodyParam(List<? extends VariableElement> parameters) {
        for (VariableElement parameter : parameters) {
            Body body = parameter.getAnnotation(Body.class);
            if (body != null) {
                CheckUtils.checkBody(ClassName.get(parameter.asType()));
                return parameter;
            }
        }
        throw new NullPointerException("do not find Body annotation");
    }
}
