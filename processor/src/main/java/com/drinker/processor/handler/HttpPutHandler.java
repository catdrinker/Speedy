package com.drinker.processor.handler;

import com.drinker.annotation.Param;
import com.drinker.annotation.Put;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

public abstract class HttpPutHandler extends HttpHandler {

    @Override
    protected String getExtraUrl(ExecutableElement executableElement) {
        Put put = executableElement.getAnnotation(Put.class);
        return put.value();
    }

    @Override
    protected void appendUrl(List<? extends VariableElement> parameters, List<Param> formatParams, StringBuilder urlString) {
        if (urlString.toString().contains("?")) {
            throw new IllegalStateException("put method url mus't has ?");
        }
        urlString.append(")\n");
    }

    @Override
    protected String getMethod() {
        return PUT;
    }

}
