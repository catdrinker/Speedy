package com.drinker.processor.writter;

import com.drinker.annotation.MultiPart;
import com.drinker.annotation.Part;
import com.drinker.annotation.Path;
import com.drinker.processor.CheckUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import static com.drinker.processor.SpeedyClassName.CALL_ADAPTER;
import static com.drinker.processor.SpeedyClassName.MEDIA_TYPE;
import static com.drinker.processor.SpeedyClassName.MULTIPART_BODY;
import static com.drinker.processor.SpeedyClassName.MULTIPART_BODY_BUILDER;
import static com.drinker.processor.SpeedyClassName.OK_HTTP_CALL;
import static com.drinker.processor.SpeedyClassName.REQUEST;
import static com.drinker.processor.SpeedyClassName.REQUEST_BODY_BUILDER;
import static com.drinker.processor.SpeedyClassName.SPEEDY_CALL;
import static com.drinker.processor.SpeedyClassName.SPEEDY_TYPE_TOKEN;
import static com.drinker.processor.SpeedyClassName.SPEEDY_WRAPPER_CALL;

public final class MultipartBodyWriter extends MethodWriter {

    @Override
    public MethodSpec write(ExecutableElement executableElement, List<? extends VariableElement> parameters, String method, TypeMirror returnType, TypeName generateType, StringBuilder urlString) {
        MultiPart multiPart = executableElement.getAnnotation(MultiPart.class);
        if (multiPart == null) {
            throw new NullPointerException("MultipartBodyWriter annotation with MultiPart must not be null");
        }
        MethodSpec.Builder methodSpec = MethodSpec.overriding(executableElement)
                .addCode("$T multipartBody = new $T()\n", MULTIPART_BODY, MULTIPART_BODY_BUILDER)
                .addCode(".setType($T.get($S))\n", MEDIA_TYPE, multiPart.value());

        List<VariableElement> parts = getParts(parameters);
        for (VariableElement part : parts) {
            methodSpec.addCode(".addPart(" + part.getSimpleName() + ")\n");
        }

        return methodSpec
                .addStatement(".build()")
                .addCode("$T request = new $T()\n", REQUEST, REQUEST_BODY_BUILDER)
                .addCode(".url(" + urlString.toString() + ")\n")
                .addCode("." + method + "(multipartBody)\n")
                .addCode(".build();\n")
                .addCode("")
                .addStatement("$T newCall = client.newCall(request)", OK_HTTP_CALL)
                .addStatement("$T<$T> wrapperCall = new $T<>(converterFactory.respBodyConverter(new $T<$T>(){}), delivery, newCall, client, request)", SPEEDY_CALL, generateType, SPEEDY_WRAPPER_CALL, SPEEDY_TYPE_TOKEN, generateType)
                .addStatement("$T<$T,$T> callAdapter = callAdapterFactory.adapter()", CALL_ADAPTER, generateType, TypeName.get(returnType))
                .addStatement("return callAdapter.adapt(wrapperCall)")
                .returns(TypeName.get(returnType))
                .build();
    }

    private List<VariableElement> getParts(List<? extends VariableElement> parameters) {
        List<VariableElement> list = new ArrayList<>();
        for (VariableElement parameter : parameters) {
            Path path = parameter.getAnnotation(Path.class);
            Part part = parameter.getAnnotation(Part.class);
            if (path == null && part == null) {
                throw new IllegalStateException("parameter must have @Path or @Part annotation on MultipartBodyWriter");
            }
            TypeName typeName = ClassName.get(parameter.asType());
            if (path != null) {
                CheckUtils.checkIsString(typeName);
            }
            if (part != null) {
                CheckUtils.checkMultipart(typeName);
                list.add(parameter);
            }
        }
        return list;
    }
}
