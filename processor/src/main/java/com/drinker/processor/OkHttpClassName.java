package com.drinker.processor;

import com.squareup.javapoet.ClassName;

public class OkHttpClassName {

    private static String OK_HTTP_PACKAGE = "okhttp3";
    private static String SPEEDY_PACKAGE = "com.drinker.speedy";


    public static ClassName OK_HTTP_CALL = ClassName.get(OK_HTTP_PACKAGE, "Call");
    public static ClassName SPEEDY_CALL = ClassName.get(SPEEDY_PACKAGE, "Call");
    public static ClassName SPEEDY_WRAPPER_CALL = ClassName.get(SPEEDY_PACKAGE, "WrapperCall");


    public static ClassName CALL_FACTORY = ClassName.get(OK_HTTP_PACKAGE, "Call")
            .nestedClass("Factory");

    public static ClassName REQ_BODY = ClassName.get(OK_HTTP_PACKAGE, "RequestBody");
    public static ClassName FORM_BODY = ClassName.get(OK_HTTP_PACKAGE, "FormBody");

    public static ClassName REQUEST = ClassName.get(OK_HTTP_PACKAGE, "Request");
    public static ClassName REQUEST_BODY_BUILDER = ClassName.get(OK_HTTP_PACKAGE, "Request")
            .nestedClass("Builder");

    public static ClassName CONVERTER = ClassName.get(SPEEDY_PACKAGE, "Converter");
    public static ClassName DELIVERY = ClassName.get(SPEEDY_PACKAGE, "IDelivery");
    public static ClassName CALL_ADAPTER = ClassName.get(SPEEDY_PACKAGE, "CallAdapter");
}
