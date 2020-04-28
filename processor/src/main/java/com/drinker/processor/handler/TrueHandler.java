package com.drinker.processor.handler;

import com.drinker.annotation.Param;
import com.drinker.annotation.Path;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

public class TrueHandler implements IHandler {
    @Override
    public boolean handle(ExecutableElement executableElement, List<? extends VariableElement> parameters) {
        for (VariableElement parameter : parameters) {
            Path path = parameter.getAnnotation(Path.class);
            Param param = parameter.getAnnotation(Param.class);
            if (path == null && param == null) {
                return false;
            }
        }
        return true;
    }
}
