package com.drinker.processor.processor;

import com.drinker.annotation.Delete;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

public abstract class DeleteProcessor extends HttpProcessor<Delete> {

    @Override
    protected Delete getAnnotations(ExecutableElement executableElement) {
        return executableElement.getAnnotation(Delete.class);
    }

    @Override
    protected String getMethod() {
        return DELETE;
    }

    @Override
    protected String getExtraUrl(ExecutableElement executableElement) {
        return executableElement.getAnnotation(Delete.class).value();
    }

    @Override
    protected void appendUrl(List<? extends VariableElement> parameters, StringBuilder urlString) {
        if (urlString.toString().contains("?")) {
            throw new IllegalStateException("post method url mus't has ?");
        }
    }
}
