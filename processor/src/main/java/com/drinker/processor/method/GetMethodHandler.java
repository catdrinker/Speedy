package com.drinker.processor.method;

import com.drinker.annotation.Get;
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

import static com.drinker.processor.SpeedyClassName.OK_HTTP_CALL;
import static com.drinker.processor.SpeedyClassName.REQUEST;
import static com.drinker.processor.SpeedyClassName.REQUEST_BODY_BUILDER;
import static com.drinker.processor.SpeedyClassName.SPEEDY_CALL;
import static com.drinker.processor.SpeedyClassName.SPEEDY_WRAPPER_CALL;


public class GetMethodHandler implements IHttpMethodHandler {


    @Override
    public MethodSpec process(ExecutableElement executableElement, List<? extends VariableElement> parameters, TypeMirror returnType, TypeName generateType) {
        Get getAnnotation = executableElement.getAnnotation(Get.class);
        if (getAnnotation != null) {
            String value = getAnnotation.value();
            Set<String> formats = RegexUtil.generateURL(value);
            List<String> originalWords = RegexUtil.getOriginalWords(value);

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
                    if (format.equals(param.value())) {
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

            StringBuilder sb = new StringBuilder(".url(baseHttpUrl+");
            if (formatParameters.size() == 0) {
                sb.append("\"").append(value).append("\"");
            }

            for (int i = 0; i < formatParameters.size(); i++) {
                String originalWord = originalWords.get(i);
                String param = formatParameters.get(i);
                sb.append("\"").append(originalWord).append("\"");

                if (RegexUtil.pattern(param)) {
                    sb.append("+").append("\"").append(param).append("\"").append("+");
                } else {
                    sb.append("+").append(param).append("+");
                }

                if (i == formatParameters.size() - 1) {
                    sb.append("\"").append(originalWords.get(i + 1)).append("\"");
                }
            }


            // break 有问题
            for (VariableElement parameter : parameters) {
                boolean hasParam = false;
                for (String realParameter : realParameters) {
                    if (realParameter.equals(parameter.getSimpleName().toString())) {
                        Log.i("has real param " + realParameter);
                        hasParam = true;
                        break;
                    }
                }
                Log.w("after loop " + parameter.getSimpleName());
                if (hasParam) {
                    break;
                }
                Param param = parameter.getAnnotation(Param.class);
                String str = sb.toString();
                int index = str.indexOf("?");
                // 没有的话就可以 split("?")
                if (index != -1) {
                    sb.append("+").append("\"").append("&").append(param.value()).append("=").append("\"").append("+").append(parameter.getSimpleName());
                } else {
                    sb.append("+").append("\"").append("?").append(param.value()).append("=").append("\"").append("+").append(parameter.getSimpleName());
                }
            }

            sb.append(")");

            return MethodSpec.overriding(executableElement)
                    .addCode("$T request = new $T()\n", REQUEST, REQUEST_BODY_BUILDER)
                    .addCode(".get()\n")
                    .addCode(sb.toString())

                    .addStatement(".build()")
                    .addStatement("$T newCall = client.newCall(request)", OK_HTTP_CALL)
                    .addStatement("$T<$T> wrapperCall = new $T<>(converterFactory.respBodyConverter($T.class), newCall, client, request)", SPEEDY_CALL, generateType, SPEEDY_WRAPPER_CALL, generateType)
                    .addStatement("return ($T)callAdapter.adapt(wrapperCall)", TypeName.get(returnType))

                    .returns(TypeName.get(returnType))
                    .build();
        }
        return null;
    }
}
