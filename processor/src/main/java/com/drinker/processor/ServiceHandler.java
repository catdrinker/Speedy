package com.drinker.processor;

import com.drinker.annotation.Get;
import com.drinker.annotation.Param;
import com.drinker.annotation.Post;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ServiceHandler {
    private Set<? extends Element> serviceElements;
    private Elements elements;
    private Messager messager;
    private Filer filer;

    ServiceHandler(Set<? extends Element> serviceElements, Elements elements, Messager messager, Filer filer) {
        this.serviceElements = serviceElements;
        this.messager = messager;
        this.elements = elements;
        this.filer = filer;
    }

    protected void process() {

        for (Element serviceElement : serviceElements) {
            String packageName = elements.getPackageOf(serviceElement).getQualifiedName().toString();
            TypeElement element = (TypeElement) serviceElement;
            List<? extends Element> enclosedElements = element.getEnclosedElements();
            // TODO 获取到对应service, 准备开始写文件
            /**
             * 准备一个classType
             */
            TypeSpec.Builder classBuilder = TypeSpec.classBuilder(element.getSimpleName().toString() + "_impl");
            for (Element enclosedElement : enclosedElements) {
                if (enclosedElement instanceof ExecutableElement) {
                    ExecutableElement executableElement = (ExecutableElement) enclosedElement;
                    messager.printMessage(Diagnostic.Kind.WARNING, "element" + element + "enclose" + enclosedElement);


                    List<? extends VariableElement> parameters = executableElement.getParameters();
                    // 解析对应的get method
                    Get getAnnotation = executableElement.getAnnotation(Get.class);
                    Post postAnnotation = executableElement.getAnnotation(Post.class);

                    if (getAnnotation != null && postAnnotation != null) {
                        throw new IllegalStateException("one method should't has get and post annotations");
                    }
                    if (getAnnotation == null && postAnnotation == null) {
                        throw new IllegalStateException("one method must have a annotation with @Get or @Post");
                    }

                    if (getAnnotation != null) {
                        MethodSpec methodSpec = MethodSpec.overriding(executableElement)
                                .addCode("$T request = new $T.Builder()", Request.class, Request.class)
                                .addCode(".get()\n")
                                .addCode(".url($S)\n", getAnnotation.url())
                                .addStatement(".build()")
                                .addStatement("return client.newCall(request)")
                                .returns(ClassName.get("okhttp3", "Call"))
                                .build();
                        classBuilder.addMethod(methodSpec);
                    }

                    if (postAnnotation != null) {
                        MethodSpec.Builder methodSpecBuilder = MethodSpec.overriding(executableElement)
                                .addCode("$T formBody = new $T.Builder()\n", RequestBody.class, FormBody.class);

                        for (VariableElement parameter : parameters) {
                            Param param = parameter.getAnnotation(Param.class);
                            methodSpecBuilder.addCode(".add($S, $S)\n", param.value(), parameter.getSimpleName().toString());
                            messager.printMessage(Diagnostic.Kind.WARNING, "params is " + param);
                        }
                        methodSpecBuilder.addCode(".build()");


                        MethodSpec methodSpec = methodSpecBuilder.addStatement("")
                                .addCode("$T request = new $T.Builder()\n", Request.class, Request.class)
                                .addCode(".url($S)\n", postAnnotation.url())
                                .addCode(".post(formBody)\n")
                                .addStatement(".build()\n")
                                .addCode("")
                                .addStatement("return client.newCall(request)")
                                .returns(ClassName.get("okhttp3", "Call"))
                                .build();

                        classBuilder.addMethod(methodSpec);
                    }
                }
            }

            FieldSpec clientFiledSpec = FieldSpec.builder(OkHttpClient.class, "client")
                    .initializer("new OkHttpClient()")
                    .build();


            TypeSpec typeSpec = classBuilder.addSuperinterface(ClassName.get(packageName, serviceElement.getSimpleName().toString()))
                    .addField(clientFiledSpec)
                    .build();

            JavaFile javaFile = JavaFile.builder(packageName, typeSpec)
                    .skipJavaLangImports(true)
                    .build();

            /**
             * 尝试write
             */
            try {
                javaFile.writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
