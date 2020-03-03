package com.drinker.processor.method;

import com.drinker.annotation.Body;
import com.drinker.annotation.Put;
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

public class PutMethodHandler implements IHttpMethodHandler {
    @Override
    public MethodSpec process(ExecutableElement executableElement, List<? extends VariableElement> parameters, TypeMirror returnType, TypeName generateType) {
        Put putAnnotation = executableElement.getAnnotation(Put.class);
        if (putAnnotation != null) {
            if (parameters.size() > 1) {
                throw new IllegalArgumentException("put can't have more than 1 param");
            }
            VariableElement variableElement = parameters.get(0);
            Body body = variableElement.getAnnotation(Body.class);
            if (body == null) {
                throw new IllegalArgumentException("put method param must annotation by @Body");
            }
            TypeMirror typeMirror = variableElement.asType();
            String simpleName = variableElement.getSimpleName().toString();
            if (!"okhttp3.RequestBody".equals(typeMirror.toString())) {
                throw new IllegalArgumentException("put param must be okhttp3.RequestBody");
            }
            return MethodSpec.overriding(executableElement)
                    .addCode("$T request = new $T()\n", REQUEST, REQUEST_BODY_BUILDER)
                    .addCode(".url(baseHttpUrl+$S)\n", putAnnotation.value())
                    .addCode(".put(" + simpleName + ")\n")
                    .addCode(".build();\n")
                    .addCode("")
                    .addStatement("$T newCall = client.newCall(request)", OK_HTTP_CALL)
                    .addStatement("$T<$T> wrapperCall = new $T<>(converterFactory.respBodyConverter($T.class), newCall, client, request)", SPEEDY_CALL, generateType, SPEEDY_WRAPPER_CALL, generateType)
                    .addStatement("return ($T)callAdapter.adapt(wrapperCall)", TypeName.get(returnType))
                    .returns(TypeName.get(returnType))
                    .build();
        }
        return null;
    }
}
