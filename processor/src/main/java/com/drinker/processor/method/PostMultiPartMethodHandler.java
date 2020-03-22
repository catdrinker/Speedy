package com.drinker.processor.method;

import com.drinker.annotation.MultiPart;
import com.drinker.annotation.Param;
import com.drinker.annotation.Post;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

public class PostMultiPartMethodHandler extends HttpPostHandler {

    @Override
    protected boolean handle(ExecutableElement executableElement, List<? extends VariableElement> parameters) {
        MultiPart multiPart = executableElement.getAnnotation(MultiPart.class);
        Post post = executableElement.getAnnotation(Post.class);
        return multiPart != null && post != null;
    }

    @Override
    protected MethodSpec process(ExecutableElement executableElement, List<? extends VariableElement> parameters, TypeMirror returnType, TypeName generateType, StringBuilder urlString, List<Param> formatParams) {

        return null;
    }


}
