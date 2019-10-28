package com.drinker.processor.method;

import com.squareup.javapoet.MethodSpec;

import java.util.List;

import javax.annotation.processing.Messager;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

public interface IHttpMethodHandler {

    MethodSpec process(ExecutableElement executableElement, List<? extends VariableElement> parameters, Messager messager);
}
