package com.drinker.processor.handler;

import com.drinker.annotation.MultiPart;
import com.drinker.annotation.PartMap;
import com.drinker.annotation.Post;
import com.drinker.annotation.Put;
import com.drinker.processor.IHandler;
import com.drinker.processor.writter.MethodWriter;
import com.drinker.processor.writter.MultipartMapWriter;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

public class PutMultipartMapHandler extends HttpPutHandler {
    @Override
    protected MethodWriter getWriter() {
        return new MultipartMapWriter();
    }

    @Override
    protected IHandler getHandler() {
        return new IHandler() {
            @Override
            public boolean handle(ExecutableElement executableElement, List<? extends VariableElement> parameters) {
                Put put = executableElement.getAnnotation(Put.class);
                MultiPart multiPart = executableElement.getAnnotation(MultiPart.class);
                if (put != null && multiPart != null) {
                    for (VariableElement parameter : parameters) {
                        PartMap partMap = parameter.getAnnotation(PartMap.class);
                        if (partMap != null) {
                            return true;
                        }
                    }
                }
                return false;
            }
        };
    }
}
