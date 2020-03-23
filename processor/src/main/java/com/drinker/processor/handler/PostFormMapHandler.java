package com.drinker.processor.handler;

import com.drinker.annotation.FormMap;
import com.drinker.annotation.ParamMap;
import com.drinker.annotation.Post;
import com.drinker.processor.IHandler;
import com.drinker.processor.writter.FormMapWriter;
import com.drinker.processor.writter.MethodWriter;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

public class PostFormMapHandler extends HttpPostHandler {

    @Override
    protected MethodWriter getWriter() {
        return new FormMapWriter();
    }

    @Override
    protected IHandler getHandler() {
        return new IHandler() {
            @Override
            public boolean handle(ExecutableElement executableElement, List<? extends VariableElement> parameters) {
                Post post = executableElement.getAnnotation(Post.class);
                FormMap formMap = executableElement.getAnnotation(FormMap.class);
                if (post != null && formMap != null) {
                    for (VariableElement parameter : parameters) {
                        ParamMap paramMap = parameter.getAnnotation(ParamMap.class);
                        if (paramMap != null) {
                            return true;
                        }
                    }
                }
                return false;
            }
        };
    }
}
