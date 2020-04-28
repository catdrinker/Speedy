package com.drinker.processor.writter;

import com.drinker.annotation.ParamMap;
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
import static com.drinker.processor.SpeedyClassName.FORM_BODY;
import static com.drinker.processor.SpeedyClassName.FORM_BODY_BUILDER;
import static com.drinker.processor.SpeedyClassName.ITERATOR;
import static com.drinker.processor.SpeedyClassName.MAP_ENTRY;
import static com.drinker.processor.SpeedyClassName.OK_HTTP_CALL;
import static com.drinker.processor.SpeedyClassName.REQUEST;
import static com.drinker.processor.SpeedyClassName.REQUEST_BODY_BUILDER;
import static com.drinker.processor.SpeedyClassName.SPEEDY_CALL;
import static com.drinker.processor.SpeedyClassName.SPEEDY_TYPE_TOKEN;
import static com.drinker.processor.SpeedyClassName.SPEEDY_WRAPPER_CALL;
import static com.drinker.processor.SpeedyClassName.STRING;

public final class FormMapWriter extends MethodWriter {
    @Override
    public MethodSpec write(ExecutableElement executableElement, List<? extends VariableElement> parameters, String method, TypeMirror returnType, TypeName generateType, StringBuilder urlString) {
        VariableElement mapParameter = getParamMapParameter(parameters);
        return MethodSpec.overriding(executableElement)
                .addStatement("$T bodyBuilder = new $T()", FORM_BODY_BUILDER, FORM_BODY_BUILDER)
                .addStatement("$T<$T<$T,$T>> iterator = " + mapParameter.getSimpleName() + ".entrySet().iterator()", ITERATOR, MAP_ENTRY, STRING, STRING)
                .addCode("for($T<$T,$T> entry : " + mapParameter.getSimpleName() + ".entrySet()) {\n", MAP_ENTRY, STRING, STRING)
                .addStatement("bodyBuilder.add(entry.getKey(), entry.getValue())")
                .addCode("}\n")

                .addStatement("$T formBody = bodyBuilder.build()", FORM_BODY)

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


    private VariableElement getParamMapParameter(List<? extends VariableElement> parameters) {
        VariableElement element = null;
        int bodyCount = 0;
        for (VariableElement parameter : parameters) {
            Path path = parameter.getAnnotation(Path.class);
            ParamMap paramMap = parameter.getAnnotation(ParamMap.class);
            if (path == null && paramMap == null) {
                throw new IllegalStateException("parameter must have @Path or @ParamMap annotation on FormMapWriter");
            }
            TypeName typeName = ClassName.get(parameter.asType());
            if (path != null) {
                CheckUtils.checkIsString(typeName);
            }
            if (paramMap != null) {
                // 参数校验
                CheckUtils.checkParamMap(typeName);
                bodyCount++;
                element = parameter;
            }
        }
        if (bodyCount == 1) {
            return element;
        }
        throw new NullPointerException("do not find legitimate ParamMap annotation with Map<String,String>");
    }
}
