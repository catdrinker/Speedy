package com.drinker.processor.handler;

import com.drinker.annotation.ParamMap;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

public class ParamMapHandler implements IHandler {
    @Override
    public boolean handle(ExecutableElement executableElement, List<? extends VariableElement> parameters) {
        for (VariableElement parameter : parameters) {
            ParamMap paramMap = parameter.getAnnotation(ParamMap.class);
            if (paramMap != null) {
                return true;
            }
        }
        return false;
    }
}
