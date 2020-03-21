package com.drinker.processor;

import org.junit.Test;

import java.util.List;
import java.util.Set;

public class RegexUtilTest {

    private String url = "node/{name}/{map}pl.json?rand=635840524184357321";

    private String noGenerateUrl = "node/name/map/pl.json?rand=635840524184357321";

    @Test
    public void testGenerateUrl() {
        Set<String> strings = RegexUtil.generateUrl(url);
        assert strings.size() == 2;
        Set<String> strings1 = RegexUtil.generateUrl(noGenerateUrl);
        assert strings1.size() == 0;
    }

    @Test
    public void testGetOriginalWords() {
        List<String> originalWords = RegexUtil.getOriginalWords(url);
        assert originalWords.size() == 3;
        assert "node/".equals(originalWords.get(0));
        assert "/".equals(originalWords.get(1));
        assert "pl.json?rand=635840524184357321".equals(originalWords.get(2));
    }

    @Test
    public void testOriginalWordsWithoutFormat() {
        List<String> originalWords = RegexUtil.getOriginalWords(noGenerateUrl);
        assert originalWords.size() == 1;
        assert noGenerateUrl.equals(originalWords.get(0));
    }

    @Test
    public void testPattern() {
        assert RegexUtil.pattern("{name}");
        assert !RegexUtil.pattern("hello");
    }
}