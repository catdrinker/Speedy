package com.drinker.processor.handler;

import com.drinker.annotation.MultiPart;
import com.drinker.annotation.Part;
import com.drinker.processor.IHandler;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

public class MultipartHandler implements IHandler {

    @Override
    public boolean handle(ExecutableElement executableElement, List<? extends VariableElement> parameters) {
        MultiPart multiPart = executableElement.getAnnotation(MultiPart.class);
        if (multiPart != null) {
            for (VariableElement parameter : parameters) {
                Part part = parameter.getAnnotation(Part.class);
                if (part != null) {
                    return true;
                }
            }
        }
        return false;
    }
}
