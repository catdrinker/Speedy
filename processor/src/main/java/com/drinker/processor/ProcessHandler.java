package com.drinker.processor;

import java.util.Set;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;

public interface ProcessHandler {

   void process(Set<? extends Element> componentElements, Elements elements, Messager messager, Filer filer);

}
