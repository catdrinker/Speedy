package com.drinker.speedy;

import org.junit.Test;

public class TypeToken2Test {

    @Test
    public void testFailTypeToken() {
        try {
            new TypeToken2<String>();
        } catch (Throwable throwable) {
            assert throwable.getMessage().equals("Missing type parameter.");
        }
    }

    @Test
    public void testStringTypeToken2() {
        TypeToken2<String> stringTypeToken2 = new TypeToken2<String>() {
        };
        System.out.println("stringToken " + stringTypeToken2 + stringTypeToken2.getRawType() + stringTypeToken2.getType());
        assert stringTypeToken2.getType() == String.class;
        assert stringTypeToken2.getRawType() == String.class;
    }

    @Test
    public void testHomeTypeToken2() {
        TypeToken2<Home> homeTypeToken2 = new TypeToken2<Home>() {
        };
        assert homeTypeToken2.getType() == Home.class;
        assert homeTypeToken2.getRawType() == Home.class;
    }


    @Test
    public void testUserTypeToken2() {
        TypeToken2<User> userTypeToken2 = new TypeToken2<User>() {
        };
        assert userTypeToken2.getType() == User.class;
        assert userTypeToken2.getRawType() == User.class;
    }

    @Test
    public void testHomeUserTypeToken2() {
        TypeToken2<User<Home>> homeUserTypeToken2 = new TypeToken2<User<Home>>() {
        };
        System.out.println("home user " + homeUserTypeToken2.getType().getTypeName() + homeUserTypeToken2.getRawType());
        assert homeUserTypeToken2.getType().getTypeName().equals("com.drinker.speedy.TypeToken2Test$User<com.drinker.speedy.TypeToken2Test$Home>");
        assert homeUserTypeToken2.getRawType() == User.class;
    }


    static class User<T> {
        T data;
        String name;
        String pwd;
    }

    static class Home {
        String address;
        int number;
    }

}