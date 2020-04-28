package com.drinker.processor.writter;

import com.drinker.annotation.Param;
import com.drinker.annotation.Path;
import com.drinker.processor.CheckUtils;
import com.drinker.processor.Log;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import static com.drinker.processor.SpeedyClassName.CALL_ADAPTER;
import static com.drinker.processor.SpeedyClassName.FORM_BODY;
import static com.drinker.processor.SpeedyClassName.OK_HTTP_CALL;
import static com.drinker.processor.SpeedyClassName.REQUEST;
import static com.drinker.processor.SpeedyClassName.REQUEST_BODY_BUILDER;
import static com.drinker.processor.SpeedyClassName.REQ_BODY;
import static com.drinker.processor.SpeedyClassName.SPEEDY_CALL;
import static com.drinker.processor.SpeedyClassName.SPEEDY_TYPE_TOKEN;
import static com.drinker.processor.SpeedyClassName.SPEEDY_WRAPPER_CALL;

public final class FormBodyWriter extends MethodWriter {

    @Override
    public MethodSpec write(ExecutableElement executableElement, List<? extends VariableElement> parameters, String method, TypeMirror returnType, TypeName generateType, StringBuilder urlString) {
        MethodSpec.Builder methodSpecBuilder = MethodSpec.overriding(executableElement)
                .addCode("$T formBody = new $T.Builder()\n", REQ_BODY, FORM_BODY);

        List<VariableElement> elements = getParams(parameters);
        for (VariableElement parameter : elements) {
            Param param = parameter.getAnnotation(Param.class);
            methodSpecBuilder.addCode(".add($S," + parameter.getSimpleName().toString() + ")\n", param.value());
        }
        methodSpecBuilder.addCode(".build()");

        return methodSpecBuilder.addStatement("")
                .addCode("$T request = new $T()\n", REQUEST, REQUEST_BODY_BUILDER)
                .addCode(".url(" + urlString.toString() + ")\n")
                .addCode("." + method + "(formBody)\n")
                .addCode(".build();\n")
                .addCode("")
                .addStatement("$T newCall = client.newCall(request)", OK_HTTP_CALL)
                .addStatement("$T<$T> wrapperCall = new $T<>(converterFactory.respBodyConverter(new $T<$T>(){}), delivery, newCall, client, request)", SPEEDY_CALL, generateType, SPEEDY_WRAPPER_CALL, SPEEDY_TYPE_TOKEN, generateType)
                .addStatement("$T<$T,$T> callAdapter = callAdapterFactory.adapter()", CALL_ADAPTER, generateType, TypeName.get(returnType))
                .addStatement("return callAdapter.adapt(wrapperCall)")
                .returns(TypeName.get(returnType))
                .build();
    }


    private List<VariableElement> getParams(List<? extends VariableElement> parameters) {
        List<VariableElement> elements = new ArrayList<>();
        for (VariableElement parameter : parameters) {
            // we just add parameter to formBody that not match with ${xxx} format, if find, just skip it
            Path path = parameter.getAnnotation(Path.class);
            Param param = parameter.getAnnotation(Param.class);
            if (path == null && param == null) {
                throw new IllegalStateException("parameter should have @Path or @Param annotation on FormBodyWriter");
            }
            if (param!=null){
                elements.add(parameter);
            }
            CheckUtils.checkIsString(ClassName.get(parameter.asType()));
        }
        return elements;
    }
}
