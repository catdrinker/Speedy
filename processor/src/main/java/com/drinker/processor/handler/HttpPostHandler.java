package com.drinker.processor.handler;

import com.drinker.annotation.Param;
import com.drinker.annotation.Post;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

public abstract class HttpPostHandler extends HttpHandler {

    @Override
    protected String getExtraUrl(ExecutableElement executableElement) {
        Post post = executableElement.getAnnotation(Post.class);
        return post.value();
    }

    @Override
    protected void appendUrl(List<? extends VariableElement> parameters, List<Param> formatParams, StringBuilder urlString) {
        if (urlString.toString().contains("?")) {
            throw new IllegalStateException("post method url mus't has ?");
        }
        urlString.append(")\n");
    }

    @Override
    protected String getMethod() {
        return POST;
    }
}