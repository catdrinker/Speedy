package com.drinker.processor.processor;

import com.drinker.annotation.Get;
import com.drinker.annotation.Param;
import com.drinker.processor.CheckUtils;
import com.drinker.processor.Log;
import com.squareup.javapoet.ClassName;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

public abstract class GetProcessor extends HttpProcessor<Get> {

    @Override
    protected Get getAnnotations(ExecutableElement executableElement) {
        return executableElement.getAnnotation(Get.class);
    }

    @Override
    protected String getMethod() {
        return GET;
    }

    @Override
    protected String getExtraUrl(ExecutableElement executableElement) {
        return executableElement.getAnnotation(Get.class).value();
    }

    @Override
    protected void appendUrl(List<? extends VariableElement> parameters, StringBuilder urlString) {
        for (VariableElement parameter : parameters) {
            Param param = parameter.getAnnotation(Param.class);
            if (param == null) {
                Log.w("find format param just skip it ");
                continue;
            }
            CheckUtils.checkParam(ClassName.get(parameter.asType()));
            String str = urlString.toString();
            int index = str.indexOf("?");
            if (index != -1) {
                urlString.append("+").append("\"").append("&").append(param.value()).append("=").append("\"").append("+").append(parameter.getSimpleName());
            } else {
                urlString.append("+").append("\"").append("?").append(param.value()).append("=").append("\"").append("+").append(parameter.getSimpleName());
            }
        }
    }
}
