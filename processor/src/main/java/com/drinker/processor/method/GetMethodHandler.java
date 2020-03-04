package com.drinker.processor.method;

import com.drinker.annotation.Get;
import com.drinker.annotation.Param;
import com.drinker.processor.Log;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import static com.drinker.processor.SpeedyClassName.OK_HTTP_CALL;
import static com.drinker.processor.SpeedyClassName.REQUEST;
import static com.drinker.processor.SpeedyClassName.REQUEST_BODY_BUILDER;
import static com.drinker.processor.SpeedyClassName.SPEEDY_CALL;
import static com.drinker.processor.SpeedyClassName.SPEEDY_WRAPPER_CALL;


public class GetMethodHandler extends HttpMethodHandler {

    @Override
    protected String getExtraUrl(ExecutableElement executableElement) {
        Get getAnnotation = executableElement.getAnnotation(Get.class);
        if (getAnnotation != null) {
            return getAnnotation.value();
        }
        return null;
    }

    @Override
    protected MethodSpec process(ExecutableElement executableElement, List<? extends VariableElement> parameters, TypeMirror returnType, TypeName generateType, StringBuilder urlString, List<String> realParameters) {
        // 添加get后面拼接的参数
        for (VariableElement parameter : parameters) {
            boolean hasParam = false;
            for (String realParameter : realParameters) {
                if (realParameter.equals(parameter.getSimpleName().toString())) {
                    Log.i("has real param " + realParameter);
                    hasParam = true;
                    break;
                }
            }
            Log.w("after loop " + parameter.getSimpleName() + hasParam);
            if (hasParam) {
                continue;
            }
            Param param = parameter.getAnnotation(Param.class);
            String str = urlString.toString();
            int index = str.indexOf("?");
            if (index != -1) {
                urlString.append("+").append("\"").append("&").append(param.value()).append("=").append("\"").append("+").append(parameter.getSimpleName());
            } else {
                urlString.append("+").append("\"").append("?").append(param.value()).append("=").append("\"").append("+").append(parameter.getSimpleName());
            }
        }

        urlString.append(")\n");

        return MethodSpec.overriding(executableElement)
                .addCode("$T request = new $T()\n", REQUEST, REQUEST_BODY_BUILDER)
                .addCode(".get()\n")
                .addCode(urlString.toString())
                .addStatement(".build()")
                .addStatement("$T newCall = client.newCall(request)", OK_HTTP_CALL)
                .addStatement("$T<$T> wrapperCall = new $T<>(converterFactory.respBodyConverter($T.class), newCall, client, request)", SPEEDY_CALL, generateType, SPEEDY_WRAPPER_CALL, generateType)
                .addStatement("return ($T)callAdapter.adapt(wrapperCall)", TypeName.get(returnType))
                .returns(TypeName.get(returnType))
                .build();
    }

}
