package com.drinker.processor.handler;

import com.drinker.annotation.MultiPart;
import com.drinker.annotation.Part;
import com.drinker.annotation.Post;
import com.drinker.processor.IHandler;
import com.drinker.processor.writter.MethodWriter;
import com.drinker.processor.writter.MultipartBodyWriter;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

public class PostMultipartHandler extends HttpPostHandler {
    @Override
    protected MethodWriter getWriter() {
        return new MultipartBodyWriter();
    }

    @Override
    protected IHandler getHandler() {
        return new IHandler() {
            @Override
            public boolean handle(ExecutableElement executableElement, List<? extends VariableElement> parameters) {
                MultiPart multiPart = executableElement.getAnnotation(MultiPart.class);
                Post post = executableElement.getAnnotation(Post.class);
                if (multiPart != null && post != null) {
                    for (VariableElement parameter : parameters) {
                        Part part = parameter.getAnnotation(Part.class);
                        if (part != null) {
                            return true;
                        }
                    }
                }
                return false;
            }
        };
    }
}
