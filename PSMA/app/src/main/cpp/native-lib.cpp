#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_capstones17_124_psma_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
