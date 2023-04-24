package com.android.work.apt_processor;

import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.SourceVersion;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

public abstract class BaseProcessor extends AbstractProcessor {

    public void logNote(Object obj){
        log(Diagnostic.Kind.NOTE, obj);
    }

    public void logError(Object obj){
        log(Diagnostic.Kind.ERROR, obj);
    }

    public void logWarning(Object obj){
        log(Diagnostic.Kind.WARNING, obj);
    }

    private void log(Diagnostic.Kind kind,Object obj){
        String Tag = "<<<BaseProcessor -> "+this.getClass().getSimpleName()+">>> ---> " + obj;
        processingEnv.getMessager().printMessage(kind,Tag);
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        String version = processingEnv.getSourceVersion().name();
        logNote("Hello APT init \n  version:" + version);
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return processingEnv.getSourceVersion();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        String canonicalName = getAnnotationType();
        logNote("canonicalName:" + canonicalName);
        Set<String> set = new HashSet<>();
        set.add(canonicalName);
        return set;
    }

    public abstract String getAnnotationType();
}
