package com.drinker.processor.writter;

import com.drinker.annotation.MultiPart;
import com.drinker.annotation.PartMap;
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
import static com.drinker.processor.SpeedyClassName.MEDIA_TYPE;
import static com.drinker.processor.SpeedyClassName.MULTIPART_BODY;
import static com.drinker.processor.SpeedyClassName.MULTIPART_BODY_BUILDER;
import static com.drinker.processor.SpeedyClassName.MULTIPART_PART;
import static com.drinker.processor.SpeedyClassName.OK_HTTP_CALL;
import static com.drinker.processor.SpeedyClassName.REQUEST;
import static com.drinker.processor.SpeedyClassName.REQUEST_BODY_BUILDER;
import static com.drinker.processor.SpeedyClassName.SPEEDY_CALL;
import static com.drinker.processor.SpeedyClassName.SPEEDY_TYPE_TOKEN;
import static com.drinker.processor.SpeedyClassName.SPEEDY_WRAPPER_CALL;

public final class MultipartMapWriter extends MethodWriter {
    @Override
    public MethodSpec write(ExecutableElement executableElement, List<? extends VariableElement> parameters, String method, TypeMirror returnType, TypeName generateType, StringBuilder urlString) {
        VariableElement partMap = getPartMap(parameters);
        MultiPart multiPart = executableElement.getAnnotation(MultiPart.class);
        if (multiPart == null) {
            throw new NullPointerException("MultipartMapWriter annotation with MultiPart must not be null");
        }

        return MethodSpec.overriding(executableElement)
                .addCode("$T builder = new $T()\n", MULTIPART_BODY_BUILDER, MULTIPART_BODY_BUILDER)
                .addStatement(".setType($T.get($S))", MEDIA_TYPE, multiPart.value())
                .addCode("for ($T part : " + partMap.getSimpleName() + ") {", MULTIPART_PART)
                .addStatement("builder.addPart(part)")
                .addCode("}")
                .addStatement("$T multipartBody = builder.build()", MULTIPART_BODY)

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

    private VariableElement getPartMap(List<? extends VariableElement> parameters) {
        VariableElement element = null;
        int mapCount = 0;
        for (VariableElement parameter : parameters) {
            Path path = parameter.getAnnotation(Path.class);
            PartMap partMap = parameter.getAnnotation(PartMap.class);
            if (path == null && partMap == null) {
                throw new IllegalStateException("parameter must have @Path or @PartMap annotation on MultipartMapWriter");
            }
            TypeName typeName = ClassName.get(parameter.asType());
            if (path != null) {
                CheckUtils.checkIsString(typeName);
            }
            if (partMap != null) {
                // 参数校验
                CheckUtils.checkMultipartMap(typeName);
                element = parameter;
                mapCount++;
            }
        }
        if (mapCount == 1) {
            return element;
        }
        throw new NullPointerException("do not find legitimate PartMap annotation with List<okhttp3.MultipartBody.Part>");
    }
}
