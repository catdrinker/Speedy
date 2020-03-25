package com.drinker.processor.processor;

import com.drinker.annotation.Put;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

public abstract class HttpPutProcessor extends HttpProcessor<Put> {

    @Override
    protected Put getAnnotations(ExecutableElement executableElement) {
        return executableElement.getAnnotation(Put.class);
    }

    @Override
    protected String getExtraUrl(ExecutableElement executableElement) {
        return getAnnotations(executableElement).value();
    }

    @Override
    protected void appendUrl(List<? extends VariableElement> parameters, StringBuilder urlString) {
        if (urlString.toString().contains("?")) {
            throw new IllegalStateException("put method url mus't has ?");
        }
    }

    @Override
    protected String getMethod() {
        return PUT;
    }

}
