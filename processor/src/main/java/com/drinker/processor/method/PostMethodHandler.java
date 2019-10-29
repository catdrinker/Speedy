package com.drinker.processor.method;

import com.drinker.annotation.Param;
import com.drinker.annotation.Post;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;

import java.util.List;

import javax.annotation.processing.Messager;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class PostMethodHandler implements IHttpMethodHandler {

    @Override
    public MethodSpec process(ExecutableElement executableElement, List<? extends VariableElement> parameters, Messager messager) {
        Post postAnnotation = executableElement.getAnnotation(Post.class);
        if (postAnnotation != null) {
            MethodSpec.Builder methodSpecBuilder = MethodSpec.overriding(executableElement)
                    .addCode("$T formBody = new $T.Builder()\n", RequestBody.class, FormBody.class);

            for (VariableElement parameter : parameters) {
                Param param = parameter.getAnnotation(Param.class);
                methodSpecBuilder.addCode(".add($S, $S)\n", param.value(), parameter.getSimpleName().toString());
                messager.printMessage(Diagnostic.Kind.WARNING, "params is " + param);
            }
            methodSpecBuilder.addCode(".build()");

            return methodSpecBuilder.addStatement("")
                    .addCode("$T request = new $T.Builder()\n", Request.class, Request.class)
                    .addCode(".url(baseHttpUrl+$S)\n", postAnnotation.value())
                    .addCode(".post(formBody)\n")
                    .addCode(".build();\n")
                    .addCode("")
                    .addStatement("return client.newCall(request)")
                    .returns(ClassName.get("okhttp3", "Call"))
                    .build();
        }
        return null;
    }
}
