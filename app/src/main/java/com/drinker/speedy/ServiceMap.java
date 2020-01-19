package com.drinker.speedy;

import java.util.HashMap;
import java.util.Map;


// apt write
public class ServiceMap {

    private static final Map<String, Object> serviceMap = new HashMap<>();


    static {
        serviceMap.put("com.drinker.speedy", new IService_impl());
    }


    public static <T> T getService(Class<T> typeName) {
        Object o = serviceMap.get(typeName);
        if (o != null) {
            return (T) o;
        }
        return null;
    }

}
