package com.drinker.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {

    private static final String PARAM = "[a-zA-Z][a-zA-Z0-9_-]*";
    private static final String WRAP_PARAM = "\\{" + PARAM + "\\}";
    private static final Pattern PARAM_URL_REGEX = Pattern.compile("\\{(" + PARAM + ")\\}");
    private static final Pattern PARAM_NAME_REGEX = Pattern.compile(PARAM);


    public static Set<String> generateUrl(String value) {
        Set<String> patterns = new LinkedHashSet<>();
        int question = value.indexOf('?');
        if (question != -1 && question < value.length() - 1) {
            String queryParams = value.substring(question + 1);
            Matcher queryParamMatcher = PARAM_URL_REGEX.matcher(queryParams);
            if (queryParamMatcher.find()) {
                throw new IllegalArgumentException("method about param should't with block \"{xxx}\"");
            }
        }
        Matcher m = PARAM_URL_REGEX.matcher(value);

        while (m.find()) {
            System.out.println("m group is " + m.group());
            patterns.add(m.group(1));
        }
        return patterns;
    }

    public static List<String> getOriginalWords(String url) {
        String[] strings = url.split(WRAP_PARAM);
        return new ArrayList<>(Arrays.asList(strings));
    }

    public static boolean pattern(String value) {
        return value.matches(WRAP_PARAM);
    }

}
