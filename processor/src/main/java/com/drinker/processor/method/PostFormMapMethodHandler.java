package com.drinker.processor.method;

import com.drinker.annotation.FormMap;
import com.drinker.annotation.Param;
import com.drinker.annotation.Post;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

public class PostFormMapMethodHandler extends HttpPostHandler {

    @Override
    protected boolean handle(ExecutableElement executableElement) {
        Post post = executableElement.getAnnotation(Post.class);
        FormMap formMap = executableElement.getAnnotation(FormMap.class);
        return post != null && formMap != null;
    }

    @Override
    protected MethodSpec process(ExecutableElement executableElement, List<? extends VariableElement> parameters, TypeMirror returnType, TypeName generateType, StringBuilder urlString, List<Param> formatParams) {


        return null;
    }
}
