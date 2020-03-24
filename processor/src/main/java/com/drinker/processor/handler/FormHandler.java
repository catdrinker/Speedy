package com.drinker.processor.handler;

import com.drinker.annotation.Form;
import com.drinker.annotation.Param;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

public class FormHandler implements IHandler {
    @Override
    public boolean handle(ExecutableElement executableElement, List<? extends VariableElement> parameters) {
        Form form = executableElement.getAnnotation(Form.class);
        if (form != null) {
            for (VariableElement parameter : parameters) {
                Param param = parameter.getAnnotation(Param.class);
                if (param != null) {
                    return true;
                }
            }
        }
        return false;
    }
}
