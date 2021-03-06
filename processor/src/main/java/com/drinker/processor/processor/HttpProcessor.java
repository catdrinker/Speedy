package com.drinker.processor.processor;

import com.drinker.annotation.Form;
import com.drinker.annotation.MultiPart;
import com.drinker.annotation.Path;
import com.drinker.processor.Log;
import com.drinker.processor.RegexUtil;
import com.drinker.processor.handler.IHandler;
import com.drinker.processor.writter.MethodWriter;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

public abstract class HttpProcessor<T> implements IHttpMethodProcessor {

    static final String GET = "get";
    static final String POST = "post";
    static final String PUT = "put";
    static final String DELETE = "delete";

    private IHandler handler = getHandler();

    private MethodWriter processor = getWriter();


    @Override
    public MethodSpec process(ExecutableElement executableElement, List<? extends VariableElement> parameters, TypeMirror returnType, TypeName generateType) {
        boolean isForm = executableElement.getAnnotation(Form.class) != null;
        boolean isMultiPart = executableElement.getAnnotation(MultiPart.class) != null;
        if (isForm && isMultiPart) {
            throw new IllegalStateException("can't use Form and MultiPart at the same method");
        }
        T annotation = getAnnotations(executableElement);
        boolean handle = handler.handle(executableElement, parameters);
        if (annotation != null && handle) {
            return processUrl(executableElement, parameters, returnType, generateType);
        }
        return null;
    }

    private MethodSpec processUrl(ExecutableElement executableElement, List<? extends VariableElement> parameters, TypeMirror returnType, TypeName generateType) {
        String extraUrl = getExtraUrl(executableElement);
        Set<String> formats = RegexUtil.generateUrl(extraUrl);
        List<String> originalWords = RegexUtil.getOriginalWords(extraUrl);
        if (originalWords.size() < formats.size()) {
            throw new IllegalStateException("original word size must >= format size");
        }
        if (originalWords.size() == formats.size()) {
            originalWords.add("");
        }
        List<String> formatParameters = getFormatParameters(parameters, formats);
        StringBuilder urlString = buildUrl(parameters, formatParameters, originalWords, extraUrl);
        return processor.write(executableElement, parameters, getMethod(), returnType, generateType, urlString);
    }

    private StringBuilder buildUrl(List<? extends VariableElement> parameters, List<String> formatParameters, List<String> originalWords, String extraUrl) {
        StringBuilder urlString = new StringBuilder("baseHttpUrl+");
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
        appendUrl(parameters, urlString);
        return urlString;
    }


    private List<String> getFormatParameters(List<? extends VariableElement> parameters, Set<String> formats) {
        List<String> formatParameters = new ArrayList<>();
        for (String format : formats) {
            Log.w("next format " + format);
            String usingFormat = null;
            for (VariableElement parameter : parameters) {
                Path path = parameter.getAnnotation(Path.class);
                // find {xxx} match with path's value, so just replace format {xxx} with it parameter's real value
                if (path != null && format.equals(path.value())) {
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

    protected abstract T getAnnotations(ExecutableElement executableElement);

    protected abstract MethodWriter getWriter();

    protected abstract IHandler getHandler();

    protected abstract String getMethod();

    protected abstract String getExtraUrl(ExecutableElement executableElement);

    protected void appendUrl(List<? extends VariableElement> parameters, StringBuilder urlString) {

    }

}
