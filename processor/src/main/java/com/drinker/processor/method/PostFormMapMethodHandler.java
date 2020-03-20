package com.drinker.processor.method;

import com.drinker.annotation.FormMap;
import com.drinker.annotation.Param;
import com.drinker.annotation.ParamMap;
import com.drinker.annotation.Post;
import com.drinker.processor.Log;
import com.drinker.processor.SpeedyClassName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import static com.drinker.processor.SpeedyClassName.FORM_BODY;
import static com.drinker.processor.SpeedyClassName.FORM_BODY_BUILDER;
import static com.drinker.processor.SpeedyClassName.ITERATOR;
import static com.drinker.processor.SpeedyClassName.MAP_ENTRY;
import static com.drinker.processor.SpeedyClassName.OK_HTTP_CALL;
import static com.drinker.processor.SpeedyClassName.REQUEST;
import static com.drinker.processor.SpeedyClassName.REQUEST_BODY_BUILDER;
import static com.drinker.processor.SpeedyClassName.SPEEDY_CALL;
import static com.drinker.processor.SpeedyClassName.SPEEDY_WRAPPER_CALL;
import static com.drinker.processor.SpeedyClassName.STRING;

public class PostFormMapMethodHandler extends HttpPostHandler {

    @Override
    protected boolean handle(ExecutableElement executableElement) {
        Post post = executableElement.getAnnotation(Post.class);
        FormMap formMap = executableElement.getAnnotation(FormMap.class);
        Log.w("post is " + post + formMap);
        return post != null && formMap != null;
    }

    @Override
    protected MethodSpec process(ExecutableElement executableElement, List<? extends VariableElement> parameters, TypeMirror returnType, TypeName generateType, StringBuilder urlString, List<Param> formatParams) {
        VariableElement mapParameter = getParamMapParameter(parameters);
        if (mapParameter != null) {
            return MethodSpec.overriding(executableElement)
                    .addStatement("$T bodyBuilder = new $T()", FORM_BODY_BUILDER, FORM_BODY_BUILDER)
                    .addStatement("$T<$T<$T,$T>> iterator = " + mapParameter.getSimpleName() + ".entrySet().iterator()", ITERATOR, MAP_ENTRY, STRING, STRING)
                    .addCode("while (iterator.hasNext()) {\n")
                    .addStatement("$T<$T,$T> entry = iterator.next()", MAP_ENTRY, STRING, STRING)
                    .addStatement("bodyBuilder.add(entry.getKey(), entry.getValue())")
                    .addCode("}\n")

                    .addStatement("$T formBody = bodyBuilder.build()", FORM_BODY)

                    .addCode("$T request = new $T()\n", REQUEST, REQUEST_BODY_BUILDER)
                    .addCode(urlString.toString())
                    .addCode(".post(formBody)\n")
                    .addCode(".build();\n")
                    .addCode("")
                    .addStatement("$T newCall = client.newCall(request)", OK_HTTP_CALL)
                    .addStatement("$T<$T> wrapperCall = new $T<>(converterFactory.respBodyConverter($T.class), delivery, newCall, client, request)", SPEEDY_CALL, generateType, SPEEDY_WRAPPER_CALL, generateType)
                    .addStatement("return ($T)callAdapter.adapt(wrapperCall)", TypeName.get(returnType))
                    .returns(TypeName.get(returnType))
                    .build();
        }
        return null;
    }

    private VariableElement getParamMapParameter(List<? extends VariableElement> parameters) {
        for (VariableElement parameter : parameters) {
            ParamMap annotation = parameter.getAnnotation(ParamMap.class);
            if (annotation != null) {
                TypeName typeName = ClassName.get(parameter.asType());
                if (typeName instanceof ParameterizedTypeName) {
                    ClassName rawType = ((ParameterizedTypeName) typeName).rawType;
                    if (!rawType.equals(SpeedyClassName.MAP)) {
                        throw new IllegalStateException("@ParamMap annotation must use parameter with java.util.Map");
                    }
                    return parameter;
                }
            }
        }
        return null;
    }
}
