#include "EntityModule.h"
#include "avtas/lmcp/Factory.h"

#include <iostream>

void EntityModule::step(double sim_time, double timestep) {
}

void EntityModule::initialize(unsigned int entity_id, avtas::lmcp::Node* xmlNode) {
}

void EntityModule::model_event_occured(avtas::lmcp::Object* obj) {
}

void EntityModule::app_event_occured(avtas::lmcp::Object* obj) {
}

void EntityModule::shutdown() {
}


/// internal methods ///

/*
        Sets up the references to the JVM and related items for	later use.
 */
void EntityModule::setJavaOwner(JNIEnv *java_env, jobject obj) {
    // need to get a reference to the owner object that controls the JNI interface
    EntityModule::calling_java_obj = java_env->NewGlobalRef(obj);
	java_env->GetJavaVM(&java_vm);

    // need to get references to the event methods inside the companion java module
    jclass clazz = java_env->GetObjectClass(obj);
    java_model_event_method = java_env->GetMethodID(clazz, "native_fire_model_event", "([B)V");
    java_app_event_method = java_env->GetMethodID(clazz, "native_fire_app_event", "([B)V");
}

/*
        Returns a reference to the java object that is the manager of this module.  This
        object contains the native hooks for connecting JNI functions.
 */
jobject EntityModule::getJavaOwner() {
    return calling_java_obj;
}

/*
        Called by the JNI manager.
 */
void EntityModule::setEntityId(unsigned int id) {
    entity_id = id;
}

/*
        Returns the entity (or aircraft) id associated with this module.
 */
unsigned int EntityModule::getEntityId() {
    return entity_id;
}

/*
        Fires a model-wide event on the java side.  This event is seen
        by all other modules loaded into the entity (or aircraft) model.
 */
void EntityModule::fireModelEvent(avtas::lmcp::Object* obj) {

    fireEvent(java_model_event_method, obj);

}

/*
        Fires an application-wide event on the java side.
 */
void EntityModule::fireApplicationEvent(avtas::lmcp::Object* obj) {
    fireEvent(java_app_event_method, obj);
}

/*
        Fires an event on the java side.  This is used to fire model events as well as
        application events.  The user should not invoke this method directly.
 */
void EntityModule::fireEvent(jmethodID methodId, avtas::lmcp::Object* obj) {

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

        java_env->CallVoidMethod(calling_java_obj, methodId, java_array);

        delete buffer;
        java_env->DeleteLocalRef(java_array);
        java_vm->DetachCurrentThread();
    }

}

avtas::lmcp::Object* EntityModule::processEvent(JNIEnv* env, jbyteArray lmcp_bytes ) {

	const int len = env->GetArrayLength(lmcp_bytes);
	char* cbytes = new char[len];

	env->GetByteArrayRegion(lmcp_bytes, 0, len, (jbyte*) cbytes);

	  
	avtas::lmcp::ByteBuffer buffer;
	buffer.allocate(len);
	buffer.putCharArray(cbytes, len);
	buffer.rewind();

	avtas::lmcp::Object* lmcp_obj = avtas::lmcp::Factory::getObject(buffer);
	  
	delete[] cbytes;
	  
	return lmcp_obj;
}
/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */