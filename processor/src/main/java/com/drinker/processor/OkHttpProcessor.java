package com.drinker.processor;

import com.drinker.annotation.Service;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;


@SupportedAnnotationTypes({"com.drinker.annotation.Get", "com.drinker.annotation.Post"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class OkHttpProcessor extends AbstractProcessor {

    private Filer filer;

    private Elements elements;

    private Messager messager;

    private ProcessHandler mapHandler;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
        elements = processingEnv.getElementUtils();
        messager = processingEnv.getMessager();
        messager.printMessage(Diagnostic.Kind.WARNING, "hehehehehehehe ----- " + elements + " filter " + filer);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // create service file
        Set<? extends Element> serviceElements = roundEnv.getElementsAnnotatedWith(Service.class);
        messager.printMessage(Diagnostic.Kind.WARNING, "serviceElements " + serviceElements);
        ProcessHandler serviceHandler = new ServiceHandler(elements, messager, filer);
        serviceHandler.process(serviceElements);
        if (mapHandler == null && !serviceElements.isEmpty()) {
            mapHandler = new ServiceMapHandler(elements, messager, filer);
            mapHandler.process(serviceElements);
        }

        return false;
    }

}
