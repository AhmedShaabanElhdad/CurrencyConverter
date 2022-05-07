#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_codegrow_currencyconverter_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string key = "H6hntPyzizCpVakStIJDbWJYu8fKWQor";
    return env->NewStringUTF(key.c_str());
}