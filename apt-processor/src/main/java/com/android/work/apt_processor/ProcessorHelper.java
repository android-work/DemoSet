package com.android.work.apt_processor;

import com.android.work.apt_annotation.BindView;
import com.google.auto.service.AutoService;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

@AutoService(Processor.class)
public class ProcessorHelper extends BaseProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // 寻找所有文件中用到的注解
        Set<? extends Element> bindViewElements = roundEnv.getElementsAnnotatedWith(BindView.class);
        logNote("bindViewElements.size:"+bindViewElements.size());
        try {
            handleBindViewLogic(bindViewElements);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void handleBindViewLogic(Set<? extends Element> bindViewElements) throws IOException{
        if(!bindViewElements.isEmpty()) {
            logNote("bindViewElements.isNotEmpty()");
            Map<Name, List<Element>> elementsMap = new HashMap<>();
            // 遍历节点
            Writer writer = null;
            try {
                for (Element element : bindViewElements) {
                    // 查找到上层节点到文件名，用于创建不同到activity的
                    Name qualifiedName = ((TypeElement)element.getEnclosingElement()).getQualifiedName();
                    logNote("qualifiedName:"+qualifiedName);
                    if (elementsMap.containsKey(qualifiedName)) {
                        // 将元素添加到同activity的注解元素集合中
                        elementsMap.get(qualifiedName).add(element);
                    } else {
                        // 不同activity的元素，需要新建集合，存放到map中
                        List<Element> elementList = new ArrayList<>();
                        elementList.add(element);
                        elementsMap.put(qualifiedName,elementList);
                    }
                }

                // 写代码
                Set<Map.Entry<Name,List<Element>>> entrySet = elementsMap.entrySet();
                for (Map.Entry<Name, List<Element>> entry : entrySet) {
                    String qualifiedName = entry.getKey().toString();
                    logNote("write start elementsMap.size:"+elementsMap.size() +"   qualifiedName:"+qualifiedName);
                    // 已上层文件名为key保存对应的file文件
                    JavaFileObject fileObject = processingEnv.getFiler().createSourceFile(qualifiedName+"$$Util");
                    writer = fileObject.openWriter();
                    List<Element> elementList = entry.getValue();
                    int lastIndex = qualifiedName.lastIndexOf(".");
                    String packName = qualifiedName.substring(0,lastIndex);
                    String activityName = qualifiedName.substring(lastIndex + 1);
                    StringBuilder sb = new StringBuilder("package ").append(packName).append(";\n")
                            .append("public class ").append(activityName).append("$$Util {\n")
                            .append("  public void findViewById").append(" (").append(qualifiedName).append(" activity) {\n");

                    for (Element element : elementList) {
                        int resourceId = element.getAnnotation(BindView.class).value();
                        writeCode(writer, sb,resourceId, element.getSimpleName().toString());
                    }
                    sb.append("  }\n")
                            .append("}");
                    writer.write(sb.toString());
                    writer.flush();
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(writer != null) {
                    writer.close();
                }
            }

        }
    }

    @Override
    public String getAnnotationType() {
        return BindView.class.getCanonicalName();
    }

    private void writeCode(Writer writer, StringBuilder sb, int resourceId, String elementName) throws IOException{
        if(writer == null){
            throw new NullPointerException("writeCode writer is null");
        }
        sb.append("    activity.").append(elementName).append(" = ").append("activity.findViewById(").append(resourceId).append(");\n");
    }
}