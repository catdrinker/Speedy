package com.drinker.processor.handler;

import com.drinker.annotation.Body;
import com.drinker.annotation.Post;
import com.drinker.annotation.Put;
import com.drinker.processor.IHandler;
import com.drinker.processor.writter.BodyWriter;
import com.drinker.processor.writter.MethodWriter;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

public class PutBodyHandler extends HttpPutHandler {
    @Override
    protected MethodWriter getWriter() {
        return new BodyWriter();
    }

    @Override
    protected IHandler getHandler() {
        return new IHandler() {
            @Override
            public boolean handle(ExecutableElement executableElement, List<? extends VariableElement> parameters) {
                Put put = executableElement.getAnnotation(Put.class);
                if (put != null) {
                    for (VariableElement parameter : parameters) {
                        if (parameter.getAnnotation(Body.class) != null) {
                            return true;
                        }
                    }
                }
                return false;
            }
        };
    }
}
