package com.drinker.processor.method;

import com.drinker.annotation.Param;
import com.drinker.annotation.Post;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import java.util.List;

import javax.annotation.processing.Messager;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

import static com.drinker.processor.OkHttpClassName.FORM_BODY;
import static com.drinker.processor.OkHttpClassName.REQUEST;
import static com.drinker.processor.OkHttpClassName.REQUEST_BODY_BUILDER;
import static com.drinker.processor.OkHttpClassName.REQ_BODY;

public class PostMethodHandler implements IHttpMethodHandler {

    @Override
    public MethodSpec process(ExecutableElement executableElement, List<? extends VariableElement> parameters, Messager messager) {
        Post postAnnotation = executableElement.getAnnotation(Post.class);
        if (postAnnotation != null) {
            MethodSpec.Builder methodSpecBuilder = MethodSpec.overriding(executableElement)
                    .addCode("$T formBody = new $T.Builder()\n", REQ_BODY, FORM_BODY);

            for (VariableElement parameter : parameters) {
                Param param = parameter.getAnnotation(Param.class);
                methodSpecBuilder.addCode(".add($S, $S)\n", param.value(), parameter.getSimpleName().toString());
                messager.printMessage(Diagnostic.Kind.WARNING, "params is " + param);
            }
            methodSpecBuilder.addCode(".build()");

            TypeMirror returnType = executableElement.getReturnType();
            return methodSpecBuilder.addStatement("")
                    .addCode("$T request = new $T()\n", REQUEST, REQUEST_BODY_BUILDER)
                    .addCode(".url(baseHttpUrl+$S)\n", postAnnotation.value())
                    .addCode(".post(formBody)\n")
                    .addCode(".build();\n")
                    .addCode("")
                    .addStatement("okhttp3.Call newCall = client.newCall(request)")
                    .addStatement("return new WrapperCall<>(respConverter, delivery, newCall, client, request)")
                    .returns(TypeName.get(returnType))
                    .build();
        }
        return null;
    }
}
