package com.drinker.processor.writter;

import com.drinker.annotation.Body;
import com.drinker.annotation.Path;
import com.drinker.processor.CheckUtils;
import com.squareup.javapoet.ClassName;
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

public final class BodyWriter extends MethodWriter {
    @Override
    public MethodSpec write(ExecutableElement executableElement, List<? extends VariableElement> parameters, String method, TypeMirror returnType, TypeName generateType, StringBuilder urlString) {
        VariableElement bodyParam = getBodyParam(parameters);
        return MethodSpec.overriding(executableElement)
                .addCode("$T request = new $T()\n", REQUEST, REQUEST_BODY_BUILDER)
                .addCode(".url(" + urlString.toString() + ")\n")
                .addCode("." + method + "(" + bodyParam.getSimpleName() + ")\n")
                .addCode(".build();\n")
                .addCode("")
                .addStatement("$T newCall = client.newCall(request)", OK_HTTP_CALL)
                .addStatement("$T<$T> wrapperCall = new $T<>(converterFactory.respBodyConverter(new $T<$T>(){}), delivery, newCall, client, request)", SPEEDY_CALL, generateType, SPEEDY_WRAPPER_CALL, SPEEDY_TYPE_TOKEN, generateType)
                .addStatement("$T<$T,$T> callAdapter = callAdapterFactory.adapter()", CALL_ADAPTER, generateType, TypeName.get(returnType))
                .addStatement("return callAdapter.adapt(wrapperCall)")
                .returns(TypeName.get(returnType))
                .build();
    }

    private VariableElement getBodyParam(List<? extends VariableElement> parameters) {
        VariableElement element = null;
        int bodyCount = 0;
        for (VariableElement parameter : parameters) {
            Path path = parameter.getAnnotation(Path.class);
            Body body = parameter.getAnnotation(Body.class);
            if (path == null && body == null) {
                throw new IllegalStateException("body writer parameter must have @Path or @Body annotation");
            }
            TypeName typeName = ClassName.get(parameter.asType());
            if (path != null) {
                CheckUtils.checkIsString(typeName);
            }
            if (body != null) {
                CheckUtils.checkBody(ClassName.get(parameter.asType()));
                element = parameter;
                bodyCount++;
            }
        }
        if (bodyCount == 1) {
            return element;
        }
        throw new NullPointerException("do not find legitimate Body annotation with okhttp3.RequestBody");
    }
}
