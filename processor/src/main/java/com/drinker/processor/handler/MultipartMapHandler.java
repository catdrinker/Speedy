package com.drinker.processor.handler;

import com.drinker.annotation.MultiPart;
import com.drinker.annotation.PartMap;
import com.drinker.processor.IHandler;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

public class MultipartMapHandler implements IHandler {

    @Override
    public boolean handle(ExecutableElement executableElement, List<? extends VariableElement> parameters) {
        MultiPart multiPart = executableElement.getAnnotation(MultiPart.class);
        if (multiPart != null) {
            for (VariableElement parameter : parameters) {
                PartMap partMap = parameter.getAnnotation(PartMap.class);
                if (partMap != null) {
                    return true;
                }
            }
        }
        return false;
    }

}
