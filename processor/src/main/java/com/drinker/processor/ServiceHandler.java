package com.drinker.processor;

import com.drinker.processor.processor.DeleteBodyProcessor;
import com.drinker.processor.processor.DeleteNoBodyProcessor;
import com.drinker.processor.processor.GetParamMapProcessor;
import com.drinker.processor.processor.GetParamProcessor;
import com.drinker.processor.processor.IHttpMethodProcessor;
import com.drinker.processor.processor.PostBodyProcessor;
import com.drinker.processor.processor.PostConverterBodyProcessor;
import com.drinker.processor.processor.PostFormMapProcessor;
import com.drinker.processor.processor.PostFormProcessor;
import com.drinker.processor.processor.PostMultipartMapProcessor;
import com.drinker.processor.processor.PostMultipartProcessor;
import com.drinker.processor.processor.PutBodyProcessor;
import com.drinker.processor.processor.PutFormMapProcessor;
import com.drinker.processor.processor.PutFormProcessor;
import com.drinker.processor.processor.PutMultipartMapProcessor;
import com.drinker.processor.processor.PutMultipartProcessor;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

import static com.drinker.processor.SpeedyClassName.CALL_ADAPTER_FACTORY;
import static com.drinker.processor.SpeedyClassName.CALL_FACTORY;
import static com.drinker.processor.SpeedyClassName.CONVERTER_FACTORY;
import static com.drinker.processor.SpeedyClassName.DELIVERY;

final class ServiceHandler implements ProcessHandler {

    private final static String IMPL = "_impl";
    private Elements elements;
    private Filer filer;


    ServiceHandler(Elements elements, Filer filer) {
        this.elements = elements;
        this.filer = filer;
    }

    @Override
    public void process(Set<? extends Element> serviceElements) {
        for (Element serviceElement : serviceElements) {
            String packageName = elements.getPackageOf(serviceElement).getQualifiedName().toString();
            TypeElement element = (TypeElement) serviceElement;
            List<? extends Element> enclosedElements = element.getEnclosedElements();

            TypeSpec.Builder classBuilder = TypeSpec.classBuilder(element.getSimpleName().toString() + IMPL);
            for (Element enclosedElement : enclosedElements) {
                if (enclosedElement instanceof ExecutableElement) {
                    // 写方法
                    ExecutableElement executableElement = (ExecutableElement) enclosedElement;
                    Log.w("element" + element + "enclose" + enclosedElement);
                    MethodSpec methodSpec = checkReturnType(executableElement);
                    classBuilder.addMethod(methodSpec);
                }
            }

            FieldSpec clientFiled = FieldSpec.builder(CALL_FACTORY, "client")
                    .addModifiers(Modifier.PRIVATE)
                    .build();

            FieldSpec baseHttpUrlField = FieldSpec.builder(String.class, "baseHttpUrl")
                    .addModifiers(Modifier.PRIVATE)
                    .build();

            FieldSpec converterField = FieldSpec.builder(CONVERTER_FACTORY, "converterFactory")
                    .addModifiers(Modifier.PRIVATE)
                    .build();

            FieldSpec deliveryField = FieldSpec.builder(DELIVERY, "delivery")
                    .addModifiers(Modifier.PRIVATE)
                    .build();

            FieldSpec callAdapterField = FieldSpec.builder(CALL_ADAPTER_FACTORY, "callAdapterFactory")
                    .addModifiers(Modifier.PRIVATE)
                    .build();

            // 添加构造函数
            MethodSpec constructor = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(CALL_FACTORY, "client")
                    .addParameter(String.class, "baseHttpUrl")
                    .addParameter(CONVERTER_FACTORY, "converterFactory")
                    .addParameter(DELIVERY, "delivery")
                    .addParameter(CALL_ADAPTER_FACTORY, "callAdapterFactory")
                    .addStatement("this.client = client")
                    .addStatement("this.baseHttpUrl = baseHttpUrl")
                    .addStatement("this.converterFactory = converterFactory")
                    .addStatement("this.delivery = delivery")
                    .addStatement("this.callAdapterFactory = callAdapterFactory")
                    .build();

            TypeSpec typeSpec = classBuilder.addSuperinterface(ClassName.get(packageName, serviceElement.getSimpleName().toString()))
                    .addModifiers(Modifier.PUBLIC)
                    .addField(clientFiled)
                    .addField(baseHttpUrlField)
                    .addField(converterField)
                    .addField(deliveryField)
                    .addField(callAdapterField)
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


    private MethodSpec getMethodSpec(ExecutableElement element, List<? extends VariableElement> parameters, TypeMirror returnType, TypeName generateType) {
        List<IHttpMethodProcessor> processors = new ArrayList<>();
        processors.add(new GetParamMapProcessor());
        processors.add(new GetParamProcessor());
        processors.add(new PostFormMapProcessor());
        processors.add(new PostFormProcessor());
        processors.add(new PostMultipartMapProcessor());
        processors.add(new PostMultipartProcessor());
        processors.add(new PostBodyProcessor());
        processors.add(new PostConverterBodyProcessor());
        processors.add(new PutFormProcessor());
        processors.add(new PutFormMapProcessor());
        processors.add(new PutFormProcessor());
        processors.add(new PutMultipartMapProcessor());
        processors.add(new PutMultipartProcessor());
        processors.add(new PutBodyProcessor());
        processors.add(new DeleteBodyProcessor());
        processors.add(new DeleteNoBodyProcessor());

        for (IHttpMethodProcessor handler : processors) {
            MethodSpec methodSpec = handler.process(element, parameters, returnType, generateType);
            if (methodSpec != null) {
                return methodSpec;
            }
        }
        throw new NullPointerException("no method handler handle it ,please check your service config");
    }

    private MethodSpec checkReturnType(ExecutableElement element) {
        List<? extends VariableElement> parameters = element.getParameters();
        TypeMirror returnType = element.getReturnType();
        TypeName returnTypeName = ClassName.get(returnType);
        Log.w("return type " + returnTypeName);
        if (returnTypeName instanceof ParameterizedTypeName) {
            List<TypeName> genericReturnTypes = ((ParameterizedTypeName) returnTypeName).typeArguments;
            if (genericReturnTypes != null && !genericReturnTypes.isEmpty()) {
                // 确保只有一个包装的返回值
                if (genericReturnTypes.size() != 1) {
                    throw new IllegalStateException("genericReturnTypes only can has one ");
                }
                TypeName generateType = genericReturnTypes.get(0);
                return getMethodSpec(element, parameters, returnType, generateType);
            } else {
                throw new IllegalStateException("return value must wrapped by generic type like Call<User> ...");
            }
        } else {
            throw new IllegalStateException("return type is not ParameterizedTypeName");
        }
    }

}
