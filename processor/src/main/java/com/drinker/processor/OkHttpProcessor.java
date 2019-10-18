package com.drinker.processor;

import com.drinker.annotation.Get;
import com.drinker.annotation.Post;
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
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;


@SupportedAnnotationTypes({"com.drinker.annotation.Get", "com.drinker.annotation.Post"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class OkHttpProcessor extends AbstractProcessor {

    private Filer filer;

    private Elements elements;

    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
        elements = processingEnv.getElementUtils();
        messager = processingEnv.getMessager();
        messager.printMessage(Diagnostic.Kind.WARNING, "hehehehehehehe ----- ");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> getElement = roundEnv.getElementsAnnotatedWith(Get.class);
        Set<? extends Element> postElement = roundEnv.getElementsAnnotatedWith(Post.class);

        for (Element element : getElement) {
            ExecutableElement executableElement = (ExecutableElement) element;
            TypeElement typeElement = (TypeElement) executableElement.getEnclosingElement();
            String string = typeElement.getQualifiedName().toString();
            messager.printMessage(Diagnostic.Kind.WARNING, "element " + element + " tpye element " + typeElement + " " + string);
        }

        for (Element element : postElement) {
            ExecutableElement executableElement = (ExecutableElement) element;
            TypeElement typeElement = (TypeElement) executableElement.getEnclosingElement();
            String string = typeElement.getQualifiedName().toString();
            messager.printMessage(Diagnostic.Kind.WARNING, "element " + element + " tpye element " + typeElement + " " + string);
        }
        return false;
    }

}
