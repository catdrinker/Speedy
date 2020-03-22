package com.drinker.processor.method;

import com.drinker.annotation.MultiPart;
import com.drinker.annotation.Param;
import com.drinker.annotation.Part;
import com.drinker.annotation.Post;
import com.drinker.processor.Log;
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

public class PostMultiPartMethodHandler extends HttpPostHandler {

    @Override
    protected boolean handle(ExecutableElement executableElement, List<? extends VariableElement> parameters) {
        MultiPart multiPart = executableElement.getAnnotation(MultiPart.class);
        Post post = executableElement.getAnnotation(Post.class);
        return multiPart != null && post != null;
    }

    @Override
    protected MethodSpec process(ExecutableElement executableElement, List<? extends VariableElement> parameters, TypeMirror returnType, TypeName generateType, StringBuilder urlString, List<Param> formatParams) {
        MultiPart multiPart = executableElement.getAnnotation(MultiPart.class);

        MethodSpec.Builder methodSpec = MethodSpec.overriding(executableElement)
                .addCode("$T multipartBody = new $T()\n", MULTIPART_BODY, MULTIPART_BODY_BUILDER)
                .addCode(".setType($T.get($S))\n", MEDIA_TYPE, multiPart.value());

        for (VariableElement parameter : parameters) {
            Part part = parameter.getAnnotation(Part.class);
            if (part != null) {
                TypeName typeName = ClassName.get(parameter.asType());
                if (typeName instanceof ClassName) {
                    Log.w("multipart type is " + typeName.getClass());
                    if (!MULTIPART_PART.equals(typeName)) {
                        throw new IllegalStateException("@ParamMap annotation must use parameter with okhttp3.RequestBody");
                    }
                    methodSpec.addCode(".addPart(" + parameter.getSimpleName() + ")\n");
                }
            }
        }

        return methodSpec
                .addStatement(".build()")
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


}
