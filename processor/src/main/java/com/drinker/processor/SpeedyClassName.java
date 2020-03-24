package com.drinker.processor;

import com.squareup.javapoet.ClassName;

public class SpeedyClassName {

    /**
     * okhttp package name and speedy package name
     */
    public static String OK_HTTP_PACKAGE = "okhttp3";
    public static String SPEEDY_PACKAGE = "com.drinker.speedy";

    public static String JAVA_UTIL_PACKAGE = "java.util";

    public static String JAVA_LANG_PACKAGE = "java.lang";

    public static ClassName MAP = ClassName.get(JAVA_UTIL_PACKAGE,"Map");
    public static ClassName LIST = ClassName.get(JAVA_UTIL_PACKAGE,"List");
    public static ClassName MAP_ENTRY = MAP.nestedClass("Entry");

    public static ClassName STRING = ClassName.get(JAVA_LANG_PACKAGE,"String");
    public static ClassName STRING_BUILDER = ClassName.get(JAVA_LANG_PACKAGE,"StringBuilder");
    public static ClassName ITERATOR = ClassName.get(JAVA_UTIL_PACKAGE,"Iterator");

    public static ClassName ILLEGAL_STATE_EXCEPTION = ClassName.get(JAVA_LANG_PACKAGE, "IllegalStateException");

    /**
     * about okhttp class name
     */
    public static ClassName OK_HTTP_CALL = ClassName.get(OK_HTTP_PACKAGE, "Call");

    public static ClassName CALL_FACTORY = ClassName.get(OK_HTTP_PACKAGE, "Call")
            .nestedClass("Factory");

    public static ClassName REQ_BODY = ClassName.get(OK_HTTP_PACKAGE, "RequestBody");
    public static ClassName FORM_BODY = ClassName.get(OK_HTTP_PACKAGE, "FormBody");
    public static ClassName FORM_BODY_BUILDER = FORM_BODY.nestedClass("Builder");


    public static ClassName RESP_BODY = ClassName.get(OK_HTTP_PACKAGE, "ResponseBody");

    public static ClassName REQUEST = ClassName.get(OK_HTTP_PACKAGE, "Request");
    public static ClassName REQUEST_BODY_BUILDER = ClassName.get(OK_HTTP_PACKAGE, "Request")
            .nestedClass("Builder");

    public static ClassName MEDIA_TYPE = ClassName.get(OK_HTTP_PACKAGE,"MediaType");
    public static ClassName MULTIPART_BODY = ClassName.get(OK_HTTP_PACKAGE,"MultipartBody");
    public static ClassName MULTIPART_PART = MULTIPART_BODY.nestedClass("Part");
    public static ClassName MULTIPART_BODY_BUILDER = MULTIPART_BODY.nestedClass("Builder");

    /**
     * about speedy class name
     */
    public static ClassName SPEEDY_TEXT_UTIL = ClassName.get(SPEEDY_PACKAGE, "TextUtil");
    public static ClassName SPEEDY_CALL = ClassName.get(SPEEDY_PACKAGE, "Call");
    public static ClassName SPEEDY_WRAPPER_CALL = ClassName.get(SPEEDY_PACKAGE, "WrapperCall");
    public static ClassName CONVERTER = ClassName.get(SPEEDY_PACKAGE, "Converter");
    public static ClassName CONVERTER_FACTORY = ClassName.get(SPEEDY_PACKAGE, "Converter").nestedClass("Factory");

    public static ClassName DELIVERY = ClassName.get(SPEEDY_PACKAGE, "IDelivery");
    public static ClassName CALL_ADAPTER = ClassName.get(SPEEDY_PACKAGE, "CallAdapter");
    public static ClassName CALL_ADAPTER_FACTORY = ClassName.get(SPEEDY_PACKAGE, "CallAdapter").nestedClass("Factory");
}
