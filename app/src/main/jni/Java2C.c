//
// Created by fengq on 2017/5/18.
//
#include "com_example_fengq_myapplication_jnis_Java2CJNI.h"
JNIEXPORT jstring JNICALL
Java_com_example_fengq_myapplication_jnis_Java2CJNI_java2C (JNIEnv *env, jobject instance){
    return (*env)->NewStringUTF(env,"I am from native c.");
}
