package com.drinker.processor.method;

import com.drinker.annotation.MultiPart;
import com.drinker.annotation.Post;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

public class PostMultiPartMethodHandler extends HttpMethodHandler {

    @Override
    protected String getExtraUrl(ExecutableElement executableElement) {
        MultiPart multiPartAnnotation = executableElement.getAnnotation(MultiPart.class);
        Post postAnnotation = executableElement.getAnnotation(Post.class);
        if (multiPartAnnotation != null && postAnnotation != null) {
            return postAnnotation.value();
        }
        return null;
    }

    @Override
    protected MethodSpec process(ExecutableElement executableElement, List<? extends VariableElement> parameters, TypeMirror returnType, TypeName generateType, StringBuilder urlString, List<String> realParameters) {
        // log in process=


        // log in with hei ke

        return null;
    }


}
