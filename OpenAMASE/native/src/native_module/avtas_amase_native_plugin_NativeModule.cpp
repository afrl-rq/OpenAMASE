// NativeCodeExample-Windows.cpp : Defines the exported functions for the DLL application.
//

#include "avtas_amase_native_plugin_NativeModule.h"
#include <iostream>
#include "avtas\lmcp\Factory.h"
#include "ModuleFactory.h"
#include <vector>
#include "avtas/lmcp/XMLParser.h"

vector<EntityModule*> moduleList;


JNIEXPORT jlong JNICALL Java_avtas_amase_natives_NativeModule_createNativeModule
  (JNIEnv *env, jobject callingObj, jstring name, jlong entity_id) {

	  jboolean isCopy;
	  const char* c_name = env->GetStringUTFChars(name, &isCopy );
	  
	  std::cout << "loading new module: " << c_name << std::endl;

	  EntityModule* module = ModuleFactory::createModule(c_name);

	  jlong ret = -1L;

	  if (module != NULL) {
		  moduleList.push_back(module);
		  module->setEntityId( (unsigned int) entity_id );
		  module->setJavaOwner(env, callingObj );
		  ret =  moduleList.size()-1;
	  }

	  if (isCopy) {
		  env->ReleaseStringUTFChars(name, c_name);
	  }

	  return ret;
}


JNIEXPORT void JNICALL Java_avtas_amase_natives_NativeModule_nativeStep
  (JNIEnv *env, jobject callingObj, jlong modulePtr, jlong entityId, jdouble simTime, jdouble timestep) {
	  
	  if (modulePtr == -1) {
		  return;
	  }
	  EntityModule* module = moduleList[modulePtr];

	  if (module != NULL) {
		  module->step(simTime, timestep);
	  }

}


JNIEXPORT void JNICALL Java_avtas_amase_natives_NativeModule_nativeInitialize
  (JNIEnv *env, jobject callingObj, jlong modulePtr, jlong entityId, jstring xmlStr) {

	  if (modulePtr == -1) {
		  return;
	  }
	  EntityModule* module = moduleList[modulePtr];

	  jboolean isCopy;
	  const char* xml_cstr = env->GetStringUTFChars(xmlStr, &isCopy);

	  if (module != NULL) {
		  avtas::lmcp::Node* xmlNode = avtas::lmcp::XMLParser::parseString( xml_cstr, false);
		  module->initialize( static_cast<unsigned int>(entityId), xmlNode);
	  }

	  if (isCopy) {
		  env->ReleaseStringUTFChars(xmlStr, xml_cstr);
	  }
}


JNIEXPORT void JNICALL Java_avtas_amase_natives_NativeModule_nativeModelEventOccured
  (JNIEnv *env, jobject callingObj, jlong modulePtr, jlong entityId, jbyteArray lmcp_bytes) {

	  if (modulePtr == -1) {
		  return;
	  }
	  EntityModule* module = moduleList[modulePtr];
	  
	  if (module != NULL) {
		  avtas::lmcp::Object* obj = module->processEvent(env, lmcp_bytes);
		  if (obj != NULL) {
			  module->model_event_occured(obj);
		  }
	  }
}

JNIEXPORT void JNICALL Java_avtas_amase_natives_NativeModule_nativeAppEventOccured
  (JNIEnv *env, jobject callingObj, jlong modulePtr, jlong entityId, jbyteArray lmcp_bytes) {

	  if (modulePtr == -1) {
		  return;
	  }
	  EntityModule* module = moduleList[modulePtr];
	  
	  if (module != NULL) {
		  avtas::lmcp::Object* obj = module->processEvent(env, lmcp_bytes);
		  if (obj != NULL) {
			  module->app_event_occured(obj);
		  }
	  }
}


JNIEXPORT void JNICALL Java_avtas_amase_natives_NativeModule_nativeShutdown
  (JNIEnv *, jobject, jlong modulePtr, jlong entity_id) {

	  if (modulePtr == -1) {
		  return;
	  }
	  EntityModule* module = moduleList[modulePtr];

	  if (module != NULL) {
		  module->shutdown();
                  delete module;
	  }


}






/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */