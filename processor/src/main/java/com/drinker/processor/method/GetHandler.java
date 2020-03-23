package com.drinker.processor.method;

import com.drinker.annotation.Get;
import com.drinker.annotation.Param;
import com.drinker.processor.CheckUtils;
import com.drinker.processor.handler.IHandler;
import com.drinker.processor.Log;
import com.drinker.processor.handler.TrueHandler;
import com.drinker.processor.writter.MethodWriter;
import com.drinker.processor.writter.NoBodyMethodWriter;
import com.squareup.javapoet.ClassName;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

public final class GetHandler extends HttpHandler<Get> {

    @Override
    protected Get getAnnotations(ExecutableElement executableElement) {
        return executableElement.getAnnotation(Get.class);
    }

    @Override
    protected MethodWriter getWriter() {
        return new NoBodyMethodWriter();
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
    protected IHandler getHandler() {
        return new TrueHandler();
    }

    @Override
    protected void appendUrl(List<? extends VariableElement> parameters, List<Param> formatParams, StringBuilder urlString) {
        for (VariableElement parameter : parameters) {
            Param param = parameter.getAnnotation(Param.class);
            CheckUtils.checkParam(ClassName.get(parameter.asType()));
            if (param == null || formatParams.contains(param)) {
                Log.w("find format param just skip it " + param);
                continue;
            }
            String str = urlString.toString();
            int index = str.indexOf("?");
            if (index != -1) {
                urlString.append("+").append("\"").append("&").append(param.value()).append("=").append("\"").append("+").append(parameter.getSimpleName());
            } else {
                urlString.append("+").append("\"").append("?").append(param.value()).append("=").append("\"").append("+").append(parameter.getSimpleName());
            }
        }
        urlString.append(")\n");
    }
}
