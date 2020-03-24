package com.drinker.processor.writter;

import com.drinker.annotation.Body;
import com.drinker.processor.CheckUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

public class ConverterBodyWriter extends MethodWriter{

    @Override
    public MethodSpec write(ExecutableElement executableElement, List<? extends VariableElement> parameters, String method, TypeMirror returnType, TypeName generateType, StringBuilder urlString) {
        VariableElement parameter = getBodyParam(parameters);


        return null;
    }

    private VariableElement getBodyParam(List<? extends VariableElement> parameters) {
        for (VariableElement parameter : parameters) {
            Body body = parameter.getAnnotation(Body.class);
            if (body != null) {
                CheckUtils.checkConverterBody(ClassName.get(parameter.asType()));
                return parameter;
            }
        }
        throw new NullPointerException("do not find Body annotation");
    }
}
