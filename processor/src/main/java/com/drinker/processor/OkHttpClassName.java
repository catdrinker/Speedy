package com.drinker.processor;

import com.squareup.javapoet.ClassName;

public class OkHttpClassName {

    public static ClassName CALL_FACTORY = ClassName.get("okhttp3", "Call")
            .nestedClass("Factory");

    public static ClassName REQ_BODY = ClassName.get("okhttp3", "RequestBody");
    public static ClassName FORM_BODY = ClassName.get("okhttp3", "FormBody");

    public static ClassName REQUEST = ClassName.get("okhttp3", "Request");
    public static ClassName REQUEST_BODY_BUILDER = ClassName.get("okhttp3", "Request")
            .nestedClass("Builder");

    public static ClassName CONVERTER = ClassName.get("com.drinker.speedy", "Converter");

}
