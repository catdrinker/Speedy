package com.drinker.processor.handler;

import com.drinker.annotation.Form;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

public class FormHandler implements IHandler {
    @Override
    public boolean handle(ExecutableElement executableElement, List<? extends VariableElement> parameters) {
        Form form = executableElement.getAnnotation(Form.class);
        return form != null;
    }
}
