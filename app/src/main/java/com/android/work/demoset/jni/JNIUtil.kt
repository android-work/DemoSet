package com.android.work.demoset.jni

object JNIUtil {

    external fun nativeInit(value:String,value1:Int)

    external fun nativeTest(value:Int):String
}