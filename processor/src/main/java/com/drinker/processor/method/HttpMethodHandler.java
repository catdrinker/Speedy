package com.drinker.processor.method;

import com.drinker.annotation.Form;
import com.drinker.annotation.MultiPart;
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
        boolean isForm = executableElement.getAnnotation(Form.class) != null;
        boolean isMultiPart = executableElement.getAnnotation(MultiPart.class) != null;
        if (isForm && isMultiPart) {
            throw new IllegalStateException("can't use Form and MultiPart at the same method");
        }
        boolean handle = handle(executableElement);
        if (handle) {
            return methodProcess(executableElement, parameters, returnType, generateType);
        }
        return null;
    }

    private MethodSpec methodProcess(ExecutableElement executableElement, List<? extends VariableElement> parameters, TypeMirror returnType, TypeName generateType) {
        String extraUrl = getExtraUrl(executableElement);
        Set<String> formats = RegexUtil.generateURL(extraUrl);
        List<String> originalWords = RegexUtil.getOriginalWords(extraUrl);
        if (originalWords.size() <= formats.size()) {
            throw new IllegalStateException("original word size must > format size");
        }

        List<String> formatParameters = getFormatParameters(parameters, formats);
        List<Param> formatParams = getFormatParams(parameters, formats);
        assert formatParameters.size() == originalWords.size() - 1;

        StringBuilder urlString = buildUrl(parameters, formatParams, formatParameters, originalWords, extraUrl);

        return process(executableElement, parameters, returnType, generateType, urlString, formatParams);
    }

    private StringBuilder buildUrl(List<? extends VariableElement> parameters, List<Param> formatParams, List<String> formatParameters, List<String> originalWords, String extraUrl) {
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
        // append url if necessary
        appendUrl(parameters, formatParams, urlString);
        return urlString;
    }


    private List<String> getFormatParameters(List<? extends VariableElement> parameters, Set<String> formats) {
        List<String> formatParameters = new ArrayList<>();
        for (String format : formats) {
            Log.w("next format " + format);
            String usingFormat = null;
            for (VariableElement parameter : parameters) {
                Param param = parameter.getAnnotation(Param.class);
                // find {xxx} match with param's value, so just replace format {xxx} with it parameter's real value
                if (param != null && format.equals(param.value())) {
                    Log.i("format has " + format);
                    usingFormat = parameter.getSimpleName().toString();
                    break;
                }
            }
            // if don't find parameter match with {xxx}, just using {xxx}
            if (usingFormat == null) {
                usingFormat = "{" + format + "}";
            }
            formatParameters.add(usingFormat);
        }
        return formatParameters;
    }

    private List<Param> getFormatParams(List<? extends VariableElement> parameters, Set<String> formats) {
        List<Param> params = new ArrayList<>();
        for (String format : formats) {
            for (VariableElement parameter : parameters) {
                Param param = parameter.getAnnotation(Param.class);
                // find {xxx} match with param's value, so just replace format {xxx} with it parameter's real value
                if (param != null && format.equals(param.value())) {
                    params.add(param);
                    break;
                }
            }
        }
        return params;
    }

    protected abstract boolean handle(ExecutableElement executableElement);

    protected abstract String getExtraUrl(ExecutableElement executableElement);

    protected abstract MethodSpec process(ExecutableElement executableElement, List<? extends VariableElement> parameters, TypeMirror returnType, TypeName generateType, StringBuilder urlString, List<Param> formatParams);

    protected void appendUrl(List<? extends VariableElement> parameters, List<Param> formatParams, StringBuilder urlString) {

    }
}
