package com.drinker.processor.handler;

import com.drinker.annotation.Body;
import com.drinker.processor.CheckUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

public class BodyConverterHandler implements IHandler {
    @Override
    public boolean handle(ExecutableElement executableElement, List<? extends VariableElement> parameters) {
        for (VariableElement parameter : parameters) {
            Body body = parameter.getAnnotation(Body.class);
            if (body != null) {
                TypeName typeName = ClassName.get(parameter.asType());
                CheckUtils.checkConverterBody(typeName);
                return true;
            }
        }
        return false;
    }
}
