package com.drinker.processor;

import com.drinker.annotation.Get;
import com.drinker.annotation.Param;
import com.drinker.annotation.Post;
import com.drinker.annotation.Service;

import java.util.List;
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
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.security.auth.login.LoginContext;
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
        messager.printMessage(Diagnostic.Kind.WARNING, "hehehehehehehe ----- " + elements + " filter " + filer);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> serviceElements = roundEnv.getElementsAnnotatedWith(Service.class);
        ServiceHandler serviceHandler = new ServiceHandler(serviceElements, elements, messager,filer);
        serviceHandler.process();
        /*for (Element serviceElement : serviceElements) {

            Name qualifiedName = elements.getPackageOf(serviceElement).getQualifiedName();
            TypeElement element = (TypeElement) serviceElement;
            List<? extends Element> enclosedElements = element.getEnclosedElements();
            // TODO 获取到对应service, 准备开始写文件
            messager.printMessage(Diagnostic.Kind.WARNING, "quailfileName" + qualifiedName.toString());

            for (Element enclosedElement : enclosedElements) {

                if (enclosedElement instanceof ExecutableElement) {
                    messager.printMessage(Diagnostic.Kind.WARNING, "element" + element + "enclose" + enclosedElement);
                    ExecutableElement executableElement = (ExecutableElement) enclosedElement;
                    List<? extends VariableElement> parameters = executableElement.getParameters();
                    // 解析对应的get method
                    Get getAnnotation = executableElement.getAnnotation(Get.class);
                    Post postAnnotation = executableElement.getAnnotation(Post.class);

                    for (VariableElement parameter : parameters) {
                        Param params = parameter.getAnnotation(Param.class);
                        messager.printMessage(Diagnostic.Kind.WARNING, "params is " + params + "parmater" + parameter);
                    }
                }


            }
        }

        Set<? extends Element> getElement = roundEnv.getElementsAnnotatedWith(Get.class);
        Set<? extends Element> postElement = roundEnv.getElementsAnnotatedWith(Post.class);

        for (Element element : getElement) {
            ExecutableElement executableElement = (ExecutableElement) element;
            TypeElement typeElement = (TypeElement) executableElement.getEnclosingElement();

            TypeMirror returnType = executableElement.getReturnType();


            List<? extends VariableElement> parameters = executableElement.getParameters();

            for (VariableElement parameter : parameters) {
                Param annotation = parameter.getAnnotation(Param.class);
                messager.printMessage(Diagnostic.Kind.WARNING, "paramater " + parameter + annotation.value());
            }
            String string = typeElement.getQualifiedName().toString();
            messager.printMessage(Diagnostic.Kind.WARNING, "element " + element + " tpye element " + typeElement + " " + string);
        }

        for (Element element : postElement) {
            ExecutableElement executableElement = (ExecutableElement) element;
            TypeElement typeElement = (TypeElement) executableElement.getEnclosingElement();
            List<? extends VariableElement> parameters = executableElement.getParameters();

            for (VariableElement parameter : parameters) {
                ElementKind kind = parameter.getKind();
                Param parameterAnnotation = parameter.getAnnotation(Param.class);
                if (parameterAnnotation == null) {
                    // lanhuazhinian  ,hong ceng xiangsiddiannian
                    // guxiang yiweinin dasdasd
                    messager.printMessage(Diagnostic.Kind.WARNING, "post paramater " + parameter + "annotation " +parameterAnnotation.value());
                }
            }
            messager.printMessage(Diagnostic.Kind.WARNING, "element " + element + " tpye element " + typeElement + " ");
        }*/
        return false;
    }

}
