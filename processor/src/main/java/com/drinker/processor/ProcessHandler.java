package com.drinker.processor;

import java.util.Set;

import javax.lang.model.element.Element;

public interface ProcessHandler {

   void process(Set<? extends Element> serviceElements);

}
