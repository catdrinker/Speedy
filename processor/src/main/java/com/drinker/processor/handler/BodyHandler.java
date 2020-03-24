package com.drinker.processor.handler;

import com.drinker.annotation.Body;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

import static com.drinker.processor.SpeedyClassName.REQ_BODY;

public class BodyHandler implements IHandler {
    @Override
    public boolean handle(ExecutableElement executableElement, List<? extends VariableElement> parameters) {
        for (VariableElement parameter : parameters) {
            Body body = parameter.getAnnotation(Body.class);
            if (body != null) {
                TypeName typeName = ClassName.get(parameter.asType());
                if (typeName.equals(REQ_BODY)) {
                    return true;
                }
            }
        }
        return false;
    }
}
