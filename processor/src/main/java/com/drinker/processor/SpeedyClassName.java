package com.drinker.processor;

import com.squareup.javapoet.ClassName;

public class SpeedyClassName {

    /**
     * okhttp package name and speedy package name
     */
    public static String OK_HTTP_PACKAGE = "okhttp3";
    public static String SPEEDY_PACKAGE = "com.drinker.speedy";


    /**
     * about okhttp class name
     */
    public static ClassName OK_HTTP_CALL = ClassName.get(OK_HTTP_PACKAGE, "Call");

    public static ClassName CALL_FACTORY = ClassName.get(OK_HTTP_PACKAGE, "Call")
            .nestedClass("Factory");

    public static ClassName REQ_BODY = ClassName.get(OK_HTTP_PACKAGE, "RequestBody");
    public static ClassName FORM_BODY = ClassName.get(OK_HTTP_PACKAGE, "FormBody");

    public static ClassName RESP_BODY = ClassName.get(OK_HTTP_PACKAGE, "ResponseBody");

    public static ClassName REQUEST = ClassName.get(OK_HTTP_PACKAGE, "Request");
    public static ClassName REQUEST_BODY_BUILDER = ClassName.get(OK_HTTP_PACKAGE, "Request")
            .nestedClass("Builder");

    /**
     * about speedy class name
     */
    public static ClassName SPEEDY_CALL = ClassName.get(SPEEDY_PACKAGE, "Call");
    public static ClassName SPEEDY_WRAPPER_CALL = ClassName.get(SPEEDY_PACKAGE, "WrapperCall");
    public static ClassName CONVERTER = ClassName.get(SPEEDY_PACKAGE, "Converter");
    public static ClassName CONVERTER_FACTORY = ClassName.get(SPEEDY_PACKAGE, "Converter").nestedClass("Factory");

    public static ClassName DELIVERY = ClassName.get(SPEEDY_PACKAGE, "IDelivery");
    public static ClassName CALL_ADAPTER = ClassName.get(SPEEDY_PACKAGE, "CallAdapter");
    public static ClassName CALL_ADAPTER_FACTORY = ClassName.get(SPEEDY_PACKAGE, "CallAdapter").nestedClass("Factory");
}
