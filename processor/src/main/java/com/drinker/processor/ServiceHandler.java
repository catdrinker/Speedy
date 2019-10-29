package com.drinker.processor;

import com.drinker.processor.method.GetMethodHandler;
import com.drinker.processor.method.IHttpMethodHandler;
import com.drinker.processor.method.PostMethodHandler;
import com.drinker.processor.method.PutMethodHandler;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

import okhttp3.Call;

class ServiceHandler {
    private Set<? extends Element> serviceElements;
    private Elements elements;
    private Messager messager;
    private Filer filer;

    private final static String IMPL = "_impl";

    ServiceHandler(Set<? extends Element> serviceElements, Elements elements, Messager messager, Filer filer) {
        this.serviceElements = serviceElements;
        this.messager = messager;
        this.elements = elements;
        this.filer = filer;
    }

    void process() {
        for (Element serviceElement : serviceElements) {
            String packageName = elements.getPackageOf(serviceElement).getQualifiedName().toString();
            TypeElement element = (TypeElement) serviceElement;
            List<? extends Element> enclosedElements = element.getEnclosedElements();

            TypeSpec.Builder classBuilder = TypeSpec.classBuilder(element.getSimpleName().toString() + IMPL);
            for (Element enclosedElement : enclosedElements) {
                if (enclosedElement instanceof ExecutableElement) {
                    // 写方法
                    ExecutableElement executableElement = (ExecutableElement) enclosedElement;
                    messager.printMessage(Diagnostic.Kind.WARNING, "element" + element + "enclose" + enclosedElement);
                    List<? extends VariableElement> parameters = executableElement.getParameters();
                    MethodSpec methodSpec = getMethodSpec(executableElement, parameters, messager);
                    classBuilder.addMethod(methodSpec);
                }
            }

            FieldSpec clientFiled = FieldSpec.builder(Call.Factory.class, "client")
                    .addModifiers(Modifier.PRIVATE)
                    .build();

            FieldSpec baseHttpUrlField = FieldSpec.builder(String.class, "baseHttpUrl")
                    .addModifiers(Modifier.PRIVATE)
                    .build();

            // 添加构造函数
            MethodSpec constructor = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(Call.Factory.class, "client")
                    .addParameter(String.class, "baseHttpUrl")
                    .addStatement("this.client = client")
                    .addStatement("this.baseHttpUrl = baseHttpUrl")
                    .build();

            TypeSpec typeSpec = classBuilder.addSuperinterface(ClassName.get(packageName, serviceElement.getSimpleName().toString()))
                    .addField(clientFiled)
                    .addField(baseHttpUrlField)
                    .addMethod(constructor)
                    .build();

            JavaFile javaFile = JavaFile.builder(packageName, typeSpec)
                    .skipJavaLangImports(true)
                    .build();

            try {
                javaFile.writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private MethodSpec getMethodSpec(ExecutableElement element, List<? extends VariableElement> parameters, Messager messager) {
        List<IHttpMethodHandler> handlers = new ArrayList<>();
        handlers.add(new GetMethodHandler());
        handlers.add(new PostMethodHandler());
        handlers.add(new PutMethodHandler());

        for (IHttpMethodHandler handler : handlers) {
            MethodSpec methodSpec = handler.process(element, parameters, messager);
            if (methodSpec != null) {
                return methodSpec;
            }
        }
        throw new NullPointerException("method must has annotation like @Get @Post...");
    }

}
