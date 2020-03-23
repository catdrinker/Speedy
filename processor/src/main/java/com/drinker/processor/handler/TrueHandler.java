package com.drinker.processor.handler;

import com.drinker.processor.IHandler;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

public class TrueHandler implements IHandler {
    @Override
    public boolean handle(ExecutableElement executableElement, List<? extends VariableElement> parameters) {
        return true;
    }
}
