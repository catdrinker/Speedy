package com.drinker.processor.handler;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

public interface IHandler {

    boolean handle(ExecutableElement executableElement, List<? extends VariableElement> parameters);
}
