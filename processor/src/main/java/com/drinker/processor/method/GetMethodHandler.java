package com.drinker.processor.method;

import com.drinker.annotation.Get;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import java.util.List;

import javax.annotation.processing.Messager;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

import static com.drinker.processor.OkHttpClassName.CALL_ADAPTER;
import static com.drinker.processor.OkHttpClassName.OK_HTTP_CALL;
import static com.drinker.processor.OkHttpClassName.REQUEST;
import static com.drinker.processor.OkHttpClassName.REQUEST_BODY_BUILDER;
import static com.drinker.processor.OkHttpClassName.SPEEDY_CALL;
import static com.drinker.processor.OkHttpClassName.SPEEDY_WRAPPER_CALL;


public class GetMethodHandler implements IHttpMethodHandler {


    @Override
    public MethodSpec process(ExecutableElement executableElement, List<? extends VariableElement> parameters, Messager messager) {
        Get getAnnotation = executableElement.getAnnotation(Get.class);
        if (getAnnotation != null) {
            TypeMirror returnType = executableElement.getReturnType();

            messager.printMessage(Diagnostic.Kind.WARNING, "return type " + returnType + " " + returnType.getKind() + " " + returnType.toString() + " " + returnType.getKind().name());
            return MethodSpec.overriding(executableElement)
                    .addCode("$T request = new $T()\n", REQUEST, REQUEST_BODY_BUILDER)
                    .addCode(".get()\n")
                    .addCode(".url(baseHttpUrl+$S)\n", getAnnotation.value())
                    .addStatement(".build()")
                    .addStatement("$T newCall = client.newCall(request)", OK_HTTP_CALL)
                    .addStatement("$T wrapperCall = new $T<>(respConverter, newCall, client, request)", SPEEDY_CALL, SPEEDY_WRAPPER_CALL)
                    .addStatement("return ($T)callAdapter.adapt(wrapperCall)", TypeName.get(returnType))

                    .returns(TypeName.get(returnType))
                    .build();
        }
        return null;
    }
}
