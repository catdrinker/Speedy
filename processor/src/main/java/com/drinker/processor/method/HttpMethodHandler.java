package com.drinker.processor.method;

import com.drinker.annotation.Param;
import com.drinker.processor.Log;
import com.drinker.processor.RegexUtil;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

public abstract class HttpMethodHandler implements IHttpMethodHandler {
    @Override
    public MethodSpec process(ExecutableElement executableElement, List<? extends VariableElement> parameters, TypeMirror returnType, TypeName generateType) {
        String extraUrl = getExtraUrl(executableElement);
        if (extraUrl == null) {
            return null;
        }
        Set<String> formats = RegexUtil.generateURL(extraUrl);
        List<String> originalWords = RegexUtil.getOriginalWords(extraUrl);

        if (originalWords.size() <= formats.size()) {
            throw new IllegalStateException("original word size must > format size");
        }

        List<String> formatParameters = new ArrayList<>();
        List<String> realParameters = new ArrayList<>();

        for (String format : formats) {
            Log.w("NEXT IS " + format);
            String usingFormat = null;
            for (VariableElement parameter : parameters) {
                Param param = parameter.getAnnotation(Param.class);
                if (param != null && format.equals(param.value())) {
                    Log.i("format has " + format);
                    usingFormat = parameter.getSimpleName().toString();
                    realParameters.add(parameter.getSimpleName().toString());
                    break;
                }
            }
            // 如果没有该参数，就用原来的{xxx}代替
            if (usingFormat == null) {
                usingFormat = "{" + format + "}";
            }
            formatParameters.add(usingFormat);
        }

        assert formatParameters.size() == originalWords.size() - 1;

        StringBuilder urlString = new StringBuilder(".url(baseHttpUrl+");
        if (formatParameters.size() == 0) {
            urlString.append("\"").append(extraUrl).append("\"");
        }

        for (int i = 0; i < formatParameters.size(); i++) {
            String originalWord = originalWords.get(i);
            String param = formatParameters.get(i);
            urlString.append("\"").append(originalWord).append("\"");

            if (RegexUtil.pattern(param)) {
                urlString.append("+").append("\"").append(param).append("\"").append("+");
            } else {
                urlString.append("+").append(param).append("+");
            }

            if (i == formatParameters.size() - 1) {
                urlString.append("\"").append(originalWords.get(i + 1)).append("\"");
            }
        }
        return process(executableElement, parameters, returnType, generateType, urlString, realParameters);
    }


    protected abstract MethodSpec process(ExecutableElement executableElement, List<? extends VariableElement> parameters, TypeMirror returnType, TypeName generateType, StringBuilder urlString, List<String> realParameters);

    protected abstract String getExtraUrl(ExecutableElement executableElement);
}
