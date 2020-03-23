package com.drinker.processor.method;

import com.drinker.annotation.MultiPart;
import com.drinker.annotation.Param;
import com.drinker.annotation.PartMap;
import com.drinker.annotation.Post;
import com.drinker.processor.CheckUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import static com.drinker.processor.SpeedyClassName.MEDIA_TYPE;
import static com.drinker.processor.SpeedyClassName.MULTIPART_BODY;
import static com.drinker.processor.SpeedyClassName.MULTIPART_BODY_BUILDER;
import static com.drinker.processor.SpeedyClassName.MULTIPART_PART;
import static com.drinker.processor.SpeedyClassName.OK_HTTP_CALL;
import static com.drinker.processor.SpeedyClassName.REQUEST;
import static com.drinker.processor.SpeedyClassName.REQUEST_BODY_BUILDER;
import static com.drinker.processor.SpeedyClassName.SPEEDY_CALL;
import static com.drinker.processor.SpeedyClassName.SPEEDY_WRAPPER_CALL;

public class PostMultiPartMapMethodHandler extends HttpPostHandler {
    @Override
    protected boolean handle(ExecutableElement executableElement, List<? extends VariableElement> parameters) {
        Post post = executableElement.getAnnotation(Post.class);
        MultiPart multiPart = executableElement.getAnnotation(MultiPart.class);
        if (post != null && multiPart != null) {
            for (VariableElement parameter : parameters) {
                PartMap partMap = parameter.getAnnotation(PartMap.class);
                if (partMap != null) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected MethodSpec process(ExecutableElement executableElement, List<? extends VariableElement> parameters, TypeMirror returnType, TypeName generateType, StringBuilder urlString, List<Param> formatParams) {
        VariableElement partMap = getPartMap(parameters);
        MultiPart multiPart = executableElement.getAnnotation(MultiPart.class);

        return MethodSpec.overriding(executableElement)
                .addCode("$T builder = new $T()\n", MULTIPART_BODY_BUILDER, MULTIPART_BODY_BUILDER)
                .addStatement(".setType($T.get($S))", MEDIA_TYPE, multiPart.value())
                .addCode("for ($T part : " + partMap.getSimpleName() + ") {", MULTIPART_PART)
                .addStatement("builder.addPart(part)")
                .addCode("}")
                .addStatement("$T multipartBody = builder.build()", MULTIPART_BODY)

                .addCode("$T request = new $T()\n", REQUEST, REQUEST_BODY_BUILDER)
                .addCode(urlString.toString())
                .addCode(".post(multipartBody)\n")
                .addCode(".build();\n")
                .addCode("")
                .addStatement("$T newCall = client.newCall(request)", OK_HTTP_CALL)
                .addStatement("$T<$T> wrapperCall = new $T<>(converterFactory.respBodyConverter($T.class), delivery, newCall, client, request)", SPEEDY_CALL, generateType, SPEEDY_WRAPPER_CALL, generateType)
                .addStatement("return ($T)callAdapter.adapt(wrapperCall)", TypeName.get(returnType))
                .returns(TypeName.get(returnType))
                .build();
    }

    private VariableElement getPartMap(List<? extends VariableElement> parameters) {
        for (VariableElement parameter : parameters) {
            PartMap partMap = parameter.getAnnotation(PartMap.class);
            if (partMap != null) {
                // 参数校验
                CheckUtils.checkMultipartMap(ClassName.get(parameter.asType()));
                return parameter;
            }
        }
        throw new NullPointerException("PostMultiPartMapMethodHandler handle partMap parameter mus't be null");
    }

}
