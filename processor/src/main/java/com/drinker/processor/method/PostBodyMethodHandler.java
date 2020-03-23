package com.drinker.processor.method;

import com.drinker.annotation.Body;
import com.drinker.annotation.Param;
import com.drinker.annotation.Post;
import com.drinker.processor.Log;
import com.drinker.processor.SpeedyClassName;
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

public class PostBodyMethodHandler extends HttpPostHandler {

    @Override
    protected boolean handle(ExecutableElement executableElement, List<? extends VariableElement> parameters) {
        Post post = executableElement.getAnnotation(Post.class);
        if (post != null) {
            for (VariableElement parameter : parameters) {
                if (parameter.getAnnotation(Body.class) != null) {
                    Log.w("handler it");
                    return true;
                }
            }
        }
        Log.w("not handler");
        return false;
    }

    @Override
    protected MethodSpec process(ExecutableElement executableElement, List<? extends VariableElement> parameters, TypeMirror returnType, TypeName generateType, StringBuilder urlString, List<Param> formatParams) {
        VariableElement bodyParam = getBodyParam(parameters);
        return MethodSpec.overriding(executableElement)
                .addCode("$T request = new $T()\n", REQUEST, REQUEST_BODY_BUILDER)
                .addCode(urlString.toString())
                .addCode(".post(" + bodyParam.getSimpleName() + ")\n")
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
                TypeName typeName = ClassName.get(parameter.asType());
                if (typeName instanceof ClassName) {
                    if (!typeName.equals(SpeedyClassName.REQ_BODY)) {
                        throw new IllegalStateException("@ParamMap annotation must use parameter with okhttp3.RequestBody");
                    }
                    return parameter;
                }
            }
        }
        throw new NullPointerException("do not find Body annotation");
    }
}
