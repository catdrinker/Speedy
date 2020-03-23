package com.drinker.processor.handler;

import com.drinker.annotation.Form;
import com.drinker.annotation.Post;
import com.drinker.processor.IHandler;
import com.drinker.processor.writter.FormBodyWriter;
import com.drinker.processor.writter.MethodWriter;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

public class PostFormHandler extends HttpPostHandler {
    @Override
    protected MethodWriter getWriter() {
        return new FormBodyWriter();
    }

    @Override
    protected IHandler getHandler() {
        return new IHandler() {
            @Override
            public boolean handle(ExecutableElement executableElement, List<? extends VariableElement> parameters) {
                Post post = executableElement.getAnnotation(Post.class);
                Form form = executableElement.getAnnotation(Form.class);
                return post != null && form != null;
            }
        };
    }
}
