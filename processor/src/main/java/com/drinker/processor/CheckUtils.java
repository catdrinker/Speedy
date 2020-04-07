package com.drinker.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.util.List;

import static com.drinker.processor.SpeedyClassName.LIST;
import static com.drinker.processor.SpeedyClassName.MAP;
import static com.drinker.processor.SpeedyClassName.MULTIPART_PART;
import static com.drinker.processor.SpeedyClassName.REQ_BODY;
import static com.drinker.processor.SpeedyClassName.STRING;

public final class CheckUtils {

    /**
     * check is legitimate Param value {@link com.drinker.annotation.Param}
     *
     * @param typeName the parameter of typeName
     */
    public static void checkParam(TypeName typeName) {
        if (!typeName.equals(STRING)) {
            Log.w("check param " + typeName + typeName.getClass());
            throw new IllegalStateException("Param parameter must use String");
        }
    }


    /**
     * check is legitimate ParamMap value {@link com.drinker.annotation.ParamMap}
     *
     * @param typeName the parameter of typeName
     */
    public static void checkParamMap(TypeName typeName) {
        if (!(typeName instanceof ParameterizedTypeName)) {
            throw new IllegalStateException("PartMap type must be ParameterizedTypeName with Map<String,String>");
        }
        ClassName rawType = ((ParameterizedTypeName) typeName).rawType;
        List<TypeName> typeArguments = ((ParameterizedTypeName) typeName).typeArguments;
        if (!rawType.equals(MAP)) {
            throw new IllegalStateException("@ParamMap annotation must use parameter with java.util.Map");
        }

        if (typeArguments.size() != 2) {
            throw new IllegalStateException("Map ParameterizedType size must as 2");
        }

        TypeName t1 = typeArguments.get(0);
        TypeName t2 = typeArguments.get(1);

        if (!STRING.equals(t1) || !STRING.equals(t2)) {
            throw new IllegalStateException("Map ParameterizedType must be String type 1 " + t1 + " type2 " + t2);
        }
    }

    /**
     * check is legitimate Body value {@link com.drinker.annotation.Body}
     *
     * @param typeName the parameter of typeName
     */
    public static void checkBody(TypeName typeName) {
        Log.w("checkBody typeName " + typeName + typeName.getClass());
        if (!typeName.equals(REQ_BODY)) {
            throw new IllegalStateException("Body annotation must use parameter with okhttp3.RequestBody with common body handler");
        }
    }

    public static void checkConverterBody(TypeName typeName) {
        if (checkIsBasicType(typeName)) {
            throw new IllegalStateException("Body annotation must use parameter without basic type like int short String Object...");
        }
    }

    private static boolean checkIsBasicType(TypeName typeName) {
        return (typeName.equals(TypeName.BOOLEAN))
                || (typeName.equals(TypeName.BYTE))
                || (typeName.equals(TypeName.SHORT))
                || (typeName.equals(TypeName.INT))
                || (typeName.equals(TypeName.LONG))
                || (typeName.equals(TypeName.CHAR))
                || (typeName.equals(TypeName.FLOAT))
                || (typeName.equals(TypeName.DOUBLE))
                || (typeName.equals(TypeName.OBJECT))
                || (typeName.equals(STRING));

    }

    /**
     * check is legitimate Part value {@link com.drinker.annotation.Part}
     *
     * @param typeName the parameter of typeName
     */
    public static void checkMultipart(TypeName typeName) {
        if (!MULTIPART_PART.equals(typeName)) {
            throw new IllegalStateException("@ParamMap annotation must use parameter with okhttp3.RequestBody");
        }
    }

    /**
     * check is legitimate PartMap value {@link com.drinker.annotation.PartMap}
     *
     * @param typeName the parameter of typeName
     */
    public static void checkMultipartMap(TypeName typeName) {
        if (!(typeName instanceof ParameterizedTypeName)) {
            throw new IllegalStateException("PartMap type must be ParameterizedTypeName with List<Multipart.Part>");
        }
        ClassName rawType = ((ParameterizedTypeName) typeName).rawType;
        List<TypeName> typeArguments = ((ParameterizedTypeName) typeName).typeArguments;
        if (!LIST.equals(rawType)) {
            throw new IllegalStateException("@ParamMap annotation must use parameter with okhttp3.RequestBody");
        }

        if (typeArguments.size() != 1) {
            throw new IllegalStateException("List typeArguments size only can have 1");
        }

        TypeName wrapperName = typeArguments.get(0);
        if (!(wrapperName instanceof ClassName) || !MULTIPART_PART.equals(wrapperName)) {
            throw new IllegalStateException("List ParameterizedType must be MultipartBody.Part");
        }

    }
}