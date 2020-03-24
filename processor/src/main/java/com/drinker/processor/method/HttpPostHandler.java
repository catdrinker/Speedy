package com.drinker.processor.method;

import com.drinker.annotation.Post;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

public abstract class HttpPostHandler extends HttpHandler<Post> {

    @Override
    protected Post getAnnotations(ExecutableElement executableElement) {
        return executableElement.getAnnotation(Post.class);
    }

    @Override
    protected String getExtraUrl(ExecutableElement executableElement) {
        Post post = executableElement.getAnnotation(Post.class);
        return post.value();
    }

    @Override
    protected void appendUrl(List<? extends VariableElement> parameters, StringBuilder urlString) {
        if (urlString.toString().contains("?")) {
            throw new IllegalStateException("post method url mus't has ?");
        }
    }

    @Override
    protected String getMethod() {
        return POST;
    }
}
