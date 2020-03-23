package com.drinker.processor.handler;

import com.drinker.annotation.Body;
import com.drinker.annotation.Post;
import com.drinker.processor.IHandler;
import com.drinker.processor.writter.BodyWriter;
import com.drinker.processor.writter.MethodWriter;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

public class PostBodyHandler extends HttpPostHandler {

    @Override
    protected MethodWriter getWriter() {
        return new BodyWriter();
    }

    @Override
    protected IHandler getHandler() {
        return new IHandler() {
            @Override
            public boolean handle(ExecutableElement executableElement, List<? extends VariableElement> parameters) {
                Post post = executableElement.getAnnotation(Post.class);
                if (post != null) {
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
