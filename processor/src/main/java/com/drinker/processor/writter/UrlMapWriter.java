package com.drinker.processor.writter;

import com.drinker.annotation.ParamMap;
import com.drinker.annotation.Path;
import com.drinker.processor.CheckUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import java.util.List;

import javax.annotation.Nonnull;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import static com.drinker.processor.SpeedyClassName.CALL_ADAPTER;
import static com.drinker.processor.SpeedyClassName.ILLEGAL_STATE_EXCEPTION;
import static com.drinker.processor.SpeedyClassName.MAP_ENTRY;
import static com.drinker.processor.SpeedyClassName.OK_HTTP_CALL;
import static com.drinker.processor.SpeedyClassName.REQUEST;
import static com.drinker.processor.SpeedyClassName.REQUEST_BODY_BUILDER;
import static com.drinker.processor.SpeedyClassName.SPEEDY_CALL;
import static com.drinker.processor.SpeedyClassName.SPEEDY_TEXT_UTIL;
import static com.drinker.processor.SpeedyClassName.SPEEDY_TYPE_TOKEN;
import static com.drinker.processor.SpeedyClassName.SPEEDY_WRAPPER_CALL;
import static com.drinker.processor.SpeedyClassName.STRING;
import static com.drinker.processor.SpeedyClassName.STRING_BUILDER;

public final class UrlMapWriter extends MethodWriter {

    @Override
    public @Nonnull
    MethodSpec write(ExecutableElement executableElement, List<? extends VariableElement> parameters, String method, TypeMirror returnType, TypeName generateType, StringBuilder urlString) {
        String s = urlString.toString();
        VariableElement parameter = getParamMapParameter(parameters);
        MethodSpec.Builder builder = MethodSpec.overriding(executableElement)

                .addCode("if(" + parameter.getSimpleName() + ".isEmpty()) {\n")
                .addStatement("throw new $T($S)", ILLEGAL_STATE_EXCEPTION, "param map should not be empty")
                .addCode("}\n\n")
                .addStatement("$T sb = new $T(" + s + ")", STRING_BUILDER, STRING_BUILDER)
                .addCode("if(!$T.contains(sb, \"?\")) {\n", SPEEDY_TEXT_UTIL)
                .addStatement("sb.append(\"?\")")
                .addCode("}\n\n")

                .addStatement("$T paramBuilder = new $T()", STRING_BUILDER, STRING_BUILDER)
                .addCode("for($T<$T,$T> entry : " + parameter.getSimpleName() + ".entrySet()) {\n", MAP_ENTRY, STRING, STRING)
                .addCode("if($T.isNotEmpty(paramBuilder)) {\n", SPEEDY_TEXT_UTIL)
                .addStatement("paramBuilder.append(\"&\")")
                .addCode("}\n")
                .addCode("paramBuilder.append(entry.getKey())")
                .addCode(".append(\"=\")")
                .addStatement(".append(entry.getValue())")
                .addCode("}\n")
                .addStatement("sb.append(paramBuilder)")

                .addCode("$T request = new $T()\n", REQUEST, REQUEST_BODY_BUILDER)
                .addCode("." + method + "()\n")
                .addCode(".url(sb.toString())")
                .addStatement(".build()")
                .addStatement("$T newCall = client.newCall(request)", OK_HTTP_CALL)
                .addStatement("$T<$T> wrapperCall = new $T<>(converterFactory.respBodyConverter(new $T<$T>(){}), delivery, newCall, client, request)", SPEEDY_CALL, generateType, SPEEDY_WRAPPER_CALL, SPEEDY_TYPE_TOKEN, generateType)
                .addStatement("$T<$T,$T> callAdapter = callAdapterFactory.adapter()", CALL_ADAPTER, generateType, TypeName.get(returnType))
                .addStatement("return callAdapter.adapt(wrapperCall)")
                .returns(TypeName.get(returnType));
        return builder.build();
    }

    private VariableElement getParamMapParameter(List<? extends VariableElement> parameters) {
        VariableElement element = null;
        int mapCount = 0;
        for (VariableElement parameter : parameters) {
            Path path = parameter.getAnnotation(Path.class);
            ParamMap paramMap = parameter.getAnnotation(ParamMap.class);
            if (path == null && paramMap == null) {
                throw new IllegalStateException("parameter must have @Path or @ParamMap annotation on UrlMapWriter");
            }
            TypeName typeName = ClassName.get(parameter.asType());
            if (path != null) {
                CheckUtils.checkIsString(typeName);
            }
            if (paramMap != null) {
                CheckUtils.checkParamMap(typeName);
                element = parameter;
                mapCount++;
            }
        }
        if (mapCount == 1) {
            return element;
        }
        throw new NullPointerException("do not find legitimate PartMap annotation with Map<String,String>");
    }
}
