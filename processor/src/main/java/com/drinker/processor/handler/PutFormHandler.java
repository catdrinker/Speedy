package com.drinker.processor.handler;

import com.drinker.annotation.Form;
import com.drinker.annotation.Put;
import com.drinker.processor.IHandler;
import com.drinker.processor.writter.FormBodyWriter;
import com.drinker.processor.writter.MethodWriter;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

public class PutFormHandler extends HttpPutHandler {
    @Override
    protected MethodWriter getWriter() {
        return new FormBodyWriter();
    }

    @Override
    protected IHandler getHandler() {
        return new IHandler() {
            @Override
            public boolean handle(ExecutableElement executableElement, List<? extends VariableElement> parameters) {
                Put put = executableElement.getAnnotation(Put.class);
                Form form = executableElement.getAnnotation(Form.class);
                return put != null && form != null;
            }
        };
    }
}
