


#ifndef _Included_avtas_amase_native_plugin_NativePlugin
#define _Included_avtas_amase_native_plugin_NativePlugin


#include <jni.h>
#include "avtas/lmcp/Object.h"

#ifdef __cplusplus
extern "C" {
#endif



JNIEXPORT jlong JNICALL Java_avtas_amase_natives_NativePlugin_createNativePlugin
  (JNIEnv *, jobject, jstring);


JNIEXPORT void JNICALL Java_avtas_amase_natives_NativePlugin_nativeStep
  (JNIEnv *, jobject, jlong, jdouble, jdouble);


JNIEXPORT void JNICALL Java_avtas_amase_natives_NativePlugin_nativeInitialize
  (JNIEnv *, jobject, jlong, jstring, jstring);


JNIEXPORT void JNICALL Java_avtas_amase_natives_NativePlugin_nativeAppEventOccured
  (JNIEnv *, jobject, jlong, jbyteArray);


JNIEXPORT void JNICALL Java_avtas_amase_natives_NativePlugin_nativeShutdown
  (JNIEnv *, jobject, jlong);



#ifdef __cplusplus
}
#endif
#endif

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */