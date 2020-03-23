package com.drinker.processor.method;

import com.drinker.annotation.Form;
import com.drinker.annotation.Param;
import com.drinker.annotation.Post;
import com.drinker.processor.CheckUtils;
import com.drinker.processor.Log;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import static com.drinker.processor.SpeedyClassName.FORM_BODY;
import static com.drinker.processor.SpeedyClassName.OK_HTTP_CALL;
import static com.drinker.processor.SpeedyClassName.REQUEST;
import static com.drinker.processor.SpeedyClassName.REQUEST_BODY_BUILDER;
import static com.drinker.processor.SpeedyClassName.REQ_BODY;
import static com.drinker.processor.SpeedyClassName.SPEEDY_CALL;
import static com.drinker.processor.SpeedyClassName.SPEEDY_WRAPPER_CALL;

public class PostFormMethodHandler extends HttpPostHandler {

    @Override
    protected boolean handle(ExecutableElement executableElement, List<? extends VariableElement> parameters) {
        Post post = executableElement.getAnnotation(Post.class);
        Form form = executableElement.getAnnotation(Form.class);
        return post != null && form != null;
    }

    @Override
    protected MethodSpec process(ExecutableElement executableElement, List<? extends VariableElement> parameters, TypeMirror returnType, TypeName generateType, StringBuilder urlString, List<Param> formatParams) {
        MethodSpec.Builder methodSpecBuilder = MethodSpec.overriding(executableElement)
                .addCode("$T formBody = new $T.Builder()\n", REQ_BODY, FORM_BODY);

        for (VariableElement parameter : parameters) {
            // we just add parameter to formBody that not match with ${xxx} format, if find, just skip it
            Param param = parameter.getAnnotation(Param.class);
            CheckUtils.checkParam(ClassName.get(parameter.asType()));
            if (formatParams.contains(param) || param == null) {
                continue;
            }
            Log.w("params is " + param);
            methodSpecBuilder.addCode(".add($S," + parameter.getSimpleName().toString() + ")\n", param.value());
        }

        methodSpecBuilder.addCode(".build()");

        return methodSpecBuilder.addStatement("")
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

}
