package com.drinker.processor;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

public class ServiceMapHandler implements ProcessHandler {

    private static final String SERVICE_MAP_NAME = "ServiceMap";

    private Filer filer;

    private Elements elements;

    private Messager messager;

    public ServiceMapHandler(Elements elements, Messager messager, Filer filer) {
        this.elements = elements;
        this.messager = messager;
        this.filer = filer;
    }

    @Override
    public void process(Set<? extends Element> serviceElements) {
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(SERVICE_MAP_NAME);

        messager.printMessage(Diagnostic.Kind.WARNING, "just generate map process");
        for (Element serviceElement : serviceElements) {
            String packageName = elements.getPackageOf(serviceElement).getQualifiedName().toString();
            TypeElement element = (TypeElement) serviceElement;

            /*element.getSimpleName().toString() + IMPL*/
        }


        TypeSpec typeSpec = classBuilder
                .addModifiers(Modifier.PUBLIC)
                .build();

        JavaFile javaFile = JavaFile.builder(SpeedyClassName.SPEEDY_PACKAGE, typeSpec)
                .skipJavaLangImports(true)
                .build();

        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
