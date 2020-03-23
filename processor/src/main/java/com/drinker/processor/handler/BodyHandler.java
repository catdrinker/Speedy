package com.drinker.processor.handler;

import com.drinker.annotation.Body;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

public class BodyHandler implements IHandler {
    @Override
    public boolean handle(ExecutableElement executableElement, List<? extends VariableElement> parameters) {
        for (VariableElement parameter : parameters) {
            if (parameter.getAnnotation(Body.class) != null) {
                return true;
            }
        }
        return false;
    }
}
