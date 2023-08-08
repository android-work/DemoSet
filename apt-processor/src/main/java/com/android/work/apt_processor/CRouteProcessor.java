package com.android.work.apt_processor;

import com.android.work.apt_annotation.CRoute;
import com.google.auto.service.AutoService;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

@AutoService(Processor.class)
public class CRouteProcessor extends BaseProcessor{

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            handleCRouteLogic(roundEnv.getElementsAnnotatedWith(CRoute.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String getAnnotationType() {
        return CRoute.class.getCanonicalName();
    }

    private void handleCRouteLogic(Set<? extends Element> cRouteElements) throws Exception{
        if (!cRouteElements.isEmpty()){
            Writer routeWriter = null;
            Map<String,String> routeMap = new HashMap<>();
            try {
                String packName = this.getClass().getPackage().getName();
                String moduleName = processingEnv.getOptions().get("AROUTER_MODULE_NAME");
                logNote("cRouteElements.isNotEmpty()");
                for (Element element : cRouteElements){
                    TypeElement typeElement = (TypeElement) element;
                    // 获取全路径名，用于下面将route与activity关联
                    String qualifiedName = typeElement.getQualifiedName().toString();
                    String route = typeElement.getAnnotation(CRoute.class).route();
                    routeMap.put(route,qualifiedName);
                }

                logNote("routeMap:"+ routeMap);

                // 创建文件
                JavaFileObject routeFileObject = processingEnv.getFiler().createSourceFile(packName+".RouteUtil_"+moduleName);
                routeWriter = routeFileObject.openWriter();

                StringBuilder classSb = new StringBuilder("package ").append(packName).append(";\n")
                        .append("import java.util.HashMap;\n")
                        .append("import ").append(packName).append(".IRoute;\n")
                        .append("public class ").append("RouteUtil_").append(moduleName).append(" implements IRoute").append("{\n")
                        .append("  @Override\n")
                        .append("  public HashMap<String,String> putActivity() {\n")
                        .append("    HashMap<String,String> routeMap = new HashMap<>();\n");
                Set<Map.Entry<String,String>> entrySet = routeMap.entrySet();
                // 遍历
                for (Map.Entry<String,String> entry : entrySet){
                    String route = entry.getKey();
                    String pathClass = entry.getValue();
                    // 将路由与activity关联到RouteUtil中 com.android.work.apt_processor.app.RouteUtil.routeMap
                    classSb.append("    ").append("routeMap.put(\"").append(route).append("\",\"").append(pathClass).append("\");\n");
                }
                classSb.append("    return routeMap;\n")
                        .append("  }\n")
                        .append("}\n");
                routeWriter.write(classSb.toString());
                routeWriter.flush();
                routeWriter.close();

            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (routeWriter != null){
                    try{
                        routeWriter.close();
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            }
        }
    }
}
