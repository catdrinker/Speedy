package com.drinker.processor;

import com.squareup.javapoet.ClassName;

public final class SpeedyClassName {

    /**
     * okhttp package name and speedy package name
     */
    public static String OK_HTTP_PACKAGE = "okhttp3";
    public static String SPEEDY_PACKAGE = "com.drinker.speedy";

    public static String JAVA_UTIL_PACKAGE = "java.util";
    public static String JAVA_LANG_PACKAGE = "java.lang";

    /**
     * common java class
     */
    public final static ClassName LIST = ClassName.get(JAVA_UTIL_PACKAGE,"List");
    public final static ClassName MAP = ClassName.get(JAVA_UTIL_PACKAGE,"Map");
    public final static ClassName MAP_ENTRY = MAP.nestedClass("Entry");
    public final static ClassName ITERATOR = ClassName.get(JAVA_UTIL_PACKAGE,"Iterator");

    public final static ClassName STRING = ClassName.get(JAVA_LANG_PACKAGE,"String");
    public final static ClassName STRING_BUILDER = ClassName.get(JAVA_LANG_PACKAGE,"StringBuilder");

    public final static ClassName ILLEGAL_STATE_EXCEPTION = ClassName.get(JAVA_LANG_PACKAGE, "IllegalStateException");

    /**
     * about okhttp class name
     */
    public final static ClassName OK_HTTP_CALL = ClassName.get(OK_HTTP_PACKAGE, "Call");

    public final static ClassName CALL_FACTORY = ClassName.get(OK_HTTP_PACKAGE, "Call")
            .nestedClass("Factory");

    public final static ClassName REQ_BODY = ClassName.get(OK_HTTP_PACKAGE, "RequestBody");
    public final static ClassName FORM_BODY = ClassName.get(OK_HTTP_PACKAGE, "FormBody");
    public final static ClassName FORM_BODY_BUILDER = FORM_BODY.nestedClass("Builder");


    public final static ClassName RESP_BODY = ClassName.get(OK_HTTP_PACKAGE, "ResponseBody");

    public final static ClassName REQUEST = ClassName.get(OK_HTTP_PACKAGE, "Request");
    public final static ClassName REQUEST_BODY_BUILDER = ClassName.get(OK_HTTP_PACKAGE, "Request")
            .nestedClass("Builder");

    public final static ClassName MEDIA_TYPE = ClassName.get(OK_HTTP_PACKAGE,"MediaType");
    public final static ClassName MULTIPART_BODY = ClassName.get(OK_HTTP_PACKAGE,"MultipartBody");
    public final static ClassName MULTIPART_PART = MULTIPART_BODY.nestedClass("Part");
    public final static ClassName MULTIPART_BODY_BUILDER = MULTIPART_BODY.nestedClass("Builder");

    /**
     * about speedy class name
     */
    public final static ClassName SPEEDY_TEXT_UTIL = ClassName.get(SPEEDY_PACKAGE, "TextUtil");
    public final static ClassName SPEEDY_TYPE_TOKEN = ClassName.get(SPEEDY_PACKAGE,"TypeToken2");
    public final static ClassName SPEEDY_CATCH_REQ_BODY = ClassName.get(SPEEDY_PACKAGE,"CatchExceptionRequestBody");
    public final static ClassName SPEEDY_CALL = ClassName.get(SPEEDY_PACKAGE, "Call");
    public final static ClassName SPEEDY_WRAPPER_CALL = ClassName.get(SPEEDY_PACKAGE, "WrapperCall");
    public final static ClassName CONVERTER = ClassName.get(SPEEDY_PACKAGE, "Converter");
    public final static ClassName CONVERTER_FACTORY = ClassName.get(SPEEDY_PACKAGE, "Converter").nestedClass("Factory");

    public final static ClassName DELIVERY = ClassName.get(SPEEDY_PACKAGE, "IDelivery");
    public final static ClassName CALL_ADAPTER = ClassName.get(SPEEDY_PACKAGE, "CallAdapter");
    public final static ClassName CALL_ADAPTER_FACTORY = ClassName.get(SPEEDY_PACKAGE, "CallAdapter").nestedClass("Factory");
}
