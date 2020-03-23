package com.drinker.processor.handler;

import com.drinker.annotation.Delete;
import com.drinker.processor.IHandler;
import com.drinker.processor.writter.BodyWriter;
import com.drinker.processor.writter.MethodWriter;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

public class DeleteHandler extends HttpHandler {
    @Override
    protected MethodWriter getWriter() {
        return new BodyWriter();
    }

    @Override
    protected IHandler getHandler() {
        return new IHandler() {
            @Override
            public boolean handle(ExecutableElement executableElement, List<? extends VariableElement> parameters) {
                return executableElement.getAnnotation(Delete.class) != null;
            }
        };
    }

    @Override
    protected String getMethod() {
        return DELETE;
    }

    @Override
    protected String getExtraUrl(ExecutableElement executableElement) {
        return executableElement.getAnnotation(Delete.class).value();
    }
}
