package com.drinker.processor.handler;

import com.drinker.annotation.FormMap;
import com.drinker.annotation.ParamMap;
import com.drinker.processor.IHandler;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

public class FormMapHandler implements IHandler {
    @Override
    public boolean handle(ExecutableElement executableElement, List<? extends VariableElement> parameters) {
        FormMap formMap = executableElement.getAnnotation(FormMap.class);
        if (formMap != null) {
            for (VariableElement parameter : parameters) {
                ParamMap paramMap = parameter.getAnnotation(ParamMap.class);
                if (paramMap != null) {
                    return true;
                }
            }
        }
        return false;
    }
}
