package com.drinker.processor.handler;

import com.drinker.annotation.MultiPart;
import com.drinker.annotation.PartMap;
import com.drinker.annotation.Post;
import com.drinker.processor.IHandler;
import com.drinker.processor.writter.MethodWriter;
import com.drinker.processor.writter.MultipartMapWriter;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

public class PostMultipartMapHandler extends HttpPostHandler {
    @Override
    protected MethodWriter getWriter() {
        return new MultipartMapWriter();
    }

    @Override
    protected IHandler getHandler() {
        return new IHandler() {
            @Override
            public boolean handle(ExecutableElement executableElement, List<? extends VariableElement> parameters) {
                Post post = executableElement.getAnnotation(Post.class);
                MultiPart multiPart = executableElement.getAnnotation(MultiPart.class);
                if (post != null && multiPart != null) {
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
