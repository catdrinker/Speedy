package com.drinker.processor;


import java.util.Set;


class RegexUtilTest {


    public static void main(String[] args) {
        testUrl();
    }

    public static void testUrl() {
//        String url = "/login/{user_name}/my_user?s=123";
        String url = "node/{name}/{map}pl.json?rand=635840524184357321";

        Set<String> strings = RegexUtil.generateURL(url);

        assert strings.size() == 1;

        for (String next : strings) {
            System.out.println("next is " + next);

            String[] split = url.split("\\{" + next + "\\}");

            for (String s : split) {
                System.out.println("s is " + s);
            }
        }

        System.out.println("replace is " + url);

    }
}