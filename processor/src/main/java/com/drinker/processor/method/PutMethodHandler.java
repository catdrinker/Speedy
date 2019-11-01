package com.drinker.processor.method;

import com.drinker.annotation.Body;
import com.drinker.annotation.Put;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;

import java.util.List;

import javax.annotation.processing.Messager;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import okhttp3.Request;

public class PutMethodHandler implements IHttpMethodHandler {
    @Override
    public MethodSpec process(ExecutableElement executableElement, List<? extends VariableElement> parameters, Messager messager) {
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
                    .addCode("$T request = new $T.Builder()\n", Request.class, Request.class)
                    .addCode(".url(baseHttpUrl+$S)\n", putAnnotation.value())
                    .addCode(".put(" + simpleName + ")\n")
                    .addCode(".build();\n")
                    .addCode("")
                    .addStatement("return client.newCall(request)")
                    .returns(ClassName.get("okhttp3", "Call"))
                    .build();
        }
        return null;
    }
}
