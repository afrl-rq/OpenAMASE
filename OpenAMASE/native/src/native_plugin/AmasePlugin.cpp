#include "AmasePlugin.h"
#include "avtas/lmcp/Factory.h"

#include <iostream>

string AmasePlugin::getPluginName() {
	return plugin_name;
}

void AmasePlugin::step(double sim_time, double timestep) {
}

void AmasePlugin::initialize(avtas::lmcp::Node* xmlNode, const char* config_directory) {
}

void AmasePlugin::app_event_occured(avtas::lmcp::Object* obj) {
}

void AmasePlugin::shutdown() {
}


/// internal methods ///

/*
        Sets up the references to the JVM and related items for	later use.
 */
void AmasePlugin::setJavaOwner(JNIEnv *java_env, jobject obj) {
    // need to get a reference to the owner object that controls the JNI interface
    AmasePlugin::calling_java_obj = java_env->NewGlobalRef(obj);
	java_env->GetJavaVM(&java_vm);

    // need to get references to the event methods inside the companion java plugin
    jclass clazz = java_env->GetObjectClass(obj);
    java_app_event_method = java_env->GetMethodID(clazz, "native_fire_app_event", "([B)V");
}

/*
        Returns a reference to the java object that is the manager of this plugin.  This
        object contains the native hooks for connecting JNI functions.
 */
jobject AmasePlugin::getJavaOwner() {
    return calling_java_obj;
}

void AmasePlugin::processEvent(JNIEnv* env, jbyteArray lmcp_bytes ) {

	const int len = env->GetArrayLength(lmcp_bytes);
	char* cbytes = new char[len];

	env->GetByteArrayRegion(lmcp_bytes, 0, len, (jbyte*) cbytes);

	  
	avtas::lmcp::ByteBuffer buffer;
	buffer.allocate(len);
	buffer.putCharArray(cbytes, len);
	buffer.rewind();

	avtas::lmcp::Object* lmcp_obj = avtas::lmcp::Factory::getObject(buffer);
	  
	delete[] cbytes;
	  
	app_event_occured(lmcp_obj);
}



/*
        Fires an application-wide event on the java side.
 */
void AmasePlugin::fireApplicationEvent(avtas::lmcp::Object* obj) {

    // need to attach to the current Java thread.  Will throw exception otherwise.
    JNIEnv* java_env;
    java_vm->AttachCurrentThread((void**) &java_env, NULL);

    if (java_env != NULL) {

        if (obj == NULL) {
            return;
        }

        avtas::lmcp::ByteBuffer* buffer = avtas::lmcp::Factory::packMessage(obj, false);
        jsize len = static_cast<jsize>(buffer->capacity());

        jbyteArray java_array = java_env->NewByteArray(len);
        java_env->SetByteArrayRegion(java_array, 0, len, (jbyte*) buffer->array());

        java_env->CallVoidMethod(calling_java_obj, java_app_event_method, java_array);

        delete buffer;
        java_env->DeleteLocalRef(java_array);
        java_vm->DetachCurrentThread();
    }

}
/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */